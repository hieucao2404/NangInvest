package dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.UserToken;

/**
 * DAO implementation for UserToken entity
 * 
 * @author Admin
 */
public class UserTokenDAO extends GenericDAOImpl<UserToken, Integer> {

  // Custom finder methods

  /**
   * Find token by token string
   */
  public Optional<UserToken> findByToken(String token) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<UserToken> query = em.createQuery(
          "SELECT ut FROM UserToken ut WHERE ut.token = :token", UserToken.class);
      query.setParameter("token", token);
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    } finally {
      em.close();
    }
  }

  /**
   * Find all tokens for a specific user
   */
  public List<UserToken> findByUserId(Integer userId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<UserToken> query = em.createQuery(
          "SELECT ut FROM UserToken ut WHERE ut.userId = :userId ORDER BY ut.expiryDate DESC", UserToken.class);
      query.setParameter("userId", userId);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find valid (non-expired) tokens for a user
   */
  public List<UserToken> findValidTokensByUserId(Integer userId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<UserToken> query = em.createQuery(
          "SELECT ut FROM UserToken ut WHERE ut.userId = :userId AND ut.expiryDate > :currentTime ORDER BY ut.expiryDate DESC",
          UserToken.class);
      query.setParameter("userId", userId);
      query.setParameter("currentTime", LocalDateTime.now());
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find expired tokens
   */
  public List<UserToken> findExpiredTokens() {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<UserToken> query = em.createQuery(
          "SELECT ut FROM UserToken ut WHERE ut.expiryDate <= :currentTime ORDER BY ut.expiryDate",
          UserToken.class);
      query.setParameter("currentTime", LocalDateTime.now());
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Check if token exists and is valid
   */
  public boolean isTokenValid(String token) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(ut) FROM UserToken ut WHERE ut.token = :token AND ut.expiryDate > :currentTime",
          Long.class);
      query.setParameter("token", token);
      query.setParameter("currentTime", LocalDateTime.now());
      return query.getSingleResult() > 0;
    } finally {
      em.close();
    }
  }

  /**
   * Get token count for a user
   */
  public long getTokenCountByUserId(Integer userId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(ut) FROM UserToken ut WHERE ut.userId = :userId", Long.class);
      query.setParameter("userId", userId);
      return query.getSingleResult();
    } finally {
      em.close();
    }
  }

  /**
   * Get valid token count for a user
   */
  public long getValidTokenCountByUserId(Integer userId) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(ut) FROM UserToken ut WHERE ut.userId = :userId AND ut.expiryDate > :currentTime",
          Long.class);
      query.setParameter("userId", userId);
      query.setParameter("currentTime", LocalDateTime.now());
      return query.getSingleResult();
    } finally {
      em.close();
    }
  }

  /**
   * Delete expired tokens
   */
  public int deleteExpiredTokens() {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      int count = em.createQuery("DELETE FROM UserToken ut WHERE ut.expiryDate <= :currentTime")
          .setParameter("currentTime", LocalDateTime.now())
          .executeUpdate();
      em.getTransaction().commit();
      return count;
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error deleting expired tokens", e);
    } finally {
      em.close();
    }
  }

  /**
   * Delete all tokens for a specific user
   */
  public int deleteTokensByUserId(Integer userId) {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      int count = em.createQuery("DELETE FROM UserToken ut WHERE ut.userId = :userId")
          .setParameter("userId", userId)
          .executeUpdate();
      em.getTransaction().commit();
      return count;
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error deleting user tokens", e);
    } finally {
      em.close();
    }
  }

  /**
   * Delete specific token by token string
   */
  public boolean deleteByToken(String token) {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      int count = em.createQuery("DELETE FROM UserToken ut WHERE ut.token = :token")
          .setParameter("token", token)
          .executeUpdate();
      em.getTransaction().commit();
      return count > 0;
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error deleting token", e);
    } finally {
      em.close();
    }
  }

  /**
   * Update token expiry date
   */
  public void updateTokenExpiry(String token, LocalDateTime newExpiryDate) {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      em.createQuery("UPDATE UserToken ut SET ut.expiryDate = :expiryDate WHERE ut.token = :token")
          .setParameter("expiryDate", newExpiryDate)
          .setParameter("token", token)
          .executeUpdate();
      em.getTransaction().commit();
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error updating token expiry", e);
    } finally {
      em.close();
    }
  }

  /**
   * Get tokens expiring soon (within specified hours)
   */
  public List<UserToken> findTokensExpiringSoon(int hours) {
    EntityManager em = getEntityManager();
    try {
      LocalDateTime thresholdTime = LocalDateTime.now().plusHours(hours);
      TypedQuery<UserToken> query = em.createQuery(
          "SELECT ut FROM UserToken ut WHERE ut.expiryDate BETWEEN :currentTime AND :thresholdTime ORDER BY ut.expiryDate",
          UserToken.class);
      query.setParameter("currentTime", LocalDateTime.now());
      query.setParameter("thresholdTime", thresholdTime);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Create or update token for user
   */
  public UserToken createOrUpdateToken(Integer userId, String token, LocalDateTime expiryDate) {
    // First try to find existing token for this user
    List<UserToken> existingTokens = findByUserId(userId);

    if (!existingTokens.isEmpty()) {
      // Update the first valid token or create new if all expired
      UserToken existingToken = existingTokens.get(0);
      existingToken.setToken(token);
      existingToken.setExpiryDate(expiryDate);
      return update(existingToken);
    } else {
      // Create new token
      UserToken newToken = new UserToken(userId, token, expiryDate);
      return save(newToken);
    }
  }
}
