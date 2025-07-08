package dao;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.Service;

/**
 * DAO implementation for Service entity
 * 
 * @author Admin
 */
public class ServiceDAO extends GenericDAOImpl<Service, Integer> {

  // Custom finder methods

  /**
   * Find service by name
   */
  public Optional<Service> findByServiceName(String serviceName) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Service> query = em.createQuery(
          "SELECT s FROM Service s WHERE s.serviceName = :serviceName", Service.class);
      query.setParameter("serviceName", serviceName);
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    } finally {
      em.close();
    }
  }

  /**
   * Find services by partial name match
   */
  public List<Service> findByServiceNameContaining(String partialName) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Service> query = em.createQuery(
          "SELECT s FROM Service s WHERE s.serviceName LIKE :partialName ORDER BY s.serviceName", Service.class);
      query.setParameter("partialName", "%" + partialName + "%");
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find all services with images
   */
  public List<Service> findServicesWithImages() {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Service> query = em.createQuery(
          "SELECT s FROM Service s WHERE s.imageUrl IS NOT NULL AND s.imageUrl != '' ORDER BY s.serviceName",
          Service.class);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find all services without images
   */
  public List<Service> findServicesWithoutImages() {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Service> query = em.createQuery(
          "SELECT s FROM Service s WHERE s.imageUrl IS NULL OR s.imageUrl = '' ORDER BY s.serviceName", Service.class);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Check if service name already exists
   */
  public boolean existsByServiceName(String serviceName) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(s) FROM Service s WHERE s.serviceName = :serviceName", Long.class);
      query.setParameter("serviceName", serviceName);
      return query.getSingleResult() > 0;
    } finally {
      em.close();
    }
  }

  /**
   * Get total count of services
   */
  public long getServiceCount() {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery("SELECT COUNT(s) FROM Service s", Long.class);
      return query.getSingleResult();
    } finally {
      em.close();
    }
  }

  /**
   * Get services with pagination
   */
  public List<Service> findServicesPaginated(int offset, int limit) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Service> query = em.createQuery(
          "SELECT s FROM Service s ORDER BY s.serviceName", Service.class);
      query.setFirstResult(offset);
      query.setMaxResults(limit);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Update service image URL
   */
  public void updateServiceImage(Integer serviceId, String newImageUrl) {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      em.createQuery("UPDATE Service s SET s.imageUrl = :imageUrl WHERE s.serviceId = :serviceId")
          .setParameter("imageUrl", newImageUrl)
          .setParameter("serviceId", serviceId)
          .executeUpdate();
      em.getTransaction().commit();
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error updating service image", e);
    } finally {
      em.close();
    }
  }

  /**
   * Delete services without images
   */
  public int deleteServicesWithoutImages() {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      int count = em.createQuery("DELETE FROM Service s WHERE s.imageUrl IS NULL OR s.imageUrl = ''")
          .executeUpdate();
      em.getTransaction().commit();
      return count;
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error deleting services without images", e);
    } finally {
      em.close();
    }
  }
}
