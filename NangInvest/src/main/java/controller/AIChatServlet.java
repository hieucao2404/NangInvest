package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ai.AnalyticsPredictor;
import ai.ContentGenerator;
import ai.RecommendationEngine;
import dao.BooksDAO;
import dao.CourseDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Book;
import model.Course;
import model.User;
import service.LLMService;

@WebServlet(name = "AIChatServlet", urlPatterns = { "/api/chat" })
@MultipartConfig
public class AIChatServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AIChatServlet.class);
    private final AnalyticsPredictor analyticsPredictor;
    private final RecommendationEngine recommendationEngine;
    private final ContentGenerator contentGenerator;
    private final LLMService llmService;
    private final UserDAO userDAO;
    private final CourseDAO courseDAO;
    private final BooksDAO booksDAO;
    private final Gson gson;
    private final Map<String, Pattern> intentPatterns;

    public AIChatServlet() {
        this(new AnalyticsPredictor(), new RecommendationEngine(), new ContentGenerator(),
             new LLMService(), new UserDAO(), new CourseDAO(), new BooksDAO());
    }

    public AIChatServlet(AnalyticsPredictor analyticsPredictor, RecommendationEngine recommendationEngine,
                        ContentGenerator contentGenerator, LLMService llmService,
                        UserDAO userDAO, CourseDAO courseDAO, BooksDAO booksDAO) {
        this.analyticsPredictor = analyticsPredictor;
        this.recommendationEngine = recommendationEngine;
        this.contentGenerator = contentGenerator;
        this.llmService = llmService;
        this.userDAO = userDAO;
        this.courseDAO = courseDAO;
        this.booksDAO = booksDAO;
        this.gson = new Gson();
        this.intentPatterns = initializeIntentPatterns();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String userMessage = request.getParameter("message");
        if (userMessage == null || userMessage.trim().isEmpty()) {
            logger.warn("Empty or null message received");
            sendErrorResponse(response, 400, "Message cannot be empty");
            return;
        }

        try {
            String aiResponse = processUserMessage(userMessage.trim(), user);
            sendSuccessResponse(response, aiResponse, userMessage);
        } catch (Exception e) {
            logger.error("Error processing chat request: {}", e.getMessage(), e);
            sendErrorResponse(response, 500, "I'm having some trouble right now. Please try again! 🤖");
        }
    }

    private String processUserMessage(String message, User user) {
        String lowerMessage = message.toLowerCase();

        // First use your powerful AI system
        String yourAIResponse = getYourAIResponse(message, user, lowerMessage);
        
        if (yourAIResponse != null && !yourAIResponse.contains("getSimpleFallbackResponse")) {
            return yourAIResponse;
        }

        if (llmService != null && llmService.isAvailable()) {
            try {
                String llmResponse = llmService.generateResponse(message, user);
                if (llmResponse != null && !llmResponse.contains("trouble accessing my advanced language capabilities")) {
                    return llmResponse;
                }
            } catch (Exception e) {
                logger.error("LLM Service failed: {}", e.getMessage(), e);
            }
        }

        return getSimpleFallbackResponse(message, user, lowerMessage);
    }

    private String getYourAIResponse(String message, User user, String lowerMessage) {
        if (analyticsPredictor == null && recommendationEngine == null) {
            return getSimpleFallbackResponse(message, user, lowerMessage);
        }

        if (matchesIntent(lowerMessage, "COURSE_RECOMMENDATION")) {
            return handleCourseRecommendationWithAI(user, lowerMessage);
        } else if (matchesIntent(lowerMessage, "BOOK_RECOMMENDATION")) {
            return handleBookRecommendationWithAI(user, lowerMessage);
        } else if (matchesIntent(lowerMessage, "LEARNING_PATH")) {
            return handleLearningPathWithAI(user, lowerMessage);
        } else if (matchesIntent(lowerMessage, "ANALYTICS")) {
            return handleAnalyticsWithAI(user);
        } else if (matchesIntent(lowerMessage, "MARKET_TRENDS")) {
            return handleMarketTrendsWithAI();
        } else if (matchesIntent(lowerMessage, "USER_INTERESTS")) {
            return handleUserInterestsWithAI(user);
        } else if (matchesIntent(lowerMessage, "PLATFORM_STATS")) {
            return handlePlatformStatsWithAI();
        } else if (matchesIntent(lowerMessage, "HELP")) {
            return handleHelp();
        } else {
            return handleGeneralQueryWithAI(message, user);
        }
    }

    private String getSimpleFallbackResponse(String message, User user, String lowerMessage) {
        if (lowerMessage.contains("course") || lowerMessage.contains("class") || lowerMessage.contains("learn")) {
            return "📚 **Course Recommendations:**\n\n" +
                   "I'd love to recommend courses for you! Here are some popular options:\n\n" +
                   "• **Investment Fundamentals** - Great for beginners\n" +
                   "• **Portfolio Management** - Intermediate level\n" +
                   "• **Advanced Trading Strategies** - For experienced investors\n\n" +
                   "Please check our course catalog for more details! 🎓";
        }
        if (lowerMessage.contains("book") || lowerMessage.contains("read")) {
            return "📖 **Book Recommendations:**\n\n" +
                   "Here are some excellent investment books:\n\n" +
                   "• **The Intelligent Investor** by Benjamin Graham\n" +
                   "• **A Random Walk Down Wall Street** by Burton Malkiel\n" +
                   "• **Rich Dad Poor Dad** by Robert Kiyosaki\n\n" +
                   "These are timeless classics for any investor! 📚";
        }
        if (lowerMessage.contains("help")) {
            return "🤖 **I'm here to help!**\n\n" +
                   "I can assist you with:\n" +
                   "• Course recommendations\n" +
                   "• Book suggestions\n" +
                   "• Investment basics\n" +
                   "• Learning paths\n\n" +
                   "Just ask me anything about investing and education! 😊";
        }
        if (lowerMessage.contains("hello") || lowerMessage.contains("hi") || lowerMessage.contains("hey")) {
            String greeting = user != null ? "Hello " + user.getName() + "!" : "Hello!";
            return "🤖 **" + greeting + "** I'm your AI investment assistant.\n\n" +
                   "I can help you with course recommendations, book suggestions, and investment guidance.\n\n" +
                   "What would you like to learn about today? 😊";
        }
        return "🤖 **Hello!** I'm your AI investment assistant.\n\n" +
               "I can help you with course recommendations, book suggestions, and investment guidance.\n\n" +
               "What would you like to learn about today? 😊";
    }

    private String handleCourseRecommendationWithAI(User user, String message) {
        StringBuilder response = new StringBuilder();
        try {
            if (user == null) {
                Map<String, Double> popularCourses = analyticsPredictor.predictCoursePopularity();
                response.append("🎓 **Popular Course Recommendations:**\n\n");
                response.append("I'd love to give you personalized recommendations! For now, here are our trending courses:\n\n");
                popularCourses.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(5)
                    .forEach(entry -> {
                        response.append("⭐ **").append(entry.getKey()).append("**\n");
                        response.append("   Popularity Score: ").append(String.format("%.1f", entry.getValue())).append("/10\n\n");
                    });
                response.append("💡 *Log in to get AI-powered personalized recommendations!*");
                return response.toString();
            }

            List<Course> recommendations = recommendationEngine.recommendCourses(user);
            List<String> userInterests = analyticsPredictor.predictUserInterests(user.getUserId());
            response.append("🎓 **AI-Powered Course Recommendations for ").append(user.getName()).append(":**\n\n");
            if (!userInterests.isEmpty()) {
                response.append("🧠 **Based on your interests:** ").append(String.join(", ", userInterests)).append("\n\n");
            }
            if (recommendations.isEmpty()) {
                response.append("I'm still learning about your preferences! Here are some great starter courses:\n\n");
                Map<String, Double> popularCourses = analyticsPredictor.predictCoursePopularity();
                popularCourses.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(3)
                    .forEach(entry -> response.append("• ").append(entry.getKey()).append("\n"));
            } else {
                response.append("**My top picks for you:**\n\n");
                for (int i = 0; i < Math.min(5, recommendations.size()); i++) {
                    Course course = recommendations.get(i);
                    response.append("**").append(i + 1).append(". ").append(course.getCourseName()).append("**\n");
                    response.append("   💰 Price: ").append(course.getIsFree() ? "FREE" : "$" + course.getPrice()).append("\n");
                    response.append("   ⏱️ Duration: ").append(course.getTime()).append("\n");
                    if (user.getExpertise() != null) {
                        response.append("   🎯 Perfect match for your ").append(user.getExpertise()).append(" background\n");
                    }
                    response.append("\n");
                }
            }
            response.append("Would you like more details about any specific course? 😊");
            return response.toString();
        } catch (Exception e) {
            logger.error("Error in course recommendation: {}", e.getMessage(), e);
            return getSimpleFallbackResponse(message, user, message.toLowerCase());
        }
    }

    private String handleBookRecommendationWithAI(User user, String message) {
        StringBuilder response = new StringBuilder();
        try {
            if (user == null) {
                response.append("📚 **Popular Book Recommendations:**\n\n");
                response.append("Here are some excellent investment books to get you started:\n\n");
                response.append("⭐ **The Intelligent Investor** by Benjamin Graham\n");
                response.append("   📖 Topic: Value Investing Fundamentals\n");
                response.append("   💡 Perfect for beginners\n\n");
                response.append("⭐ **A Random Walk Down Wall Street** by Burton Malkiel\n");
                response.append("   📖 Topic: Market Theory & Index Investing\n");
                response.append("   💡 Great for understanding markets\n\n");
                response.append("⭐ **Rich Dad Poor Dad** by Robert Kiyosaki\n");
                response.append("   📖 Topic: Financial Literacy & Mindset\n");
                response.append("   💡 Essential financial education\n\n");
                response.append("💡 *Log in to get personalized AI recommendations!*");
                return response.toString();
            }

            List<Book> recommendations = recommendationEngine.recommendBooks(user);
            List<String> userInterests = analyticsPredictor.predictUserInterests(user.getUserId());
            response.append("📚 **AI Book Recommendations for ").append(user.getName()).append(":**\n\n");
            if (userInterests != null && !userInterests.isEmpty()) {
                response.append("🧠 **Tailored to your interests:** ").append(String.join(", ", userInterests)).append("\n\n");
            }
            if (recommendations == null || recommendations.isEmpty()) {
                response.append("Based on your ").append(user.getExpertise()).append(" level, here are my top picks:\n\n");
                if ("Beginner".equalsIgnoreCase(user.getExpertise())) {
                    response.append("• **The Intelligent Investor** - Warren Buffett's favorite book\n");
                    response.append("• **The Bogleheads' Guide to Investing** - Simple, proven strategies\n");
                    response.append("• **Your Money or Your Life** - Financial independence basics\n\n");
                } else if ("Intermediate".equalsIgnoreCase(user.getExpertise())) {
                    response.append("• **Security Analysis** - Advanced Graham & Dodd principles\n");
                    response.append("• **The Little Book of Common Sense Investing** - Index fund mastery\n");
                    response.append("• **Options as a Strategic Investment** - Advanced trading\n\n");
                } else {
                    response.append("• **Quantitative Portfolio Theory** - Mathematical approaches\n");
                    response.append("• **Market Wizards** - Professional trader insights\n");
                    response.append("• **The Art of Currency Trading** - Advanced forex strategies\n\n");
                }
            } else {
                response.append("**My AI-curated selection for you:**\n\n");
                for (int i = 0; i < Math.min(5, recommendations.size()); i++) {
                    Book book = recommendations.get(i);
                    response.append("**").append(i + 1).append(". ").append(book.getBookName()).append("**\n");
                    response.append("   📖 Topic: ").append(book.getTopic()).append("\n");
                    if (book.getRating() != null) {
                        response.append("   ⭐ Rating: ").append(book.getRating()).append("/5\n");
                    }
                    if (book.getIsPreviewAvailable()) {
                        response.append("   👀 Preview available\n");
                    }
                    response.append("\n");
                }
            }
            response.append("Want affiliate links to purchase any of these books? 📖");
            return response.toString();
        } catch (Exception e) {
            logger.error("Error in book recommendation: {}", e.getMessage(), e);
            return getSimpleFallbackResponse(message, user, message.toLowerCase());
        }
    }

    private String handleLearningPathWithAI(User user, String lowerMessage) {
        StringBuilder response = new StringBuilder();
        try {
            if (user == null) {
                List<String> trendingTopics = analyticsPredictor.getTrendingTopics();
                Map<String, Double> popularCourses = analyticsPredictor.predictCoursePopularity();
                response.append("🗺️ **AI-Curated Learning Roadmap:**\n\n");
                response.append("**🚀 Recommended Path for New Investors:**\n\n");
                response.append("**Phase 1: Foundation (Weeks 1-4)**\n");
                response.append("1. Investment Fundamentals (FREE course)\n");
                response.append("2. Understanding Risk vs. Return\n");
                response.append("3. Basic Portfolio Theory\n\n");
                response.append("**Phase 2: Trending Focus (Weeks 5-8)**\n");
                if (!trendingTopics.isEmpty()) {
                    response.append("4. ").append(trendingTopics.get(0)).append(" (Hot topic!)\n");
                    if (trendingTopics.size() > 1) {
                        response.append("5. ").append(trendingTopics.get(1)).append("\n");
                    }
                }
                response.append("6. Market Analysis Basics\n\n");
                response.append("**Phase 3: Specialization (Weeks 9-12)**\n");
                response.append("7. Advanced Portfolio Management\n");
                response.append("8. Alternative Investments\n");
                response.append("9. Personal Finance Integration\n\n");
                response.append("💡 *Log in for a personalized AI learning path based on your goals and experience!*");
                return response.toString();
            }

            List<String> userInterests = analyticsPredictor.predictUserInterests(user.getUserId());
            List<Course> recommendedCourses = recommendationEngine.recommendCourses(user);
            List<Book> recommendedBooks = recommendationEngine.recommendBooks(user);
            Map<Integer, Double> churnRisk = analyticsPredictor.predictUserChurnRisk();
            response.append("🗺️ **Your Personalized AI Learning Path, ").append(user.getName()).append(":**\n\n");
            Double userChurnRisk = churnRisk.get(user.getUserId());
            double engagementLevel = userChurnRisk != null ? Math.max(0, 10 - userChurnRisk) : 5.0;
            String experienceLevel = determineExperienceLevel(user, userInterests, engagementLevel);
            response.append("**🧠 AI Analysis:**\n");
            response.append("• Experience Level: ").append(experienceLevel).append("\n");
            response.append("• Expertise Area: ").append(user.getExpertise()).append("\n");
            response.append("• Engagement Score: ").append(String.format("%.1f", engagementLevel)).append("/10\n");
            if (!userInterests.isEmpty()) {
                response.append("• Primary Interest: ").append(userInterests.get(0)).append("\n");
            }
            response.append("\n");
            if (experienceLevel.equals("Beginner") || userInterests.isEmpty()) {
                response.append(createBeginnerPath(user, recommendedCourses, recommendedBooks));
            } else if (experienceLevel.equals("Intermediate")) {
                response.append(createIntermediatePath(user, userInterests, recommendedCourses, recommendedBooks));
            } else {
                response.append(createAdvancedPath(user, userInterests, recommendedCourses, recommendedBooks));
            }
            response.append("**🎯 AI-Recommended Next Steps:**\n");
            if (!recommendedCourses.isEmpty()) {
                response.append("• **Priority Course:** ").append(recommendedCourses.get(0).getCourseName()).append("\n");
            }
            if (!recommendedBooks.isEmpty()) {
                response.append("• **Essential Reading:** ").append(recommendedBooks.get(0).getBookName()).append("\n");
            }
            if (engagementLevel < 3) {
                response.append("• **Boost Engagement:** Try shorter, more interactive content\n");
            } else if (engagementLevel > 7) {
                response.append("• **Keep Momentum:** You're doing great! Consider advanced topics\n");
            }
            response.append("\n💡 *This path adapts based on your progress and interests. Keep learning!*");
            return response.toString();
        } catch (Exception e) {
            logger.error("Error in learning path: {}", e.getMessage(), e);
            return getSimpleFallbackResponse(lowerMessage, user, lowerMessage);
        }
    }

    private String handleAnalyticsWithAI(User user) {
        StringBuilder response = new StringBuilder();
        try {
            if (user == null) {
                Map<String, Object> platformAnalytics = analyticsPredictor.getPlatformAnalytics();
                response.append("📊 **Platform Analytics Dashboard:**\n\n");
                response.append("**📈 Current Stats:**\n");
                response.append("• Total Users: ").append(platformAnalytics.get("totalUsers")).append("\n");
                response.append("• Active Users: ").append(platformAnalytics.get("activeUsers")).append("\n");
                response.append("• Total Courses: ").append(platformAnalytics.get("totalCourses")).append("\n");
                response.append("• Free Courses: ").append(platformAnalytics.get("freeCourses")).append("\n");
                response.append("• Total Orders: ").append(platformAnalytics.get("totalOrders")).append("\n\n");
                response.append("**🔥 Trending Topics:**\n");
                List<String> trending = analyticsPredictor.getTrendingTopics();
                for (int i = 0; i < Math.min(3, trending.size()); i++) {
                    response.append("• ").append(trending.get(i)).append("\n");
                }
                response.append("\n💡 *Log in to see your personalized analytics!*");
                return response.toString();
            }

            List<String> userInterests = analyticsPredictor.predictUserInterests(user.getUserId());
            Map<Integer, Double> churnRisk = analyticsPredictor.predictUserChurnRisk();
            response.append("📊 **Your Personal Analytics, ").append(user.getName()).append(":**\n\n");
            response.append("**🎯 Your Interest Profile:**\n");
            if (userInterests.isEmpty()) {
                response.append("• Still building your profile... Take more courses!\n");
            } else {
                for (int i = 0; i < userInterests.size(); i++) {
                    response.append("• ").append(userInterests.get(i)).append(" (Interest Level: ").append(5 - i).append("/5)\n");
                }
            }
            response.append("\n**📈 Your Engagement:**\n");
            response.append("• Expertise Area: ").append(user.getExpertise()).append("\n");
            response.append("• Account Level: ").append(user.getRole()).append("\n");
            Double userChurnRisk = churnRisk.get(user.getUserId());
            if (userChurnRisk != null) {
                double engagementScore = Math.max(0, 10 - userChurnRisk);
                response.append("• Engagement Score: ").append(String.format("%.1f", engagementScore)).append("/10\n");
            }
            response.append("\n**🚀 Recommendations:**\n");
            if (userInterests.size() < 3) {
                response.append("• Explore more courses to build your interest profile\n");
                response.append("• Try books in your expertise area: ").append(user.getExpertise()).append("\n");
            } else {
                response.append("• You're highly engaged! Consider advanced courses\n");
                response.append("• Share your knowledge with our community\n");
            }
            return response.toString();
        } catch (Exception e) {
            logger.error("Error in analytics: {}", e.getMessage(), e);
            return getSimpleFallbackResponse("analytics", user, "analytics");
        }
    }

    private String handleMarketTrendsWithAI() {
        StringBuilder response = new StringBuilder();
        try {
            List<String> trendingTopics = analyticsPredictor.getTrendingTopics();
            Map<String, Double> coursePopularity = analyticsPredictor.predictCoursePopularity();
            response.append("📊 **AI Market Trends Analysis:**\n\n");
            response.append("**🔥 Hot Topics Right Now:**\n");
            for (int i = 0; i < Math.min(5, trendingTopics.size()); i++) {
                response.append((i + 1)).append(". ").append(trendingTopics.get(i));
                if (i == 0) response.append(" 🚀 (Trending #1)");
                response.append("\n");
            }
            response.append("\n**📈 Course Demand Predictions:**\n");
            coursePopularity.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> {
                    response.append("• ").append(entry.getKey())
                           .append(" (Score: ").append(String.format("%.1f", entry.getValue())).append(")\n");
                });
            response.append("\n**🎯 AI Insights:**\n");
            response.append("• Investment education demand is surging\n");
            response.append("• Users prefer practical, hands-on learning\n");
            response.append("• Mobile-friendly content performs best\n\n");
            response.append("Want course recommendations based on these trends? 🚀");
            return response.toString();
        } catch (Exception e) {
            logger.error("Error in market trends: {}", e.getMessage(), e);
            return getSimpleFallbackResponse("trends", null, "trends");
        }
    }

    private String handleUserInterestsWithAI(User user) {
        StringBuilder response = new StringBuilder();
        try {
            if (user == null) {
                return "🔐 **Interest Analysis:**\n\nPlease log in to see your personalized interest analysis based on your activity and AI predictions!";
            }
            List<String> interests = analyticsPredictor.predictUserInterests(user.getUserId());
            response.append("🧠 **Your AI-Analyzed Interests, ").append(user.getName()).append(":**\n\n");
            if (interests.isEmpty()) {
                response.append("🌱 **Just Getting Started:**\n");
                response.append("I haven't detected strong interest patterns yet. Here's how to build your profile:\n\n");
                response.append("• Take a few courses in your expertise area: ").append(user.getExpertise()).append("\n");
                response.append("• Click on books that interest you\n");
                response.append("• Add courses to your cart\n");
                response.append("• The more you engage, the better my recommendations!\n\n");
                response.append("Want me to recommend some starter courses? 🚀");
            } else {
                response.append("**🎯 Your Top Interest Areas:**\n");
                for (int i = 0; i < interests.size(); i++) {
                    String level = i == 0 ? "Very High" : i == 1 ? "High" : i == 2 ? "Medium" : "Growing";
                    response.append((i + 1)).append(". **").append(interests.get(i)).append("** (").append(level).append(" Interest)\n");
                }
                response.append("\n**🤖 AI Analysis:**\n");
                response.append("• Your primary focus is ").append(interests.get(0)).append("\n");
                if (interests.size() > 1) {
                    response.append("• You're also exploring ").append(interests.get(1)).append("\n");
                }
                response.append("• Your expertise in ").append(user.getExpertise()).append(" shows in your choices\n\n");
                response.append("Want personalized course recommendations based on these interests? 📚");
            }
            return response.toString();
        } catch (Exception e) {
            logger.error("Error in user interests: {}", e.getMessage(), e);
            return getSimpleFallbackResponse("interests", user, "interests");
        }
    }

    private String handlePlatformStatsWithAI() {
        StringBuilder response = new StringBuilder();
        try {
            Map<String, Object> analytics = analyticsPredictor.getPlatformAnalytics();
            response.append("📊 **NangInvest Platform Intelligence:**\n\n");
            response.append("**👥 Community Stats:**\n");
            response.append("• Total Learners: ").append(analytics.get("totalUsers")).append("\n");
            response.append("• Active This Month: ").append(analytics.get("activeUsers")).append("\n");
            response.append("• Growth Rate: ").append(analytics.get("userGrowthRate")).append("%\n\n");
            response.append("**📚 Learning Library:**\n");
            response.append("• Total Courses: ").append(analytics.get("totalCourses")).append("\n");
            response.append("• Free Courses: ").append(analytics.get("freeCourses")).append("\n");
            response.append("• Premium Courses: ").append(analytics.get("paidCourses")).append("\n\n");
            response.append("**💼 Business Metrics:**\n");
            response.append("• Total Orders: ").append(analytics.get("totalOrders")).append("\n");
            response.append("• Pending Orders: ").append(analytics.get("pendingOrders")).append("\n");
            response.append("• Completed Orders: ").append(analytics.get("completedOrders")).append("\n\n");
            response.append("**🎯 Engagement:**\n");
            response.append("• Total Affiliate Clicks: ").append(analytics.get("totalClicks")).append("\n");
            response.append("• Blog Articles: ").append(analytics.get("totalBlogs")).append("\n");
            response.append("• Avg Cart Size: ").append(String.format("%.1f", (Double) analytics.get("averageCartSize"))).append(" items\n\n");
            response.append("🤖 **AI-Powered platform delivering personalized education!**");
            return response.toString();
        } catch (Exception e) {
            logger.error("Error in platform stats: {}", e.getMessage(), e);
            return getSimpleFallbackResponse("stats", null, "stats");
        }
    }

    private String handleGeneralQueryWithAI(String message, User user) {
        String lowerMessage = message.toLowerCase();
        try {
            if (lowerMessage.contains("price") || lowerMessage.contains("cost") || lowerMessage.contains("free")) {
                Map<String, Object> analytics = analyticsPredictor.getPlatformAnalytics();
                return "💰 **Smart Pricing Information:**\n\n" +
                       "**📊 Current Offering:**\n" +
                       "• Free Courses: " + analytics.get("freeCourses") + " available\n" +
                       "• Premium Courses: " + analytics.get("paidCourses") + " available\n" +
                       "• Average Value: Exceptional ROI on education\n\n" +
                       "**🎯 AI Recommendation:**\n" +
                       "Start with our free courses to explore your interests, then invest in premium courses for your top interest areas!\n\n" +
                       "Want me to find free courses in your area of interest? 🚀";
            }
            if (lowerMessage.contains("beginner") || lowerMessage.contains("start") || lowerMessage.contains("new")) {
                List<String> trendingTopics = analyticsPredictor.getTrendingTopics();
                StringBuilder response = new StringBuilder();
                response.append("🌱 **Perfect Starting Point!**\n\n");
                response.append("**🤖 AI-Curated Beginner Path:**\n");
                response.append("1. Start with Investment Fundamentals (FREE)\n");
                response.append("2. Learn Risk Management Basics\n");
                response.append("3. Explore trending topic: ").append(trendingTopics.get(0)).append("\n\n");
                if (user != null) {
                    response.append("**🎯 Personalized for ").append(user.getName()).append(":**\n");
                    response.append("Since your expertise is ").append(user.getExpertise()).append(", I recommend starting there!\n\n");
                }
                response.append("**🚀 Why Start with Us:**\n");
                response.append("• AI-powered personalized learning\n");
                response.append("• Real-world examples and case studies\n");
                response.append("• Community of ").append(analyticsPredictor.getPlatformAnalytics().get("totalUsers")).append("+ learners\n\n");
                response.append("Ready to begin? Ask me \"recommend beginner courses\"! 🎓");
                return response.toString();
            }
            return "🤖 **I'm your AI Learning Assistant!**\n\n" +
                   "I use advanced analytics to understand what you need. I can help with:\n\n" +
                   "📚 **Smart Recommendations** - \"recommend courses\" or \"suggest books\"\n" +
                   "🧠 **Interest Analysis** - \"analyze my interests\" or \"what do I like?\"\n" +
                   "📊 **Trend Analysis** - \"what's trending?\" or \"market insights\"\n" +
                   "📈 **Personal Analytics** - \"my progress\" or \"show my stats\"\n\n" +
                   "💡 **I learn from your behavior** to provide better recommendations!\n\n" +
                   "What would you like to explore? I'm powered by real AI! 🚀";
        } catch (Exception e) {
            logger.error("Error in general query: {}", e.getMessage(), e);
            return getSimpleFallbackResponse(message, user, lowerMessage);
        }
    }

    private String handleHelp() {
        return "🤖 **Your AI-Powered Learning Assistant!**\n\n" +
               "I use real analytics and machine learning to help you succeed!\n\n" +
               "**🧠 AI Capabilities:**\n" +
               "📚 **Smart Course Recommendations** - \"recommend courses\"\n" +
               "📖 **Personalized Book Suggestions** - \"suggest books\"\n" +
               "🎯 **Interest Analysis** - \"analyze my interests\"\n" +
               "📊 **Market Trend Analysis** - \"what's trending?\"\n" +
               "📈 **Personal Analytics** - \"show my progress\"\n" +
               "🗺️ **Learning Paths** - \"create learning path\"\n" +
               "📋 **Platform Statistics** - \"platform stats\"\n\n" +
               "**🚀 Quick Commands:**\n" +
               "• \"courses\" - Get AI course recommendations\n" +
               "• \"books\" - Get AI book suggestions\n" +
               "• \"learning path\" - Get personalized roadmap\n" +
               "• \"trends\" - See what's hot right now\n" +
               "• \"my interests\" - Analyze your preferences\n" +
               "• \"stats\" - Your personal analytics\n\n" +
               "I understand natural language - just ask me anything! 😊\n\n" +
               "**Powered by NangInvest AI Engine** 🤖✨";
    }

    private boolean matchesIntent(String message, String intent) {
        Pattern pattern = intentPatterns.get(intent);
        return pattern != null && pattern.matcher(message).find();
    }

    private Map<String, Pattern> initializeIntentPatterns() {
        Map<String, Pattern> patterns = new HashMap<>();
        patterns.put("COURSE_RECOMMENDATION",
                Pattern.compile("(recommend|suggest|show|find).*(course|class|lesson|training)|what.*learn|course.*recommendation"));
        patterns.put("BOOK_RECOMMENDATION",
                Pattern.compile("(recommend|suggest|show|find).*(book|reading|read)|book.*recommendation|what.*read"));
        patterns.put("LEARNING_PATH",
                Pattern.compile("learning path|roadmap|how.*start|where.*begin|study plan|curriculum"));
        patterns.put("ANALYTICS",
                Pattern.compile("my.*progress|analytics|my.*stats|my.*data|performance|my.*analytics"));
        patterns.put("MARKET_TRENDS",
                Pattern.compile("trend|trending|popular|hot.*topic|what.*popular|market.*trend"));
        patterns.put("USER_INTERESTS",
                Pattern.compile("my.*interest|analyze.*interest|what.*like|my.*preference"));
        patterns.put("PLATFORM_STATS",
                Pattern.compile("platform.*stat|site.*stat|total.*user|how.*many"));
        patterns.put("HELP",
                Pattern.compile("help|what.*do|how.*work|commands|capabilities"));
        return patterns;
    }

    private String determineExperienceLevel(User user, List<String> interests, double engagementLevel) {
        int experiencePoints = 0;
        if (user.getExpertise() != null && !user.getExpertise().isEmpty()) {
            if (user.getExpertise().toLowerCase().contains("finance") ||
                user.getExpertise().toLowerCase().contains("investment") ||
                user.getExpertise().toLowerCase().contains("economics")) {
                experiencePoints += 3;
            } else {
                experiencePoints += 1;
            }
        }
        experiencePoints += Math.min(interests.size(), 3);
        if (engagementLevel > 7) experiencePoints += 2;
        else if (engagementLevel > 4) experiencePoints += 1;
        if (user.getRole() != null && user.getRole().toString().equals("ADMIN")) {
            experiencePoints += 2;
        }
        if (experiencePoints <= 2) return "Beginner";
        else if (experiencePoints <= 5) return "Intermediate";
        else return "Advanced";
    }

    private String createBeginnerPath(User user, List<Course> courses, List<Book> books) {
        StringBuilder path = new StringBuilder();
        path.append("**📚 Beginner's Journey (12-16 weeks):**\n\n");
        path.append("**Phase 1: Foundation Building (Weeks 1-4)**\n");
        path.append("1. Investment Basics & Terminology\n");
        path.append("2. Understanding Different Asset Classes\n");
        path.append("3. Risk Assessment & Tolerance\n");
        path.append("4. Setting Financial Goals\n\n");
        path.append("**Phase 2: Core Concepts (Weeks 5-8)**\n");
        path.append("5. Portfolio Diversification\n");
        path.append("6. Market Analysis Fundamentals\n");
        path.append("7. Value vs. Growth Investing\n");
        path.append("8. Introduction to ETFs & Mutual Funds\n\n");
        path.append("**Phase 3: Practical Application (Weeks 9-12)**\n");
        path.append("9. Building Your First Portfolio\n");
        path.append("10. Understanding Market Cycles\n");
        path.append("11. Tax-Efficient Investing\n");
        path.append("12. Investment Psychology\n\n");
        path.append("**Phase 4: Specialized Focus (Weeks 13-16)**\n");
        if (user.getExpertise() != null) {
            path.append("13. ").append(user.getExpertise()).append("-focused Investment Strategies\n");
        } else {
            path.append("13. Choose your specialization area\n");
        }
        path.append("14. Advanced Portfolio Management\n");
        path.append("15. Alternative Investment Options\n");
        path.append("16. Creating Your Investment Plan\n\n");
        return path.toString();
    }

    private String createIntermediatePath(User user, List<String> interests, List<Course> courses, List<Book> books) {
        StringBuilder path = new StringBuilder();
        path.append("**🚀 Intermediate Advancement Path (8-12 weeks):**\n\n");
        path.append("**Phase 1: Deepening Knowledge (Weeks 1-3)**\n");
        if (!interests.isEmpty()) {
            path.append("1. Advanced ").append(interests.get(0)).append(" Strategies\n");
        }
        path.append("2. Technical Analysis Fundamentals\n");
        path.append("3. Financial Statement Analysis\n\n");
        path.append("**Phase 2: Specialized Skills (Weeks 4-6)**\n");
        path.append("4. Options & Derivatives Basics\n");
        path.append("5. Sector Analysis & Rotation\n");
        if (interests.size() > 1) {
            path.append("6. ").append(interests.get(1)).append(" Deep Dive\n");
        } else {
            path.append("6. International Markets\n");
        }
        path.append("\n");
        path.append("**Phase 3: Advanced Strategies (Weeks 7-9)**\n");
        path.append("7. Risk Management Techniques\n");
        path.append("8. Portfolio Optimization\n");
        path.append("9. Alternative Investments\n\n");
        path.append("**Phase 4: Mastery (Weeks 10-12)**\n");
        path.append("10. Advanced Trading Strategies\n");
        path.append("11. Institutional Investment Approaches\n");
        path.append("12. Building Investment Models\n\n");
        return path.toString();
    }

    private String createAdvancedPath(User user, List<String> interests, List<Course> courses, List<Book> books) {
        StringBuilder path = new StringBuilder();
        path.append("**🎯 Advanced Mastery Path (6-8 weeks):**\n\n");
        path.append("**Phase 1: Expert-Level Strategies (Weeks 1-2)**\n");
        if (!interests.isEmpty()) {
            path.append("1. Cutting-edge ").append(interests.get(0)).append(" Techniques\n");
        }
        path.append("2. Quantitative Analysis & Modeling\n");
        path.append("3. Algorithmic Trading Concepts\n\n");
        path.append("**Phase 2: Innovation & Trends (Weeks 3-4)**\n");
        path.append("4. ESG Investing & Impact Metrics\n");
        path.append("5. Cryptocurrency & Digital Assets\n");
        path.append("6. Real Estate Investment Trusts (REITs)\n\n");
        path.append("**Phase 3: Leadership & Teaching (Weeks 5-6)**\n");
        path.append("7. Investment Management Leadership\n");
        path.append("8. Mentoring & Knowledge Sharing\n");
        path.append("9. Building Investment Communities\n\n");
        path.append("**Phase 4: Innovation (Weeks 7-8)**\n");
        path.append("10. Developing Investment Products\n");
        path.append("11. Research & Market Innovation\n");
        path.append("12. Advanced Risk Management\n\n");
        return path.toString();
    }

    private List<Map<String, Object>> getTopBooksWithDetails(int limit) {
        try {
            Object topBooksObj = analyticsPredictor.getPlatformAnalytics().get("topBooks");
            if (topBooksObj instanceof List) {
                List<?> rawList = (List<?>) topBooksObj;
                List<Map<String, Object>> topBooks = new ArrayList<>();
                for (Object item : rawList) {
                    if (item instanceof Map) {
                        topBooks.add((Map<String, Object>) item);
                    }
                }
                return topBooks.subList(0, Math.min(limit, topBooks.size()));
            }
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error in getTopBooksWithDetails: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private void sendSuccessResponse(HttpServletResponse response, String aiResponse, String userMessage)
            throws IOException {
        try (PrintWriter out = response.getWriter()) {
            JsonObject responseObj = new JsonObject();
            responseObj.addProperty("success", true);
            responseObj.addProperty("message", aiResponse);
            responseObj.addProperty("userMessage", userMessage);
            responseObj.addProperty("timestamp", System.currentTimeMillis());
            responseObj.addProperty("powered_by", "NangInvest AI Engine");
            out.print(gson.toJson(responseObj));
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String errorMessage) throws IOException {
        response.setStatus(status);
        try (PrintWriter out = response.getWriter()) {
            JsonObject error = new JsonObject();
            error.addProperty("success", false);
            error.addProperty("error", errorMessage);
            error.addProperty("timestamp", System.currentTimeMillis());
            out.print(gson.toJson(error));
        }
    }
}