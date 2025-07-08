package test;

import dao.UserDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * Test to check the current database schema and column names
 */
public class DatabaseSchemaTest {

  public static void main(String[] args) {
    System.out.println("🔍 Database Schema Analysis...\n");

    EntityManagerFactory emf = null;
    EntityManager em = null;

    try {
      emf = Persistence.createEntityManagerFactory("NangInvestPU");
      em = emf.createEntityManager();

      // Get the underlying JDBC connection
      SessionImplementor sessionImpl = em.unwrap(SessionImplementor.class);
      Connection connection = sessionImpl.getJdbcConnectionAccess().obtainConnection();

      // Get database metadata
      DatabaseMetaData metaData = connection.getMetaData();

      // Check if users table exists and get column information
      System.out.println("📋 Checking 'users' table structure:");
      ResultSet columns = metaData.getColumns(null, null, "users", null);

      boolean tableExists = false;
      while (columns.next()) {
        tableExists = true;
        String columnName = columns.getString("COLUMN_NAME");
        String dataType = columns.getString("TYPE_NAME");
        int columnSize = columns.getInt("COLUMN_SIZE");
        String nullable = columns.getString("IS_NULLABLE");

        System.out.println("  ✅ Column: " + columnName +
            " (Type: " + dataType +
            ", Size: " + columnSize +
            ", Nullable: " + nullable + ")");
      }

      if (!tableExists) {
        System.out.println("  ⚠️  'users' table not found!");
      }

      // Also try to query the table directly to see what works
      System.out.println("\n🔄 Testing direct queries:");

      // Test different column name variations
      String[] columnTests = {
          "SELECT COUNT(*) FROM users",
          "SELECT TOP 1 userName FROM users",
          "SELECT TOP 1 user_name FROM users",
          "SELECT TOP 1 googleId FROM users",
          "SELECT TOP 1 google_id FROM users"
      };

      for (String sql : columnTests) {
        try {
          Query query = em.createNativeQuery(sql);
          Object result = query.getSingleResult();
          System.out.println("  ✅ " + sql + " -> Works");
        } catch (Exception e) {
          System.out.println("  ❌ " + sql + " -> " + e.getMessage().split("\n")[0]);
        }
      }

    } catch (Exception e) {
      System.out.println("❌ Error: " + e.getMessage());
      e.printStackTrace();
    } finally {
      if (em != null)
        em.close();
      if (emf != null)
        emf.close();
    }
  }
}
