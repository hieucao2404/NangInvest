package test;

import java.math.BigDecimal;
import java.util.List;

import dao.BooksDAO;
import model.Book;
import util.JPAUtil;

/**
 * Test class for BooksDAO
 * Tests CRUD operations and custom queries for Books
 */
public class BooksDAOTest {

  private static BooksDAO booksDAO;

  public static void main(String[] args) {
    System.out.println("=== Starting BooksDAO Test ===");

    try {
      // Initialize DAO
      booksDAO = new BooksDAO();

      // Run all tests
      testCRUDOperations();
      testCustomQueries();
      testUtilityMethods();
      testEdgeCases();

      System.out.println("=== All BooksDAO tests completed successfully! ===");

    } catch (Exception e) {
      System.err.println("Test failed with exception: " + e.getMessage());
      e.printStackTrace();
    } finally {
      JPAUtil.closeEntityManagerFactory();
    }
  }

  private static void testCRUDOperations() {
    System.out.println("\n--- Testing CRUD Operations ---");

    // Test Create
    Book book1 = new Book();
    book1.setBookName("Investing for Beginners");
    book1.setTopic("Investment");
    book1.setAffiliateLink("https://example.com/book1");
    book1.setIsPreviewAvailable(true);
    book1.setCoverImage("book1.jpg");
    book1.setRating(new BigDecimal("4.5"));
    book1.setPreviewContent("This is a preview of investing basics...");

    Book savedBook1 = booksDAO.save(book1);
    System.out.println("Created book: " + savedBook1.getBookName() + " (ID: " + savedBook1.getBookId() + ")");

    Book book2 = new Book();
    book2.setBookName("Advanced Trading Strategies");
    book2.setTopic("Trading");
    book2.setAffiliateLink("https://example.com/book2");
    book2.setIsPreviewAvailable(false);
    book2.setCoverImage("book2.jpg");
    book2.setRating(new BigDecimal("4.8"));
    book2.setPreviewContent("Advanced strategies for experienced traders...");

    Book savedBook2 = booksDAO.save(book2);
    System.out.println("Created book: " + savedBook2.getBookName() + " (ID: " + savedBook2.getBookId() + ")");

    // Test Read
    Book foundBook = booksDAO.findById(savedBook1.getBookId()).orElse(null);
    System.out.println("Found book by ID: " + (foundBook != null ? foundBook.getBookName() : "Not found"));

    // Test Update
    savedBook1.setRating(new BigDecimal("4.7"));
    savedBook1.setPreviewContent("Updated preview content...");
    Book updatedBook = booksDAO.update(savedBook1);
    System.out.println("Updated book rating to: " + updatedBook.getRating());

    // Test findAll
    List<Book> allBooks = booksDAO.findAll();
    System.out.println("Total books in database: " + allBooks.size());
  }

  private static void testCustomQueries() {
    System.out.println("\n--- Testing Custom Queries ---");

    // Test findByTopic
    List<Book> investmentBooks = booksDAO.findByTopic("Investment");
    System.out.println("Books in 'Investment' topic: " + investmentBooks.size());

    List<Book> tradingBooks = booksDAO.findByTopic("Trading");
    System.out.println("Books in 'Trading' topic: " + tradingBooks.size());

    // Test findBooksWithPreview
    List<Book> booksWithPreview = booksDAO.findBooksWithPreview();
    System.out.println("Books with preview available: " + booksWithPreview.size());

    // Test findByRatingRange
    List<Book> highRatedBooks = booksDAO.findByRatingRange(new BigDecimal("4.0"), new BigDecimal("5.0"));
    System.out.println("Books with rating 4.0-5.0: " + highRatedBooks.size());

    // Test findByNamePattern
    List<Book> investingBooks = booksDAO.findByNamePattern("invest");
    System.out.println("Books with 'invest' in name: " + investingBooks.size());

    // Test findBooksWithAffiliateLinks
    List<Book> booksWithLinks = booksDAO.findBooksWithAffiliateLinks();
    System.out.println("Books with affiliate links: " + booksWithLinks.size());
  }

  private static void testUtilityMethods() {
    System.out.println("\n--- Testing Utility Methods ---");

    // Test getAverageRating
    BigDecimal avgRating = booksDAO.getAverageRating();
    System.out.println("Average rating of all books: " + avgRating);

    // Test countByTopic
    Long investmentCount = booksDAO.countByTopic("Investment");
    System.out.println("Count of Investment books: " + investmentCount);

    Long tradingCount = booksDAO.countByTopic("Trading");
    System.out.println("Count of Trading books: " + tradingCount);

    // Test getTopRatedBooks
    List<Book> topBooks = booksDAO.getTopRatedBooks(3);
    System.out.println("Top 3 rated books:");
    for (Book book : topBooks) {
      System.out.println("  - " + book.getBookName() + " (Rating: " + book.getRating() + ")");
    }

    // Test existsByName
    boolean existsInvesting = booksDAO.existsByName("Investing for Beginners");
    System.out.println("Book 'Investing for Beginners' exists: " + existsInvesting);

    boolean existsNonExistent = booksDAO.existsByName("Non-existent Book");
    System.out.println("Book 'Non-existent Book' exists: " + existsNonExistent);

    // Test getAllTopics
    List<String> topics = booksDAO.getAllTopics();
    System.out.println("All unique topics: " + topics);
  }

  private static void testEdgeCases() {
    System.out.println("\n--- Testing Edge Cases ---");

    // Test with book having null values
    Book bookWithNulls = new Book();
    bookWithNulls.setBookName("Book with Nulls");
    // Leave other fields null

    Book savedNullBook = booksDAO.save(bookWithNulls);
    System.out.println("Created book with null fields: " + savedNullBook.getBookName());

    // Test queries with null/empty parameters
    List<Book> nullTopicBooks = booksDAO.findByTopic("NonExistentTopic");
    System.out.println("Books with non-existent topic: " + nullTopicBooks.size());

    // Test rating range with extreme values
    List<Book> extremeRatingBooks = booksDAO.findByRatingRange(new BigDecimal("0"), new BigDecimal("1"));
    System.out.println("Books with rating 0-1: " + extremeRatingBooks.size());

    // Test empty pattern search
    List<Book> emptyPatternBooks = booksDAO.findByNamePattern("");
    System.out.println("Books with empty pattern (should return all): " + emptyPatternBooks.size());

    // Test pagination-like behavior
    List<Book> limitedBooks = booksDAO.getTopRatedBooks(1);
    System.out.println("Top 1 rated book: " +
        (limitedBooks.isEmpty() ? "None" : limitedBooks.get(0).getBookName()));

    // Test count and exists methods
    Long totalCount = booksDAO.count();
    System.out.println("Total book count: " + totalCount);

    if (totalCount > 0) {
      List<Book> allBooks = booksDAO.findAll();
      Book firstBook = allBooks.get(0);
      boolean exists = booksDAO.existsById(firstBook.getBookId());
      System.out.println("First book exists by ID: " + exists);
    }
  }
}
