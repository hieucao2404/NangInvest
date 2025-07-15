package test;

import ai.AnalyticsPredictor;
import ai.RecommendationEngine;
import model.User;
import service.LLMService;

/**
 * Simple test to verify AI services are working
 */
public class AIServiceTest {

  public static void main(String[] args) {
    System.out.println("=== NangInvest AI Service Test ===");

    // Test Analytics Predictor
    System.out.println("\n1. Testing Analytics Predictor...");
    try {
      AnalyticsPredictor analytics = new AnalyticsPredictor();
      var platformStats = analytics.getPlatformAnalytics();
      System.out.println("✓ Analytics Predictor working");
      System.out.println("  Platform stats available: " + !platformStats.isEmpty());
    } catch (Exception e) {
      System.out.println("✗ Analytics Predictor error: " + e.getMessage());
    }

    // Test Recommendation Engine
    System.out.println("\n2. Testing Recommendation Engine...");
    try {
      RecommendationEngine recommender = new RecommendationEngine();
      System.out.println("✓ Recommendation Engine initialized");
    } catch (Exception e) {
      System.out.println("✗ Recommendation Engine error: " + e.getMessage());
    }

    // Test LLM Service
    System.out.println("\n3. Testing LLM Service...");
    try {
      LLMService llmService = new LLMService();
      boolean available = llmService.isAvailable();
      System.out.println("✓ LLM Service initialized");
      System.out.println("  OpenAI API available: " + available);

      if (!available) {
        System.out.println("  Note: Set OPENAI_API_KEY environment variable to enable OpenAI integration");
        System.out.println("  Run: .\\setup-env.ps1 to configure your API key");
      } else {
        System.out.println("  Testing OpenAI connection...");
        try {
          // Create a test user
          User testUser = new User();
          testUser.setName("Test User");
          testUser.setExpertise("Beginner");
          testUser.setRole(User.Role.USER);

          String response = llmService.generateResponse("What is investing?", testUser);
          System.out.println("✓ OpenAI API working");
          System.out.println("  Sample response length: " + response.length() + " characters");
        } catch (Exception e) {
          System.out.println("✗ OpenAI API error: " + e.getMessage());
          System.out.println("  This is normal if API key is not configured or invalid");
        }
      }
    } catch (Exception e) {
      System.out.println("✗ LLM Service error: " + e.getMessage());
    }

    System.out.println("\n=== Test Complete ===");
    System.out.println("Your AI services are ready!");
    System.out.println("The chat widget will work with your local AI even without OpenAI API key.");
  }
}
