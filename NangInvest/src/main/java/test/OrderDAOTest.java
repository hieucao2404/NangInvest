package test;

import dao.OrderDAO;
import model.Order;
import util.JPAUtil;

import java.util.List;

/**
 * Test class for OrderDAO
 * Tests CRUD operations and custom queries for Order
 */
public class OrderDAOTest {

    private static final String STATUS_PENDING = "Pending";
    private static final String STATUS_COMPLETED = "Completed";
    private static final String STATUS_CANCELLED = "Cancelled";
    
    private static OrderDAO orderDAO;

    public static void main(String[] args) {
        System.out.println("=== Starting OrderDAO Test ===");
        
        try {
            // Initialize DAO
            orderDAO = new OrderDAO();
            
            // Run all tests
            testCRUDOperations();
            testCustomQueries();
            testUtilityMethods();
            testEdgeCases();
            
            System.out.println("=== All OrderDAO tests completed successfully! ===");
            
        } catch (Exception e) {
            System.err.println("Test failed with exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            JPAUtil.closeEntityManagerFactory();
        }
    }

    private static void testCRUDOperations() {
        System.out.println("\n--- Testing CRUD Operations ---");

        // Test Create
        Order order1 = new Order();
        order1.setUserId(1);
        order1.setProductId(101);
        order1.setPaymentStatus(STATUS_PENDING);

        Order savedOrder1 = orderDAO.save(order1);
        System.out.println("Created order: User " + savedOrder1.getUserId() + 
                          ", Product " + savedOrder1.getProductId() + 
                          ", Status " + savedOrder1.getPaymentStatus() + 
                          " (ID: " + savedOrder1.getOrderId() + ")");

        Order order2 = new Order();
        order2.setUserId(1);
        order2.setProductId(102);
        order2.setPaymentStatus(STATUS_COMPLETED);

        Order savedOrder2 = orderDAO.save(order2);
        System.out.println("Created order: User " + savedOrder2.getUserId() + 
                          ", Product " + savedOrder2.getProductId() + 
                          ", Status " + savedOrder2.getPaymentStatus() + 
                          " (ID: " + savedOrder2.getOrderId() + ")");

        Order order3 = new Order();
        order3.setUserId(2);
        order3.setProductId(101);
        order3.setPaymentStatus(STATUS_PENDING);

        Order savedOrder3 = orderDAO.save(order3);
        System.out.println("Created order: User " + savedOrder3.getUserId() + 
                          ", Product " + savedOrder3.getProductId() + 
                          " (ID: " + savedOrder3.getOrderId() + ")");

        // Test Read
        Order foundOrder = orderDAO.findById(savedOrder1.getOrderId()).orElse(null);
        System.out.println("Found order by ID: " + 
                          (foundOrder != null ? "User " + foundOrder.getUserId() + ", Product " + foundOrder.getProductId() : "Not found"));

        // Test Update
        savedOrder1.setPaymentStatus(STATUS_COMPLETED);
        Order updatedOrder = orderDAO.update(savedOrder1);
        System.out.println("Updated order status to: " + updatedOrder.getPaymentStatus());

        // Test findAll
        List<Order> allOrders = orderDAO.findAll();
        System.out.println("Total orders in database: " + allOrders.size());
    }

    private static void testCustomQueries() {
        System.out.println("\n--- Testing Custom Queries ---");

        // Test findByUserId
        List<Order> user1Orders = orderDAO.findByUserId(1);
        System.out.println("Orders for user 1: " + user1Orders.size());

        List<Order> user2Orders = orderDAO.findByUserId(2);
        System.out.println("Orders for user 2: " + user2Orders.size());

        // Test findByPaymentStatus
        List<Order> pendingOrders = orderDAO.findByPaymentStatus(STATUS_PENDING);
        System.out.println("Pending orders: " + pendingOrders.size());

        List<Order> completedOrders = orderDAO.findByPaymentStatus(STATUS_COMPLETED);
        System.out.println("Completed orders: " + completedOrders.size());

        // Test findByUserIdAndPaymentStatus
        List<Order> user1PendingOrders = orderDAO.findByUserIdAndPaymentStatus(1, STATUS_PENDING);
        System.out.println("Pending orders for user 1: " + user1PendingOrders.size());

        // Test findByProductId
        List<Order> product101Orders = orderDAO.findByProductId(101);
        System.out.println("Orders for product 101: " + product101Orders.size());

        // Test findCompletedOrders
        List<Order> user1CompletedOrders = orderDAO.findCompletedOrders();
        System.out.println("All completed orders: " + user1CompletedOrders.size());
    }

    private static void testUtilityMethods() {
        System.out.println("\n--- Testing Utility Methods ---");

        // Test getOrderCountByUserId
        long user1OrderCount = orderDAO.getOrderCountByUserId(1);
        System.out.println("Order count for user 1: " + user1OrderCount);

        long user2OrderCount = orderDAO.getOrderCountByUserId(2);
        System.out.println("Order count for user 2: " + user2OrderCount);

        // Test getOrderCountByPaymentStatus
        long pendingCount = orderDAO.getOrderCountByPaymentStatus(STATUS_PENDING);
        System.out.println("Pending order count: " + pendingCount);

        long completedCount = orderDAO.getOrderCountByPaymentStatus(STATUS_COMPLETED);
        System.out.println("Completed order count: " + completedCount);

        // Test getTotalOrderCount
        long totalOrderCount = orderDAO.getTotalOrderCount();
        System.out.println("Total order count: " + totalOrderCount);

        // Test getMostPopularOrderedProducts
        List<Object[]> popularProducts = orderDAO.getMostPopularOrderedProducts(5);
        System.out.println("Top 5 most ordered products:");
        for (Object[] row : popularProducts) {
            Integer productId = (Integer) row[0];
            Long orderCount = (Long) row[1];
            System.out.println("  - Product ID: " + productId + ", Order Count: " + orderCount);
        }

        // Test findOrdersPaginated
        List<Order> paginatedOrders = orderDAO.findOrdersPaginated(0, 5);
        System.out.println("First 5 orders (paginated): " + paginatedOrders.size());

        // Test hasUserPurchasedProduct
        boolean user1HasProduct101 = orderDAO.hasUserPurchasedProduct(1, 101);
        System.out.println("User 1 has purchased product 101: " + user1HasProduct101);

        boolean user1HasProduct999 = orderDAO.hasUserPurchasedProduct(1, 999);
        System.out.println("User 1 has purchased product 999: " + user1HasProduct999);

        // Test updatePaymentStatus
        List<Order> user2PendingOrders = orderDAO.findByUserIdAndPaymentStatus(2, STATUS_PENDING);
        if (!user2PendingOrders.isEmpty()) {
            Order orderToUpdate = user2PendingOrders.get(0);
            orderDAO.updatePaymentStatus(orderToUpdate.getOrderId(), STATUS_COMPLETED);
            System.out.println("Updated payment status for order " + orderToUpdate.getOrderId() + " to: " + STATUS_COMPLETED);
        }
    }

    private static void testEdgeCases() {
        System.out.println("\n--- Testing Edge Cases ---");

        // Test with order having minimal data
        Order minimalOrder = new Order();
        minimalOrder.setUserId(999);
        minimalOrder.setProductId(999);
        // Default payment status should be "Pending"

        Order savedMinimalOrder = orderDAO.save(minimalOrder);
        System.out.println("Created minimal order: User " + savedMinimalOrder.getUserId() + 
                          ", Product " + savedMinimalOrder.getProductId() + 
                          ", Status " + savedMinimalOrder.getPaymentStatus());

        // Test queries with non-existent data
        List<Order> nonExistentUserOrders = orderDAO.findByUserId(99999);
        System.out.println("Orders for non-existent user: " + nonExistentUserOrders.size());

        List<Order> nonExistentStatusOrders = orderDAO.findByPaymentStatus("NonExistentStatus");
        System.out.println("Orders with non-existent status: " + nonExistentStatusOrders.size());

        List<Order> nonExistentProductOrders = orderDAO.findByProductId(99999);
        System.out.println("Orders for non-existent product: " + nonExistentProductOrders.size());

        // Test utility methods with non-existent data
        long nonExistentUserCount = orderDAO.getOrderCountByUserId(99999);
        System.out.println("Order count for non-existent user: " + nonExistentUserCount);

        long nonExistentStatusCount = orderDAO.getOrderCountByPaymentStatus("NonExistentStatus");
        System.out.println("Order count for non-existent status: " + nonExistentStatusCount);

        // Test hasUserPurchasedProduct with non-existent data
        boolean nonExistentUserHasProduct = orderDAO.hasUserPurchasedProduct(99999, 101);
        System.out.println("Non-existent user has purchased product: " + nonExistentUserHasProduct);

        // Test updatePaymentStatus with non-existent order
        try {
            orderDAO.updatePaymentStatus(99999, STATUS_COMPLETED);
            System.out.println("Updated payment status for non-existent order (no error)");
        } catch (Exception e) {
            System.out.println("Expected error updating non-existent order: " + e.getMessage());
        }

        // Test deleteCancelledOrders
        Order cancelledOrder = new Order();
        cancelledOrder.setUserId(999);
        cancelledOrder.setProductId(888);
        cancelledOrder.setPaymentStatus(STATUS_CANCELLED);
        orderDAO.save(cancelledOrder);
        
        int deletedCount = orderDAO.deleteCancelledOrders(30);
        System.out.println("Deleted cancelled orders: " + deletedCount);

        // Test findPendingOrders and findCancelledOrders
        List<Order> pendingOrders = orderDAO.findPendingOrders();
        System.out.println("Pending orders: " + pendingOrders.size());

        List<Order> cancelledOrders = orderDAO.findCancelledOrders();
        System.out.println("Cancelled orders: " + cancelledOrders.size());

        // Test count methods
        Long totalCount = orderDAO.count();
        System.out.println("Total order count (generic): " + totalCount);

        if (totalCount > 0) {
            List<Order> allOrders = orderDAO.findAll();
            Order firstOrder = allOrders.get(0);
            boolean exists = orderDAO.existsById(firstOrder.getOrderId());
            System.out.println("First order exists by ID: " + exists);
        }
    }
}
