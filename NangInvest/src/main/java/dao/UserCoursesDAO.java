package dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.sql.Timestamp;
import model.UserCourses;
import model.UserCourses.UserCoursesId;

/**
 * DAO implementation for UserCourses entity (Many-to-Many relationship)
 *
 * @author Admin
 */
public class UserCoursesDAO extends GenericDAOImpl<UserCourses, UserCoursesId> {

    private LessonDAO lessonDAO = new LessonDAO();
    private UserLessonsDAO userLessonDAO = new UserLessonsDAO();

    // Custom finder methods
    public void updateProgress(Integer userId, Integer courseId, int progress) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            UserCourses userCourse = findByUserAndCourse(userId, courseId);
            if (userCourse != null) {
                userCourse.setProgress(progress);
                if (progress == 100 && userCourse.getCompletionDate() == null) {
                    userCourse.setCompletionDate(new Timestamp(System.currentTimeMillis()));
                }
                em.merge(userCourse);
            } else {
                UserCourses newUserCourse = new UserCourses(userId, courseId);
                newUserCourse.setProgress(progress);
                if (progress == 100) {
                    newUserCourse.setCompletionDate(new Timestamp(System.currentTimeMillis()));
                }
                em.persist((newUserCourse));
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update progress" + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void calculateAndUpdateProgress(Integer userId, Integer courseId) {
        long totalLessons = lessonDAO.getLessonCountByCourseId(courseId);
        long completedLessons = userLessonDAO.getCompletedLessonCount(userId, courseId);
        int progress = totalLessons > 0 ? (int) ((completedLessons * 100) / totalLessons) : 0;
        updateProgress(userId, courseId, progress);
    }

    /**
     * Find all courses for a specific user
     */
    public List<UserCourses> findByUserId(Integer userId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<UserCourses> query = em.createQuery(
                    "SELECT uc FROM UserCourses uc WHERE uc.userId = :userId ORDER BY uc.courseId", UserCourses.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find all users enrolled in a specific course
     */
    public List<UserCourses> findByCourseId(Integer courseId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<UserCourses> query = em.createQuery(
                    "SELECT uc FROM UserCourses uc WHERE uc.courseId = :courseId ORDER BY uc.userId", UserCourses.class);
            query.setParameter("courseId", courseId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Check if a user is enrolled in a specific course
     */
    public boolean isUserEnrolledInCourse(Integer userId, Integer courseId) {
        return findByUserAndCourse(userId, courseId) != null;
    }

    /**
     * Get count of courses for a user
     */
    public long getCourseCountByUserId(Integer userId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(uc) FROM UserCourses uc WHERE uc.userId = :userId", Long.class);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Get count of enrolled users for a course
     */
    public long getUserCountByCourseId(Integer courseId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(uc) FROM UserCourses uc WHERE uc.courseId = :courseId", Long.class);
            query.setParameter("courseId", courseId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Enroll user in course
     */
    public void enrollUserInCourse(int userId, int courseId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            UserCourses userCourse = new UserCourses();
            userCourse.setUserId(userId);
            userCourse.setCourseId(courseId);
            userCourse.setProgress(0);
            userCourse.setEnrollmentDate(new Timestamp(System.currentTimeMillis()));
            em.merge(userCourse);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Unenroll user from course
     */
    public boolean unenrollUserFromCourse(Integer userId, Integer courseId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            int count = em.createQuery("DELETE FROM UserCourses uc WHERE uc.userId = :userId AND uc.courseId = :courseId")
                    .setParameter("userId", userId)
                    .setParameter("courseId", courseId)
                    .executeUpdate();
            em.getTransaction().commit();
            return count > 0;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error unenrolling user from course", e);
        } finally {
            em.close();
        }
    }

    /**
     * Get most popular courses (by enrollment count)
     */
    public List<Object[]> getMostPopularCourses(int limit) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT uc.courseId, COUNT(uc) as enrollmentCount FROM UserCourses uc GROUP BY uc.courseId ORDER BY enrollmentCount DESC",
                    Object[].class);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get users with most course enrollments
     */
    public List<Object[]> getMostActiveUsers(int limit) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT uc.userId, COUNT(uc) as courseCount FROM UserCourses uc GROUP BY uc.userId ORDER BY courseCount DESC",
                    Object[].class);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Remove all enrollments for a specific user
     */
    public int removeAllEnrollmentsByUserId(Integer userId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            int count = em.createQuery("DELETE FROM UserCourses uc WHERE uc.userId = :userId")
                    .setParameter("userId", userId)
                    .executeUpdate();
            em.getTransaction().commit();
            return count;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error removing user enrollments", e);
        } finally {
            em.close();
        }
    }

    /**
     * Remove all enrollments for a specific course
     */
    public int removeAllEnrollmentsByCourseId(Integer courseId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            int count = em.createQuery("DELETE FROM UserCourses uc WHERE uc.courseId = :courseId")
                    .setParameter("courseId", courseId)
                    .executeUpdate();
            em.getTransaction().commit();
            return count;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error removing course enrollments", e);
        } finally {
            em.close();
        }
    }

    /**
     * Get total enrollment count
     */
    public long getTotalEnrollmentCount() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(uc) FROM UserCourses uc", Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Find a specific UserCourses record by user ID and course ID
     */
    public UserCourses findByUserAndCourse(int userId, int courseId) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT uc FROM UserCourses uc JOIN FETCH uc.course WHERE uc.userId = :userId AND uc.courseId = :courseId";
            TypedQuery<UserCourses> query = em.createQuery(jpql, UserCourses.class);
            query.setParameter("userId", userId);
            query.setParameter("courseId", courseId);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}
