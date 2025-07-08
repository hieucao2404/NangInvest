package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA Entity for Blog table
 * 
 * @author Admin
 */
@Entity
@Table(name = "Blog")
public class Blog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BlogID")
  private Integer blogId;

  @Column(name = "BlogName", nullable = false)
  private String blogName;

  @Column(name = "Topic")
  private String topic;

  @Column(name = "ImageUrl")
  private String imageUrl;

  @Column(name = "DetailedContent", columnDefinition = "TEXT")
  private String detailedContent;

  // Constructors
  public Blog() {
  }

  public Blog(String blogName) {
    this.blogName = blogName;
  }

  public Blog(String blogName, String topic, String imageUrl, String detailedContent) {
    this.blogName = blogName;
    this.topic = topic;
    this.imageUrl = imageUrl;
    this.detailedContent = detailedContent;
  }

  // Getters and Setters
  public Integer getBlogId() {
    return blogId;
  }

  public void setBlogId(Integer blogId) {
    this.blogId = blogId;
  }

  public String getBlogName() {
    return blogName;
  }

  public void setBlogName(String blogName) {
    this.blogName = blogName;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getDetailedContent() {
    return detailedContent;
  }

  public void setDetailedContent(String detailedContent) {
    this.detailedContent = detailedContent;
  }

  // Utility methods
  public String getPreviewContent(int length) {
    if (detailedContent == null)
      return null;
    return detailedContent.length() > length ? detailedContent.substring(0, length) + "..." : detailedContent;
  }

  public boolean hasImage() {
    return imageUrl != null && !imageUrl.trim().isEmpty();
  }

  @Override
  public String toString() {
    return "Blog{" +
        "blogId=" + blogId +
        ", blogName='" + blogName + '\'' +
        ", topic='" + topic + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        ", detailedContent='"
        + (detailedContent != null ? detailedContent.substring(0, Math.min(50, detailedContent.length())) + "..."
            : null)
        + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Blog blog = (Blog) o;
    return blogId != null ? blogId.equals(blog.blogId) : blog.blogId == null;
  }

  @Override
  public int hashCode() {
    return blogId != null ? blogId.hashCode() : 0;
  }
}
