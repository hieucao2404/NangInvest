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
 * JPA Entity for Order table
 * 
 * @author Admin
 */
@Entity
@Table(name = "[Order]") // Escape the reserved keyword "Order" with square brackets for SQL Server
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "OrderID")
  private Integer orderId;

  @Column(name = "UserID", nullable = false)
  private Integer userId;

  @Column(name = "ProductID")
  private Integer productId;

  @Column(name = "PaymentStatus", columnDefinition = "VARCHAR(50) DEFAULT 'Pending'")
  private String paymentStatus = "Pending";

  // JPA relationships (optional)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "UserID", insertable = false, updatable = false)
  private User user;

  // Note: ProductID can reference either Course or Book or Service
  // We'll handle this through application logic rather than JPA relationships
  // since it's a polymorphic relationship

  // Constructors
  public Order() {
  }

  public Order(Integer userId, Integer productId) {
    this.userId = userId;
    this.productId = productId;
    this.paymentStatus = "Pending";
  }

  public Order(Integer userId, Integer productId, String paymentStatus) {
    this.userId = userId;
    this.productId = productId;
    this.paymentStatus = paymentStatus;
  }

  // Getters and Setters
  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
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

  public String getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(String paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  // Utility methods
  public boolean isPending() {
    return "Pending".equalsIgnoreCase(paymentStatus);
  }

  public boolean isCompleted() {
    return "Completed".equalsIgnoreCase(paymentStatus) || "Paid".equalsIgnoreCase(paymentStatus);
  }

  public boolean isCancelled() {
    return "Cancelled".equalsIgnoreCase(paymentStatus);
  }

  public void markAsPaid() {
    this.paymentStatus = "Paid";
  }

  public void markAsCompleted() {
    this.paymentStatus = "Completed";
  }

  public void markAsCancelled() {
    this.paymentStatus = "Cancelled";
  }

  @Override
  public String toString() {
    return "Order{" +
        "orderId=" + orderId +
        ", userId=" + userId +
        ", productId=" + productId +
        ", paymentStatus='" + paymentStatus + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Order order = (Order) o;
    return orderId != null ? orderId.equals(order.orderId) : order.orderId == null;
  }

  @Override
  public int hashCode() {
    return orderId != null ? orderId.hashCode() : 0;
  }
}
