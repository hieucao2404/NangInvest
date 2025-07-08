package test;

import dao.UserDAO;
import model.User;
import java.util.List;
import java.util.Optional;

/**
 * Test class for UserDAO and JPA setup
 * This will test all CRUD operations and custom methods
 */
public class UserDAOTest {

  private static UserDAO userDAO;

  public static void main(String[] args) {
    System.out.println("🚀 Starting UserDAO Test Suite...\n");

    userDAO = new UserDAO();

    try {
      // Test 1: Basic CRUD operations
      testBasicCRUD();

      // Test 2: Custom finder methods
      testCustomFinders();

      // Test 3: Existence checks
      testExistenceMethods();

      // Test 4: Role-based queries
      testRoleQueries();

      // Test 5: Pagination
      testPagination();

      // Test 6: Batch operations
      testBatchOperations();

      System.out.println("\n🎉 ALL TESTS PASSED! Your UserDAO is working perfectly!");

    } catch (Exception e) {
      System.err.println("\n❌ TEST FAILED!");
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static void testBasicCRUD() {
    System.out.println("📝 Test 1: Basic CRUD Operations");

    // CREATE
    System.out.println("  Creating new user...");
    User newUser = new User();
    newUser.setUserName("testuser123");
    newUser.setEmail("testuser@example.com");
    newUser.setPassword("password123");
    newUser.setName("Test User");
    newUser.setAge(25);
    newUser.setExpertise("Software Development");

    User savedUser = userDAO.save(newUser);
    System.out.println("  ✅ User created with ID: " + savedUser.getUserId());

    // READ
    System.out.println("  Reading user by ID...");
    Optional<User> foundUser = userDAO.findById(savedUser.getUserId());
    if (foundUser.isPresent()) {
      System.out.println("  ✅ User found: " + foundUser.get().getUserName());
    } else {
      throw new RuntimeException("User not found after creation!");
    }

    // UPDATE
    System.out.println("  Updating user...");
    foundUser.get().setName("Updated Test User");
    foundUser.get().setAge(26);
    User updatedUser = userDAO.update(foundUser.get());
    System.out.println("  ✅ User updated: " + updatedUser.getName() + ", Age: " + updatedUser.getAge());

    // COUNT
    long userCount = userDAO.count();
    System.out.println("  ✅ Total users in database: " + userCount);

    // DELETE
    System.out.println("  Deleting user...");
    userDAO.deleteById(savedUser.getUserId());

    // Verify deletion
    Optional<User> deletedUser = userDAO.findById(savedUser.getUserId());
    if (deletedUser.isEmpty()) {
      System.out.println("  ✅ User successfully deleted");
    } else {
      throw new RuntimeException("User was not deleted!");
    }

    System.out.println("  ✅ Basic CRUD operations completed successfully!\n");
  }

  private static void testCustomFinders() {
    System.out.println("🔍 Test 2: Custom Finder Methods");

    // Create test users
    User user1 = createTestUser("john_doe", "john@example.com", "John Doe");
    User user2 = createTestUser("jane_smith", "jane@example.com", "Jane Smith");
    user2.setGoogleId("google123456");

    User savedUser1 = userDAO.save(user1);
    User savedUser2 = userDAO.save(user2);

    // Test findByUsername
    System.out.println("  Testing findByUsername...");
    Optional<User> userByUsername = userDAO.findByUsername("john_doe");
    if (userByUsername.isPresent()) {
      System.out.println("  ✅ Found user by username: " + userByUsername.get().getName());
    } else {
      throw new RuntimeException("findByUsername failed!");
    }

    // Test findByEmail
    System.out.println("  Testing findByEmail...");
    Optional<User> userByEmail = userDAO.findByEmail("jane@example.com");
    if (userByEmail.isPresent()) {
      System.out.println("  ✅ Found user by email: " + userByEmail.get().getName());
    } else {
      throw new RuntimeException("findByEmail failed!");
    }

    // Test findByGoogleId
    System.out.println("  Testing findByGoogleId...");
    Optional<User> userByGoogleId = userDAO.findByGoogleId("google123456");
    if (userByGoogleId.isPresent()) {
      System.out.println("  ✅ Found user by Google ID: " + userByGoogleId.get().getName());
    } else {
      throw new RuntimeException("findByGoogleId failed!");
    }

    // Clean up
    userDAO.deleteById(savedUser1.getUserId());
    userDAO.deleteById(savedUser2.getUserId());

    System.out.println("  ✅ Custom finder methods completed successfully!\n");
  }

  private static void testExistenceMethods() {
    System.out.println("🔎 Test 3: Existence Check Methods");

    // Create test user
    User testUser = createTestUser("existence_test", "exists@example.com", "Existence Test");
    User savedUser = userDAO.save(testUser);

    // Test existsByUsername
    System.out.println("  Testing existsByUsername...");
    boolean existsByUsername = userDAO.existsByUsername("existence_test");
    if (existsByUsername) {
      System.out.println("  ✅ existsByUsername returned true");
    } else {
      throw new RuntimeException("existsByUsername should return true!");
    }

    // Test existsByEmail
    System.out.println("  Testing existsByEmail...");
    boolean existsByEmail = userDAO.existsByEmail("exists@example.com");
    if (existsByEmail) {
      System.out.println("  ✅ existsByEmail returned true");
    } else {
      throw new RuntimeException("existsByEmail should return true!");
    }

    // Test non-existent user
    boolean notExists = userDAO.existsByUsername("nonexistent_user");
    if (!notExists) {
      System.out.println("  ✅ existsByUsername correctly returned false for non-existent user");
    } else {
      throw new RuntimeException("existsByUsername should return false for non-existent user!");
    }

    // Clean up
    userDAO.deleteById(savedUser.getUserId());

    System.out.println("  ✅ Existence check methods completed successfully!\n");
  }

  private static void testRoleQueries() {
    System.out.println("👥 Test 4: Role-based Queries");

    // Create users with different roles
    User adminUser = createTestUser("admin_user", "admin@example.com", "Admin User");
    adminUser.setRole(User.Role.ADMIN);

    User regularUser = createTestUser("regular_user", "user@example.com", "Regular User");
    regularUser.setRole(User.Role.USER);

    User affiliateUser = createTestUser("affiliate_user", "affiliate@example.com", "Affiliate User");
    affiliateUser.setRole(User.Role.AFFILIATE);

    User savedAdmin = userDAO.save(adminUser);
    User savedUser = userDAO.save(regularUser);
    User savedAffiliate = userDAO.save(affiliateUser);

    // Test findByRole
    System.out.println("  Testing findByRole for ADMIN...");
    List<User> adminUsers = userDAO.findByRole(User.Role.ADMIN);
    if (!adminUsers.isEmpty()) {
      System.out.println("  ✅ Found " + adminUsers.size() + " admin user(s)");
    } else {
      throw new RuntimeException("Should find at least one admin user!");
    }

    System.out.println("  Testing findByRole for USER...");
    List<User> regularUsers = userDAO.findByRole(User.Role.USER);
    if (!regularUsers.isEmpty()) {
      System.out.println("  ✅ Found " + regularUsers.size() + " regular user(s)");
    } else {
      throw new RuntimeException("Should find at least one regular user!");
    }

    // Clean up
    userDAO.deleteById(savedAdmin.getUserId());
    userDAO.deleteById(savedUser.getUserId());
    userDAO.deleteById(savedAffiliate.getUserId());

    System.out.println("  ✅ Role-based queries completed successfully!\n");
  }

  private static void testPagination() {
    System.out.println("📄 Test 5: Pagination");

    // Create multiple users for pagination test
    System.out.println("  Creating 5 test users for pagination...");
    User[] users = new User[5];
    for (int i = 0; i < 5; i++) {
      users[i] = createTestUser("pageuser" + i, "page" + i + "@example.com", "Page User " + i);
      users[i] = userDAO.save(users[i]);
    }

    // Test pagination
    System.out.println("  Testing pagination (page 0, size 3)...");
    List<User> firstPage = userDAO.findAll(0, 3);
    System.out.println("  ✅ First page returned " + firstPage.size() + " users");

    System.out.println("  Testing pagination (page 1, size 3)...");
    List<User> secondPage = userDAO.findAll(1, 3);
    System.out.println("  ✅ Second page returned " + secondPage.size() + " users");

    // Clean up
    for (User user : users) {
      userDAO.deleteById(user.getUserId());
    }

    System.out.println("  ✅ Pagination test completed successfully!\n");
  }

  private static void testBatchOperations() {
    System.out.println("🔄 Test 6: Batch Operations");

    // Create multiple users
    System.out.println("  Creating multiple users for batch operations...");
    User[] users = new User[3];
    for (int i = 0; i < 3; i++) {
      users[i] = createTestUser("batchuser" + i, "batch" + i + "@example.com", "Batch User " + i);
      users[i] = userDAO.save(users[i]);
    }

    // Test findAll
    System.out.println("  Testing findAll...");
    List<User> allUsers = userDAO.findAll();
    System.out.println("  ✅ Found " + allUsers.size() + " total users");

    // Test count
    long totalCount = userDAO.count();
    System.out.println("  ✅ Total count: " + totalCount);

    // Clean up
    for (User user : users) {
      userDAO.deleteById(user.getUserId());
    }

    System.out.println("  ✅ Batch operations completed successfully!\n");
  }

  private static User createTestUser(String username, String email, String name) {
    User user = new User();
    user.setUserName(username);
    user.setEmail(email);
    user.setPassword("password123");
    user.setName(name);
    user.setAge(25);
    user.setRole(User.Role.USER);
    return user;
  }
}
