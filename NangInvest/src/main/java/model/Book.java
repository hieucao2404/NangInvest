package model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Book entity representing the Books table in the database
 * Schema: BookID, BookName, Topic, AffiliateLink, IsPreviewAvailable,
 * CoverImage, Rating, PreviewContent
 */
@Entity
@Table(name = "Books")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BookID")
  private Integer bookId;

  @Column(name = "BookName", nullable = false)
  private String bookName;

  @Column(name = "Topic")
  private String topic;

  @Column(name = "AffiliateLink")
  private String affiliateLink;

  @Column(name = "IsPreviewAvailable")
  private Boolean isPreviewAvailable;

  @Column(name = "CoverImage")
  private String coverImage;

  @Column(name = "Rating")
  private BigDecimal rating;

  @Column(name = "PreviewContent")
  private String previewContent;

  // Default constructor
  public Book() {
  }

  // Constructor with required fields
  public Book(String bookName) {
    this.bookName = bookName;
    this.isPreviewAvailable = false;
    this.rating = BigDecimal.ZERO;
  }

  // Getters and Setters
  public Integer getBookId() {
    return bookId;
  }

  public void setBookId(Integer bookId) {
    this.bookId = bookId;
  }

  public String getBookName() {
    return bookName;
  }

  public void setBookName(String bookName) {
    this.bookName = bookName;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getAffiliateLink() {
    return affiliateLink;
  }

  public void setAffiliateLink(String affiliateLink) {
    this.affiliateLink = affiliateLink;
  }

  public Boolean getIsPreviewAvailable() {
    return isPreviewAvailable;
  }

  public void setIsPreviewAvailable(Boolean isPreviewAvailable) {
    this.isPreviewAvailable = isPreviewAvailable;
  }

  public String getCoverImage() {
    return coverImage;
  }

  public void setCoverImage(String coverImage) {
    this.coverImage = coverImage;
  }

  public BigDecimal getRating() {
    return rating;
  }

  public void setRating(BigDecimal rating) {
    this.rating = rating;
  }

  public String getPreviewContent() {
    return previewContent;
  }

  public void setPreviewContent(String previewContent) {
    this.previewContent = previewContent;
  }

  // Convenience methods
  public boolean hasPreview() {
    return Boolean.TRUE.equals(isPreviewAvailable) && previewContent != null && !previewContent.trim().isEmpty();
  }

  public boolean hasAffiliateLink() {
    return affiliateLink != null && !affiliateLink.trim().isEmpty();
  }

  public boolean hasCoverImage() {
    return coverImage != null && !coverImage.trim().isEmpty();
  }

  public String getFormattedRating() {
    if (rating == null)
      return "0.0";
    return rating.toString();
  }

  // toString method
  @Override
  public String toString() {
    return "Book{" +
        "bookId=" + bookId +
        ", bookName='" + bookName + '\'' +
        ", topic='" + topic + '\'' +
        ", rating=" + rating +
        ", isPreviewAvailable=" + isPreviewAvailable +
        '}';
  }

  // equals and hashCode
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Book book = (Book) o;
    return bookId != null ? bookId.equals(book.bookId) : book.bookId == null;
  }

  @Override
  public int hashCode() {
    return bookId != null ? bookId.hashCode() : 0;
  }
}
