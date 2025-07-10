/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai;

import dao.AffiliateClickDAO;
import dao.BlogDAO;
import dao.BooksDAO;
import dao.CartDAO;
import dao.CourseDAO;
import dao.OrderDAO;
import dao.ServiceDAO;
import dao.UserDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import model.AffiliateClick;
import model.Book;
import model.Course;
import model.Order;
import model.Service;
import model.User;

/**
 *
 * @author Admin
 */
public class RecommendationEngine {

    private final UserDAO userDAO;
    private final CourseDAO courseDAO;
    private final BooksDAO booksDAO;
    private final ServiceDAO serviceDAO;
    private final OrderDAO orderDAO;
    private final AffiliateClickDAO affiliateClickDAO;
    private final CartDAO cartDAO;
    private final BlogDAO blogDAO;
    private final AnalyticsPredictor analyticsPredictor;

    public RecommendationEngine() {
        this.userDAO = new UserDAO();
        this.courseDAO = new CourseDAO();
        this.booksDAO = new BooksDAO();
        this.serviceDAO = new ServiceDAO();
        this.orderDAO = new OrderDAO();
        this.affiliateClickDAO = new AffiliateClickDAO();
        this.cartDAO = new CartDAO();
        this.blogDAO = new BlogDAO();
        this.analyticsPredictor = new AnalyticsPredictor();
    }

