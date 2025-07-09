/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai;

import dao.BlogDAO;
import dao.BooksDAO;
import dao.CourseDAO;
import dao.UserDAO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import model.Course;
import model.User;

/**
 *
 * @author Admin
 */
public class ContentGenerator {
    
    private final UserDAO userDAO;
    private final CourseDAO courseDAO;
    private final BooksDAO booksDAO;
    private final BlogDAO blogDAO;
    private final AnalyticsPredictor analyticsPredictor;
    
    // Content templates and patterns
    private final Map<String, List<String>> topicTemplates;
    private final Map<String, List<String>> marketingTemplates;
    private final Map<String, List<String>> seoKeywords;
    
    public ContentGenerator() {
        this.userDAO = new UserDAO();
        this.courseDAO = new CourseDAO();
        this.booksDAO = new BooksDAO();
        this.blogDAO = new BlogDAO();
        this.analyticsPredictor = new AnalyticsPredictor();
        
        this.topicTemplates = initializeTopicTemplates();
        this.marketingTemplates = initializeMarketingTemplates();
        this.seoKeywords = initializeSeoKeywords();
    }
    
    /**
     * Generate blog topics based on user expertise and trends
     */
    public List<String> generateBlogTopics(String expertise) {
        List<String> topics = new ArrayList<>();
        
        // Get trending topics
        List<String> trendingTopics = analyticsPredictor.getTrendingTopics();
        
        // Get topic templates based on expertise
        List<String> templates = topicTemplates.getOrDefault(expertise, 
                topicTemplates.get("General Finance"));
        
        // Combine trending topics with templates
        for (String trend : trendingTopics.subList(0, Math.min(3, trendingTopics.size()))) {
            for (String template : templates.subList(0, Math.min(2, templates.size()))) {
                topics.add(template.replace("{TOPIC}", trend));
            }
        }
        
        // Add seasonal/timely topics
        topics.addAll(generateSeasonalTopics(expertise));
        
        // Add beginner-friendly topics
        topics.addAll(generateBeginnerTopics(expertise));
        
        return topics.stream().distinct().limit(10).collect(Collectors.toList());
    }
    
    /**
     * Generate course descriptions based on course name and topic
     */
    public String generateCourseDescription(String courseName, String topic) {
        StringBuilder description = new StringBuilder();
        
        // Course intro
        description.append("Master the art of ").append(topic.toLowerCase())
                  .append(" with our comprehensive course: ").append(courseName).append(".\n\n");
        
        // What you'll learn section
        description.append("What You'll Learn:\n");
        List<String> learningPoints = generateLearningPoints(topic);
        for (String point : learningPoints) {
            description.append("• ").append(point).append("\n");
        }
        
        // Course features
        description.append("\nCourse Features:\n");
        description.append("• Expert-led instruction with real-world examples\n");
        description.append("• Interactive exercises and case studies\n");
        description.append("• Lifetime access to course materials\n");
        description.append("• Certificate of completion\n");
        description.append("• 30-day money-back guarantee\n\n");
        
        // Target audience
        description.append("Perfect for:\n");
        List<String> targetAudience = generateTargetAudience(topic);
        for (String audience : targetAudience) {
            description.append("• ").append(audience).append("\n");
        }
        
        // Call to action
        description.append("\nStart your journey to financial success today!");
        
        return description.toString();
    }
    
