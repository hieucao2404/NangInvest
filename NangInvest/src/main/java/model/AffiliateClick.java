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
 * JPA Entity for AffiliateClick table
 * 
 * @author Admin
 */
@Entity
@Table(name = "AffiliateClick")
public class AffiliateClick {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ClickID")
  private Integer clickId;

  @Column(name = "BookID", nullable = false)
  private Integer bookId;

  @Column(name = "UserID")
  private Integer userId;

  @Column(name = "ClickTime", columnDefinition = "DATETIME DEFAULT GETDATE()")
  private LocalDateTime clickTime;

  // JPA relationships (optional)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BookID", insertable = false, updatable = false)
  private Book book;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "UserID", insertable = false, updatable = false)
  private User user;

  // Constructors
  public AffiliateClick() {
    this.clickTime = LocalDateTime.now();
  }

  public AffiliateClick(Integer bookId) {
    this.bookId = bookId;
    this.clickTime = LocalDateTime.now();
  }

  public AffiliateClick(Integer bookId, Integer userId) {
    this.bookId = bookId;
    this.userId = userId;
    this.clickTime = LocalDateTime.now();
  }

  // Getters and Setters
  public Integer getClickId() {
    return clickId;
  }

  public void setClickId(Integer clickId) {
    this.clickId = clickId;
  }

  public Integer getBookId() {
    return bookId;
  }

  public void setBookId(Integer bookId) {
    this.bookId = bookId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public LocalDateTime getClickTime() {
    return clickTime;
  }

  public void setClickTime(LocalDateTime clickTime) {
    this.clickTime = clickTime;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  // Utility methods
  public boolean isAnonymousClick() {
    return userId == null;
  }

  @Override
  public String toString() {
    return "AffiliateClick{" +
        "clickId=" + clickId +
        ", bookId=" + bookId +
        ", userId=" + userId +
        ", clickTime=" + clickTime +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    AffiliateClick that = (AffiliateClick) o;
    return clickId != null ? clickId.equals(that.clickId) : that.clickId == null;
  }

  @Override
  public int hashCode() {
    return clickId != null ? clickId.hashCode() : 0;
  }
}
