# NangInvest AI Features Guide

## Overview

NangInvest includes a comprehensive AI system with both local AI capabilities and optional OpenAI integration for enhanced natural language processing.

## AI Components

### 1. Local AI Engine

Your application includes three core AI components that work without external dependencies:

- **AnalyticsPredictor**: Analyzes user behavior, predicts churn risk, and identifies trending topics
- **RecommendationEngine**: Provides personalized course and book recommendations
- **ContentGenerator**: Generates dynamic content based on user profiles

### 2. OpenAI Integration (Optional)

The LLMService provides enhanced natural language processing using OpenAI's GPT models.

## Chat Widget Features

The AI chat widget provides:

- **Course Recommendations**: Personalized based on user profile and AI analysis
- **Investment Education**: Answers questions about investing basics
- **Book Suggestions**: Recommends books based on user interests
- **Market Insights**: Provides analytics-driven market information
- **Learning Paths**: Creates personalized learning journeys

## Setup Instructions

### Option 1: Quick Setup (Hardcoded API Key)

**Simplest for development - API key always available:**

1. Edit `src/main/java/service/LLMService.java`
2. Replace `"sk-proj-YOUR_API_KEY_HERE"` with your actual OpenAI API key
3. Restart your server
4. ✅ AI chat will work immediately every time you start the project

### Option 2: Config File Setup

**Easy setup with external configuration:**

1. Edit `config.properties` in your project root
2. Replace `sk-proj-YOUR_API_KEY_HERE` with your actual API key
3. Restart your server
4. ✅ AI chat will work automatically

### Option 3: Environment Variable (Most Secure)

**For production or if you're concerned about security:**

1. Run the setup script:
   ```powershell
   .\setup-env.ps1
   ```
2. Enter your OpenAI API key when prompted
3. Restart your IDE/server
4. The chat will now use both local AI + OpenAI for enhanced responses

### Manual OpenAI Setup

If you prefer manual setup:

```powershell
# Set environment variable for current session
$env:OPENAI_API_KEY = "your-api-key-here"

# Set persistent environment variable
[Environment]::SetEnvironmentVariable("OPENAI_API_KEY", "your-api-key-here", "User")
```

### Basic Setup (Local AI Only)

Your local AI components work out of the box without any configuration. The chat widget will provide data-driven responses using your analytics even without OpenAI.

## API Key Security

⚠️ **IMPORTANT SECURITY NOTES:**

- Never commit API keys to version control
- The code now properly loads keys from environment variables
- Keep your API key secure and don't share it
- Monitor your OpenAI usage at: https://platform.openai.com/usage

## Testing Your Setup

Run the AI service test:

```bash
mvn compile
java -cp target/classes test.AIServiceTest
```

This will verify:

- ✓ Analytics Predictor functionality
- ✓ Recommendation Engine functionality
- ✓ LLM Service availability
- ✓ OpenAI API connection (if configured)

## Chat Widget Integration

The chat widget is automatically included in pages that use:

```jsp
<%@include file="../includes/ai-chat-widget.jsp" %>
```

It provides:

- Floating chat button in bottom-right corner
- Real-time AI responses
- Quick action buttons for common queries
- Responsive design for mobile/desktop

## How It Works

1. **User sends message** → Chat widget
2. **Message sent to** → AIChatServlet (/api/chat)
3. **Servlet processes with** → Your AI components (Analytics, Recommendations)
4. **Optional enhancement** → OpenAI LLM Service
5. **Response returned** → Chat widget displays to user

## Response Flow

### Without OpenAI (Local AI Only):

User Question → Analytics + Recommendations → Data-driven response

### With OpenAI (Enhanced):

User Question → Analytics + Recommendations → Data-driven response → OpenAI enhancement → Natural language response

## Customization

You can customize the AI behavior by modifying:

- `AIChatServlet.java`: Main chat logic and intent patterns
- `LLMService.java`: OpenAI integration and prompts
- `AnalyticsPredictor.java`: User analytics and predictions
- `RecommendationEngine.java`: Recommendation algorithms

## Troubleshooting

### Chat shows "connection error"

1. Check if servlet is accessible at `/api/chat`
2. Verify server is running
3. Check browser console for detailed errors

### OpenAI not working

1. Verify API key is set: `echo $env:OPENAI_API_KEY`
2. Check API key validity at OpenAI platform
3. Ensure you have sufficient API credits
4. Check server logs for API errors

### Local AI not working

1. Verify database connections
2. Check if DAOs are properly initialized
3. Review server logs for exceptions

## Performance Notes

- Local AI responses are fast (< 100ms typically)
- OpenAI responses take longer (1-3 seconds)
- The system gracefully falls back to local AI if OpenAI fails
- Chat widget shows typing indicators during processing

## Features in Chat

The AI assistant can help with:

- 📚 Course recommendations based on your profile
- 📖 Book suggestions for your investment journey
- 📈 Market trends and analytics insights
- 🎯 Personalized learning paths
- 💡 Investment education and basics
- 📊 Platform statistics and user analytics

Enjoy your AI-powered investment education platform! 🚀
