/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Lesson;
import java.util.List;

public class LessonDAO extends GenericDAOImpl<Lesson, Integer> {
    public List<Lesson> findByCourseId(int courseId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Lesson> query = em.createQuery(
                "SELECT l FROM Lesson l WHERE l.courseId = :courseId ORDER BY l.lessonId", Lesson.class);
            query.setParameter("courseId", courseId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long getLessonCountByCourseId(int courseId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(l) FROM Lesson l WHERE l.courseId = :courseId", Long.class);
            query.setParameter("courseId", courseId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