    /**
     * Generate personalized marketing email content
     */
    public String generateMarketingEmail(User user, List<Course> recommendations) {
        StringBuilder email = new StringBuilder();
        
        // Subject line
        String subject = generateEmailSubject(user, recommendations);
        email.append("Subject: ").append(subject).append("\n\n");
        
        // Greeting
        email.append("Hi ").append(user.getName() != null ? user.getName() : user.getUserName())
             .append(",\n\n");
        
        // Personalized intro
        if (user.getExpertise() != null) {
            email.append("As someone interested in ").append(user.getExpertise())
                 .append(", we thought you'd love these handpicked courses:\n\n");
        } else {
            email.append("We've found some amazing courses that match your interests:\n\n");
        }
        
        // Course recommendations
        for (int i = 0; i < Math.min(3, recommendations.size()); i++) {
            Course course = recommendations.get(i);
            email.append("📚 ").append(course.getCourseName()).append("\n");
            email.append("   Price: ").append(course.getIsFree() ? "FREE" : "$" + course.getPrice())
                 .append(" | Duration: ").append(course.getTime()).append("\n");
            email.append("   Perfect for mastering ").append(extractTopicFromCourse(course)).append("\n\n");
        }
        
        // Call to action
        email.append("🚀 Don't miss out! Limited time offer - Use code SAVE20 for 20% off any course.\n\n");
        
        // Social proof
        email.append("Join over 10,000+ students who have already transformed their financial future with NangInvest.\n\n");
        
        // Footer
        email.append("Best regards,\n");
        email.append("The NangInvest Team\n\n");
        email.append("P.S. This offer expires in 48 hours!");
        
        return email.toString();
    }
    
    /**
     * Generate SEO-optimized content
     */
    public String generateSEOContent(String topic, List<String> keywords) {
        StringBuilder content = new StringBuilder();
        
        // Title with primary keyword
        String primaryKeyword = keywords.get(0);
        content.append("# The Ultimate Guide to ").append(primaryKeyword).append(" in 2025\n\n");
        
        // Introduction
        content.append("Are you looking to master ").append(primaryKeyword.toLowerCase())
               .append("? You're in the right place! This comprehensive guide will cover everything you need to know about ")
               .append(topic.toLowerCase()).append(".\n\n");
        
        // Table of contents
        content.append("## Table of Contents\n");
        List<String> sections = generateSEOSections(topic, keywords);
        for (int i = 0; i < sections.size(); i++) {
            content.append((i + 1)).append(". ").append(sections.get(i)).append("\n");
        }
        content.append("\n");
        
        // Content sections
        for (String section : sections) {
            content.append("## ").append(section).append("\n\n");
            content.append(generateSectionContent(section, keywords)).append("\n\n");
        }
        
        // FAQ section
        content.append(generateFAQSection(topic, keywords));
        
        // Conclusion with CTA
        content.append("## Conclusion\n\n");
        content.append("Mastering ").append(primaryKeyword.toLowerCase())
               .append(" is essential for financial success. Start your journey today with our expert-designed courses and resources.\n\n");
        content.append("**Ready to get started?** [Explore our ").append(topic).append(" courses now!]\n\n");
        
        return content.toString();
    }
    
    /**
     * Generate social media posts
     */
    public List<String> generateSocialMediaPosts(String topic, int count) {
        List<String> posts = new ArrayList<>();
        
        // Educational posts
        posts.add("💡 Did you know? " + generateFinancialTip(topic) + " #" + topic.replace(" ", "") + " #FinancialTips");
        
        // Motivational posts
        posts.add("🚀 Your financial future starts with a single step. Master " + topic + " today! #InvestmentEducation #FinancialFreedom");
        
        // Question posts
        posts.add("🤔 What's your biggest challenge with " + topic + "? Share in comments! #" + topic.replace(" ", "") + " #Community");
        
        // Statistic posts
        posts.add("📊 " + generateStatPost(topic) + " Source: NangInvest Research #" + topic.replace(" ", "") + " #Statistics");
        
        // Success story template
        posts.add("✅ Success Story: How one student mastered " + topic + " and changed their financial life! Read more in our blog. #SuccessStory");
        
        return posts.stream().limit(count).collect(Collectors.toList());
    }
    
    /**
     * Generate course quiz questions
     */
    public List<Map<String, Object>> generateQuizQuestions(String topic, int count) {
        List<Map<String, Object>> questions = new ArrayList<>();
        
        // Generate questions based on topic
        Map<String, List<String>> questionBank = getQuestionBank(topic);
        List<String> questionsList = questionBank.getOrDefault(topic, new ArrayList<>());
        
        for (int i = 0; i < Math.min(count, questionsList.size()); i++) {
            Map<String, Object> question = new HashMap<>();
            question.put("question", questionsList.get(i));
            question.put("options", generateQuizOptions(questionsList.get(i)));
            question.put("correctAnswer", 0); // First option is correct
            questions.add(question);
        }
        
        return questions;
    }
    
