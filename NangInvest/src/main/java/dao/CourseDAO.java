package dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import model.Course;

/**
 * Data Access Object for Course entity
 * Extends the generic DAO pattern and provides course-specific queries
 */
public class CourseDAO extends GenericDAOImpl<Course, Integer> {

    /**
     * Find course by name
     * 
     * @param courseName the course name to search for
     * @return Optional containing the course if found
     */

    public Optional<Course> findByCourseName(String courseName) {
        String jpql = "SELECT c FROM Course c WHERE c.courseName = ?1";
        return findSingleByQuery(jpql, courseName);
    }

    /**
     * Check if a course exists by name
     * 
     * @param courseName the course name to check
     * @return true if course exists, false otherwise
     */
    public boolean existsByCourseName(String courseName) {
        String jpql = "SELECT COUNT(c) FROM Course c WHERE c.courseName = ?1";
        return countByQuery(jpql, courseName) > 0;
    }

    /**
     * Find all free courses
     * 
     * @return List of courses that are free
     */
    public List<Course> findFreeCourses() {
        String jpql = "SELECT c FROM Course c WHERE c.isFree = true ORDER BY c.courseName";
        return findByQuery(jpql);
    }

    /**
     * find all paid course
     * 
     * @return List of courses that are paid
     */
    public List<Course> findPaidCourses() {
        String jpql = "SELECT c FROM Course c WHERE c.isFree = false ORDER BY c.price ASC";
        return findByQuery(jpql);
    }

    /**
     * Find course within a price range
     * 
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return List of courses within the specified price range
     */
    public List<Course> findCoursesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        String jpql = "SELECT c FROM Course c WHERE c.price BETWEEN ?1 AND ?2 ORDER BY c.price";
        return findByQuery(jpql, minPrice, maxPrice);
    }

    /**
     * Find courses by maximum prie
     * 
     * @param maxPrice maximum price (inclusive)
     * @return List of courses under or equal to the specified price
     */
    public List<Course> findCoursesUnderPrice(BigDecimal maxPrice) {
        String jpql = "SELECT c FROM Course c WHERE c.price <= ?1 ORDER BY c.price";
        return findByQuery(jpql, maxPrice);
    }

    /**
     * Find courses by time duration
     * 
     * @param timeDuration the time duration to search for
     * @return List of courses with the specifued duration
     */
    public List<Course> findCoursesByTime(String timeDuration) {
        String jpql = "SELECT c FROM Course c WHERE c.time = ?1 ORDER BY c.courseName";
        return findByQuery(jpql, timeDuration);
    }

    /**
     * Search courses by name (partial match)
     * 
     * @param searchTerm the search term to look for in course names
     * @return List of courses whose names contain the search term
     */
    public List<Course> searchCoursesByName(String searchTerm) {
        String jpql = "SELECT c FROM Course c WHERE LOWER(c.courseName) LIKE LOWER(?1) ORDER BY c.courseName";
        return findByQuery(jpql, "%" + searchTerm + "%");
    }

    /**
     * Find courses by free status
     * 
     * @param isFree true for free courses, false for paid courses
     * @return List of courses with the specified free status
     */
    public List<Course> findCoursesByFreeStatus(boolean isFree) {
        String jpql = "SELECT c FROM Course c WHERE c.isFree = ?1 ORDER BY c.courseName";
        return findByQuery(jpql, isFree);
    }

    /**
     * Get the most expensive course
     * 
     * @return Optional containing the most expensive course if found
     */
    public Optional<Course> findMostExpensiveCourse() {
        String jpql = "SELECT  c FROM Course c WHERE c.price = (SELECT MAX(c2.price) FROM Course c2)";
        return findSingleByQuery(jpql);
    }

    /**
     * Get the cheapest paid course (excluding free courses)
     * 
     * @return Optional containing the cheapest paid course if found
     */
    public Optional<Course> findCheapestPaidCourse() {
        String jpql = "SELECT c FROM Course c WHERE c.isFree = false AND c.price = (SELECT MIN(c2.price) FROM Course c2 WHERE c2.isFree = false)";
        return findSingleByQuery(jpql);
    }

    /**
     * Count free couses
     * 
     * @return number of free course
     */
    public long countFreeCourses() {
        String jpql = "SELECT COUNT(c) FROM Course c WHERE c.isFree = true";
        return countByQuery(jpql);
    }

    /**
     * count paid courses
     * 
     * @return number of paid courses
     */
    public long countPaidCourses() {
        String jpql = "SELECT COUNT(c) FROM Course c WHERE c.isFree = false";
        return countByQuery(jpql);
    }

    /**
     * Get average course price (excluding free courses)
     * 
     * @return average price of paid courses
     */
    public BigDecimal getAverageCoursePrice() {
        String jpql = "SELECT AVG(c.price) FROM Course c WHERE c.isFree = false";
        List<?> result = findByQuery(jpql);
        if (result.isEmpty() || result.get(0) == null) {
            return BigDecimal.ZERO;
        }
        // AVG() returns Double, so convert it to BigDecimal
        Double avgValue = (Double) result.get(0);
        return BigDecimal.valueOf(avgValue);
    }

    /**
     * Find courses with images
     * 
     * @return List of courses that have image URLs
     */
    public List<Course> findCoursesWithImages() {
        String jpql = "SELECT c FROM Course c WHERE c.imageUrl IS NOT NULL AND c.imageUrl != '' ORDER BY c.courseName";
        return findByQuery(jpql);
    }

    /**
     * Find courses without images
     * 
     * @return List of courses that don't have image URLs
     */
    public List<Course> findCoursesWithoutImages() {
        String jpql = "SELECT c FROM Course c WHERE c.imageUrl IS NULL OR c.imageUrl = '' ORDER BY c.courseName";
        return findByQuery(jpql);
    }
}
