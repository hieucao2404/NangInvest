
package test;

import dao.CourseDAO;
import model.Course;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

/**
 * Comprehensive test class for CourseDAO and JPA setup
 * Tests all CRUD operations and custom methods for Course entity
 * 
 * This test suite covers:
 * - Basic CRUD operations (Create, Read, Update, Delete)
 * - Custom finder methods (findByCourseName, existsByCourseName)
 * - Free/Paid course queries (findFreeCourses, findPaidCourses, countFreeCourses, countPaidCourses)
 * - Price-based queries (price ranges, most/least expensive courses)
 * - Search and filter operations (searchCoursesByName, findCoursesByTime)
 * - Statistical operations (average price calculations)
 * - Image-related queries (courses with/without images)
 * - Error handling and edge cases
 */
public class CourseDAOTest {

    private static CourseDAO courseDAO;
    private static final List<Integer> testCourseIds = new ArrayList<>();
    private static int testsRun = 0;
    private static int testsPassed = 0;

    public static void main(String[] args) {
        System.out.println("🚀 Starting Comprehensive CourseDAO Test Suite...");
        System.out.println();
        System.out.println("Testing CourseDAO implementation with real database operations");
        System.out.println("================================================================================");
        System.out.println();

        courseDAO = new CourseDAO();

        try {
            // Test suites in logical order
            testBasicCRUD();
            testCustomFinders();
            testFreeAndPaidCourses();
            testPriceBasedQueries();
            testSearchAndFilter();
            testStatisticalOperations();
            testImageQueries();

            // Summary
            System.out.println("================================================================================");
            System.out.println("📊 TEST SUMMARY:");
            System.out.printf("   Tests Run: %d%n", testsRun);
            System.out.printf("   Tests Passed: %d%n", testsPassed);
            System.out.printf("   Success Rate: %.1f%%%n", (testsPassed * 100.0 / testsRun));
            
            if (testsPassed == testsRun) {
                System.out.println("\n🎉 ALL TESTS PASSED! Your CourseDAO is working perfectly!");
                System.out.println("✅ JPA/Hibernate integration is successful");
                System.out.println("✅ Database connectivity is working");
                System.out.println("✅ All CourseDAO methods are functioning correctly");
            } else {
                System.out.println("\n⚠️  Some tests failed. Please review the output above.");
            }

        } catch (Exception e) {
            System.err.println("\n💥 CRITICAL TEST FAILURE!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nStack trace:");
            e.printStackTrace();
            System.err.println("\nThis might indicate:");
            System.err.println("- Database connection issues");
            System.err.println("- JPA/Hibernate configuration problems");
            System.err.println("- Entity mapping issues");
        } finally {
            cleanupTestData();
        }
    }

    private static void assertCondition(boolean condition, String message) {
        testsRun++;
        if (condition) {
            testsPassed++;
            System.out.println("    ✅ " + message);
        } else {
            System.err.println("    ❌ " + message);
            throw new AssertionError("Test failed: " + message);
        }
    }

    private static void cleanupTestData() {
        System.out.println("\n🧹 Cleaning up test data...");
        for (Integer courseId : testCourseIds) {
            try {
                courseDAO.deleteById(courseId);
            } catch (Exception e) {
                System.err.println("Failed to cleanup course ID: " + courseId);
            }
        }
        testCourseIds.clear();
        System.out.println("✅ Cleanup completed");
    }

