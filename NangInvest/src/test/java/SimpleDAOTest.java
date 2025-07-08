import dao.UserDAO;
import model.User;

public class SimpleDAOTest {
  public static void main(String[] args) {
    UserDAO userDAO = new UserDAO();
    System.out.println("UserDAO created successfully!");

    // Test that inherited methods are available
    long count = userDAO.count();
    System.out.println("User count: " + count);

    // Test custom method
    boolean exists = userDAO.existsByUsername("test");
    System.out.println("Test user exists: " + exists);
  }
}
