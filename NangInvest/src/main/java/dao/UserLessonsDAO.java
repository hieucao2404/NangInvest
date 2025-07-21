/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.UserLessons;
import java.sql.Timestamp;
import java.util.List;

public class UserLessonsDAO extends GenericDAOImpl<UserLessons, UserLessons.UserLessonsId> {
    public void markLessonCompleted(int userId, int lessonId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            UserLessons userLesson = em.find(UserLessons.class, new UserLessons.UserLessonsId());
            if (userLesson == null) {
                userLesson = new UserLessons(userId, lessonId);
                userLesson.setCompleted(true);
                userLesson.setCompletionDate(new Timestamp(System.currentTimeMillis()));
                em.persist(userLesson);
            } else {
                userLesson.setCompleted(true);
                userLesson.setCompletionDate(new Timestamp(System.currentTimeMillis()));
                em.merge(userLesson);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to mark lesson completed: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public long getCompletedLessonCount(int userId, int courseId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(ul) FROM UserLessons ul JOIN Lesson l ON ul.lessonId = l.lessonId " +
                "WHERE ul.userId = :userId AND l.courseId = :courseId AND ul.completed = true", Long.class);
            query.setParameter("userId", userId);
            query.setParameter("courseId", courseId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