    /**
     * recommend courses based on user profile and behavior
     */
    public List<Course> recommendCourses(User user) {
        Map<Course, Double> courseScores = new HashMap<>();
        List<Course> allCourses = courseDAO.findAll();

        //get user's predicted interests
        List<String> userInterests = analyticsPredictor.predictUserInterests(user.getUserId());

        //get user's purchase history
        List<Order> userOrders = orderDAO.findByUserId(user.getUserId());
        Set<Integer> purchasedCourseIds = userOrders.stream()
                .map(Order::getProductId)
                .collect(Collectors.toSet());

        for (Course course : allCourses) {
            // Skip already purchased courses
            if (purchasedCourseIds.contains(course.getCourseId())) {
                continue;
            }

            double score = 0.0;

            //Factor 1: user expertise match
            if (user.getExpertise() != null) {
                String courseTopic = extractTopicFromCourse(course);
                if (courseTopic != null && courseTopic.equalsIgnoreCase(user.getExpertise())) {
                    score += 3.0;
                }
            }

            //Facctor 2: User interest alignment
            String courseTopic = extractTopicFromCourse(course);
            if (courseTopic != null && userInterests.contains(courseTopic)) {
                int interestrank = userInterests.indexOf(courseTopic);
                score += (5 - interestrank) * 0.5; //higher screo for top interest
            }

            //Factor 3: Course popularity
            Map<String, Double> popularityScores = analyticsPredictor.predictCoursePopularity();
            Double popularityScore = popularityScores.get(course.getCourseName());
            if (popularityScore != null) {
                score += popularityScore * 0.1;
            }

            //Factor 4: price preference(free courses get bonus for new users
            if (userOrders.isEmpty() && course.getIsFree()) {
                score += 2.0;
            }

            //Factor 5: Similar users'preferences
            score += getSimilarUsersScore(user, course);

            //Factor 6: Course rating/quality
            if (course.getImageUrl() != null && !course.getImageUrl().isEmpty()) {
                score += 0.5;
            }

            courseScores.put(course, score);

        }
        return courseScores.entrySet().stream()
                .sorted(Map.Entry.<Course, Double>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

    }

    /**
     * Recommend books based on user interest and expertiese
     */
    public List<Book> recommendBooks(User user) {
        Map<Book, Double> bookScores = new HashMap<>();
        List<Book> allBooks = booksDAO.findAll();

        //get users predicted interests
        List<String> userInterests = analyticsPredictor.predictUserInterests(user.getUserId());

        //get user's click history
        List<AffiliateClick> userClicks = affiliateClickDAO.findByUserId(user.getUserId());
        Set<Integer> clickedBookIds = userClicks.stream()
                .map(AffiliateClick::getBookId)
                .collect(Collectors.toSet());

        for (Book book : allBooks) {
            double score = 0.0;

            //Factor 1: Topic match with user interests
            if (book.getTopic() != null && userInterests.contains(book.getTopic())) {
                int interestRank = userInterests.indexOf(book.getTopic());
                score += (5 - interestRank) * 1.0;
            }

            //Factor 2. expertise algnment
            if (user.getExpertise() != null && book.getTopic() != null
                    && book.getTopic().toLowerCase().contains(user.getExpertise().toLowerCase())) {
                score += 2.0;
            }

            //Factor 3 Book rating
            if (book.getRating() != null) {
                score += book.getRating().doubleValue() * 0.5;
            }
            Map<Integer, Double> affiliatePerformance = analyticsPredictor.analyzeAffiliatePerformance();
            Double performanceScore = affiliatePerformance.get(book.getBookId());
            if (performanceScore != null) {
                score += performanceScore * 0.1;
            }

            // Factor 5: Preview availability bonus
            if (book.getIsPreviewAvailable()) {
                score += 1.0;
            }

            // Factor 6: Avoid already clicked books (reduce score)
            if (clickedBookIds.contains(book.getBookId())) {
                score *= 0.5;
            }

            bookScores.put(book, score);
        }

        return bookScores.entrySet().stream()
                .sorted(Map.Entry.<Book, Double>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Recommend services based on user profile
     */
    public List<Service> recommendServices(User user) {
        Map<Service, Double> serviceScores = new HashMap<>();
        List<Service> allServices = serviceDAO.findAll();

        // Get user's predicted interests
        List<String> userInterests = analyticsPredictor.predictUserInterests(user.getUserId());

        for (Service service : allServices) {
            double score = 0.0;

            // Factor 1: Service name alignment with user interests
            String serviceName = service.getServiceName().toLowerCase();
            for (String interest : userInterests) {
                if (serviceName.contains(interest.toLowerCase())) {
                    score += 2.0;
                }
            }

            // Factor 2: Expertise match
            if (user.getExpertise() != null
                    && serviceName.contains(user.getExpertise().toLowerCase())) {
                score += 3.0;
            }

            // Factor 3: Service has image (quality indicator)
            if (service.getImageUrl() != null && !service.getImageUrl().isEmpty()) {
                score += 1.0;
            }

            // Factor 4: User purchase history influence
            List<Order> userOrders = orderDAO.findByUserId(user.getUserId());
            if (userOrders.size() > 2) {
                score += 1.5; // Experienced users get service recommendations
            }

            serviceScores.put(service, score);
        }

        return serviceScores.entrySet().stream()
                .sorted(Map.Entry.<Service, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * get personalized content mix
     */
    public Map<String, Object> getPersonalizedContent(User user) {
        Map<String, Object> personalizedContent = new HashMap<>();

        personalizedContent.put("recommendedCourses", recommendCourses(user));
        personalizedContent.put("recommendedBooks", recommendBooks(user));
        personalizedContent.put("recommendedServices", recommendServices(user));
        personalizedContent.put("trendingTopics", analyticsPredictor.getTrendingTopics());
        personalizedContent.put("userInterests", analyticsPredictor.predictUserInterests(user.getUserId()));

        return personalizedContent;
    }

    /**
     * Ger Course recommendations for homepage
     */
    public List<Course> getHomepageRecommendations(User user, int limit) {
        if (user == null) {
            //Return popular courses for anonymous users
            Map<String, Double> popularity = analyticsPredictor.predictCoursePopularity();
            return courseDAO.findAll().stream()
                    .sorted((c1, c2) -> Double.compare(
                    popularity.getOrDefault(c2.getCourseName(), 0.0),
                    popularity.getOrDefault(c1.getCourseName(), 0.0)))
                    .limit(limit)
                    .collect(Collectors.toList());
        }
        
        return recommendCourses(user).stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Get "you might also like recommendations
     */
    public List<Course> getSimilarCourses(Integer courseId, int limit){
        Optional<Course> courseOpt = courseDAO.findById(courseId);
        if(!courseOpt.isPresent()){
            return new ArrayList<>();
        }
        
        Course targetCourse = courseOpt.get();
        String targetTopic = extractTopicFromCourse(targetCourse);
        
        List<Course> similarCourses = courseDAO.findAll().stream()
                .filter(course -> !course.getCourseId().equals(courseId))
                .filter(course -> {
                    String courseTopic = extractTopicFromCourse(course);
                    return courseTopic != null && courseTopic.equals(targetTopic);
                })
                .sorted((c1, c2) -> {
                    //sort by price similarity and popularity
                    double priceDiff1 = Math.abs(c1.getPrice().doubleValue() - targetCourse.getPrice().doubleValue());
                    double priceDiff2 = Math.abs(c2.getPrice().doubleValue() - targetCourse.getPrice().doubleValue());
                    return Double.compare(priceDiff1, priceDiff2);
                })
                .limit(limit)
                .collect(Collectors.toList());
        
        return similarCourses;
    }
    
    private String extractTopicFromCourse(Course course) {
        String name = course.getCourseName().toLowerCase();
        if (name.contains("stock") || name.contains("trading")) return "Stock Trading";
        if (name.contains("investment") || name.contains("invest")) return "Investment Strategy";
        if (name.contains("crypto") || name.contains("bitcoin")) return "Cryptocurrency";
        if (name.contains("real estate") || name.contains("property")) return "Real Estate";
        if (name.contains("forex") || name.contains("currency")) return "Forex Trading";
        if (name.contains("options") || name.contains("derivatives")) return "Options & Derivatives";
        if (name.contains("retirement") || name.contains("pension")) return "Retirement Planning";
        if (name.contains("tax") || name.contains("taxation")) return "Tax Planning";
        return "General Finance";
    }
    
    private double getSimilarUsersScore(User user, Course course) {
        // Find users with similar interests/expertise
        List<User> similarUsers = userDAO.findAll().stream()
                .filter(u -> u.getUserId() != user.getUserId())
                .filter(u -> u.getExpertise() != null && 
                           user.getExpertise() != null && 
                           u.getExpertise().equalsIgnoreCase(user.getExpertise()))
                .collect(Collectors.toList());
        
        // Count how many similar users have purchased this course
        long purchaseCount = similarUsers.stream()
                .mapToLong(u -> orderDAO.findByUserId(u.getUserId()).stream()
                        .filter(order -> order.getProductId().equals(course.getCourseId()))
                        .count())
                .sum();
        
        return purchaseCount * 0.5; // Weight factor for similar users
    }
}