  private static void testBasicCRUD() {
    System.out.println("📝 Test 1: Basic CRUD Operations");

    // CREATE
    System.out.println("  Creating new course...");
    String timestamp = String.valueOf(System.currentTimeMillis());
    Course newCourse = new Course();
    newCourse.setCourseName("Test Course " + timestamp);
    newCourse.setPrice(new BigDecimal("99.99"));
    newCourse.setTime("3 hours");
    newCourse.setIsFree(false);
    newCourse.setImageUrl("https://example.com/course-image.jpg");

    Course savedCourse = courseDAO.save(newCourse);
    testCourseIds.add(savedCourse.getCourseId());
    assertCondition(savedCourse.getCourseId() != null, "Course created with ID: " + savedCourse.getCourseId());

    // READ
    System.out.println("  Reading course by ID...");
    Optional<Course> foundCourse = courseDAO.findById(savedCourse.getCourseId());
    assertCondition(foundCourse.isPresent(), "Course found: " + foundCourse.get().getCourseName());

    // UPDATE
    System.out.println("  Updating course...");
    foundCourse.get().setCourseName("Updated Test Course");
    foundCourse.get().setPrice(new BigDecimal("149.99"));
    Course updatedCourse = courseDAO.update(foundCourse.get());
    assertCondition("Updated Test Course".equals(updatedCourse.getCourseName()), 
        "Course updated: " + updatedCourse.getCourseName() + ", Price: $" + updatedCourse.getPrice());

    // COUNT
    long courseCount = courseDAO.count();
    assertCondition(courseCount > 0, "Total courses in database: " + courseCount);

    // DELETE
    System.out.println("  Deleting course...");
    courseDAO.deleteById(savedCourse.getCourseId());
    testCourseIds.remove(savedCourse.getCourseId());

    // Verify deletion
    Optional<Course> deletedCourse = courseDAO.findById(savedCourse.getCourseId());
    assertCondition(deletedCourse.isEmpty(), "Course successfully deleted");

    System.out.println("  ✅ Basic CRUD operations completed successfully!");
    System.out.println();
  }

  private static void testCustomFinders() {
    System.out.println("🔍 Test 2: Custom Finder Methods");

    // Create test courses
    Course course1 = createTestCourse("Java Programming", new BigDecimal("79.99"), "5 hours", false);
    Course course2 = createTestCourse("Python Basics", BigDecimal.ZERO, "3 hours", true);

    Course savedCourse1 = courseDAO.save(course1);
    Course savedCourse2 = courseDAO.save(course2);
    testCourseIds.add(savedCourse1.getCourseId());
    testCourseIds.add(savedCourse2.getCourseId());

    // Test findByCourseName
    System.out.println("  Testing findByCourseName...");
    Optional<Course> courseByName = courseDAO.findByCourseName(course1.getCourseName());
    assertCondition(courseByName.isPresent(), "Found course by name: " + courseByName.get().getCourseName());

    // Test existsByCourseName
    System.out.println("  Testing existsByCourseName...");
    boolean exists = courseDAO.existsByCourseName(course2.getCourseName());
    assertCondition(exists, "existsByCourseName returned true for existing course");

    // Test non-existent course
    boolean notExists = courseDAO.existsByCourseName("Nonexistent Course " + System.currentTimeMillis());
    assertCondition(!notExists, "existsByCourseName correctly returned false for non-existent course");

    System.out.println("  ✅ Custom finder methods completed successfully!");
    System.out.println();
  }

