package dao;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.AffiliateClick;

/**
 * DAO implementation for AffiliateClick entity
 * 
 * @author Admin
 */
public class AffiliateClickDAO extends GenericDAOImpl<AffiliateClick, Integer> {

  // Custom finder methods

  /**
   * Find all clicks for a specific book
   */
  public List<AffiliateClick> findByBookId(Integer bookId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<AffiliateClick> query = em.createQuery(
          "SELECT ac FROM AffiliateClick ac WHERE ac.bookId = :bookId ORDER BY ac.clickTime DESC",
          AffiliateClick.class);
      query.setParameter("bookId", bookId);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find all clicks for a specific user
   */
  public List<AffiliateClick> findByUserId(Integer userId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<AffiliateClick> query = em.createQuery(
          "SELECT ac FROM AffiliateClick ac WHERE ac.userId = :userId ORDER BY ac.clickTime DESC",
          AffiliateClick.class);
      query.setParameter("userId", userId);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find anonymous clicks (clicks without user ID)
   */
  public List<AffiliateClick> findAnonymousClicks() {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<AffiliateClick> query = em.createQuery(
          "SELECT ac FROM AffiliateClick ac WHERE ac.userId IS NULL ORDER BY ac.clickTime DESC",
          AffiliateClick.class);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find clicks within a date range
   */
  public List<AffiliateClick> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<AffiliateClick> query = em.createQuery(
          "SELECT ac FROM AffiliateClick ac WHERE ac.clickTime BETWEEN :startDate AND :endDate ORDER BY ac.clickTime DESC",
          AffiliateClick.class);
      query.setParameter("startDate", startDate);
      query.setParameter("endDate", endDate);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find clicks for a book within a date range
   */
  public List<AffiliateClick> findByBookIdAndDateRange(Integer bookId, LocalDateTime startDate, LocalDateTime endDate) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<AffiliateClick> query = em.createQuery(
          "SELECT ac FROM AffiliateClick ac WHERE ac.bookId = :bookId AND ac.clickTime BETWEEN :startDate AND :endDate ORDER BY ac.clickTime DESC",
          AffiliateClick.class);
      query.setParameter("bookId", bookId);
      query.setParameter("startDate", startDate);
      query.setParameter("endDate", endDate);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Get click count for a specific book
   */
  public long getClickCountByBookId(Integer bookId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(ac) FROM AffiliateClick ac WHERE ac.bookId = :bookId", Long.class);
      query.setParameter("bookId", bookId);
      return query.getSingleResult();
    } finally {
      em.close();
    }
  }

  /**
   * Get click count for a specific user
   */
  public long getClickCountByUserId(Integer userId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(ac) FROM AffiliateClick ac WHERE ac.userId = :userId", Long.class);
      query.setParameter("userId", userId);
      return query.getSingleResult();
    } finally {
      em.close();
    }
  }

  /**
   * Get total click count
   */
  public long getTotalClickCount() {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery("SELECT COUNT(ac) FROM AffiliateClick ac", Long.class);
      return query.getSingleResult();
    } finally {
      em.close();
    }
  }

  /**
   * Get click count within a date range
   */
  public long getClickCountByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(ac) FROM AffiliateClick ac WHERE ac.clickTime BETWEEN :startDate AND :endDate", Long.class);
      query.setParameter("startDate", startDate);
      query.setParameter("endDate", endDate);
      return query.getSingleResult();
    } finally {
      em.close();
    }
  }

  /**
   * Get most clicked books (with click counts)
   */
  public List<Object[]> getMostClickedBooks(int limit) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Object[]> query = em.createQuery(
          "SELECT ac.bookId, COUNT(ac) as clickCount FROM AffiliateClick ac GROUP BY ac.bookId ORDER BY clickCount DESC",
          Object[].class);
      query.setMaxResults(limit);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Get clicks with pagination
   */
  public List<AffiliateClick> findClicksPaginated(int offset, int limit) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<AffiliateClick> query = em.createQuery(
          "SELECT ac FROM AffiliateClick ac ORDER BY ac.clickTime DESC", AffiliateClick.class);
      query.setFirstResult(offset);
      query.setMaxResults(limit);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Delete old clicks (older than specified days)
   */
  public int deleteOldClicks(int daysOld) {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
      int count = em.createQuery("DELETE FROM AffiliateClick ac WHERE ac.clickTime < :cutoffDate")
          .setParameter("cutoffDate", cutoffDate)
          .executeUpdate();
      em.getTransaction().commit();
      return count;
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error deleting old clicks", e);
    } finally {
      em.close();
    }
  }
}
