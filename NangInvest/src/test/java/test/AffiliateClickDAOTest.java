package test;

import java.time.LocalDateTime;
import java.util.List;

import dao.AffiliateClickDAO;
import model.AffiliateClick;
import util.JPAUtil;

/**
 * Test class for AffiliateClickDAO
 * Tests CRUD operations and custom queries for AffiliateClick
 */
public class AffiliateClickDAOTest {

  private static AffiliateClickDAO affiliateClickDAO;

  public static void main(String[] args) {
    System.out.println("=== Starting AffiliateClickDAO Test ===");

    try {
      // Initialize DAO
      affiliateClickDAO = new AffiliateClickDAO();

      // Run all tests
      testCRUDOperations();
      testCustomQueries();
      testUtilityMethods();
      testEdgeCases();

      System.out.println("=== All AffiliateClickDAO tests completed successfully! ===");

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
    AffiliateClick click1 = new AffiliateClick();
    click1.setBookId(4); // Use existing book ID
    click1.setUserId(1); // Use existing user ID
    click1.setClickTime(LocalDateTime.now());

    AffiliateClick savedClick1 = affiliateClickDAO.save(click1);
    System.out.println(
        "Created affiliate click for book: " + savedClick1.getBookId() + " (ID: " + savedClick1.getClickId() + ")");

    AffiliateClick click2 = new AffiliateClick();
    click2.setBookId(5); // Use existing book ID
    click2.setUserId(2); // Use existing user ID
    click2.setClickTime(LocalDateTime.now().minusDays(1));

    AffiliateClick savedClick2 = affiliateClickDAO.save(click2);
    System.out.println(
        "Created affiliate click for book: " + savedClick2.getBookId() + " (ID: " + savedClick2.getClickId() + ")");

    // Test Read
    AffiliateClick foundClick = affiliateClickDAO.findById(savedClick1.getClickId()).orElse(null);
    System.out.println("Found click by ID: " + (foundClick != null ? foundClick.getBookId() : "Not found"));

    // Test Update
    savedClick1.setUserId(3);
    AffiliateClick updatedClick = affiliateClickDAO.update(savedClick1);
    System.out.println("Updated click user ID to: " + updatedClick.getUserId());

    // Test findAll
    List<AffiliateClick> allClicks = affiliateClickDAO.findAll();
    System.out.println("Total affiliate clicks in database: " + allClicks.size());
  }

  private static void testCustomQueries() {
    System.out.println("\n--- Testing Custom Queries ---");

    // Test findByUserId
    List<AffiliateClick> userClicks = affiliateClickDAO.findByUserId(1);
    System.out.println("Clicks for user 1: " + userClicks.size());

    // Test findByBookId
    List<AffiliateClick> bookClicks = affiliateClickDAO.findByBookId(4); // Use existing book ID
    System.out.println("Clicks for book 4: " + bookClicks.size());

    // Test findAnonymousClicks
    AffiliateClick anonymousClick = new AffiliateClick();
    anonymousClick.setBookId(6); // Use existing book ID
    // Don't set userId (leave as null for anonymous)
    affiliateClickDAO.save(anonymousClick);

    List<AffiliateClick> anonymousClicks = affiliateClickDAO.findAnonymousClicks();
    System.out.println("Anonymous clicks: " + anonymousClicks.size());

    // Test findByDateRange
    LocalDateTime startDate = LocalDateTime.now().minusDays(7);
    LocalDateTime endDate = LocalDateTime.now().plusDays(1);
    List<AffiliateClick> dateRangeClicks = affiliateClickDAO.findByDateRange(startDate, endDate);
    System.out.println("Clicks in last 7 days: " + dateRangeClicks.size());

    // Test findByBookIdAndDateRange
    List<AffiliateClick> bookDateClicks = affiliateClickDAO.findByBookIdAndDateRange(4, startDate, endDate); // Use existing book ID
    System.out.println("Clicks for book 4 in date range: " + bookDateClicks.size());
  }

  private static void testUtilityMethods() {
    System.out.println("\n--- Testing Utility Methods ---");

    // Test getClickCountByBookId
    long bookClickCount = affiliateClickDAO.getClickCountByBookId(4); // Use existing book ID
    System.out.println("Click count for book 4: " + bookClickCount);

    // Test getClickCountByUserId
    long userClickCount = affiliateClickDAO.getClickCountByUserId(1);
    System.out.println("Click count for user 1: " + userClickCount);

    // Test getTotalClickCount
    long totalClickCount = affiliateClickDAO.getTotalClickCount();
    System.out.println("Total click count: " + totalClickCount);

    // Test getClickCountByDateRange
    LocalDateTime startDate = LocalDateTime.now().minusDays(7);
    LocalDateTime endDate = LocalDateTime.now().plusDays(1);
    long dateRangeCount = affiliateClickDAO.getClickCountByDateRange(startDate, endDate);
    System.out.println("Click count in date range: " + dateRangeCount);

    // Test getMostClickedBooks
    List<Object[]> topBooks = affiliateClickDAO.getMostClickedBooks(5);
    System.out.println("Top 5 clicked books:");
    for (Object[] row : topBooks) {
      Integer bookId = (Integer) row[0];
      Long clickCount = (Long) row[1];
      System.out.println("  - Book ID: " + bookId + ", Clicks: " + clickCount);
    }

    // Test findClicksPaginated
    List<AffiliateClick> paginatedClicks = affiliateClickDAO.findClicksPaginated(0, 5);
    System.out.println("First 5 clicks (paginated): " + paginatedClicks.size());
  }

  private static void testEdgeCases() {
    System.out.println("\n--- Testing Edge Cases ---");

    // Test with click having minimal data
    AffiliateClick minimalClick = new AffiliateClick();
    minimalClick.setBookId(5); // Use existing book ID
    // Leave userId as null and use default clickTime

    AffiliateClick savedMinimalClick = affiliateClickDAO.save(minimalClick);
    System.out.println("Created minimal click for book: " + savedMinimalClick.getBookId());

    // Test queries with non-existent data
    List<AffiliateClick> nonExistentUserClicks = affiliateClickDAO.findByUserId(99999);
    System.out.println("Clicks for non-existent user: " + nonExistentUserClicks.size());

    List<AffiliateClick> nonExistentBookClicks = affiliateClickDAO.findByBookId(99999);
    System.out.println("Clicks for non-existent book: " + nonExistentBookClicks.size());

    // Test extreme date range
    LocalDateTime veryOldDate = LocalDateTime.now().minusYears(1);
    LocalDateTime futureDate = LocalDateTime.now().plusYears(1);
    List<AffiliateClick> extremeDateClicks = affiliateClickDAO.findByDateRange(veryOldDate, futureDate);
    System.out.println("Clicks in extreme date range: " + extremeDateClicks.size());

    // Test count methods with non-existent data
    long nonExistentBookCount = affiliateClickDAO.getClickCountByBookId(99999);
    System.out.println("Click count for non-existent book: " + nonExistentBookCount);

    long nonExistentUserCount = affiliateClickDAO.getClickCountByUserId(99999);
    System.out.println("Click count for non-existent user: " + nonExistentUserCount);

    // Test deleteOldClicks (simulate but don't actually delete recent data)
    // Create an old click first
    AffiliateClick oldClick = new AffiliateClick();
    oldClick.setBookId(4); // Use existing book ID
    oldClick.setClickTime(LocalDateTime.now().minusDays(100));
    affiliateClickDAO.save(oldClick);

    // Delete clicks older than 90 days
    int deletedCount = affiliateClickDAO.deleteOldClicks(90);
    System.out.println("Deleted old clicks: " + deletedCount);

    // Test count methods
    Long totalCount = affiliateClickDAO.count();
    System.out.println("Total affiliate click count (generic): " + totalCount);

    if (totalCount > 0) {
      List<AffiliateClick> allClicks = affiliateClickDAO.findAll();
      AffiliateClick firstClick = allClicks.get(0);
      boolean exists = affiliateClickDAO.existsById(firstClick.getClickId());
      System.out.println("First click exists by ID: " + exists);
    }
  }
}