    // Helper methods
    private Map<String, List<String>> initializeTopicTemplates() {
        Map<String, List<String>> templates = new HashMap<>();
        
        templates.put("Stock Trading", Arrays.asList(
            "How to Master {TOPIC} in 30 Days",
            "The Ultimate {TOPIC} Strategy for Beginners",
            "5 Common {TOPIC} Mistakes to Avoid",
            "Advanced {TOPIC} Techniques for 2025"
        ));
        
        templates.put("Investment Strategy", Arrays.asList(
            "Building Wealth Through {TOPIC}",
            "The Complete Guide to {TOPIC}",
            "{TOPIC} for Retirement Planning",
            "Maximizing Returns with {TOPIC}"
        ));
        
        templates.put("Cryptocurrency", Arrays.asList(
            "Understanding {TOPIC} in Simple Terms",
            "The Future of {TOPIC} in 2025",
            "How to Safely Invest in {TOPIC}",
            "{TOPIC} vs Traditional Investments"
        ));
        
        templates.put("General Finance", Arrays.asList(
            "Essential {TOPIC} Tips for Everyone",
            "Getting Started with {TOPIC}",
            "Why {TOPIC} Matters for Your Future",
            "The Psychology of {TOPIC}"
        ));
        
        return templates;
    }
    
    private Map<String, List<String>> initializeMarketingTemplates() {
        Map<String, List<String>> templates = new HashMap<>();
        
        templates.put("subject_lines", Arrays.asList(
            "Your personalized course recommendations are here!",
            "Don't miss out on these trending courses",
            "Last chance: 20% off your favorite courses",
            "New courses added to your watchlist"
        ));
        
        return templates;
    }
    
    private Map<String, List<String>> initializeSeoKeywords() {
        Map<String, List<String>> keywords = new HashMap<>();
        
        keywords.put("Stock Trading", Arrays.asList(
            "stock trading", "day trading", "swing trading", "technical analysis",
            "stock market", "trading strategies", "investment portfolio"
        ));
        
        keywords.put("Investment Strategy", Arrays.asList(
            "investment strategy", "portfolio management", "asset allocation",
            "long-term investing", "value investing", "growth investing"
        ));
        
        keywords.put("Cryptocurrency", Arrays.asList(
            "cryptocurrency", "bitcoin", "ethereum", "blockchain",
            "crypto trading", "digital assets", "DeFi"
        ));
        
        return keywords;
    }
    
    private List<String> generateSeasonalTopics(String expertise) {
        List<String> seasonal = new ArrayList<>();
        int month = LocalDateTime.now().getMonthValue();
        
        if (month == 1) {
            seasonal.add("New Year Financial Resolutions for " + expertise);
            seasonal.add("Tax Planning Strategies for the New Year");
        } else if (month == 4) {
            seasonal.add("Tax Season: Maximizing Your " + expertise + " Deductions");
        } else if (month == 12) {
            seasonal.add("Year-End " + expertise + " Review and Planning");
        }
        
        return seasonal;
    }
    
     private List<String> generateBeginnerTopics(String expertise) {
        return Arrays.asList(
            expertise + " for Complete Beginners",
            "Getting Started with " + expertise + ": A Step-by-Step Guide",
            "Common " + expertise + " Mistakes and How to Avoid Them"
        );
    }
     
