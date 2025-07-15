package test;

import java.util.List;

import dao.BooksDAO;
import dao.CourseDAO;
import dao.UserDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Book;
import model.Course;
import model.Order;
import model.User;
import util.JPAUtil;

/**
 * Comprehensive JPA Integration Test
 * Tests all entities, DAOs, and database connectivity
 */
public class JPAIntegrationTest {

  public static void main(String[] args) {
    System.out.println("🔍 === COMPREHENSIVE JPA DIAGNOSIS ===\n");

    JPAIntegrationTest test = new JPAIntegrationTest();
    test.runAllTests();
  }

  public void runAllTests() {
    // Test 1: Basic JPA Setup
    testEntityManagerFactory();

    // Test 2: Database Connection
    testDatabaseConnection();

    // Test 3: Entity Loading
    testEntityRegistration();

    // Test 4: DAO Initialization
    testDAOInitialization();

    // Test 5: CRUD Operations
    testCRUDOperations();

    // Test 6: SQL Queries
    testNativeQueries();

    System.out.println("\n🎯 === TEST SUMMARY ===");
    System.out.println("Check above for any ❌ errors to identify JPA issues");
  }

  /**
   * Test 1: EntityManagerFactory Creation
   */
  public void testEntityManagerFactory() {
    System.out.println("1️⃣ Testing EntityManagerFactory...");

    try {
      EntityManagerFactory emf = Persistence.createEntityManagerFactory("NangInvestPU");
      System.out.println("   ✅ EntityManagerFactory created successfully");

      EntityManager em = emf.createEntityManager();
      System.out.println("   ✅ EntityManager created successfully");

      em.close();
      emf.close();

    } catch (Exception e) {
      System.err.println("   ❌ EntityManagerFactory Error: " + e.getClass().getSimpleName());
      System.err.println("      Message: " + e.getMessage());
      if (e.getCause() != null) {
        System.err.println("      Root Cause: " + e.getCause().getMessage());
      }
      printSolution("EMF_ERROR", e);
    }
    System.out.println();
  }

  /**
   * Test 2: Database Connection
   */
  public void testDatabaseConnection() {
    System.out.println("2️⃣ Testing Database Connection...");

    try {
      EntityManager em = JPAUtil.gEntityManager();

      // Test basic SQL query
      Object result = em.createNativeQuery("SELECT 1").getSingleResult();
      System.out.println("   ✅ Database connection successful (Result: " + result + ")");

      // Test database info
      try {
        String dbName = (String) em.createNativeQuery("SELECT DATABASE()").getSingleResult();
        System.out.println("   ✅ Connected to database: " + dbName);
      } catch (Exception e) {
        // Try SQL Server version
        try {
          String dbName = (String) em.createNativeQuery("SELECT DB_NAME()").getSingleResult();
          System.out.println("   ✅ Connected to SQL Server database: " + dbName);
        } catch (Exception e2) {
          System.out.println("   ⚠️ Cannot determine database type");
        }
      }

      em.close();

    } catch (Exception e) {
      System.err.println("   ❌ Database Connection Error: " + e.getClass().getSimpleName());
      System.err.println("      Message: " + e.getMessage());
      printSolution("DB_CONNECTION_ERROR", e);
    }
    System.out.println();
  }

  /**
   * Test 3: Entity Registration
   */
  public void testEntityRegistration() {
    System.out.println("3️⃣ Testing Entity Registration...");

    try {
      EntityManager em = JPAUtil.gEntityManager();

      // Test each entity
      testEntity(em, User.class, "users");
      testEntity(em, Course.class, "Courses");
      testEntity(em, Book.class, "Books");
      testEntity(em, Order.class, "[Order]"); // SQL Server syntax

      em.close();
      System.out.println("   ✅ All entities registered correctly");

    } catch (Exception e) {
      System.err.println("   ❌ Entity Registration Error: " + e.getClass().getSimpleName());
      System.err.println("      Message: " + e.getMessage());
      printSolution("ENTITY_ERROR", e);
    }
    System.out.println();
  }

  /**
   * Test 4: DAO Initialization
   */
  public void testDAOInitialization() {
    System.out.println("4️⃣ Testing DAO Initialization...");

    try {
      // Test UserDAO
      UserDAO userDAO = new UserDAO();
      System.out.println("   ✅ UserDAO initialized");

      // Test CourseDAO
      CourseDAO courseDAO = new CourseDAO();
      System.out.println("   ✅ CourseDAO initialized");

      // Test BooksDAO
      BooksDAO booksDAO = new BooksDAO();
      System.out.println("   ✅ BooksDAO initialized");

    } catch (Exception e) {
      System.err.println("   ❌ DAO Initialization Error: " + e.getClass().getSimpleName());
      System.err.println("      Message: " + e.getMessage());
      printSolution("DAO_ERROR", e);
    }
    System.out.println();
  }

