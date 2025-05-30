# 🇮🇹 Italian Person Generator API

A fast, lightweight REST API service for generating realistic Italian person data. Built with Kotlin + Ktor and compiled to native executables with GraalVM for maximum performance.

## ✨ Features

- **Realistic Italian Data**: Authentic names, addresses, and personal information
- **REST API**: Clean JSON endpoints for easy integration
- **Native Compilation**: Fast startup (~80ms) and small binary size (~14MB)
- **Multi-Platform**: Windows, Linux, and macOS executables
- **Zero Dependencies**: Self-contained executable with no external runtime requirements

## 🚀 Quick Start

### Download Pre-built Binaries

Download the latest release for your platform from the [GitHub Releases](../../releases) page:

- **Windows**: `italian-person-generator-windows-x64.exe`
- **Linux**: `italian-person-generator-linux-x64`  
- **macOS**: `italian-person-generator-macos-x64`

### Run the Service

```bash
# Windows
./italian-person-generator-windows-x64.exe

# Linux/macOS
./italian-person-generator-linux-x64
```

The service will start on port 8080 by default. You can customize the port with the `PORT` environment variable:

```bash
PORT=3000 ./italian-person-generator-linux-x64
```

## 📡 API Endpoints

### Health Check
```http
GET /health
```

Returns service status and version information.

**Response:**
```json
{
  "status": "UP",
  "service": "Italian Person Generator",
  "version": "1.0.0"
}
```

### Generate Single Person
```http
GET /api/v1/person
```

Generates a single Italian person with complete data.

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "name": "Marco",
      "surname": "Rossi",
      "dateOfBirth": "15/03/1985",
      "address": "Via Roma, 42, 00100 Roma RM",
      "cap": "00100",
      "fiscalCode": "RSSMRC85C15H501Z",
      "mobilePhone": "+39 3201234567",
      "idCard": "AB12345CD",
      "idCardIssueDate": "12/07/2020"
    }
  ]
}
```

### Generate Multiple Persons
```http
GET /api/v1/persons?count=N
```

Generates multiple persons (max 100 per request).

**Parameters:**
- `count` (optional): Number of persons to generate (1-100, default: 1)

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "name": "Giulia",
      "surname": "Bianchi",
      "dateOfBirth": "22/09/1992",
      "address": "Corso Italia, 156, 20100 Milano MI",
      "cap": "20100",
      "fiscalCode": "BNCGLI92P62F205W",
      "mobilePhone": "+39 3339876543",
      "idCard": "XY98765ZW",
      "idCardIssueDate": "05/02/2019"
    }
    // ... more persons
  ]
}
```

### API Information
```http
GET /
```

Returns API information and available endpoints.

## 🏗️ Building from Source

### Prerequisites

- **Java 21** (or higher)
- **Gradle** (included via wrapper)
- **GraalVM with Native Image** (for native compilation)

### Clone and Build

```bash
git clone https://github.com/your-username/italian-person-generator.git
cd italian-person-generator

# Run with JVM
./gradlew run

# Build native executable
./gradlew nativeCompile

# The native executable will be in build/native/nativeCompile/
```

### Development

```bash
# Run tests
./gradlew test

# Build JAR
./gradlew build

# Clean build
./gradlew clean
```

## 🌍 Multi-Platform CI/CD

This project includes GitHub Actions workflows for:

- **Continuous Integration**: Automated testing on every PR and push
- **Multi-Platform Releases**: Automatic building and releasing of native executables for Windows, Linux, and macOS

### Creating a Release

To create a new release with native binaries:

1. Create and push a new tag:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

2. GitHub Actions will automatically:
   - Build native executables for all platforms
   - Run tests and integration checks
   - Create a GitHub release with downloadable binaries

## 📊 Generated Data Structure

Each Italian person includes:

| Field | Description | Example |
|-------|-------------|---------|
| `name` | Italian first name (male/female) | "Marco", "Giulia" |
| `surname` | Italian family name | "Rossi", "Bianchi" |
| `dateOfBirth` | Birth date (dd/MM/yyyy, age 18-80) | "15/03/1985" |
| `address` | Full Italian address with province | "Via Roma, 42, 00100 Roma RM" |
| `cap` | CAP postal code | "00100" |
| `fiscalCode` | Valid Codice Fiscale calculation | "RSSMRC85C15H501Z" |
| `mobilePhone` | Italian mobile number | "+39 3201234567" |
| `idCard` | Italian ID card (CIE format) | "AB12345CD" |
| `idCardIssueDate` | ID card issue date | "12/07/2020" |

### Data Authenticity

- **Names**: Real Italian first and last names
- **Addresses**: Authentic Italian cities with correct CAP codes and provinces
- **Fiscal Codes**: Calculated using the official Italian algorithm
- **Dates**: Consistent relationships (ID cards issued after 18th birthday, etc.)
- **Phone Numbers**: Valid Italian mobile prefixes

## 🔧 Configuration

### Environment Variables

- `PORT`: Server port (default: 8080)

### Customization

The data can be customized by modifying the source files:

- Names: Edit `maleNames`, `femaleNames`, `surnames` lists
- Cities: Edit `cities` list with CAP and province codes
- Streets: Edit `streets` list for address generation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🛡️ Disclaimer

This tool generates **fake data for testing and development purposes only**. Do not use this data for any illegal or unethical purposes. The generated fiscal codes and personal information are not real and should not be used for identity verification or any official purposes.

---

**Made with ❤️ in Italy** 🇮🇹