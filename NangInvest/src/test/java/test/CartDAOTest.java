package test;

import java.util.List;

import dao.CartDAO;
import model.Cart;
import util.JPAUtil;

/**
 * Test class for CartDAO
 * Tests CRUD operations and custom queries for Cart
 */
public class CartDAOTest {

  private static CartDAO cartDAO;

  public static void main(String[] args) {
    System.out.println("=== Starting CartDAO Test ===");

    try {
      // Initialize DAO
      cartDAO = new CartDAO();

      // Run all tests
      testCRUDOperations();
      testCustomQueries();
      testUtilityMethods();
      testEdgeCases();

      System.out.println("=== All CartDAO tests completed successfully! ===");

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
    Cart cart1 = new Cart();
    cart1.setUserId(1);
    cart1.setProductId(101);
    cart1.setQuantity(2);

    Cart savedCart1 = cartDAO.save(cart1);
    System.out.println("Created cart item: User " + savedCart1.getUserId() +
        ", Product " + savedCart1.getProductId() +
        ", Quantity " + savedCart1.getQuantity() +
        " (ID: " + savedCart1.getCartId() + ")");

    Cart cart2 = new Cart();
    cart2.setUserId(1);
    cart2.setProductId(102);
    cart2.setQuantity(1);

    Cart savedCart2 = cartDAO.save(cart2);
    System.out.println("Created cart item: User " + savedCart2.getUserId() +
        ", Product " + savedCart2.getProductId() +
        " (ID: " + savedCart2.getCartId() + ")");

    Cart cart3 = new Cart();
    cart3.setUserId(2);
    cart3.setProductId(101);
    cart3.setQuantity(3);

    Cart savedCart3 = cartDAO.save(cart3);
    System.out.println("Created cart item: User " + savedCart3.getUserId() +
        ", Product " + savedCart3.getProductId() +
        " (ID: " + savedCart3.getCartId() + ")");

    // Test Read
    Cart foundCart = cartDAO.findById(savedCart1.getCartId()).orElse(null);
    System.out.println("Found cart by ID: " +
        (foundCart != null ? "User " + foundCart.getUserId() + ", Product " + foundCart.getProductId() : "Not found"));

    // Test Update
    savedCart1.setQuantity(5);
    Cart updatedCart = cartDAO.update(savedCart1);
    System.out.println("Updated cart quantity to: " + updatedCart.getQuantity());

    // Test findAll
    List<Cart> allCarts = cartDAO.findAll();
    System.out.println("Total cart items in database: " + allCarts.size());
  }

  private static void testCustomQueries() {
    System.out.println("\n--- Testing Custom Queries ---");

    // Test findByUserId
    List<Cart> user1Cart = cartDAO.findByUserId(1);
    System.out.println("Cart items for user 1: " + user1Cart.size());

    List<Cart> user2Cart = cartDAO.findByUserId(2);
    System.out.println("Cart items for user 2: " + user2Cart.size());

    // Test findByUserIdAndProductId
    Cart specificItem = cartDAO.findByUserIdAndProductId(1, 101);
    System.out.println("Specific cart item (User 1, Product 101): " +
        (specificItem != null ? "Found (Quantity: " + specificItem.getQuantity() + ")" : "Not found"));

    // Test existsByUserIdAndProductId
    boolean itemExists = cartDAO.existsByUserIdAndProductId(1, 101);
    System.out.println("Cart item exists (User 1, Product 101): " + itemExists);

    boolean itemNotExists = cartDAO.existsByUserIdAndProductId(1, 999);
    System.out.println("Cart item exists (User 1, Product 999): " + itemNotExists);
  }

  private static void testUtilityMethods() {
    System.out.println("\n--- Testing Utility Methods ---");

    // Test getTotalQuantityByUserId
    long totalQuantityUser1 = cartDAO.getTotalQuantityByUserId(1);
    System.out.println("Total quantity for user 1: " + totalQuantityUser1);

    long totalQuantityUser2 = cartDAO.getTotalQuantityByUserId(2);
    System.out.println("Total quantity for user 2: " + totalQuantityUser2);

    // Test getCartItemCountByUserId
    long cartItemCount = cartDAO.getCartItemCountByUserId(1);
    System.out.println("Cart item count for user 1: " + cartItemCount);

    // Test getMostPopularCartProducts
    List<Object[]> popularProducts = cartDAO.getMostPopularCartProducts(5);
    System.out.println("Top 5 popular products in carts:");
    for (Object[] row : popularProducts) {
      Integer productId = (Integer) row[0];
      Long totalQuantity = (Long) row[1];
      System.out.println("  - Product ID: " + productId + ", Total Quantity: " + totalQuantity);
    }

    // Test updateQuantity
    List<Cart> user1Items = cartDAO.findByUserId(1);
    if (!user1Items.isEmpty()) {
      Cart firstItem = user1Items.get(0);
      int originalQuantity = firstItem.getQuantity();
      cartDAO.updateQuantity(firstItem.getCartId(), originalQuantity + 10);
      System.out.println("Updated quantity for cart ID " + firstItem.getCartId() + " to: " + (originalQuantity + 10));
    }

    // Test addOrUpdateCartItem
    Cart addedItem = cartDAO.addOrUpdateCartItem(1, 999, 3);
    System.out
        .println("Added/Updated cart item: User 1, Product 999, Quantity 3 (Cart ID: " + addedItem.getCartId() + ")");

    // Try adding again to same product (should update quantity)
    Cart updatedItem = cartDAO.addOrUpdateCartItem(1, 999, 2);
    System.out.println("Updated cart item quantity to: " + updatedItem.getQuantity());
  }

  private static void testEdgeCases() {
    System.out.println("\n--- Testing Edge Cases ---");

    // Test with cart having minimal data
    Cart minimalCart = new Cart();
    minimalCart.setUserId(3); // Use existing user ID
    minimalCart.setProductId(999);
    // Default quantity should be 1

    Cart savedMinimalCart = cartDAO.save(minimalCart);
    System.out.println("Created minimal cart: User " + savedMinimalCart.getUserId() +
        ", Product " + savedMinimalCart.getProductId() +
        ", Quantity " + savedMinimalCart.getQuantity());

    // Test queries with non-existent data
    List<Cart> nonExistentUserCart = cartDAO.findByUserId(99999);
    System.out.println("Cart items for non-existent user: " + nonExistentUserCart.size());

    Cart nonExistentSpecific = cartDAO.findByUserIdAndProductId(99999, 99999);
    System.out.println("Non-existent specific cart item: " + (nonExistentSpecific != null ? "Found" : "Not found"));

    // Test existsByUserIdAndProductId with non-existent data
    boolean nonExistentExists = cartDAO.existsByUserIdAndProductId(99999, 99999);
    System.out.println("Non-existent cart item exists: " + nonExistentExists);

    // Test utility methods with non-existent users
    long nonExistentUserQuantity = cartDAO.getTotalQuantityByUserId(99999);
    System.out.println("Total quantity for non-existent user: " + nonExistentUserQuantity);

    long nonExistentUserCount = cartDAO.getCartItemCountByUserId(99999);
    System.out.println("Cart item count for non-existent user: " + nonExistentUserCount);

    // Test clearCartByUserId functionality
    int clearedCount = cartDAO.clearCartByUserId(3); // Use existing user ID
    System.out.println("Cleared cart items for user 3: " + clearedCount);

    // Test removeProductFromCart
    boolean removed = cartDAO.removeProductFromCart(1, 999);
    System.out.println("Removed product 999 from user 1 cart: " + removed);

    // Test removing non-existent product
    boolean removedNonExistent = cartDAO.removeProductFromCart(1, 99999);
    System.out.println("Removed non-existent product from cart: " + removedNonExistent);

    // Test count methods
    Long totalCount = cartDAO.count();
    System.out.println("Total cart count (generic): " + totalCount);

    if (totalCount > 0) {
      List<Cart> allCarts = cartDAO.findAll();
      Cart firstCart = allCarts.get(0);
      boolean exists = cartDAO.existsById(firstCart.getCartId());
      System.out.println("First cart exists by ID: " + exists);
    }
  }
}
