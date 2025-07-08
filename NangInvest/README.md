# NangInvest - Maven Migration

This project has been migrated from Ant to Maven build system.

## Project Structure

The project now follows Maven's standard directory layout:

```
src/
├── main/
│   ├── java/           # Java source files
│   ├── resources/      # Configuration files (persistence.xml, etc.)
│   └── webapp/         # Web resources (JSP, CSS, JS, images)
└── test/
    ├── java/           # Test source files
    └── resources/      # Test configuration files
```

## Dependencies

All dependencies are now managed through Maven and defined in `pom.xml`:

- Jakarta EE Web APIs (Servlet, JSP, JSTL)
- Hibernate ORM 6.2.7 (JPA implementation)
- Microsoft SQL Server JDBC Driver
- Google API Client libraries
- HTTP Client libraries
- Gson for JSON processing
- JUnit 5 for testing

## Environment Setup

Your Maven installation path: `D:\FPT_SoftWareEngineering\SUMMER2025\PRJ301\apache-maven-3.9.10-bin\apache-maven-3.9.10`

### Option 1: Use Full Path (Current Setup)

```powershell
& "D:\FPT_SoftWareEngineering\SUMMER2025\PRJ301\apache-maven-3.9.10-bin\apache-maven-3.9.10\bin\mvn.cmd" clean compile
```

### Option 2: Use PowerShell Alias (Recommended)

```powershell
# Load the alias (run once per session)
. .\mvn-alias.ps1

# Then use normally
mvn clean compile
mvn clean package
mvn tomcat7:run
```

### Option 3: Add to System PATH

To use `mvn` directly, restart PowerShell as Administrator and run:

```powershell
$env:PATH += ";D:\FPT_SoftWareEngineering\SUMMER2025\PRJ301\apache-maven-3.9.10-bin\apache-maven-3.9.10\bin"
```

## Building and Running

### Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- Apache Tomcat 10+

### Build Commands

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package as WAR
mvn package

# Clean and package
mvn clean package

# Install to local repository
mvn install
```

### Running the Application

1. **Using Tomcat Maven Plugin (Development):**

   ```bash
   mvn clean package tomcat7:run
   ```

   Application will be available at: http://localhost:8080/nanginvest

2. **Deploy to External Tomcat:**
   - Build the WAR file: `mvn clean package`
   - Copy `target/nanginvest.war` to your Tomcat `webapps` directory
   - Start Tomcat

### Database Configuration

Update the database connection settings in `src/main/resources/persistence.xml`:

```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:sqlserver://localhost:1433;databaseName=NangInvest;encrypt=false"/>
<property name="jakarta.persistence.jdbc.user" value="your_username"/>
<property name="jakarta.persistence.jdbc.password" value="your_password"/>
```

## Migration Benefits

1. **Better Dependency Management**: All dependencies are now centrally managed in `pom.xml`
2. **Standard Project Structure**: Follows Maven conventions, making it easier for new developers
3. **Improved Build Process**: More reliable and reproducible builds
4. **IDE Support**: Better support in modern IDEs (IntelliJ IDEA, Eclipse, VS Code)
5. **Plugin Ecosystem**: Access to Maven's extensive plugin ecosystem
6. **CI/CD Ready**: Easier integration with continuous integration systems

## Development Tips

1. **IDE Setup**: Import as a Maven project in your IDE
2. **Hot Reload**: Use `mvn tomcat7:run` for development with hot reload
3. **Dependency Updates**: Use `mvn versions:display-dependency-updates` to check for newer versions
4. **Clean Build**: Always use `mvn clean` when switching between different build tools

## Troubleshooting

### Common Issues

1. **Java Version Mismatch**: Ensure you're using Java 17+
2. **Port Conflicts**: Change the port in tomcat7-maven-plugin configuration if 8080 is occupied
3. **Database Connection**: Verify SQL Server is running and connection details are correct
4. **Memory Issues**: Increase JVM memory with `export MAVEN_OPTS="-Xmx1024m"`

### Old Build Files

The following files are no longer needed and can be removed:

- `build.xml`
- `nbproject/` directory
- `build/` directory (build output)

Keep them temporarily until you verify everything works correctly with Maven.
