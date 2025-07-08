package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * JPA Entity for Cart table
 * 
 * @author Admin
 */
@Entity
@Table(name = "Cart")
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CartID")
  private Integer cartId;

  @Column(name = "UserID", nullable = false)
  private Integer userId;

  @Column(name = "ProductID")
  private Integer productId;

  @Column(name = "Quantity", columnDefinition = "INT DEFAULT 1")
  private Integer quantity = 1;

  // JPA relationships (optional)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "UserID", insertable = false, updatable = false)
  private User user;

  // Note: ProductID can reference either Course or Book or Service
  // We'll handle this through application logic rather than JPA relationships
  // since it's a polymorphic relationship

  // Constructors
  public Cart() {
  }

  public Cart(Integer userId, Integer productId) {
    this.userId = userId;
    this.productId = productId;
    this.quantity = 1;
  }

  public Cart(Integer userId, Integer productId, Integer quantity) {
    this.userId = userId;
    this.productId = productId;
    this.quantity = quantity;
  }

  // Getters and Setters
  public Integer getCartId() {
    return cartId;
  }

  public void setCartId(Integer cartId) {
    this.cartId = cartId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  // Utility methods
  public void increaseQuantity() {
    this.quantity++;
  }

  public void decreaseQuantity() {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  public boolean isValidQuantity() {
    return quantity != null && quantity > 0;
  }

  @Override
  public String toString() {
    return "Cart{" +
        "cartId=" + cartId +
        ", userId=" + userId +
        ", productId=" + productId +
        ", quantity=" + quantity +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Cart cart = (Cart) o;
    return cartId != null ? cartId.equals(cart.cartId) : cart.cartId == null;
  }

  @Override
  public int hashCode() {
    return cartId != null ? cartId.hashCode() : 0;
  }
}
