# NangInvest Environment Setup Script
# This script helps set up environment variables for the application

Write-Host "NangInvest Environment Setup" -ForegroundColor Cyan
Write-Host "=============================" -ForegroundColor Cyan

# Check if OpenAI API key is already set
$currentKey = $env:OPENAI_API_KEY
if ($currentKey) {
    Write-Host "OpenAI API Key is already set (showing first 8 characters): $($currentKey.Substring(0,8))..." -ForegroundColor Green
    $override = Read-Host "Do you want to override it? (y/n)"
    if ($override -ne 'y' -and $override -ne 'Y') {
        Write-Host "Keeping existing API key." -ForegroundColor Yellow
        exit 0
    }
}

Write-Host ""
Write-Host "Setting up OpenAI API Key..." -ForegroundColor Yellow
Write-Host "You can get your API key from: https://platform.openai.com/api-keys" -ForegroundColor Blue
Write-Host ""

# Prompt for API key
$apiKey = Read-Host "Enter your OpenAI API Key" -MaskedInput
if (-not $apiKey) {
    Write-Host "No API key provided. Exiting." -ForegroundColor Red
    exit 1
}

# Validate API key format (should start with sk-)
if (-not $apiKey.StartsWith("sk-")) {
    Write-Host "Warning: API key should start with 'sk-'. Please verify your key." -ForegroundColor Yellow
    $continue = Read-Host "Continue anyway? (y/n)"
    if ($continue -ne 'y' -and $continue -ne 'Y') {
        exit 1
    }
}

try {
    # Set environment variable for current session
    $env:OPENAI_API_KEY = $apiKey
    Write-Host "✓ Environment variable set for current session" -ForegroundColor Green

    # Set persistent environment variable for current user
    [Environment]::SetEnvironmentVariable("OPENAI_API_KEY", $apiKey, "User")
    Write-Host "✓ Environment variable set persistently for current user" -ForegroundColor Green

    Write-Host ""
    Write-Host "Setup Complete!" -ForegroundColor Green
    Write-Host "===============" -ForegroundColor Green
    Write-Host "• OpenAI API key has been configured" -ForegroundColor White
    Write-Host "• The key is set for both current session and future sessions" -ForegroundColor White
    Write-Host "• Restart your IDE/terminal for changes to take effect" -ForegroundColor White
    Write-Host ""
    Write-Host "Security Note:" -ForegroundColor Yellow
    Write-Host "• Never commit API keys to version control" -ForegroundColor White
    Write-Host "• Keep your API key secure and don't share it" -ForegroundColor White
    Write-Host "• Monitor your OpenAI usage at: https://platform.openai.com/usage" -ForegroundColor White

} catch {
    Write-Host "Error setting environment variable: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}
