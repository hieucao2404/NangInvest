# Maven Installation Guide for Windows

## Option 1: Install using Chocolatey (Recommended)

If you have Chocolatey installed:

```powershell
choco install maven
```

## Option 2: Manual Installation

1. **Download Maven**

   - Go to https://maven.apache.org/download.cgi
   - Download the latest "Binary zip archive" (apache-maven-3.x.x-bin.zip)

2. **Extract and Install**

   - Extract to a directory like `C:\Program Files\Apache\maven`
   - Add `C:\Program Files\Apache\maven\bin` to your system PATH

3. **Verify Installation**
   ```powershell
   mvn --version
   ```

## Option 3: Install using Scoop

If you have Scoop installed:

```powershell
scoop install maven
```

## Option 4: Using Maven Wrapper (No Installation Required)

I can create a Maven Wrapper that downloads Maven automatically:

1. The wrapper will be included in your project
2. Use `.\mvnw.cmd` instead of `mvn` on Windows
3. No system-wide Maven installation needed

## Next Steps After Maven Installation

Once Maven is installed, you can:

1. **Test the build:**

   ```bash
   mvn clean compile
   ```

2. **Package the application:**

   ```bash
   mvn clean package
   ```

3. **Run with Tomcat plugin:**

   ```bash
   mvn clean package tomcat7:run
   ```

4. **Run tests:**
   ```bash
   mvn test
   ```

## IDE Integration

### VS Code

Install the "Extension Pack for Java" which includes Maven support.

### IntelliJ IDEA

Maven support is built-in. Simply open the folder containing `pom.xml`.

### Eclipse

Import as "Existing Maven Project".
