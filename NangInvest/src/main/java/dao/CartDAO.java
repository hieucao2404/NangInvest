
package dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Cart;

/**
 * DAO implementation for Cart entity
 * 
 * @author Admin
 */
public class CartDAO extends GenericDAOImpl<Cart, Integer> {

  // Custom finder methods

  /**
   * Find all cart items for a specific user
   */
  public List<Cart> findByUserId(Integer userId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Cart> query = em.createQuery(
          "SELECT c FROM Cart c WHERE c.userId = :userId ORDER BY c.cartId", Cart.class);
      query.setParameter("userId", userId);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Remove a cart item by its cartId
   */
  public boolean removeCartItemById(Integer cartId) {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      int count = em.createQuery("DELETE FROM Cart c WHERE c.cartId = :cartId")
          .setParameter("cartId", cartId)
          .executeUpdate();
      em.getTransaction().commit();
      return count > 0;
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error removing cart item by cartId", e);
    } finally {
      em.close();
    }
  }

  /**
   * Find cart item by user and product
   */
  public Cart findByUserIdAndProductId(Integer userId, Integer productId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Cart> query = em.createQuery(
          "SELECT c FROM Cart c WHERE c.userId = :userId AND c.productId = :productId", Cart.class);
      query.setParameter("userId", userId);
      query.setParameter("productId", productId);
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    } finally {
      em.close();
    }
  }

  /**
   * Get total quantity in cart for user
   */
  public long getTotalQuantityByUserId(Integer userId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COALESCE(SUM(c.quantity), 0) FROM Cart c WHERE c.userId = :userId", Long.class);
      query.setParameter("userId", userId);
      return query.getSingleResult();
    } finally {
      em.close();
    }
  }

  /**
   * Get cart item count for user
   */
  public long getCartItemCountByUserId(Integer userId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(c) FROM Cart c WHERE c.userId = :userId", Long.class);
      query.setParameter("userId", userId);
      return query.getSingleResult();
    } finally {
      em.close();
    }
  }

  /**
   * Check if product is already in user's cart
   */
  public boolean existsByUserIdAndProductId(Integer userId, Integer productId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(c) FROM Cart c WHERE c.userId = :userId AND c.productId = :productId", Long.class);
      query.setParameter("userId", userId);
      query.setParameter("productId", productId);
      return query.getSingleResult() > 0;
    } finally {
      em.close();
    }
  }

  /**
   * Update quantity for cart item
   */
  public void updateQuantity(Integer cartId, Integer newQuantity) {
    if (newQuantity <= 0) {
      throw new IllegalArgumentException("Quantity must be positive");
    }
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      em.createQuery("UPDATE Cart c SET c.quantity = :quantity WHERE c.cartId = :cartId")
          .setParameter("quantity", newQuantity)
          .setParameter("cartId", cartId)
          .executeUpdate();
      em.getTransaction().commit();
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error updating cart quantity", e);
    } finally {
      em.close();
    }
  }

  /**
   * Remove all items from user's cart
   */
  public int clearCartByUserId(Integer userId) {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      int count = em.createQuery("DELETE FROM Cart c WHERE c.userId = :userId")
          .setParameter("userId", userId)
          .executeUpdate();
      em.getTransaction().commit();
      return count;
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error clearing cart", e);
    } finally {
      em.close();
    }
  }

  /**
   * Remove specific product from user's cart
   */
  public boolean removeProductFromCart(Integer userId, Integer productId) {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      int count = em.createQuery("DELETE FROM Cart c WHERE c.userId = :userId AND c.productId = :productId")
          .setParameter("userId", userId)
          .setParameter("productId", productId)
          .executeUpdate();
      em.getTransaction().commit();
      return count > 0;
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error removing product from cart", e);
    } finally {
      em.close();
    }
  }

  /**
   * Add or update cart item (if exists, update quantity; if not, create new)
   */
  public Cart addOrUpdateCartItem(Integer userId, Integer productId, Integer quantity) {
    Cart existingCart = findByUserIdAndProductId(userId, productId);

    if (existingCart != null) {
      // Update existing item
      existingCart.setQuantity(existingCart.getQuantity() + quantity);
      return update(existingCart);
    } else {
      // Create new cart item
      Cart newCart = new Cart(userId, productId, quantity);
      return save(newCart);
    }
  }
  
  /**
 * Remove all cart items associated with a specific product ID
 * @param productId The ID of the product (course) to remove from carts
 * @return true if at least one cart item was deleted, false otherwise
 */
public boolean removeCartItemsByProductId(Integer productId) {
    EntityManager em = getEntityManager();
    try {
        em.getTransaction().begin();
        int count = em.createQuery("DELETE FROM Cart c WHERE c.productId = :productId")
                .setParameter("productId", productId)
                .executeUpdate();
        em.getTransaction().commit();
        return count > 0;
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        throw new RuntimeException("Error removing cart items by productId: " + productId, e);
    } finally {
        em.close();
    }
}

  /**
   * Get most popular products in carts
   */
  public List<Object[]> getMostPopularCartProducts(int limit) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Object[]> query = em.createQuery(
          "SELECT c.productId, SUM(c.quantity) as totalQuantity FROM Cart c GROUP BY c.productId ORDER BY totalQuantity DESC",
          Object[].class);
      query.setMaxResults(limit);
      return query.getResultList();
    } finally {
      em.close();
    }
  }
}