      private List<String> generateLearningPoints(String topic) {
        Map<String, List<String>> learningPoints = new HashMap<>();
        
        learningPoints.put("Stock Trading", Arrays.asList(
            "Technical and fundamental analysis techniques",
            "Risk management and position sizing",
            "Chart patterns and trading indicators",
            "Portfolio diversification strategies"
        ));
        
        learningPoints.put("Investment Strategy", Arrays.asList(
            "Asset allocation and portfolio construction",
            "Long-term vs short-term investment strategies",
            "Risk assessment and management",
            "Tax-efficient investing methods"
        ));
        
        return learningPoints.getOrDefault(topic, Arrays.asList(
            "Fundamental concepts and principles",
            "Practical implementation strategies",
            "Risk management techniques",
            "Advanced tips and tricks"
        ));
    }
    
    private List<String> generateTargetAudience(String topic) {
        return Arrays.asList(
            "Beginners looking to start their " + topic.toLowerCase() + " journey",
            "Intermediate learners wanting to improve their skills",
            "Professionals seeking to enhance their knowledge",
            "Anyone interested in financial independence"
        );
    }
    
    private String generateEmailSubject(User user, List<Course> recommendations) {
        if (recommendations.isEmpty()) {
            return "New courses added just for you!";
        }
        
        Course topRecommendation = recommendations.get(0);
        String topic = extractTopicFromCourse(topRecommendation);
        
        return "Master " + topic + " with these personalized recommendations";
    }
    
    private String extractTopicFromCourse(Course course) {
        String name = course.getCourseName().toLowerCase();
        if (name.contains("stock") || name.contains("trading")) return "Stock Trading";
        if (name.contains("investment")) return "Investment Strategy";
        if (name.contains("crypto")) return "Cryptocurrency";
        return "Finance";
    }
    
    private List<String> generateSEOSections(String topic, List<String> keywords) {
        return Arrays.asList(
            "What is " + topic + "?",
            "Getting Started with " + topic,
            "Best " + topic + " Strategies",
            "Common " + topic + " Mistakes",
            "Advanced " + topic + " Techniques",
            "Tools and Resources for " + topic
        );
    }
    
    private String generateSectionContent(String section, List<String> keywords) {
        return "This section covers essential information about " + section.toLowerCase() + 
               ". Understanding " + keywords.get(0) + " is crucial for success in this area. " +
               "Here are the key points to remember...";
    }
    
    private String generateFAQSection(String topic, List<String> keywords) {
        StringBuilder faq = new StringBuilder();
        faq.append("## Frequently Asked Questions\n\n");
        
        faq.append("**Q: What is ").append(topic).append("?**\n");
        faq.append("A: ").append(topic).append(" is a crucial aspect of financial planning that involves...\n\n");
        
        faq.append("**Q: How do I get started with ").append(keywords.get(0)).append("?**\n");
        faq.append("A: The best way to start is by educating yourself through our comprehensive courses...\n\n");
        
        faq.append("**Q: Is ").append(topic).append(" suitable for beginners?**\n");
        faq.append("A: Absolutely! Our courses are designed for all skill levels...\n\n");
        
        return faq.toString();
    }
    
    private String generateFinancialTip(String topic) {
        Map<String, List<String>> tips = new HashMap<>();
        tips.put("Stock Trading", Arrays.asList(
            "Never invest more than you can afford to lose",
            "Diversification is key to reducing risk",
            "Always do your research before buying any stock"
        ));
        
        List<String> topicTips = tips.getOrDefault(topic, Arrays.asList("Education is the best investment"));
        return topicTips.get(new Random().nextInt(topicTips.size()));
    }
    
    private String generateStatPost(String topic) {
        return "Studies show that 90% of successful investors in " + topic + " started with proper education!";
    }
    
    private Map<String, List<String>> getQuestionBank(String topic) {
        Map<String, List<String>> questions = new HashMap<>();
        
        questions.put("Stock Trading", Arrays.asList(
            "What is the primary goal of technical analysis?",
            "Which indicator is best for identifying trend reversals?",
            "What is the recommended position size for beginners?"
        ));
        
        return questions;
    }
    
    private List<String> generateQuizOptions(String question) {
        return Arrays.asList(
            "Correct answer option",
            "Incorrect option 1",
            "Incorrect option 2",
            "Incorrect option 3"
        );
    }
}
    

