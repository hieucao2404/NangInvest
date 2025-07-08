package model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * JPA Entity for UserCourses table (Many-to-Many relationship table)
 * 
 * @author Admin
 */
@Entity
@Table(name = "UserCourses")
@IdClass(UserCourses.UserCoursesId.class)
public class UserCourses {

  @Id
  @Column(name = "UserID")
  private Integer userId;

  @Id
  @Column(name = "CourseID")
  private Integer courseId;

  // JPA relationships
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "UserID", insertable = false, updatable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CourseID", insertable = false, updatable = false)
  private Course course;

  // Constructors
  public UserCourses() {
  }

  public UserCourses(Integer userId, Integer courseId) {
    this.userId = userId;
    this.courseId = courseId;
  }

  // Getters and Setters
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getCourseId() {
    return courseId;
  }

  public void setCourseId(Integer courseId) {
    this.courseId = courseId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  @Override
  public String toString() {
    return "UserCourses{" +
        "userId=" + userId +
        ", courseId=" + courseId +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    UserCourses that = (UserCourses) o;

    if (userId != null ? !userId.equals(that.userId) : that.userId != null)
      return false;
    return courseId != null ? courseId.equals(that.courseId) : that.courseId == null;
  }

  @Override
  public int hashCode() {
    int result = userId != null ? userId.hashCode() : 0;
    result = 31 * result + (courseId != null ? courseId.hashCode() : 0);
    return result;
  }

  /**
   * Composite primary key class for UserCourses
   */
  public static class UserCoursesId implements Serializable {
    private Integer userId;
    private Integer courseId;

    public UserCoursesId() {
    }

    public UserCoursesId(Integer userId, Integer courseId) {
      this.userId = userId;
      this.courseId = courseId;
    }

    public Integer getUserId() {
      return userId;
    }

    public void setUserId(Integer userId) {
      this.userId = userId;
    }

    public Integer getCourseId() {
      return courseId;
    }

    public void setCourseId(Integer courseId) {
      this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;

      UserCoursesId that = (UserCoursesId) o;

      if (userId != null ? !userId.equals(that.userId) : that.userId != null)
        return false;
      return courseId != null ? courseId.equals(that.courseId) : that.courseId == null;
    }

    @Override
    public int hashCode() {
      int result = userId != null ? userId.hashCode() : 0;
      result = 31 * result + (courseId != null ? courseId.hashCode() : 0);
      return result;
    }
  }
}