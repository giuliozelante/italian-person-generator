plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    id("org.graalvm.buildtools.native") version "0.9.28"
    application
}

group = "it.gzelante.italianperson"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    
    // Ktor Server
    implementation("io.ktor:ktor-server-core:2.3.7")
    implementation("io.ktor:ktor-server-netty:2.3.7")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    
    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    
    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

application {
    mainClass.set("it.gzelante.italianperson.AppKt")
}

kotlin {
    jvmToolchain(21)
}

// Ensure run task always executes and shows output
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
    outputs.upToDateWhen { false }
}

graalvmNative {
    binaries {
        named("main") {
            imageName.set("italian-person-generator")
            mainClass.set("it.gzelante.italianperson.AppKt")
            
            buildArgs.add("--no-fallback")
            buildArgs.add("-H:+ReportExceptionStackTraces")
            
            // Class initialization configuration for Ktor/Netty
            buildArgs.add("--initialize-at-build-time=ch.qos.logback")
            buildArgs.add("--initialize-at-build-time=org.slf4j")
            buildArgs.add("--initialize-at-run-time=io.netty")
            buildArgs.add("--initialize-at-run-time=io.netty.util.internal.shaded.org.jctools")
            
            // JNI and reflection configuration for Netty
            buildArgs.add("--enable-url-protocols=http,https")
            buildArgs.add("-H:+AllowVMInspection")
            buildArgs.add("--enable-all-security-services")
            
            // Optimize for size and startup time
            buildArgs.add("-Ob")
            buildArgs.add("--gc=serial")
        }
    }
    
    // Enable agent for reflection configuration if needed
    agent {
        defaultMode.set("standard")
        builtinCallerFilter.set(true)
        builtinHeuristicFilter.set(true)
        enableExperimentalPredefinedClasses.set(false)
        trackReflectionMetadata.set(true)
        
        modes {
            standard {
                //options.add("config-output-dir=src/main/resources/META-INF/native-image")
            }
        }
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "it.gzelante.italianperson.AppKt"
    }
}

// Task to create a fat jar if needed
tasks.register<Jar>("fatJar") {
    archiveClassifier.set("fat")
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "it.gzelante.italianperson.AppKt"
    }
}

// Optimize for Atom processors (low-power, limited resources)
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "21"
        freeCompilerArgs = listOf(
            "-Xjsr305=strict",
            "-Xjvm-default=all",
            "-opt-in=kotlin.RequiresOptIn"
        )
    }
}