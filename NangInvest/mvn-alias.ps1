# NangInvest Maven Helper Script
# Save this as mvn-alias.ps1 and run: Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser (if needed)

# Set Maven path
$MVN_PATH = "D:\FPT_SoftWareEngineering\SUMMER2025\PRJ301\apache-maven-3.9.10-bin\apache-maven-3.9.10\bin\mvn.cmd"

# Create an alias for easier Maven usage
function mvn { & $MVN_PATH $args }

# Usage examples:
# mvn clean compile       # Clean and compile
# mvn clean package       # Build WAR file  
# mvn tomcat7:run        # Run with embedded Tomcat
# mvn test               # Run tests

Write-Host "Maven alias configured! You can now use 'mvn' command directly." -ForegroundColor Green
Write-Host "Examples:" -ForegroundColor Yellow
Write-Host "  mvn clean compile" -ForegroundColor Cyan
Write-Host "  mvn clean package" -ForegroundColor Cyan  
Write-Host "  mvn tomcat7:run" -ForegroundColor Cyan
