package test;

import dao.UserCoursesDAO;
import model.UserCourses;
import model.UserCourses.UserCoursesId;
import util.JPAUtil;

import java.util.List;

/**
 * Test class for UserCoursesDAO
 * Tests CRUD operations and custom queries for UserCourses
 */
public class UserCoursesDAOTest {

    private static UserCoursesDAO userCoursesDAO;

    public static void main(String[] args) {
        System.out.println("=== Starting UserCoursesDAO Test ===");
        
        try {
            // Initialize DAO
            userCoursesDAO = new UserCoursesDAO();
            
            // Run all tests
            testCRUDOperations();
            testCustomQueries();
            testUtilityMethods();
            testEdgeCases();
            
            System.out.println("=== All UserCoursesDAO tests completed successfully! ===");
            
        } catch (Exception e) {
            System.err.println("Test failed with exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            JPAUtil.closeEntityManagerFactory();
        }
    }

    private static void testCRUDOperations() {
        System.out.println("\n--- Testing CRUD Operations ---");

        // Test Create
        UserCourses userCourse1 = new UserCourses();
        userCourse1.setUserId(1);
        userCourse1.setCourseId(101);

        UserCourses savedUserCourse1 = userCoursesDAO.save(userCourse1);
        System.out.println("Created user-course: User " + savedUserCourse1.getUserId() + 
                          ", Course " + savedUserCourse1.getCourseId());

        UserCourses userCourse2 = new UserCourses();
        userCourse2.setUserId(1);
        userCourse2.setCourseId(102);

        UserCourses savedUserCourse2 = userCoursesDAO.save(userCourse2);
        System.out.println("Created user-course: User " + savedUserCourse2.getUserId() + 
                          ", Course " + savedUserCourse2.getCourseId());

        UserCourses userCourse3 = new UserCourses();
        userCourse3.setUserId(2);
        userCourse3.setCourseId(101);

        UserCourses savedUserCourse3 = userCoursesDAO.save(userCourse3);
        System.out.println("Created user-course: User " + savedUserCourse3.getUserId() + 
                          ", Course " + savedUserCourse3.getCourseId());

        // Test Read with composite key
        UserCoursesId compositeId = new UserCoursesId(1, 101);
        UserCourses foundUserCourse = userCoursesDAO.findById(compositeId).orElse(null);
        System.out.println("Found user-course by composite ID: " + 
                          (foundUserCourse != null ? "User " + foundUserCourse.getUserId() + ", Course " + foundUserCourse.getCourseId() : "Not found"));

        // Test findAll
        List<UserCourses> allUserCourses = userCoursesDAO.findAll();
        System.out.println("Total user-course relationships in database: " + allUserCourses.size());
    }

    private static void testCustomQueries() {
        System.out.println("\n--- Testing Custom Queries ---");

        // Test findByUserId
        List<UserCourses> user1Courses = userCoursesDAO.findByUserId(1);
        System.out.println("Courses for user 1: " + user1Courses.size());

        List<UserCourses> user2Courses = userCoursesDAO.findByUserId(2);
        System.out.println("Courses for user 2: " + user2Courses.size());

        // Test findByCourseId
        List<UserCourses> course101Users = userCoursesDAO.findByCourseId(101);
        System.out.println("Users enrolled in course 101: " + course101Users.size());

        List<UserCourses> course102Users = userCoursesDAO.findByCourseId(102);
        System.out.println("Users enrolled in course 102: " + course102Users.size());

        // Test isUserEnrolledInCourse
        boolean user1InCourse101 = userCoursesDAO.isUserEnrolledInCourse(1, 101);
        System.out.println("User 1 enrolled in course 101: " + user1InCourse101);

        boolean user1InCourse999 = userCoursesDAO.isUserEnrolledInCourse(1, 999);
        System.out.println("User 1 enrolled in course 999: " + user1InCourse999);
    }

    private static void testUtilityMethods() {
        System.out.println("\n--- Testing Utility Methods ---");

        // Test getCourseCountByUserId
        long user1CourseCount = userCoursesDAO.getCourseCountByUserId(1);
        System.out.println("Course count for user 1: " + user1CourseCount);

        long user2CourseCount = userCoursesDAO.getCourseCountByUserId(2);
        System.out.println("Course count for user 2: " + user2CourseCount);

        // Test getUserCountByCourseId
        long course101UserCount = userCoursesDAO.getUserCountByCourseId(101);
        System.out.println("User count for course 101: " + course101UserCount);

        long course102UserCount = userCoursesDAO.getUserCountByCourseId(102);
        System.out.println("User count for course 102: " + course102UserCount);

        // Test getMostPopularCourses
        List<Object[]> popularCourses = userCoursesDAO.getMostPopularCourses(5);
        System.out.println("Top 5 most popular courses:");
        for (Object[] row : popularCourses) {
            Integer courseId = (Integer) row[0];
            Long userCount = (Long) row[1];
            System.out.println("  - Course ID: " + courseId + ", User Count: " + userCount);
        }

        // Test getMostActiveUsers
        List<Object[]> activeUsers = userCoursesDAO.getMostActiveUsers(5);
        System.out.println("Top 5 users with most courses:");
        for (Object[] row : activeUsers) {
            Integer userId = (Integer) row[0];
            Long courseCount = (Long) row[1];
            System.out.println("  - User ID: " + userId + ", Course Count: " + courseCount);
        }

        // Test enrollUserInCourse
        UserCourses enrolled = userCoursesDAO.enrollUserInCourse(3, 101);
        System.out.println("Enrolled user 3 in course 101: " + (enrolled != null));

        // Try enrolling again (should return null)
        UserCourses enrolledAgain = userCoursesDAO.enrollUserInCourse(3, 101);
        System.out.println("Tried enrolling user 3 in course 101 again: " + (enrolledAgain != null));

        // Test getTotalEnrollmentCount
        long totalEnrollments = userCoursesDAO.getTotalEnrollmentCount();
        System.out.println("Total enrollment count: " + totalEnrollments);
    }

    private static void testEdgeCases() {
        System.out.println("\n--- Testing Edge Cases ---");

        // Test queries with non-existent data
        List<UserCourses> nonExistentUserCourses = userCoursesDAO.findByUserId(99999);
        System.out.println("Courses for non-existent user: " + nonExistentUserCourses.size());

        List<UserCourses> nonExistentCoursesUsers = userCoursesDAO.findByCourseId(99999);
        System.out.println("Users for non-existent course: " + nonExistentCoursesUsers.size());

        // Test isUserEnrolledInCourse with non-existent data
        boolean nonExistentUserEnrolled = userCoursesDAO.isUserEnrolledInCourse(99999, 101);
        System.out.println("Non-existent user enrolled in course: " + nonExistentUserEnrolled);

        boolean userInNonExistentCourse = userCoursesDAO.isUserEnrolledInCourse(1, 99999);
        System.out.println("User enrolled in non-existent course: " + userInNonExistentCourse);

        // Test utility methods with non-existent data
        long nonExistentUserCourseCount = userCoursesDAO.getCourseCountByUserId(99999);
        System.out.println("Course count for non-existent user: " + nonExistentUserCourseCount);

        long nonExistentCourseUserCount = userCoursesDAO.getUserCountByCourseId(99999);
        System.out.println("User count for non-existent course: " + nonExistentCourseUserCount);

        // Test unenrollUserFromCourse
        boolean unenrolled = userCoursesDAO.unenrollUserFromCourse(3, 101);
        System.out.println("Unenrolled user 3 from course 101: " + unenrolled);

        // Try unenrolling again (should return false)
        boolean unenrolledAgain = userCoursesDAO.unenrollUserFromCourse(3, 101);
        System.out.println("Tried unenrolling user 3 from course 101 again: " + unenrolledAgain);

        // Test removeAllEnrollmentsByUserId
        int unenrolledCount = userCoursesDAO.removeAllEnrollmentsByUserId(2);
        System.out.println("Removed all enrollments for user 2: " + unenrolledCount);

        // Test removeAllEnrollmentsByCourseId
        int removedUsers = userCoursesDAO.removeAllEnrollmentsByCourseId(102);
        System.out.println("Removed all users from course 102: " + removedUsers);

        // Test count methods
        Long totalCount = userCoursesDAO.count();
        System.out.println("Total user-course relationships (generic): " + totalCount);

        // Test findById with non-existent composite key
        UserCoursesId nonExistentId = new UserCoursesId(99999, 99999);
        UserCourses nonExistentUserCourse = userCoursesDAO.findById(nonExistentId).orElse(null);
        System.out.println("Non-existent user-course relationship: " + (nonExistentUserCourse != null ? "Found" : "Not found"));

        if (totalCount > 0) {
            List<UserCourses> allUserCourses = userCoursesDAO.findAll();
            UserCourses firstUserCourse = allUserCourses.get(0);
            UserCoursesId firstId = new UserCoursesId(firstUserCourse.getUserId(), firstUserCourse.getCourseId());
            boolean exists = userCoursesDAO.existsById(firstId);
            System.out.println("First user-course relationship exists by ID: " + exists);
        }
    }
}
