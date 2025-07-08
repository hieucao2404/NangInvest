import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.User;

/**
 * Simple test to verify JPA setup is working
 */
public class JPASetupTest {

  public static void main(String[] args) {
    EntityManagerFactory emf = null;
    EntityManager em = null;

    try {
      // Create EntityManagerFactory
      System.out.println("Creating EntityManagerFactory...");
      emf = Persistence.createEntityManagerFactory("NangInvestPU");
      System.out.println("✅ EntityManagerFactory created successfully!");

      // Create EntityManager
      System.out.println("Creating EntityManager...");
      em = emf.createEntityManager();
      System.out.println("✅ EntityManager created successfully!");

      // Test creating a user
      System.out.println("Testing User entity creation...");
      User testUser = new User();
      testUser.setUserName("testuser");
      testUser.setEmail("test@example.com");
      testUser.setPassword("password123");
      testUser.setName("Test User");
      testUser.setAge(25);

      // Begin transaction
      em.getTransaction().begin();

      // Persist user
      em.persist(testUser);

      // Commit transaction
      em.getTransaction().commit();

      System.out.println("✅ User entity persisted successfully!");
      System.out.println("Generated User ID: " + testUser.getUserId());

      // Query the user back
      User foundUser = em.find(User.class, testUser.getUserId());
      if (foundUser != null) {
        System.out.println("✅ User found: " + foundUser.getUserName());
      }

      System.out.println("\n🎉 JPA Setup Test PASSED! Your JPA configuration is working correctly.");

    } catch (Exception e) {
      System.err.println("❌ JPA Setup Test FAILED!");
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();

      if (em != null && em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
    } finally {
      // Clean up
      if (em != null) {
        em.close();
      }
      if (emf != null) {
        emf.close();
      }
    }
  }
}
