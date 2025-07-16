/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import dao.AffiliateClickDAO;
import dao.BlogDAO;
import dao.BooksDAO;
import dao.CartDAO;
import dao.CourseDAO;
import dao.OrderDAO;
import dao.UserDAO;
import model.AffiliateClick;
import model.Book;
import model.Cart;
import model.Course;
import model.Order;
import model.User;

/**
 *
 * @author Admin
 */
public class AnalyticsPredictor {

    private final UserDAO userDAO;
    private final CourseDAO courseDAO;
    private final BooksDAO booksDAO;
    private final OrderDAO orderDAO;
    private final AffiliateClickDAO affiliateClickDAO;
    private final CartDAO cartDAO;
    private final BlogDAO blogDAO;

    public AnalyticsPredictor() {
        this.userDAO = new UserDAO();
        this.courseDAO = new CourseDAO();
        this.booksDAO = new BooksDAO();
        this.orderDAO = new OrderDAO();
        this.affiliateClickDAO = new AffiliateClickDAO();
        this.cartDAO = new CartDAO();
        this.blogDAO = new BlogDAO();
    }

    public List<String> predictUserInterests(Integer userId) {
        Map<String, Integer> interestScores = new HashMap<>();

        // analyze user's course purchases
        List<Order> userOrders = orderDAO.findByUserId(userId);
        for (Order order : userOrders) {
            // simple approach not getCourseById
            String topic = extractTopicFromOrder(order);
            if (topic != null) {
                interestScores.put(topic, interestScores.getOrDefault(topic, 0) + 3);
            }
        }

        List<AffiliateClick> userClicks = affiliateClickDAO.findByUserId(userId);
        for (AffiliateClick click : userClicks) {
            Optional<Book> bookOpt = booksDAO.findById(click.getBookId());
            if (bookOpt.isPresent()) {
                String topic = bookOpt.get().getTopic();
                if (topic != null) {
                    interestScores.put(topic, interestScores.getOrDefault(topic, 0) + 1);
                }
            }
        }

        // Analyze cart contents
        List<Cart> cartItems = cartDAO.findByUserId(userId);
        for (Cart item : cartItems) {
            String topic = extractTopicFromCart(item);
            if (topic != null) {
                interestScores.put(topic, interestScores.getOrDefault(topic, 0) + 2);
            }
        }

        // sort by interst score and return top interst
        return interestScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Map<String, Double> predictCoursePopularity() {
        Map<String, Double> popularityScores = new HashMap<>();

        // get all courses
        List<Course> allCourses = courseDAO.findAll();

        for (Course course : allCourses) {
            double score = 0.0;

            // Factor 1: Recent orders (last 30 days)
            long recentOrders = orderDAO.findByProductId(course.getCourseId()).stream()
                    .filter(order -> isWithinDays(order.getOrderId(), 30)).count();
            score += recentOrders * 0.4;

            // Factor 2: Cart additions
            long cartAdditions = cartDAO.findAll()
                    .stream()
                    .filter(cart -> cart.getProductId().equals(course.getCourseId()))
                    .count();
            score += cartAdditions * 0.3;

            // Factor 3: Price factor (free got bonus)
            if (course.getIsFree()) {
                score += 10.0;
            } else {
                // lower price = higher popularity potential
                score += (100.0 - course.getPrice().doubleValue()) * 0.1;
            }

            // factor 4L course topic trend
            String courseTopic = extractTopicFromCourse(course);
            if (courseTopic != null) {
                score += getTopicTrendScore(courseTopic);
            }

            popularityScores.put(course.getCourseName(), score);
        }
        return popularityScores;
    }

    /**
     * Predic optimal marketing times based on user acitivity patterns
     */
    public List<LocalDateTime> predictOptimalMarketingTimes() {
        List<LocalDateTime> optimalTimes = new ArrayList<>();
        // analyze affiliate click patterns

        List<AffiliateClick> allClicks = affiliateClickDAO.findAll();
        Map<Integer, Integer> hourlyActivity = new HashMap<>();

        for (AffiliateClick click : allClicks) {
            int hour = click.getClickTime().getHour();
            hourlyActivity.put(hour, hourlyActivity.getOrDefault(hour, 0) + 1);
        }

        // find top 3 most actives hourse
        List<Integer> topHours = hourlyActivity.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Generate optimal times for the next 7 days
        LocalDateTime now = LocalDateTime.now();
        for (int day = 0; day < 7; day++) {
            for (Integer hour : topHours) {
                LocalDateTime optimalTime = now.plusDays(day)
                        .withHour(hour)
                        .withMinute(0)
                        .withSecond(0);
                optimalTimes.add(optimalTime);
            }
        }
        return optimalTimes;
    }

    /**
     * Analyze affiliate link performance
     *
     * @return
     */
    public Map<Integer, Double> analyzeAffiliatePerformance() {
        Map<Integer, Double> performanceScores = new HashMap<>();

        List<Book> allBooks = booksDAO.findAll();

        for (Book book : allBooks) {
            double score = 0.0;

            // click count
            long clickCount = affiliateClickDAO.getClickCountByBookId(book.getBookId());
            score += clickCount * 1.0;

            // Click rate based on book rating
            if (book.getRating() != null) {
                score += book.getRating().doubleValue() * 0.5;
            }

            // recent click activity (last 7 days)
            LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
            long recentClicks = affiliateClickDAO.findByBookIdAndDateRange(
                    book.getBookId(), weekAgo, LocalDateTime.now()).size();
            score += recentClicks * 2.0;

            // Topic popularity factor
            if (book.getTopic() != null) {
                score += getTopicTrendScore(book.getTopic());
            }
            performanceScores.put(book.getBookId(), score);
        }
        return performanceScores;
    }

    /**
     * get overall platform statistics
     *
     * @return
     */
    public Map<String, Object> getPlatformAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        // user memtrics
        analytics.put("totalUsers", userDAO.count());
        analytics.put("activeUsers", getActiveUsersCount());
        analytics.put("userGrowthRate", calculateUserGrowthRate());

        // couse metrics
        analytics.put("totalCourses", courseDAO.countAllCourses());
        analytics.put("freeCourses", courseDAO.findFreeCourses().size());
        analytics.put("paidCourses", courseDAO.findPaidCourses().size());

        // Revenue metrics
        analytics.put("totalOrders", orderDAO.count());
        analytics.put("pendingOrders", orderDAO.findPendingOrders().size());
        analytics.put("completedOrders", orderDAO.findCompletedOrders().size());
        analytics.put("totalRevenue", getTotalRevenue());

        // Engagement metrics
        analytics.put("totalClicks", affiliateClickDAO.getTotalClickCount());
        analytics.put("totalBlogs", blogDAO.getBlogCount());
        analytics.put("averageCartSize", calculateAverageCartSize());

        // Top performers
        analytics.put("topBooks", getTopPerformingBooks(5));
        analytics.put("topCourses", getTopPerformingCourses(5));
        analytics.put("topUsers", getTopUsers(5));

        return analytics;
    }

