package test;

import util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;

/**
 * Test to examine the Courses table schema in the database
 */
public class CourseSchemaTest {

  public static void main(String[] args) {
    EntityManager em = null;
    try {
      em = JPAUtil.gEntityManager();

      System.out.println("=== COURSES TABLE SCHEMA TEST ===");

      // Try to query the courses table to see what columns exist
      try {
        Query query = em.createNativeQuery("SELECT TOP 1 * FROM Courses");
        List<?> result = query.getResultList();
        System.out.println("Courses table exists and is accessible");
        System.out.println("Found " + result.size() + " rows in sample query");
      } catch (Exception e) {
        System.out.println("Error querying Courses table: " + e.getMessage());
      }

      // Try to get table information using information_schema
      try {
        Query columnQuery = em.createNativeQuery(
            "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT " +
                "FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME = 'Courses' " +
                "ORDER BY ORDINAL_POSITION");

        @SuppressWarnings("unchecked")
        List<Object[]> columns = columnQuery.getResultList();

        System.out.println("\nCourses table columns:");
        for (Object[] column : columns) {
          System.out.printf("  %s - %s - Nullable: %s - Default: %s%n",
              column[0], column[1], column[2], column[3]);
        }

      } catch (Exception e) {
        System.out.println("Error getting column information: " + e.getMessage());
      }

    } catch (Exception e) {
      System.err.println("Error examining Courses table schema: " + e.getMessage());
      e.printStackTrace();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }
}
