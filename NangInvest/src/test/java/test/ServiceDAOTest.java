package test;

import java.util.List;
import java.util.Optional;

import dao.ServiceDAO;
import model.Service;
import util.JPAUtil;

/**
 * Test class for ServiceDAO functionality
 */
public class ServiceDAOTest {

  public static void main(String[] args) {
    ServiceDAO serviceDAO = new ServiceDAO();

    System.out.println("🧪 Testing ServiceDAO functionality...\n");

    try {
      // Test 1: Basic CRUD Operations
      System.out.println("1️⃣ Testing basic CRUD operations:");

      // Create a new service
      Service testService = new Service("Test Investment Consulting", "https://example.com/test-image.jpg");
      Service savedService = serviceDAO.save(testService);
      System.out.println("✅ Created service: " + savedService);

      // Read the service
      Optional<Service> foundService = serviceDAO.findById(savedService.getServiceId());
      System.out.println("✅ Found service: " + foundService.orElse(null));

      // Update the service
      if (foundService.isPresent()) {
        Service service = foundService.get();
        service.setImageUrl("https://example.com/updated-image.jpg");
        Service updatedService = serviceDAO.update(service);
        System.out.println("✅ Updated service: " + updatedService);
      }

      System.out.println();

      // Test 2: Custom finder methods
      System.out.println("2️⃣ Testing custom finder methods:");

      // Find by service name
      Optional<Service> serviceByName = serviceDAO.findByServiceName("Test Investment Consulting");
      System.out.println("✅ Found by name: " + serviceByName.orElse(null));

      // Find services with images
      List<Service> servicesWithImages = serviceDAO.findServicesWithImages();
      System.out.println("✅ Services with images count: " + servicesWithImages.size());

      // Check if service name exists
      boolean exists = serviceDAO.existsByServiceName("Test Investment Consulting");
      System.out.println("✅ Service name exists: " + exists);

      System.out.println();

      // Test 3: Statistical queries
      System.out.println("3️⃣ Testing statistical queries:");

      long serviceCount = serviceDAO.getServiceCount();
      System.out.println("✅ Total service count: " + serviceCount);

      // Find services containing keyword
      List<Service> investmentServices = serviceDAO.findByServiceNameContaining("Investment");
      System.out.println("✅ Services containing 'Investment': " + investmentServices.size());

      System.out.println();

      // Test 4: Pagination
      System.out.println("4️⃣ Testing pagination:");

      List<Service> paginatedServices = serviceDAO.findServicesPaginated(0, 5);
      System.out.println("✅ First 5 services: " + paginatedServices.size());

      System.out.println();

      // Test 5: Update operations
      System.out.println("5️⃣ Testing update operations:");

      if (savedService != null) {
        serviceDAO.updateServiceImage(savedService.getServiceId(), "https://example.com/final-image.jpg");
        System.out.println("✅ Updated service image URL");

        // Verify the update
        Optional<Service> verifyService = serviceDAO.findById(savedService.getServiceId());
        if (verifyService.isPresent()) {
          System.out.println("✅ Verified image URL: " + verifyService.get().getImageUrl());
        }
      }

      System.out.println();

      // Test 6: Cleanup - Delete the test service
      System.out.println("6️⃣ Testing delete operations:");

      if (savedService != null) {
        serviceDAO.delete(savedService);
        System.out.println("✅ Deleted test service");

        // Verify deletion
        Optional<Service> deletedService = serviceDAO.findById(savedService.getServiceId());
        System.out.println(
            "✅ Service after deletion: " + (deletedService.isEmpty() ? "Not found (correct)" : "Still exists (error)"));
      }

      System.out.println("\n🎉 All ServiceDAO tests completed successfully!");

    } catch (Exception e) {
      System.err.println("❌ Error during testing: " + e.getMessage());
      e.printStackTrace();
    } finally {
      JPAUtil.closeEntityManagerFactory();
    }
  }
}