    private static BigDecimal totalRevenue = BigDecimal.ZERO;

    public void addRevenue(BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            totalRevenue = totalRevenue.add(amount);
        }
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    /**
     * Predict User churn risk
     *
     * @return
     */
    public Map<Integer, Double> predictUserChurnRisk() {
        Map<Integer, Double> churnRisk = new HashMap<>();

        List<User> allUsers = userDAO.findAll();

        for (User user : allUsers) {
            double risk = 0.0;

            // Factor 1 : lsit activity (oers, clicks, cart activity)
            LocalDateTime lastActivity = getLastUserActivity(user.getUserId());
            if (lastActivity != null) {
                long daysSinceActivity = ChronoUnit.DAYS.between(lastActivity, LocalDateTime.now());
                risk += Math.min(daysSinceActivity * 0.1, 0.5);
            }

            // Factor 2: Order frequency
            List<Order> userOrders = orderDAO.findByUserId(user.getUserId());
            if (userOrders.size() < 2) {
                risk += 2.0;
            }

            // Factor 3: Cart abandonment
            List<Cart> userCart = cartDAO.findByUserId(user.getUserId());
            if (userCart.size() > 0) {
                risk += 1.0; // has items in cart but has not purchased
            }
            churnRisk.put(user.getUserId(), Math.min(risk, 10.0));
        }
        return churnRisk;
    }

    /**
     * get trending topics based on recent activity
     * 
     * @return
     */

