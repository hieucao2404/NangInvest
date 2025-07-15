package test;

import service.LLMService;
import service.StubLLMService;

/**
 * Simple diagnostic to test LLM Service connection with stubbed Gemini API
 */
public class LLMConnectionTest {
  public static void main(String[] args) {
    System.out.println("=== LLM Service Connection Test ===");

    try {
      // Use stubbed LLMService
      LLMService llmService = new StubLLMService();

      // Check if API key is configured
      System.out.println("LLM Service Available: " + llmService.isAvailable());

      if (llmService.isAvailable()) {
        System.out.println("✅ API Key is configured");

        // Test simple query
        System.out.println("\n--- Testing Simple Query ---");
        String testMessage = "Hello, can you help me with investment advice?";

        try {
          String response = llmService.generateResponse(testMessage, null);
          System.out.println("User: " + testMessage);
          System.out.println("AI: " + response);
          System.out.println("✅ LLM Service is working!");
        } catch (Exception e) {
          System.out.println("❌ Exception in generateResponse: " + e.getMessage());
          e.printStackTrace();
        }

      } else {
        System.out.println("❌ API Key not configured properly");
        System.out.println("Please check:");
        System.out.println("1. Environment variable GEMINI_API_KEY");
        System.out.println("2. System property gemini.api.key");
        System.out.println("3. config.properties file");
      }

    } catch (Exception e) {
      System.out.println("❌ Error testing LLM Service: " + e.getMessage());
      e.printStackTrace();
    }
  }
}