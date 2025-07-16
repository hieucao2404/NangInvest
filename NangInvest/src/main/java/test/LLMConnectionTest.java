package test;

import service.LLMService;

/**
 * Simple diagnostic to test LLM Service connection with stubbed Gemini API
 */
public class LLMConnectionTest {
  public static void main(String[] args) {
    System.out.println("=== LLM Service Connection Test ===");

    try {

      // Use real LLMService for actual API key/config test
      LLMService llmService = new LLMService();

      // Print API key source (for diagnostics only, do not log real key)
      String envKey = System.getenv("GEMINI_API_KEY");
      String sysPropKey = System.getProperty("gemini.api.key");
      String configKey = null;
      try {
        java.util.Properties props = new java.util.Properties();
        try (java.io.FileInputStream fis = new java.io.FileInputStream("config.properties")) {
          props.load(fis);
          configKey = props.getProperty("gemini.api.key");
        }
      } catch (Exception e) {
        // ignore
      }
      System.out.println("API Key Sources:");
      System.out.println("- GEMINI_API_KEY env: " + (envKey != null && !envKey.isEmpty() ? "FOUND" : "NOT FOUND"));
      System.out.println(
          "- gemini.api.key sysprop: " + (sysPropKey != null && !sysPropKey.isEmpty() ? "FOUND" : "NOT FOUND"));
      System.out.println("- config.properties: " + (configKey != null && !configKey.isEmpty() ? "FOUND" : "NOT FOUND"));

      // Check if API key is configured and test real Gemini API
      System.out.println("LLM Service Available: " + llmService.isAvailable());

      if (llmService.isAvailable()) {
        System.out.println("✅ API Key is configured");

        // Test simple query
        System.out.println("\n--- Testing Simple Query (Real Gemini API) ---");
        String testMessage = "Hello, can you help me with investment advice?";

        try {
          String response = llmService.generateResponse(testMessage, null);
          System.out.println("User: " + testMessage);
          System.out.println("AI: " + response);
          if (response != null && !response.contains("trouble accessing my advanced language capabilities")) {
            System.out.println("✅ LLM Service is working and Gemini API responded!");
          } else {
            System.out.println("⚠️  LLM Service fallback response. Check API key and network.");
          }
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