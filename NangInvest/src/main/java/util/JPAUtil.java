package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
  private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
      .createEntityManagerFactory("NangInvestPU");

  public static EntityManager gEntityManager() {
    return ENTITY_MANAGER_FACTORY.createEntityManager();
  }

  public static void closeEntityManagerFactory() {
    ENTITY_MANAGER_FACTORY.close();
  }
}
