package test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * Test to check the Courses table schema and column names
 */
public class CoursesSchemaTest {

  public static void main(String[] args) {
    System.out.println("🔍 Courses Table Schema Analysis...\n");

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

      // Check if courses table exists and get column information
      System.out.println("📋 Checking 'Courses' table structure:");
      ResultSet columns = metaData.getColumns(null, null, "Courses", null);

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
        System.out.println("  ⚠️  'Courses' table not found!");

        // Try with lowercase table name
        System.out.println("\n📋 Checking 'courses' table structure:");
        columns = metaData.getColumns(null, null, "courses", null);

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
      }

      if (!tableExists) {
        System.out.println("  ❌ Neither 'Courses' nor 'courses' table found!");
        return;
      }

      // Also check primary key information
      System.out.println("\n🔑 Checking primary keys:");
      ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, "Courses");
      while (primaryKeys.next()) {
        String pkColumnName = primaryKeys.getString("COLUMN_NAME");
        System.out.println("  ✅ Primary Key: " + pkColumnName);
      }

      // If no primary keys found for "Courses", try "courses"
      if (!primaryKeys.next()) {
        primaryKeys = metaData.getPrimaryKeys(null, null, "courses");
        while (primaryKeys.next()) {
          String pkColumnName = primaryKeys.getString("COLUMN_NAME");
          System.out.println("  ✅ Primary Key: " + pkColumnName);
        }
      }

      System.out.println("\n✅ Courses table schema analysis completed successfully!");

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
