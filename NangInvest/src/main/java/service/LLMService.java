/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ai.AnalyticsPredictor;
import model.User;

/**
 *
 * @author Admin
 */
public class LLMService {

    private final String apiKey;
    private final HttpClient httpClient; // Corrected variable name
    private final Gson gson;
    private final AnalyticsPredictor analyticsPredictor;

    // OpenAI API endpoints
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-3.5-turbo"; // or "gpt-4" if you have access

    public LLMService() {
        // Load form environment variables or config
        this.apiKey = System.getenv(
                "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");
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
        this.analyticsPredictor = new AnalyticsPredictor();
    }

    /**
     * Generate AI response for user query
     */
    public String generateResponse(String userMessage, User user) {
        try {
            // build context from your platform data
            String context = buildPlatformContext(user);

            // create the prompt
            String prompt = buildPrompt(userMessage, context, user);

            // call OpenAi API
            return callOpenAI(prompt);
        } catch (Exception e) {
            // fall back
            return """
                    I'm having trouble accessing my advanced language capabilities right now.
                    Let me help you with my core AI features instead! 🤖

                    Try asking me about:
                    • Course recommendations
                    • Investment analytics
                    • Learning paths
                    • Market trends
                    """;
        }
    }

    /**
     * Build platform context for better AI responses
     *
     */
    private String buildPlatformContext(User user) {
        StringBuilder context = new StringBuilder();

        // Platform information
        Map<String, Object> analytics = analyticsPredictor.getPlatformAnalytics();
        context.append("PLATFORM STATS:\n");
        context.append("- Total Users: ").append(analytics.get("totalUsers")).append("\n");
        context.append("- Total Courses: ").append(analytics.get("totalCourses")).append("\n");
        context.append("- Free Courses: ").append(analytics.get("freeCourses")).append("\n");

        // TRENDING topics
        List<String> trending = analyticsPredictor.getTrendingTopics();
        if (!trending.isEmpty()) {
            context.append("- Trending Topics: ")
                    .append(String.join(", ", trending.subList(0, Math.min(3, trending.size())))).append("\n");
        }

        // User- specific context
        if (user != null) {
            context.append("\nUSER PROFILE:\n");
            context.append("-Name: ").append(user.getName()).append("\n");
            context.append(" - Expertise: ").append(user.getExpertise()).append("\n");
            context.append("- Role: ").append(user.getRole()).append("\n");

            // User interests
            List<String> interests = analyticsPredictor.predictUserInterests(user.getUserId());
            if (!interests.isEmpty()) {
                context.append("- AI-Detected Interests: ").append(String.join(", ", interests)).append("\n");
            }

            // Engaement level
            Map<Integer, Double> churnRisk = analyticsPredictor.predictUserChurnRisk();
            Double userRisk = churnRisk.get(user.getUserId());
            if (userRisk != null) {
                double engagement = Math.max(0, 10 - userRisk);
                context.append("- Engagement Level: ").append(String.format("%.1f", engagement)).append("/10\n");
            }
        }

        return context.toString();
    }

    /**
     * Build the complete prompt for OpenAI
     */
    private String buildPrompt(String userMessage, String context, User user) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are an AI Learning Assistant for NangInvest, an investment education platform. ");
        prompt.append(
                "You help users learn about investing, recommend courses and books, and provide personalized guidance.\n\n");

        prompt.append("INSTRUCTIONS:\n");
        prompt.append("- Be helpful, friendly, and encouraging\n");
        prompt.append("- Focus on investment education and learning\n");
        prompt.append("- Use emojis appropriately for engagement\n");
        prompt.append("- Provide actionable advice and recommendations\n");
        prompt.append("- Keep responses concise but informative\n");
        prompt.append("- Use the platform context to personalize responses\n");
        prompt.append("- Encourage users to explore courses, books, and platform features\n\n");

        prompt.append("PLATFORM CONTEXT:\n");
        prompt.append(context).append("\n");

        prompt.append("USER QUESTION: ").append(userMessage).append("\n\n");

        prompt.append("RESPONSE (provide a helpful, personalized response as NangInvest's AI Learning Assistant):");

        return prompt.toString();
    }

    /**
     * call OpenAI API
     */
    private String callOpenAI(String prompt) throws Exception {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new Exception("Open API key not configured");
        }

        // Build request payload
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", MODEL);
        requestBody.addProperty("max_tokens", 500);
        requestBody.addProperty("temperature", 0.7);

        JsonArray messages = new JsonArray();
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);
        messages.add(message);
        requestBody.add("message", messages);

        // create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        // Send request
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Parse response
            JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);
            JsonArray choices = responseJson.getAsJsonArray("choices");

            if (choices.size() > 0) {
                JsonObject choice = choices.get(0).getAsJsonObject();
                JsonObject responseMessage = choice.getAsJsonObject("message");
                String content = responseMessage.get("content").getAsString();

                return content.trim();
            } else {
                throw new Exception("No response choices from OpenAI");
            }
        } else {
            // Handle API errors
            JsonObject errorResponse = gson.fromJson(response.body(), JsonObject.class);
            String errorMessage = "OpenAI API Error: " + response.statusCode();

            if (errorResponse.has("error")) {
                JsonObject error = errorResponse.getAsJsonObject("error");
                if (error.has("message")) {
                    errorMessage += " - " + error.get("message").getAsString();
                }
            }

            throw new Exception(errorMessage);
        }

    }

    /**
     * Check if LLM service is available
     */
    public boolean isAvailable() {
        return apiKey != null && !apiKey.isEmpty();
    }

    public String getEnhancedResponse(String userMessage, User user, String yourAIResponse) {
        try {
            if (!isAvailable()) {
                return yourAIResponse; // Fallback to your AI
            }

            // Create enhanced prompt that includes your AI's response
            String enhancedPrompt = buildEnhancedPrompt(userMessage, user, yourAIResponse);
            String llmResponse = callOpenAI(enhancedPrompt);

            // Add signature to show it's enhanced
            return llmResponse + "\n\n*🤖 Enhanced with AI language understanding*";

        } catch (Exception e) {
            // Fallback to your original AI response
            return yourAIResponse;
        }
    }

    /**
     * Build enhanced prompt that combines your AI data with natural language
     * processing
     */
    private String buildEnhancedPrompt(String userMessage, User user, String aiResponse) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are enhancing a response from NangInvest's AI system. ");
        prompt.append("The AI has provided data-driven insights, and you should make the response more natural, ");
        prompt.append("conversational, and engaging while preserving all the important information.\n\n");

        prompt.append("USER QUESTION: ").append(userMessage).append("\n\n");
        prompt.append("AI SYSTEM RESPONSE (contains accurate data and recommendations):\n");
        prompt.append(aiResponse).append("\n\n");

        prompt.append("INSTRUCTIONS:\n");
        prompt.append("- Rewrite the response to be more natural and conversational\n");
        prompt.append("- Keep ALL data, recommendations, and insights from the AI response\n");
        prompt.append("- Add smooth transitions and natural language flow\n");
        prompt.append("- Maintain the same helpful tone and emojis\n");
        prompt.append("- Don't add information not in the original response\n");
        prompt.append("- Keep the response length similar\n\n");

        prompt.append("ENHANCED RESPONSE:");

        return prompt.toString();
    }
}
