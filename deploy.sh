#!/bin/bash

# Italian Person Generator Deployment Script for Alpine Linux (Atom)
set -e

echo "=== Italian Person Generator Deployment ==="
echo "Target: Alpine Linux on Atom architecture"
echo

# Configuration
APP_NAME="italian-person-generator"
DOCKER_IMAGE="$APP_NAME:latest"
CONTAINER_NAME="$APP_NAME-container"

# Function to check if running on Atom architecture
check_architecture() {
    ARCH=$(uname -m)
    echo "Detected architecture: $ARCH"
    
    case $ARCH in
        x86_64|i686|i386)
            echo "✓ Compatible with Atom processors"
            ;;
        *)
            echo "⚠ Warning: Architecture $ARCH may not be optimal for Atom processors"
            ;;
    esac
}

# Function to build native binary locally (without Docker)
build_native() {
    echo "Building native binary with GraalVM..."
    
    # Check if GraalVM is installed
    if ! command -v native-image &> /dev/null; then
        echo "Error: GraalVM native-image not found!"
        echo "Please install GraalVM and run: gu install native-image"
        exit 1
    fi
    
    # Build with Gradle
    ./gradlew clean nativeCompile
    
    echo "✓ Native binary built successfully"
    echo "Binary location: build/native/nativeCompile/$APP_NAME"
}

# Function to build Docker image
build_docker() {
    echo "Building Docker image for Alpine Linux..."
    
    # Build multi-arch image for better Atom compatibility
    docker build --platform linux/amd64 -t $DOCKER_IMAGE .
    
    echo "✓ Docker image built successfully"
}

# Function to run the application
run_app() {
    echo "Running Italian Person Generator..."
    
    if [ -f "build/native/nativeCompile/$APP_NAME" ]; then
        echo "Running native binary..."
        ./build/native/nativeCompile/$APP_NAME
    elif docker images | grep -q $APP_NAME; then
        echo "Running Docker container..."
        docker run --rm --name $CONTAINER_NAME $DOCKER_IMAGE
    else
        echo "No executable found. Please build first."
        exit 1
    fi
}

# Function to install as system service
install_service() {
    echo "Installing as systemd service..."
    
    if [ ! -f "build/native/nativeCompile/$APP_NAME" ]; then
        echo "Error: Native binary not found. Please build first."
        exit 1
    fi
    
    # Copy binary to system location
    sudo cp "build/native/nativeCompile/$APP_NAME" /usr/local/bin/
    
    # Create systemd service file
    cat << EOF | sudo tee /etc/systemd/system/$APP_NAME.service
[Unit]
Description=Italian Person Generator
After=network.target

[Service]
Type=oneshot
User=nobody
Group=nobody
ExecStart=/usr/local/bin/$APP_NAME
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
EOF

    # Enable and start service
    sudo systemctl daemon-reload
    sudo systemctl enable $APP_NAME.service
    
    echo "✓ Service installed successfully"
    echo "Run with: sudo systemctl start $APP_NAME.service"
}

# Function to show system info
show_system_info() {
    echo "=== System Information ==="
    echo "OS: $(cat /etc/os-release | grep PRETTY_NAME | cut -d= -f2 | tr -d '\"')"
    echo "Kernel: $(uname -r)"
    echo "Architecture: $(uname -m)"
    echo "CPU: $(grep 'model name' /proc/cpuinfo | head -1 | cut -d: -f2 | xargs)"
    echo "Memory: $(free -h | grep Mem | awk '{print $2}')"
    echo "Available space: $(df -h . | tail -1 | awk '{print $4}')"
    echo
}

# Main menu
case "${1:-menu}" in
    "build")
        check_architecture
        build_native
        ;;
    "docker")
        check_architecture
        build_docker
        ;;
    "run")
        run_app
        ;;
    "install")
        install_service
        ;;
    "info")
        show_system_info
        ;;
    "menu"|*)
        echo "Usage: $0 [build|docker|run|install|info]"
        echo
        echo "Commands:"
        echo "  build   - Build native binary with GraalVM"
        echo "  docker  - Build Docker image"
        echo "  run     - Run the application"
        echo "  install - Install as system service"
        echo "  info    - Show system information"
        echo
        show_system_info
        check_architecture
        ;;
esac