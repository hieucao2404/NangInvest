package model;

import java.time.LocalDateTime;

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
 * JPA Entity for user_tokens table
 * 
 * @author Admin
 */
@Entity
@Table(name = "user_tokens")
public class UserToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "UserID", nullable = false)
  private Integer userId;

  @Column(name = "token", nullable = false)
  private String token;

  @Column(name = "expiry_date", nullable = false)
  private LocalDateTime expiryDate;

  // JPA relationships (optional)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "UserID", insertable = false, updatable = false)
  private User user;

  // Constructors
  public UserToken() {
  }

  public UserToken(Integer userId, String token, LocalDateTime expiryDate) {
    this.userId = userId;
    this.token = token;
    this.expiryDate = expiryDate;
  }

  // Getters and Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public LocalDateTime getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDateTime expiryDate) {
    this.expiryDate = expiryDate;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  // Utility methods
  public boolean isExpired() {
    return expiryDate != null && expiryDate.isBefore(LocalDateTime.now());
  }

  public boolean isValid() {
    return token != null && !token.trim().isEmpty() && !isExpired();
  }

  public void extendExpiry(int hours) {
    this.expiryDate = LocalDateTime.now().plusHours(hours);
  }

  @Override
  public String toString() {
    return "UserToken{" +
        "id=" + id +
        ", userId=" + userId +
        ", token='" + (token != null ? token.substring(0, Math.min(10, token.length())) + "..." : null) + '\'' +
        ", expiryDate=" + expiryDate +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    UserToken userToken = (UserToken) o;
    return id != null ? id.equals(userToken.id) : userToken.id == null;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}