  /**
   * Test 5: CRUD Operations
   */
  public void testCRUDOperations() {
    System.out.println("5️⃣ Testing CRUD Operations...");

    try {
      UserDAO userDAO = new UserDAO();

      // Test Read operations
      List<User> users = userDAO.findAll();
      System.out.println("   ✅ Read operation successful (" + users.size() + " users found)");

      // Test specific queries
      long userCount = userDAO.count();
      System.out.println("   ✅ Count operation successful (" + userCount + " users)");

      // Test Course operations
      CourseDAO courseDAO = new CourseDAO();
      List<Course> courses = courseDAO.findAll();
      System.out.println("   ✅ Course read successful (" + courses.size() + " courses found)");

      // Test Book operations
      BooksDAO booksDAO = new BooksDAO();
      List<Book> books = booksDAO.findAll();
      System.out.println("   ✅ Book read successful (" + books.size() + " books found)");

    } catch (Exception e) {
      System.err.println("   ❌ CRUD Operations Error: " + e.getClass().getSimpleName());
      System.err.println("      Message: " + e.getMessage());
      printSolution("CRUD_ERROR", e);
    }
    System.out.println();
  }

  /**
   * Test 6: Native Queries
   */
  public void testNativeQueries() {
    System.out.println("6️⃣ Testing Native Queries...");

    try {
      EntityManager em = JPAUtil.gEntityManager();

      // Test table existence
      testTableExists(em, "users", "User");
      testTableExists(em, "Courses", "Course");
      testTableExists(em, "Books", "Book");
      testTableExists(em, "[Order]", "Order");

      em.close();

    } catch (Exception e) {
      System.err.println("   ❌ Native Query Error: " + e.getClass().getSimpleName());
      System.err.println("      Message: " + e.getMessage());
      printSolution("QUERY_ERROR", e);
    }
    System.out.println();
  }

  // Helper Methods
  private void testEntity(EntityManager em, Class<?> entityClass, String tableName) {
    try {
      String jpql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
      Long count = (Long) em.createQuery(jpql).getSingleResult();
      System.out.println("   ✅ " + entityClass.getSimpleName() + " entity OK (" + count + " records)");
    } catch (Exception e) {
      System.err.println("   ❌ " + entityClass.getSimpleName() + " entity error: " + e.getMessage());
    }
  }

  private void testTableExists(EntityManager em, String tableName, String entityName) {
    try {
      String sql = "SELECT COUNT(*) FROM " + tableName;
      Object result = em.createNativeQuery(sql).getSingleResult();
      System.out.println("   ✅ Table " + tableName + " exists (" + result + " records)");
    } catch (Exception e) {
      System.err.println("   ❌ Table " + tableName + " error: " + e.getMessage());
    }
  }

  private void printSolution(String errorType, Exception e) {
    System.err.println("\n   🛠️ SOLUTION SUGGESTIONS:");

    switch (errorType) {
      case "EMF_ERROR":
        System.err.println("      1. Check persistence.xml configuration");
        System.err.println("      2. Verify database driver is in classpath");
        System.err.println("      3. Check database URL and credentials");
        break;

      case "DB_CONNECTION_ERROR":
        if (e.getMessage().contains("Access denied")) {
          System.err.println("      1. Check database username/password in persistence.xml");
          System.err.println("      2. Verify database user has proper permissions");
        } else if (e.getMessage().contains("Communications link failure")) {
          System.err.println("      1. Check if MySQL/SQL Server is running");
          System.err.println("      2. Verify database URL and port");
          System.err.println("      3. Check firewall settings");
        } else {
          System.err.println("      1. Verify database server is running");
          System.err.println("      2. Check connection parameters");
        }
        break;

      case "ENTITY_ERROR":
        System.err.println("      1. Add missing entities to persistence.xml");
        System.err.println("      2. Check entity annotations");
        System.err.println("      3. Verify table names match database");
        break;

      case "DAO_ERROR":
        System.err.println("      1. Fix entity mapping issues first");
        System.err.println("      2. Check DAO constructor dependencies");
        break;

      case "CRUD_ERROR":
        System.err.println("      1. Check table structure matches entities");
        System.err.println("      2. Verify foreign key constraints");
        System.err.println("      3. Check data types compatibility");
        break;

      case "QUERY_ERROR":
        System.err.println("      1. Verify table names exist in database");
        System.err.println("      2. Check SQL syntax for your database type");
        break;
    }
    System.err.println();
  }
}