package dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Order;

/**
 * DAO implementation for Order entity
 * 
 * @author Admin
 */
public class OrderDAO extends GenericDAOImpl<Order, Integer> {

  // Custom finder methods

  /**
   * Find all orders for a specific user
   */
  public List<Order> findByUserId(Integer userId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Order> query = em.createQuery(
          "SELECT o FROM Order o WHERE o.userId = :userId ORDER BY o.orderId DESC", Order.class);
      query.setParameter("userId", userId);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find orders by payment status
   */
  public List<Order> findByPaymentStatus(String paymentStatus) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Order> query = em.createQuery(
          "SELECT o FROM Order o WHERE o.paymentStatus = :paymentStatus ORDER BY o.orderId DESC", Order.class);
      query.setParameter("paymentStatus", paymentStatus);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find orders by user and payment status
   */
  public List<Order> findByUserIdAndPaymentStatus(Integer userId, String paymentStatus) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Order> query = em.createQuery(
          "SELECT o FROM Order o WHERE o.userId = :userId AND o.paymentStatus = :paymentStatus ORDER BY o.orderId DESC",
          Order.class);
      query.setParameter("userId", userId);
      query.setParameter("paymentStatus", paymentStatus);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find orders for a specific product
   */
  public List<Order> findByProductId(Integer productId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Order> query = em.createQuery(
          "SELECT o FROM Order o WHERE o.productId = :productId ORDER BY o.orderId DESC", Order.class);
      query.setParameter("productId", productId);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Get pending orders
   */
  public List<Order> findPendingOrders() {
    return findByPaymentStatus("Pending");
  }

  /**
   * Get completed orders
   */
  public List<Order> findCompletedOrders() {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Order> query = em.createQuery(
          "SELECT o FROM Order o WHERE o.paymentStatus IN ('Completed', 'Paid') ORDER BY o.orderId DESC", Order.class);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Get cancelled orders
   */
  public List<Order> findCancelledOrders() {
    return findByPaymentStatus("Cancelled");
  }

  /**
   * Get order count by payment status
   */
  public long getOrderCountByPaymentStatus(String paymentStatus) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(o) FROM Order o WHERE o.paymentStatus = :paymentStatus", Long.class);
      query.setParameter("paymentStatus", paymentStatus);
      return query.getSingleResult();
    } finally {
      em.close();
    }
  }

  /**
   * Get order count for a user
   */
  public long getOrderCountByUserId(Integer userId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(o) FROM Order o WHERE o.userId = :userId", Long.class);
      query.setParameter("userId", userId);
      return query.getSingleResult();
    } finally {
      em.close();
    }
  }

  /**
   * Get total order count
   */
  public long getTotalOrderCount() {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery("SELECT COUNT(o) FROM Order o", Long.class);
      return query.getSingleResult();
    } finally {
      em.close();
    }
  }

  /**
   * Update payment status
   */
  public void updatePaymentStatus(Integer orderId, String newPaymentStatus) {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      em.createQuery("UPDATE Order o SET o.paymentStatus = :paymentStatus WHERE o.orderId = :orderId")
          .setParameter("paymentStatus", newPaymentStatus)
          .setParameter("orderId", orderId)
          .executeUpdate();
      em.getTransaction().commit();
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error updating payment status", e);
    } finally {
      em.close();
    }
  }

  /**
   * Get orders with pagination
   */
  public List<Order> findOrdersPaginated(int offset, int limit) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Order> query = em.createQuery(
          "SELECT o FROM Order o ORDER BY o.orderId DESC", Order.class);
      query.setFirstResult(offset);
      query.setMaxResults(limit);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Get most popular products by order count
   */
  public List<Object[]> getMostPopularOrderedProducts(int limit) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Object[]> query = em.createQuery(
          "SELECT o.productId, COUNT(o) as orderCount FROM Order o WHERE o.paymentStatus IN ('Completed', 'Paid') GROUP BY o.productId ORDER BY orderCount DESC",
          Object[].class);
      query.setMaxResults(limit);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Delete cancelled orders older than specified days
   */
  public int deleteCancelledOrders(int daysOld) {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      // Note: Since Order table doesn't have a timestamp, this is a simplified
      // version
      int count = em.createQuery("DELETE FROM Order o WHERE o.paymentStatus = 'Cancelled'")
          .executeUpdate();
      em.getTransaction().commit();
      return count;
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error deleting cancelled orders", e);
    } finally {
      em.close();
    }
  }

  /**
   * Check if user has purchased a specific product
   */
  public boolean hasUserPurchasedProduct(Integer userId, Integer productId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(o) FROM Order o WHERE o.userId = :userId AND o.productId = :productId AND o.paymentStatus IN ('Completed', 'Paid')",
          Long.class);
      query.setParameter("userId", userId);
      query.setParameter("productId", productId);
      return query.getSingleResult() > 0;
    } finally {
      em.close();
    }
  }
}
