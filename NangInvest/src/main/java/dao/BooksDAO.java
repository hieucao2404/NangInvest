package dao;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.TypedQuery;
import model.Book;

/**
 * Data Access Object for Book entities
 * Provides CRUD operations and custom queries for Books
 */
public class BooksDAO extends GenericDAOImpl<Book, Integer> {

  public BooksDAO() {
    super();
  }

  /**
   * Find books by topic
   */
  public List<Book> findByTopic(String topic) {
    TypedQuery<Book> query = getEntityManager()
        .createQuery("SELECT b FROM Book b WHERE b.topic = :topic", Book.class);
    query.setParameter("topic", topic);
    return query.getResultList();
  }

  /**
   * Find books with preview available
   */
  public List<Book> findBooksWithPreview() {
    TypedQuery<Book> query = getEntityManager()
        .createQuery("SELECT b FROM Book b WHERE b.isPreviewAvailable = true", Book.class);
    return query.getResultList();
  }

  /**
   * Find books by rating range
   */
  public List<Book> findByRatingRange(BigDecimal minRating, BigDecimal maxRating) {
    TypedQuery<Book> query = getEntityManager()
        .createQuery("SELECT b FROM Book b WHERE b.rating BETWEEN :minRating AND :maxRating ORDER BY b.rating DESC",
            Book.class);
    query.setParameter("minRating", minRating);
    query.setParameter("maxRating", maxRating);
    return query.getResultList();
  }

  /**
   * Find books by name pattern (case-insensitive)
   */
  public List<Book> findByNamePattern(String namePattern) {
    TypedQuery<Book> query = getEntityManager()
        .createQuery("SELECT b FROM Book b WHERE LOWER(b.bookName) LIKE LOWER(:pattern)", Book.class);
    query.setParameter("pattern", "%" + namePattern + "%");
    return query.getResultList();
  }

  /**
   * Get books with affiliate links
   */
  public List<Book> findBooksWithAffiliateLinks() {
    TypedQuery<Book> query = getEntityManager()
        .createQuery("SELECT b FROM Book b WHERE b.affiliateLink IS NOT NULL AND b.affiliateLink != ''", Book.class);
    return query.getResultList();
  }

  /**
   * Get average rating of all books
   */
  public BigDecimal getAverageRating() {
    TypedQuery<BigDecimal> query = getEntityManager()
        .createQuery("SELECT AVG(b.rating) FROM Book b WHERE b.rating IS NOT NULL", BigDecimal.class);
    BigDecimal result = query.getSingleResult();
    return result != null ? result : BigDecimal.ZERO;
  }

  /**
   * Count books by topic
   */
  public Long countByTopic(String topic) {
    TypedQuery<Long> query = getEntityManager()
        .createQuery("SELECT COUNT(b) FROM Book b WHERE b.topic = :topic", Long.class);
    query.setParameter("topic", topic);
    return query.getSingleResult();
  }

  /**
   * Get top rated books (limit)
   */
  public List<Book> getTopRatedBooks(int limit) {
    TypedQuery<Book> query = getEntityManager()
        .createQuery("SELECT b FROM Book b WHERE b.rating IS NOT NULL ORDER BY b.rating DESC", Book.class);
    query.setMaxResults(limit);
    return query.getResultList();
  }

  /**
   * Check if a book exists by name
   */
  public boolean existsByName(String bookName) {
    TypedQuery<Long> query = getEntityManager()
        .createQuery("SELECT COUNT(b) FROM Book b WHERE b.bookName = :bookName", Long.class);
    query.setParameter("bookName", bookName);
    return query.getSingleResult() > 0;
  }

  /**
   * Get all unique topics
   */
  public List<String> getAllTopics() {
    TypedQuery<String> query = getEntityManager()
        .createQuery("SELECT DISTINCT b.topic FROM Book b WHERE b.topic IS NOT NULL ORDER BY b.topic", String.class);
    return query.getResultList();
  }
}
