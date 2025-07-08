package test;

import util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * Comprehensive database schema testing program
 * Tests all tables in the database and provides detailed schema information
 * for future application development
 */
public class ComprehensiveSchemaTest {

  // List of tables we expect to find in the database
  private static final List<String> EXPECTED_TABLES = Arrays.asList(
      "Users", "Courses", "Books", "Services", "Orders", "Cart", "Blog",
      "AffiliateClicks", "Comments", "Notifications");

  public static void main(String[] args) {
    System.out.println("🚀 Comprehensive Database Schema Analysis");
    System.out.println("=========================================\n");

    EntityManagerFactory emf = null;
    EntityManager em = null;

    try {
      emf = Persistence.createEntityManagerFactory("NangInvestPU");
      em = emf.createEntityManager();

      // Get the underlying JDBC connection
      SessionImplementor sessionImpl = em.unwrap(SessionImplementor.class);
      Connection connection = sessionImpl.getJdbcConnectionAccess().obtainConnection();
      DatabaseMetaData metaData = connection.getMetaData();

      // 1. Database Information
      printDatabaseInfo(metaData);

      // 2. List all tables in the database
      listAllTables(metaData);

      // 3. Analyze each expected table
      for (String tableName : EXPECTED_TABLES) {
        analyzeTable(metaData, tableName);
      }

      // 4. Check for foreign key relationships
      System.out.println("🔗 FOREIGN KEY RELATIONSHIPS");
      System.out.println("============================");
      checkForeignKeys(metaData);

      System.out.println("\n✅ Comprehensive schema analysis completed successfully!");
      System.out.println("\n📋 SUMMARY FOR ENTITY DEVELOPMENT:");
      System.out.println("==================================");
      provideDevelopmentSummary();

    } catch (Exception e) {
      System.out.println("❌ Error during schema analysis: " + e.getMessage());
      e.printStackTrace();
    } finally {
      if (em != null)
        em.close();
      if (emf != null)
        emf.close();
    }
  }

  private static void printDatabaseInfo(DatabaseMetaData metaData) throws SQLException {
    System.out.println("📊 DATABASE INFORMATION");
    System.out.println("=======================");
    System.out.println("Database Product: " + metaData.getDatabaseProductName());
    System.out.println("Database Version: " + metaData.getDatabaseProductVersion());
    System.out.println("Driver Name: " + metaData.getDriverName());
    System.out.println("Driver Version: " + metaData.getDriverVersion());
    System.out.println();
  }

  private static void listAllTables(DatabaseMetaData metaData) throws SQLException {
    System.out.println("📋 ALL TABLES IN DATABASE");
    System.out.println("=========================");

    ResultSet tables = metaData.getTables(null, null, "%", new String[] { "TABLE" });
    boolean foundTables = false;

    while (tables.next()) {
      foundTables = true;
      String tableName = tables.getString("TABLE_NAME");
      String tableType = tables.getString("TABLE_TYPE");
      System.out.println("  ✅ " + tableName + " (" + tableType + ")");
    }

    if (!foundTables) {
      System.out.println("  ⚠️  No tables found!");
    }

    tables.close();
    System.out.println();
  }

  private static void analyzeTable(DatabaseMetaData metaData, String tableName) throws SQLException {
    System.out.println("🔍 ANALYZING TABLE: " + tableName);
    System.out.println("====================");

    // Check if table exists
    ResultSet tables = metaData.getTables(null, null, tableName, null);
    if (!tables.next()) {
      System.out.println("  ❌ Table '" + tableName + "' not found!");
      tables.close();
      System.out.println();
      return;
    }
    tables.close();

    // Get column information
    System.out.println("📊 Columns:");
    ResultSet columns = metaData.getColumns(null, null, tableName, null);
    while (columns.next()) {
      String columnName = columns.getString("COLUMN_NAME");
      String dataType = columns.getString("TYPE_NAME");
      int columnSize = columns.getString("COLUMN_SIZE") != null ? columns.getInt("COLUMN_SIZE") : 0;
      String nullable = columns.getString("IS_NULLABLE");
      String defaultValue = columns.getString("COLUMN_DEF");
      String autoIncrement = columns.getString("IS_AUTOINCREMENT");

      System.out.printf("  %-20s | %-15s | Size: %-6s | Nullable: %-3s | Auto: %-3s | Default: %s%n",
          columnName, dataType, columnSize > 0 ? columnSize : "N/A",
          nullable, autoIncrement != null ? autoIncrement : "N/A",
          defaultValue != null ? defaultValue : "None");
    }
    columns.close();

    // Get primary key information
    System.out.println("\n🔑 Primary Keys:");
    ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
    boolean hasPrimaryKey = false;
    while (primaryKeys.next()) {
      hasPrimaryKey = true;
      String pkColumnName = primaryKeys.getString("COLUMN_NAME");
      String pkName = primaryKeys.getString("PK_NAME");
      System.out.println("  ✅ " + pkColumnName + " (Constraint: " + pkName + ")");
    }
    if (!hasPrimaryKey) {
      System.out.println("  ⚠️  No primary key found!");
    }
    primaryKeys.close();

    // Get unique constraints
    System.out.println("\n🔒 Unique Constraints:");
    ResultSet indexInfo = metaData.getIndexInfo(null, null, tableName, true, false);
    boolean hasUniqueConstraints = false;
    while (indexInfo.next()) {
      hasUniqueConstraints = true;
      String indexName = indexInfo.getString("INDEX_NAME");
      String columnName = indexInfo.getString("COLUMN_NAME");
      if (indexName != null && columnName != null) {
        System.out.println("  🔒 " + columnName + " (Index: " + indexName + ")");
      }
    }
    if (!hasUniqueConstraints) {
      System.out.println("  ℹ️  No unique constraints found");
    }
    indexInfo.close();

    System.out.println();
  }

  private static void checkForeignKeys(DatabaseMetaData metaData) throws SQLException {
    for (String tableName : EXPECTED_TABLES) {
      ResultSet foreignKeys = metaData.getImportedKeys(null, null, tableName);
      boolean hasForeignKeys = false;

      while (foreignKeys.next()) {
        if (!hasForeignKeys) {
          System.out.println("Table: " + tableName);
          hasForeignKeys = true;
        }

        String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
        String pkTableName = foreignKeys.getString("PKTABLE_NAME");
        String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
        String fkName = foreignKeys.getString("FK_NAME");

        System.out.println("  🔗 " + fkColumnName + " -> " + pkTableName + "." + pkColumnName +
            " (FK: " + fkName + ")");
      }

      foreignKeys.close();
      if (hasForeignKeys) {
        System.out.println();
      }
    }
  }

  private static void provideDevelopmentSummary() {
    System.out.println("1. ✅ Users table - Entity and DAO already implemented");
    System.out.println("2. 🔄 Courses table - Ready for entity implementation");
    System.out.println("   - Primary Key: CourseID (int, auto-increment)");
    System.out.println("   - Required: CourseName");
    System.out.println("   - Optional: Price, Time, IsFree, ImageUrl");
    System.out.println("3. 📋 Other tables - Pending analysis and implementation");
    System.out.println("\n🎯 NEXT STEPS:");
    System.out.println("1. Implement Course entity with JPA annotations");
    System.out.println("2. Create CourseDAO extending GenericDAO");
    System.out.println("3. Create CourseDAOTest for validation");
    System.out.println("4. Repeat for other entities (Books, Services, etc.)");
  }
}
