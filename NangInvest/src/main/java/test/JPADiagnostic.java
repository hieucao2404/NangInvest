package test;

import util.JPAUtil;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPADiagnostic {
    
    public static void main(String[] args) {
        System.out.println("=== JPA Diagnostic Test ===");
        
        try {
            System.out.println("1. Testing EntityManagerFactory creation...");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("NangInvestPU");
            System.out.println("✅ EntityManagerFactory created successfully");
            
            System.out.println("2. Testing EntityManager creation...");
            var em = emf.createEntityManager();
            System.out.println("✅ EntityManager created successfully");
            
            System.out.println("3. Testing database connection...");
            em.createNativeQuery("SELECT 1").getSingleResult();
            System.out.println("✅ Database connection successful");
            
            em.close();
            emf.close();
            
            System.out.println("4. Testing JPAUtil...");
            var utilEM = JPAUtil.gEntityManager();
            System.out.println("✅ JPAUtil working");
            utilEM.close();
            
            System.out.println("\n🎉 ALL JPA COMPONENTS WORKING!");
            
        } catch (Exception e) {
            System.err.println("❌ JPA Error: " + e.getClass().getSimpleName());
            System.err.println("   Message: " + e.getMessage());
            
            if (e.getCause() != null) {
                System.err.println("   Root Cause: " + e.getCause().getClass().getSimpleName());
                System.err.println("   Root Message: " + e.getCause().getMessage());
            }
            
            e.printStackTrace();
        }
    }
}
