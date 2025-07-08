package model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Course entity representing the Courses table in the database
 * Based on schema analysis:
 * - CourseID (int, NOT NULL, Primary Key, Auto-increment)
 * - CourseName (varchar, NOT NULL)
 * - Price (decimal, Nullable, Default: 0.00)
 * - Time (varchar, Nullable)
 * - IsFree (bit, Nullable, Default: 0)
 * - ImageUrl (varchar, Nullable)
 */
@Entity
@Table(name = "Courses")
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CourseID")
  private Integer courseId;

  @Column(name = "CourseName", nullable = false)
  private String courseName;

  @Column(name = "Price")
  private BigDecimal price;

  @Column(name = "Time")
  private String time;

  @Column(name = "IsFree")
  private Boolean isFree;

  @Column(name = "ImageUrl")
  private String imageUrl;

  // Default constructor
  public Course() {
  }

  // Constructor with required fields
  public Course(String courseName) {
    this.courseName = courseName;
    this.price = BigDecimal.ZERO;
    this.isFree = false;
  }

  // Constructor with all fields
  public Course(String courseName, BigDecimal price, String time, Boolean isFree, String imageUrl) {
    this.courseName = courseName;
    this.price = price != null ? price : BigDecimal.ZERO;
    this.time = time;
    this.isFree = isFree != null ? isFree : false;
    this.imageUrl = imageUrl;
  }

  // Getters and Setters
  public Integer getCourseId() {
    return courseId;
  }

  public void setCourseId(Integer courseId) {
    this.courseId = courseId;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public Boolean getIsFree() {
    return isFree;
  }

  public void setIsFree(Boolean isFree) {
    this.isFree = isFree;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  // Convenience methods
  public boolean isFreeOfCharge() {
    return Boolean.TRUE.equals(isFree);
  }

  public boolean hasPaidContent() {
    return !Boolean.TRUE.equals(isFree);
  }

  public BigDecimal getEffectivePrice() {
    if (Boolean.TRUE.equals(isFree)) {
      return BigDecimal.ZERO;
    }
    return price != null ? price : BigDecimal.ZERO;
  }

  // toString method
  @Override
  public String toString() {
    return "Course{" +
        "courseId=" + courseId +
        ", courseName='" + courseName + '\'' +
        ", price=" + price +
        ", time='" + time + '\'' +
        ", isFree=" + isFree +
        ", imageUrl='" + imageUrl + '\'' +
        '}';
  }

  // equals and hashCode
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Course course = (Course) o;

    return courseId != null ? courseId.equals(course.courseId) : course.courseId == null;
  }

  @Override
  public int hashCode() {
    return courseId != null ? courseId.hashCode() : 0;
  }
}
