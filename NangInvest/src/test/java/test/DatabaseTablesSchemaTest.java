package test;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import util.JPAUtil;

/**
 * Test to examine all tables in the database schema
 */
public class DatabaseTablesSchemaTest {

  public static void main(String[] args) {
    EntityManager em = null;
    try {
      em = JPAUtil.gEntityManager();

      System.out.println("🔍 Database Tables Schema Analysis...\n");

      // Get all tables in the database
      Query tableQuery = em.createNativeQuery(
          "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' ORDER BY TABLE_NAME");

      @SuppressWarnings("unchecked")
      List<String> tables = tableQuery.getResultList();

      System.out.println("📋 Found " + tables.size() + " tables:");
      for (String tableName : tables) {
        System.out.println("  • " + tableName);

        // Get column information for each table
        Query columnQuery = em.createNativeQuery(
            "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT " +
                "FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME = ? " +
                "ORDER BY ORDINAL_POSITION");
        columnQuery.setParameter(1, tableName);

        @SuppressWarnings("unchecked")
        List<Object[]> columns = columnQuery.getResultList();

        for (Object[] column : columns) {
          System.out.printf("    - %s (%s, Nullable: %s, Default: %s)%n",
              column[0], column[1], column[2], column[3]);
        }
        System.out.println();
      }

    } catch (Exception e) {
      System.err.println("❌ Error: " + e.getMessage());
      e.printStackTrace();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }
}
