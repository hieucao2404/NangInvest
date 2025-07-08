package test;

import java.util.List;

import dao.BooksDAO;
import dao.UserDAO;
import model.Book;
import model.User;
import util.JPAUtil;

/**
 * Test to verify what data exists in the database
 */
public class DataVerificationTest {

  public static void main(String[] args) {
    System.out.println("=== Database Data Verification ===");

    try {
      // Check Users
      UserDAO userDAO = new UserDAO();
      List<User> users = userDAO.findAll();
      System.out.println("Total users in database: " + users.size());

      if (!users.isEmpty()) {
        System.out.println("Sample users:");
        for (int i = 0; i < Math.min(3, users.size()); i++) {
          User user = users.get(i);
          System.out.println("  - User ID: " + user.getUserId() +
              ", Username: " + user.getUserName() +
              ", Email: " + user.getEmail());
        }
      }

      // Check Books
      BooksDAO booksDAO = new BooksDAO();
      List<Book> books = booksDAO.findAll();
      System.out.println("\nTotal books in database: " + books.size());

      if (!books.isEmpty()) {
        System.out.println("Sample books:");
        for (int i = 0; i < Math.min(3, books.size()); i++) {
          Book book = books.get(i);
          System.out.println("  - Book ID: " + book.getBookId() +
              ", Title: " + book.getBookName() +
              ", Topic: " + book.getTopic());
        }
      }

      System.out.println("\n=== Data verification completed successfully! ===");

    } catch (Exception e) {
      System.err.println("Data verification failed: " + e.getMessage());
      e.printStackTrace();
    } finally {
      JPAUtil.closeEntityManagerFactory();
    }
  }
}