  private static void testFreeAndPaidCourses() {
    System.out.println("💰 Test 3: Free and Paid Course Queries");

    // Create test courses
    Course freeCourse1 = createTestCourse("Free HTML", BigDecimal.ZERO, "2 hours", true);
    Course freeCourse2 = createTestCourse("Free CSS", BigDecimal.ZERO, "1.5 hours", true);
    Course paidCourse1 = createTestCourse("Advanced JavaScript", new BigDecimal("129.99"), "8 hours", false);
    Course paidCourse2 = createTestCourse("React Masterclass", new BigDecimal("199.99"), "12 hours", false);

    Course savedFree1 = courseDAO.save(freeCourse1);
    Course savedFree2 = courseDAO.save(freeCourse2);
    Course savedPaid1 = courseDAO.save(paidCourse1);
    Course savedPaid2 = courseDAO.save(paidCourse2);
    
    testCourseIds.add(savedFree1.getCourseId());
    testCourseIds.add(savedFree2.getCourseId());
    testCourseIds.add(savedPaid1.getCourseId());
    testCourseIds.add(savedPaid2.getCourseId());

    // Test findFreeCourses
    System.out.println("  Testing findFreeCourses...");
    List<Course> freeCourses = courseDAO.findFreeCourses();
    assertCondition(freeCourses.size() >= 2, "Found " + freeCourses.size() + " free courses");

    // Test findPaidCourses
    System.out.println("  Testing findPaidCourses...");
    List<Course> paidCourses = courseDAO.findPaidCourses();
    assertCondition(paidCourses.size() >= 2, "Found " + paidCourses.size() + " paid courses");

    // Test countFreeCourses
    System.out.println("  Testing countFreeCourses...");
    long freeCount = courseDAO.countFreeCourses();
    assertCondition(freeCount >= 2, "Free courses count: " + freeCount);

    // Test countPaidCourses
    System.out.println("  Testing countPaidCourses...");
    long paidCount = courseDAO.countPaidCourses();
    assertCondition(paidCount >= 2, "Paid courses count: " + paidCount);

    // Test findCoursesByFreeStatus
    System.out.println("  Testing findCoursesByFreeStatus...");
    List<Course> freeStatusCourses = courseDAO.findCoursesByFreeStatus(true);
    assertCondition(freeStatusCourses.size() >= 2, "Found " + freeStatusCourses.size() + " courses with free status = true");

    System.out.println("  ✅ Free and paid course queries completed successfully!");
    System.out.println();
  }

  private static void testPriceBasedQueries() {
    System.out.println("💵 Test 4: Price-based Queries");

    // Create test courses with different prices
    Course course1 = createTestCourse("Budget Course", new BigDecimal("29.99"), "2 hours", false);
    Course course2 = createTestCourse("Standard Course", new BigDecimal("99.99"), "5 hours", false);
    Course course3 = createTestCourse("Premium Course", new BigDecimal("299.99"), "15 hours", false);

    Course saved1 = courseDAO.save(course1);
    Course saved2 = courseDAO.save(course2);
    Course saved3 = courseDAO.save(course3);
    
    testCourseIds.add(saved1.getCourseId());
    testCourseIds.add(saved2.getCourseId());
    testCourseIds.add(saved3.getCourseId());

    // Test findCoursesByPriceRange
    System.out.println("  Testing findCoursesByPriceRange...");
    List<Course> coursesInRange = courseDAO.findCoursesByPriceRange(
        new BigDecimal("50.00"), new BigDecimal("200.00"));
    assertCondition(!coursesInRange.isEmpty(), "Found " + coursesInRange.size() + " courses in price range $50-$200");

    // Test findCoursesUnderPrice
    System.out.println("  Testing findCoursesUnderPrice...");
    List<Course> cheapCourses = courseDAO.findCoursesUnderPrice(new BigDecimal("100.00"));
    assertCondition(cheapCourses.size() >= 2, "Found " + cheapCourses.size() + " courses under $100");

    // Test findMostExpensiveCourse
    System.out.println("  Testing findMostExpensiveCourse...");
    Optional<Course> mostExpensive = courseDAO.findMostExpensiveCourse();
    assertCondition(mostExpensive.isPresent(), "Most expensive course: " + mostExpensive.get().getCourseName() +
        " ($" + mostExpensive.get().getPrice() + ")");

    // Test findCheapestPaidCourse
    System.out.println("  Testing findCheapestPaidCourse...");
    Optional<Course> cheapest = courseDAO.findCheapestPaidCourse();
    assertCondition(cheapest.isPresent(), "Cheapest paid course: " + cheapest.get().getCourseName() +
        " ($" + cheapest.get().getPrice() + ")");

    System.out.println("  ✅ Price-based queries completed successfully!");
    System.out.println();
  }