    public List<String> getTrendingTopics() {
        Map<String, Integer> topicCounts = new HashMap<>();

        // Analyze recent clicks(last 7days)
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        List<AffiliateClick> recentClicks = affiliateClickDAO.findByDateRange(weekAgo, LocalDateTime.now());

        for (AffiliateClick click : recentClicks) {
            Optional<Book> bookOpt = booksDAO.findById(click.getBookId());
            if (bookOpt.isPresent() && bookOpt.get().getTopic() != null) {
                String topic = bookOpt.get().getTopic();
                topicCounts.put(topic, topicCounts.getOrDefault(topic, 0) + 1);
            }
        }
        return topicCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private String extractTopicFromOrder(Order order) {
        // not finish - finsih based on produc structure
        try {
            Optional<Course> courseOpt = courseDAO.findById(order.getOrderId());
            if (courseOpt.isPresent()) {
                return extractTopicFromCourse(courseOpt.get());
            }
        } catch (Exception e) {
            // hand if product not a course
        }
        return "General Investment";
    }

    private String extractTopicFromCart(Cart cartItem) {
        try {
            Optional<Course> courseOpt = courseDAO.findById(cartItem.getProductId());
            if (courseOpt.isPresent()) {
                return extractTopicFromCourse(courseOpt.get());
            }
        } catch (Exception e) {
            // Handle if product is not a course
        }
        return "General Finance";
    }

    private String extractTopicFromCourse(Course course) {
        // extract from name or des
        String name = course.getCourseName().toLowerCase();
        if (name.contains("stock") || name.contains("trading"))
            return "Trading";
        if (name.contains("investment") || name.contains("invest"))
            return "Investment";
        if (name.contains("retirement") || name.contains("plan"))
            return "Retirement Plan";
        if (name.contains("real estate") || name.contains("property"))
            return "Real Estate";
        return "General Finance";
    }

    private double getTopicTrendScore(String topic) {
        // Simulate trend scoreing based popularity
        Map<String, Double> trendScores = new HashMap<>();
        trendScores.put("Trading", 8.5);
        trendScores.put("Investment", 9.0);
        trendScores.put("Cryptocurrency", 7.5);
        trendScores.put("Real Estate", 8.0);
        trendScores.put("AI", 9.5);
        trendScores.put("General Finance", 7.0);

        return trendScores.getOrDefault(topic, 5.0);
    }

    private boolean isWithinDays(Integer orderId, int days) {
        // This would need to check the order date
        // For now, return true for recent orders
        return true;
    }

    private long getActiveUsersCount() {
        return userDAO.count() / 2;
    }

    private double calculateUserGrowthRate() {
        // Calculate month-over-month growth
        return 15.5; // Simplified return
    }

    private double calculateAverageCartSize() {
        List<Cart> allCarts = cartDAO.findAll();
        if (allCarts.isEmpty())
            return 0.0;

        double totalItems = allCarts.stream()
                .mapToInt(Cart::getQuantity)
                .sum();

        return totalItems / allCarts.size();
    }

    private LocalDateTime getLastUserActivity(Integer userId) {
        // get the most recent activity
        LocalDateTime lastActivity = null;

        // check last order
        List<Order> orders = orderDAO.findByUserId(userId);
        if (!orders.isEmpty()) {
            // order date filed
            lastActivity = LocalDateTime.now().minusDays(5);
        }

        // check last click
        List<AffiliateClick> clicks = affiliateClickDAO.findByUserId(userId);
        if (!clicks.isEmpty()) {
            LocalDateTime lastClick = clicks.get(0).getClickTime();
            if (lastActivity == null || lastClick.isAfter(lastActivity)) {
                lastActivity = lastClick;
            }
        }
        return lastActivity;
    }

    private List<Map<String, Object>> getTopPerformingBooks(int limit) {
        return affiliateClickDAO.getMostClickedBooks(limit)
                .stream()
                .map(result -> {
                    Map<String, Object> book = new HashMap<>();
                    book.put("bookId", result[0]);
                    book.put("clickCount", result[1]);
                    // Add book details
                    Optional<Book> bookOpt = booksDAO.findById((Integer) result[0]);
                    if (bookOpt.isPresent()) {
                        book.put("bookName", bookOpt.get().getBookName());
                        book.put("topic", bookOpt.get().getTopic());
                        book.put("rating", bookOpt.get().getRating());
                    }
                    return book;
                })
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> getTopPerformingCourses(int limit) {
        return orderDAO.getMostPopularOrderedProducts(limit)
                .stream()
                .map(result -> {
                    Map<String, Object> course = new HashMap<>();
                    course.put("courseId", result[0]);
                    course.put("orderCount", result[1]);
                    // Add course details
                    Optional<Course> courseOpt = courseDAO.findById((Integer) result[0]);
                    if (courseOpt.isPresent()) {
                        course.put("courseName", courseOpt.get().getCourseName());
                        course.put("price", courseOpt.get().getPrice());
                        course.put("isFree", courseOpt.get().getIsFree());
                    }
                    return course;
                })
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> getTopUsers(int limit) {
        return userDAO.findAll()
                .stream()
                .limit(limit)
                .map(user -> {
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("userId", user.getUserId());
                    userData.put("username", user.getUserName());
                    userData.put("expertise", user.getExpertise());
                    userData.put("orderCount", orderDAO.getOrderCountByUserId(user.getUserId()));
                    userData.put("clickCount", affiliateClickDAO.getClickCountByUserId(user.getUserId()));
                    return userData;
                })
                .sorted((a, b) -> Long.compare(
                        (Long) b.get("orderCount"),
                        (Long) a.get("orderCount")))
                .collect(Collectors.toList());
    }
}
