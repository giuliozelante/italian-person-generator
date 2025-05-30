# Multi-stage build for optimal size on Alpine Linux
FROM ghcr.io/graalvm/graalvm-ce:ol8-java17-22.3.0 AS builder

# Install necessary build tools
RUN microdnf install -y gcc glibc-devel zlib-devel libstdc++-static

# Set working directory
WORKDIR /app

# Copy Gradle files
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle/ gradle/
COPY gradlew ./

# Make gradlew executable
RUN chmod +x gradlew

# Copy source code
COPY src/ src/

# Build native image
RUN ./gradlew clean nativeCompile

# Final stage - minimal Alpine Linux image
FROM alpine:3.18

# Install minimal runtime dependencies for Atom architecture
RUN apk add --no-cache \
    libc6-compat \
    libstdc++ \
    && rm -rf /var/cache/apk/*

# Create non-root user for security
RUN addgroup -g 1000 appuser && \
    adduser -D -s /bin/sh -u 1000 -G appuser appuser

# Create app directory
RUN mkdir -p /app && chown appuser:appuser /app

# Copy the native binary from builder stage
COPY --from=builder /app/build/native/nativeCompile/italian-person-generator /app/

# Set permissions
RUN chmod +x /app/italian-person-generator && \
    chown appuser:appuser /app/italian-person-generator

# Switch to non-root user
USER appuser

# Set working directory
WORKDIR /app

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD /app/italian-person-generator > /dev/null || exit 1

# Set the entry point
ENTRYPOINT ["/app/italian-person-generator"]