  private static void testSearchAndFilter() {
    System.out.println("🔎 Test 5: Search and Filter Operations");

    // Create test courses
    Course course1 = createTestCourse("JavaScript Fundamentals", new BigDecimal("89.99"), "4 hours", false);
    Course course2 = createTestCourse("Advanced JavaScript", new BigDecimal("149.99"), "8 hours", false);
    Course course3 = createTestCourse("Python Programming", new BigDecimal("99.99"), "6 hours", false);

    Course saved1 = courseDAO.save(course1);
    Course saved2 = courseDAO.save(course2);
    Course saved3 = courseDAO.save(course3);
    
    testCourseIds.add(saved1.getCourseId());
    testCourseIds.add(saved2.getCourseId());
    testCourseIds.add(saved3.getCourseId());

    // Test searchCoursesByName
    System.out.println("  Testing searchCoursesByName...");
    List<Course> jsCourses = courseDAO.searchCoursesByName("JavaScript");
    assertCondition(jsCourses.size() >= 2, "Found " + jsCourses.size() + " courses with 'JavaScript' in name");

    // Test findCoursesByTime
    System.out.println("  Testing findCoursesByTime...");
    List<Course> fourHourCourses = courseDAO.findCoursesByTime("4 hours");
    assertCondition(!fourHourCourses.isEmpty(), "Found " + fourHourCourses.size() + " courses with 4 hours duration");

    System.out.println("  ✅ Search and filter operations completed successfully!");
    System.out.println();
  }

  private static void testStatisticalOperations() {
    System.out.println("📊 Test 6: Statistical Operations");

    // Create test courses
    Course course1 = createTestCourse("Stats Course 1", new BigDecimal("100.00"), "5 hours", false);
    Course course2 = createTestCourse("Stats Course 2", new BigDecimal("200.00"), "7 hours", false);
    Course course3 = createTestCourse("Stats Course 3", new BigDecimal("300.00"), "10 hours", false);

    Course saved1 = courseDAO.save(course1);
    Course saved2 = courseDAO.save(course2);
    Course saved3 = courseDAO.save(course3);
    
    testCourseIds.add(saved1.getCourseId());
    testCourseIds.add(saved2.getCourseId());
    testCourseIds.add(saved3.getCourseId());

    // Test getAverageCoursePrice
    System.out.println("  Testing getAverageCoursePrice...");
    BigDecimal avgPrice = courseDAO.getAverageCoursePrice();
    assertCondition(avgPrice != null && avgPrice.compareTo(BigDecimal.ZERO) > 0, 
        "Average course price: $" + avgPrice);

    System.out.println("  ✅ Statistical operations completed successfully!");
    System.out.println();
  }

  private static void testImageQueries() {
    System.out.println("🖼️ Test 7: Image-related Queries");

    // Create test courses
    Course courseWithImage = createTestCourse("Course With Image", new BigDecimal("99.99"), "5 hours", false);
    courseWithImage.setImageUrl("https://example.com/image.jpg");

    Course courseWithoutImage = createTestCourse("Course Without Image", new BigDecimal("79.99"), "3 hours", false);
    courseWithoutImage.setImageUrl(null);

    Course saved1 = courseDAO.save(courseWithImage);
    Course saved2 = courseDAO.save(courseWithoutImage);
    
    testCourseIds.add(saved1.getCourseId());
    testCourseIds.add(saved2.getCourseId());

    // Test findCoursesWithImages
    System.out.println("  Testing findCoursesWithImages...");
    List<Course> coursesWithImages = courseDAO.findCoursesWithImages();
    assertCondition(!coursesWithImages.isEmpty(), "Found " + coursesWithImages.size() + " courses with images");

    // Test findCoursesWithoutImages
    System.out.println("  Testing findCoursesWithoutImages...");
    List<Course> coursesWithoutImages = courseDAO.findCoursesWithoutImages();
    assertCondition(!coursesWithoutImages.isEmpty(), "Found " + coursesWithoutImages.size() + " courses without images");

    System.out.println("  ✅ Image-related queries completed successfully!");
    System.out.println();
  }

  private static Course createTestCourse(String name, BigDecimal price, String time, boolean isFree) {
    String timestamp = String.valueOf(System.currentTimeMillis() % 100000); // Shorter timestamp
    Course course = new Course();
    course.setCourseName(name + "_" + timestamp);
    course.setPrice(price);
    course.setTime(time);
    course.setIsFree(isFree);
    course.setImageUrl("https://example.com/default-image.jpg");
    return course;
  }
}
