package dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Optional;
import model.Course;
import model.Order;

/**
 * DAO implementation for Order entity
 *
 * @author Admin
 */
public class OrderDAO extends GenericDAOImpl<Order, Integer> {

    public OrderDAO() {
        // Assuming GenericDAOImpl initializes EntityManagerFactory
    }

    public int create(Order order) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(order);
            em.getTransaction().commit();
            em.refresh(order); // Ensure orderId is updated
            return order.getOrderId(); // Return generated orderId
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
   public List<Course> findPurchasedCoursesNotEnrolled(int userId) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT c FROM Course c JOIN Order o ON c.courseId = o.productId " +
                          "WHERE o.userId = :userId AND o.paymentStatus = 'Completed' " +
                          "AND NOT EXISTS (SELECT uc FROM UserCourses uc WHERE uc.userId = :userId AND uc.courseId = c.courseId)";
            TypedQuery<Course> query = em.createQuery(jpql, Course.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Order> findById(int orderId) {
        EntityManager em = getEntityManager();
        try {
            Order order = em.find(Order.class, orderId);
            return Optional.ofNullable(order);
        } finally {
            em.close();
        }
    }

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

    public List<Order> findPendingOrders() {
        return findByPaymentStatus("Pending");
    }

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

    public List<Order> findCancelledOrders() {
        return findByPaymentStatus("Cancelled");
    }

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

    public long getTotalOrderCount() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(o) FROM Order o", Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

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

    public int deleteCancelledOrders(int daysOld) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
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
