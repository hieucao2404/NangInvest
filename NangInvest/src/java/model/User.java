/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model;

/**
 * Represents a user in the NangInvest system.
 * Stores user information such as username, email, password, role, and more.
 */

/**
 *
 * @author Admin
 */
public class User {
  private int userId;
  private String userName;
  private String email;
  private String password;
  private Role role;
  private String googleId;
  private int age;
  private String name;
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
  
    

  public User(int userId, String userName, String email, String password, Role role, String googleId, int age,
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

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
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
