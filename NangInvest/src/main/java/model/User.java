package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a user in the NangInvest system.
 * Stores user information such as username, email, password, role, and more.
 */
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "UserID")
  private int userId;

  @Column(name = "Username", unique = true, nullable = false, length = 50)
  private String userName;

  @Column(name = "email", unique = true, nullable = false, length = 100)
  private String email;

  @Column(name = "password", length = 255)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false, length = 20)
  private Role role = Role.USER;

  @Column(name = "GoogleID", unique = true, length = 100)
  private String googleId;

  @Column(name = "Age")
  private Integer age;

  @Column(name = "Name", length = 100)
  private String name;

  @Column(name = "Expertise", length = 500)
  private String expertise;

  public enum Role {
    USER("user"), ADMIN("admin"), PUBLIC("public"), AFFILIATE("affiliate");

    private final String dbValue;

    Role(String dbValue) {
      this.dbValue = dbValue;
    }

    public String getDbValue() {
      return dbValue;
    }
  }

  public User() {
  }

  public User(int userId, String userName, String email, String password, Role role, String googleId, Integer age,
      String name, String expertise) {
    this.userId = userId;
    this.userName = userName;
    this.email = email;
    this.password = password;
    this.role = role;
    this.googleId = googleId;
    this.age = age;
    this.name = name;
    this.expertise = expertise;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public String getGoogleId() {
    return googleId;
  }

  public void setGoogleId(String googleId) {
    this.googleId = googleId;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getExpertise() {
    return expertise;
  }

  public void setExpertise(String expertise) {
    this.expertise = expertise;
  }

  @Override
  public String toString() {
    return "User [userId=" + userId + ", userName=" + userName + ", email=" + email + ", password=" + password
        + ", role=" + role + ", googleId=" + googleId + ", age=" + age + ", name=" + name + ", expertise=" + expertise
        + "]";
  }

  // default is user
  public String getRoleDbValue() {
    return role != null ? role.getDbValue() : "user";
  }

}
