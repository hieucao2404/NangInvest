package test;

import dao.AffiliateClickDAO;
import dao.BlogDAO;
import dao.CartDAO;
import dao.OrderDAO;
import dao.ServiceDAO;
import dao.UserCoursesDAO;
import dao.UserTokenDAO;
import model.AffiliateClick;
import model.Blog;
import model.Cart;
import model.Order;
import model.Service;
import model.UserCourses;
import model.UserToken;

/**
 * Comprehensive JPA Integration Test for all entities and DAOs
 */
public class JPAIntegrationTest {

  public static void main(String[] args) {
    System.out.println("🚀 Starting JPA Integration Test for all entities...\n");

    try {
      // Test Service
      testService();

      // Test Blog
      testBlog();

      // Test AffiliateClick
      testAffiliateClick();

      // Test Cart
      testCart();

      // Test Order
      testOrder();

      // Test UserCourses
      testUserCourses();

      // Test UserToken
      testUserToken();

      System.out.println("\n🎉 All JPA integration tests completed successfully!");

    } catch (Exception e) {
      System.err.println("❌ Error during integration testing: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static void testService() {
    System.out.println("📋 Testing Service entity and DAO...");
    ServiceDAO serviceDAO = new ServiceDAO();

    // Create and save
    Service service = new Service("Financial Planning", "https://example.com/financial.jpg");
    Service saved = serviceDAO.save(service);
    System.out.println("✅ Service saved with ID: " + saved.getServiceId());

    // Query
    long count = serviceDAO.getServiceCount();
    System.out.println("✅ Total services count: " + count);

    // Cleanup
    serviceDAO.delete(saved);
    System.out.println("✅ Service deleted\n");
  }

  private static void testBlog() {
    System.out.println("📝 Testing Blog entity and DAO...");
    BlogDAO blogDAO = new BlogDAO();

    // Create and save
    Blog blog = new Blog("Market Analysis", "Finance", "https://example.com/market.jpg",
        "Detailed market analysis content here...");
    Blog saved = blogDAO.save(blog);
    System.out.println("✅ Blog saved with ID: " + saved.getBlogId());

    // Query
    long count = blogDAO.getBlogCount();
    System.out.println("✅ Total blogs count: " + count);

    // Test utility methods
    System.out.println("✅ Has image: " + saved.hasImage());
    System.out.println("✅ Preview: " + saved.getPreviewContent(50));

    // Cleanup
    blogDAO.delete(saved);
    System.out.println("✅ Blog deleted\n");
  }

  private static void testAffiliateClick() {
    System.out.println("🖱️ Testing AffiliateClick entity and DAO...");
    AffiliateClickDAO clickDAO = new AffiliateClickDAO();

    // Create and save
    AffiliateClick click = new AffiliateClick(1, 1); // bookId=1, userId=1
    AffiliateClick saved = clickDAO.save(click);
    System.out.println("✅ AffiliateClick saved with ID: " + saved.getClickId());

    // Query
    long count = clickDAO.getTotalClickCount();
    System.out.println("✅ Total clicks count: " + count);

    // Test utility methods
    System.out.println("✅ Is anonymous click: " + saved.isAnonymousClick());

    // Cleanup
    clickDAO.delete(saved);
    System.out.println("✅ AffiliateClick deleted\n");
  }

  private static void testCart() {
    System.out.println("🛒 Testing Cart entity and DAO...");
    CartDAO cartDAO = new CartDAO();

    // Create and save
    Cart cart = new Cart(1, 1, 2); // userId=1, productId=1, quantity=2
    Cart saved = cartDAO.save(cart);
    System.out.println("✅ Cart saved with ID: " + saved.getCartId());

    // Query
    long totalQuantity = cartDAO.getTotalQuantityByUserId(1);
    System.out.println("✅ Total quantity for user 1: " + totalQuantity);

    // Test utility methods
    System.out.println("✅ Is valid quantity: " + saved.isValidQuantity());
    saved.increaseQuantity();
    System.out.println("✅ Quantity after increase: " + saved.getQuantity());

    // Cleanup
    cartDAO.delete(saved);
    System.out.println("✅ Cart deleted\n");
  }

  private static void testOrder() {
    System.out.println("📦 Testing Order entity and DAO...");
    OrderDAO orderDAO = new OrderDAO();

    // Create and save
    Order order = new Order(1, 1); // userId=1, productId=1
    Order saved = orderDAO.save(order);
    System.out.println("✅ Order saved with ID: " + saved.getOrderId());

    // Query
    long count = orderDAO.getTotalOrderCount();
    System.out.println("✅ Total orders count: " + count);

    // Test utility methods
    System.out.println("✅ Is pending: " + saved.isPending());
    saved.markAsPaid();
    System.out.println("✅ Payment status after marking paid: " + saved.getPaymentStatus());

    // Cleanup
    orderDAO.delete(saved);
    System.out.println("✅ Order deleted\n");
  }

  private static void testUserCourses() {
    System.out.println("👨‍🎓 Testing UserCourses entity and DAO...");
    UserCoursesDAO userCoursesDAO = new UserCoursesDAO();

    // Create and save
    UserCourses userCourse = new UserCourses(1, 1); // userId=1, courseId=1
    UserCourses saved = userCoursesDAO.save(userCourse);
    System.out.println("✅ UserCourses saved with UserID: " + saved.getUserId() + ", CourseID: " + saved.getCourseId());

    // Query
    long count = userCoursesDAO.getTotalEnrollmentCount();
    System.out.println("✅ Total enrollments count: " + count);

    // Test enrollment check
    boolean isEnrolled = userCoursesDAO.isUserEnrolledInCourse(1, 1);
    System.out.println("✅ User 1 enrolled in course 1: " + isEnrolled);

    // Cleanup
    userCoursesDAO.delete(saved);
    System.out.println("✅ UserCourses deleted\n");
  }

  private static void testUserToken() {
    System.out.println("🔑 Testing UserToken entity and DAO...");
    UserTokenDAO tokenDAO = new UserTokenDAO();

    // Create and save
    UserToken token = new UserToken(1, "test-token-123",
        java.time.LocalDateTime.now().plusHours(24));
    UserToken saved = tokenDAO.save(token);
    System.out.println("✅ UserToken saved with ID: " + saved.getId());

    // Query
    boolean isValid = tokenDAO.isTokenValid("test-token-123");
    System.out.println("✅ Token is valid: " + isValid);

    // Test utility methods
    System.out.println("✅ Is expired: " + saved.isExpired());
    System.out.println("✅ Is valid: " + saved.isValid());

    // Cleanup
    tokenDAO.delete(saved);
    System.out.println("✅ UserToken deleted\n");
  }
}
