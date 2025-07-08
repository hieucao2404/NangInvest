package test;

import jakarta.persistence.EntityManager;

import model.User;
import util.JPAUtil;


public class JPATest{
  public static void main(String[] args){
    EntityManager em = JPAUtil.gEntityManager();

    try {
        //begin transaction
        em.getTransaction().begin();

        //create a new User entity
         // Create a new User entity
            User user = new User();
            user.setUserName("testuser");
            user.setEmail("testuser@example.com");
            user.setPassword("password123");
            user.setRole(User.Role.USER);
            user.setAge(25);
            user.setName("Test User");
            user.setExpertise("Testing");

            //persiste the User entity
            em.persist(user);

            //commit transaction
            em.getTransaction().commit();

            System.out.println("User persisted successfully!");
    } catch (Exception e) {
      e.printStackTrace();
      em.getTransaction().rollback();
    }finally{
      em.close();
      JPAUtil.closeEntityManagerFactory();
    }
  }
}