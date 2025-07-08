package dao;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.Blog;

/**
 * DAO implementation for Blog entity
 * 
 * @author Admin
 */
public class BlogDAO extends GenericDAOImpl<Blog, Integer> {

  // Custom finder methods

  /**
   * Find blog by name
   */
  public Optional<Blog> findByBlogName(String blogName) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Blog> query = em.createQuery(
          "SELECT b FROM Blog b WHERE b.blogName = :blogName", Blog.class);
      query.setParameter("blogName", blogName);
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    } finally {
      em.close();
    }
  }

  /**
   * Find blogs by topic
   */
  public List<Blog> findByTopic(String topic) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Blog> query = em.createQuery(
          "SELECT b FROM Blog b WHERE b.topic = :topic ORDER BY b.blogName", Blog.class);
      query.setParameter("topic", topic);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find blogs by partial name match
   */
  public List<Blog> findByBlogNameContaining(String partialName) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Blog> query = em.createQuery(
          "SELECT b FROM Blog b WHERE b.blogName LIKE :partialName ORDER BY b.blogName", Blog.class);
      query.setParameter("partialName", "%" + partialName + "%");
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find blogs with content containing keyword
   */
  public List<Blog> findByContentContaining(String keyword) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Blog> query = em.createQuery(
          "SELECT b FROM Blog b WHERE b.detailedContent LIKE :keyword ORDER BY b.blogName", Blog.class);
      query.setParameter("keyword", "%" + keyword + "%");
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find all blogs with images
   */
  public List<Blog> findBlogsWithImages() {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Blog> query = em.createQuery(
          "SELECT b FROM Blog b WHERE b.imageUrl IS NOT NULL AND b.imageUrl != '' ORDER BY b.blogName", Blog.class);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Find all blogs without images
   */
  public List<Blog> findBlogsWithoutImages() {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Blog> query = em.createQuery(
          "SELECT b FROM Blog b WHERE b.imageUrl IS NULL OR b.imageUrl = '' ORDER BY b.blogName", Blog.class);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Get all unique topics
   */
  public List<String> findAllTopics() {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<String> query = em.createQuery(
          "SELECT DISTINCT b.topic FROM Blog b WHERE b.topic IS NOT NULL ORDER BY b.topic", String.class);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Get blogs with pagination
   */
  public List<Blog> findBlogsPaginated(int offset, int limit) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Blog> query = em.createQuery(
          "SELECT b FROM Blog b ORDER BY b.blogName", Blog.class);
      query.setFirstResult(offset);
      query.setMaxResults(limit);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Get blogs by topic with pagination
   */
  public List<Blog> findBlogsByTopicPaginated(String topic, int offset, int limit) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Blog> query = em.createQuery(
          "SELECT b FROM Blog b WHERE b.topic = :topic ORDER BY b.blogName", Blog.class);
      query.setParameter("topic", topic);
      query.setFirstResult(offset);
      query.setMaxResults(limit);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  /**
   * Check if blog name already exists
   */
  public boolean existsByBlogName(String blogName) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(b) FROM Blog b WHERE b.blogName = :blogName", Long.class);
      query.setParameter("blogName", blogName);
      return query.getSingleResult() > 0;
    } finally {
      em.close();
    }
  }

  /**
   * Get total count of blogs
   */
  public long getBlogCount() {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery("SELECT COUNT(b) FROM Blog b", Long.class);
      return query.getSingleResult();
    } finally {
      em.close();
    }
  }

  /**
   * Get count of blogs by topic
   */
  public long getBlogCountByTopic(String topic) {
    EntityManager em = getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(b) FROM Blog b WHERE b.topic = :topic", Long.class);
      query.setParameter("topic", topic);
      return query.getSingleResult();
    } finally {
      em.close();
    }
  }

  /**
   * Update blog image URL
   */
  public void updateBlogImage(Integer blogId, String newImageUrl) {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      em.createQuery("UPDATE Blog b SET b.imageUrl = :imageUrl WHERE b.blogId = :blogId")
          .setParameter("imageUrl", newImageUrl)
          .setParameter("blogId", blogId)
          .executeUpdate();
      em.getTransaction().commit();
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error updating blog image", e);
    } finally {
      em.close();
    }
  }

  /**
   * Update blog content
   */
  public void updateBlogContent(Integer blogId, String newContent) {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      em.createQuery("UPDATE Blog b SET b.detailedContent = :content WHERE b.blogId = :blogId")
          .setParameter("content", newContent)
          .setParameter("blogId", blogId)
          .executeUpdate();
      em.getTransaction().commit();
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException("Error updating blog content", e);
    } finally {
      em.close();
    }
  }
}
