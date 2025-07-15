package service;

import model.User;
import ai.AnalyticsPredictor;
import java.util.*;

public class StubLLMService extends LLMService {

    public StubLLMService() {
    }

   

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String generateResponse(String userMessage, User user) {
        StringBuilder response = new StringBuilder("Stub Gemini response for: " + userMessage + "\n");
        if (user == null) {
            response.append("No user context provided.\n");
        } else {
            response.append("User: ").append(user.getName() != null ? user.getName() : "Anonymous").append("\n");
        }
        response.append("Platform Stats: 1000 users, 50 courses, 20 free courses\n");
        response.append("Trending Topics: Stocks, Crypto\n");
        return response.toString();
    }

    @Override
    public String getEnhancedResponse(String userMessage, User user, String yourAIResponse) {
        return yourAIResponse + " (Stub Gemini Enhanced)";
    }
}

class StubAnalyticsPredictor extends AnalyticsPredictor {

    @Override
    public Map<String, Double> predictCoursePopularity() {
        Map<String, Double> courses = new HashMap<>();
        courses.put("Investment Fundamentals", 8.5);
        courses.put("Portfolio Management", 7.8);
        return courses;
    }


    public List<String> predictUserInterests(int userId) {
        return Arrays.asList("Trading", "Investing");
    }

    @Override
    public Map<Integer, Double> predictUserChurnRisk() {
        Map<Integer, Double> churnRisk = new HashMap<>();
        churnRisk.put(1, 2.0);
        return churnRisk;
    }

    @Override
    public List<String> getTrendingTopics() {
        return Arrays.asList("Stocks", "Crypto");
    }

    @Override
    public Map<String, Object> getPlatformAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalUsers", 1000);
        analytics.put("totalCourses", 50);
        analytics.put("freeCourses", 20);
        return analytics;
    }
}
