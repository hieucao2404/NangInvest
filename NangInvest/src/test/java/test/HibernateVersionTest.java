package test;

import org.hibernate.Version;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.metamodel.EntityType;

public class HibernateVersionTest {
  public static void main(String[] args) {
    try {
      System.out.println("=== HIBERNATE & JPA COMPREHENSIVE TEST ===");
      System.out.println("Hibernate Version: " + Version.getVersionString());

      // Test 1: Check if JPA provider is available
      try {
        Class<?> providerClass = Class.forName("org.hibernate.jpa.HibernatePersistenceProvider");
        System.out.println("✅ JPA Provider class found: " + providerClass.getName());

        Object provider = providerClass.getDeclaredConstructor().newInstance();
        System.out.println("✅ JPA Provider instance created successfully");

      } catch (Exception e) {
        System.out.println("❌ JPA Provider instantiation failed: " + e.getMessage());
        e.printStackTrace();
        return;
      }

      // Test 2: Test persistence.xml and EntityManagerFactory creation
      EntityManagerFactory emf = null;
      EntityManager em = null;

      try {
        System.out.println("\n=== TESTING JPA CONFIGURATION ===");

        // This will test persistence.xml configuration
        emf = Persistence.createEntityManagerFactory("NangInvestPU");
        System.out.println("✅ EntityManagerFactory created successfully");

        // This will test database connection
        em = emf.createEntityManager();
        System.out.println("✅ EntityManager created successfully");

        // Test 3: Check entity discovery
        System.out.println("\n=== DISCOVERED JPA ENTITIES ===");
        var entities = em.getMetamodel().getEntities();
        if (entities.isEmpty()) {
          System.out.println("⚠️  No entities discovered - add @Entity annotations to your model classes");
        } else {
          System.out.println("✅ Found " + entities.size() + " JPA entities:");
          for (EntityType<?> entityType : entities) {
            System.out.println("  - " + entityType.getName() + " (" + entityType.getJavaType().getSimpleName() + ")");
          }
        }

        // Test 4: Test basic JPA operations (without actual database operations)
        System.out.println("\n=== JPA OPERATIONS TEST ===");

        // Test transaction creation
        var transaction = em.getTransaction();
        System.out.println("✅ Transaction object created: " + transaction.getClass().getSimpleName());

        // Test query creation
        var query = em.createQuery("SELECT 1", Integer.class);
        System.out.println("✅ Query creation successful");

        System.out.println("\n🎉 ALL JPA TESTS PASSED!");
        System.out.println("Your JPA configuration is working correctly!");

      } catch (Exception e) {
        System.out.println("❌ JPA Configuration failed: " + e.getMessage());
        System.out.println("\nPossible issues:");
        System.out.println("- Check persistence.xml configuration");
        System.out.println("- Verify database connection settings");
        System.out.println("- Ensure database server is running");
        System.out.println("- Check entity annotations");
        e.printStackTrace();
      } finally {
        // Clean up resources
        if (em != null) {
          try {
            em.close();
            System.out.println("✅ EntityManager closed");
          } catch (Exception e) {
            System.out.println("⚠️  Error closing EntityManager: " + e.getMessage());
          }
        }
        if (emf != null) {
          try {
            emf.close();
            System.out.println("✅ EntityManagerFactory closed");
          } catch (Exception e) {
            System.out.println("⚠️  Error closing EntityManagerFactory: " + e.getMessage());
          }
        }
      }

    } catch (Exception e) {
      System.out.println("❌ Critical error: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
