/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.User;
import util.DatabaseUtil;

/**
 *
 * @author Admin DAO implementation for the User entity. Provides CRUP
 * operations for User objects in the database
 * Extends{@link AbstractDAO for database connection managment} implements
 * {Iuserdao} to define user-specific data access met
 *
 * Method including adding, finding, updating, deleting, and listing Uses
 * prepared statements to prent SQL injection and ensure efficiency
 *
 */
public class UserDAO implements IUserDAO {

    private static final String ADD_USER = "INSERT INTO Users (Username, Email, Password, Role, GoogleID, Age, Name, Expertise) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM Users WHERE Username = ?";
    private static final String LIST_ALL_USER = "SELECT * FROM Users";
    private static final String DELETE_USER_BY_ID = "DELETE FROM Users WHERE UserID = ?";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM Users WHERE UserID = ?";
    private static final String UPDATE_USER = "UPDATE Users SET Username = ?, Email = ?, Password = ?, Role = ?, GoogleID = ?, Age = ?, Name = ?, Expertise = ? WHERE UserID = ?";

    private static final String CHECK_LOGIN_BY_USERNAME_AND_PASSWORD = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
    private static final String CHECK_LOGIN_BY_EMAIL = "SELECT * FROM Users WHERE Email = ?";
    private static final String CHECK_LOGIN_BY_GOOGLE_ID = "SELECT * FROM Users WHERE GoogleID = ?";
    private static final String UPDATE_GOOGLE_ID = "UPDATE Users SET GoogleID = ? WHERE Email = ?";

    @Override
    public void addUser(User user) {
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stm = conn.prepareStatement(ADD_USER)) {
            stm.setString(1, user.getUserName());
            stm.setString(2, user.getEmail());
            stm.setString(3, user.getPassword());
            stm.setString(4, user.getRoleDbValue());
            stm.setString(5, user.getGoogleId());
            stm.setInt(6, user.getAge());
            stm.setString(7, user.getName());
            stm.setString(8, user.getExpertise());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findByUsername(String username) {
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stm = conn.prepareStatement(SELECT_USER_BY_USERNAME)) {
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateUser(User user) {
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stm = conn.prepareStatement(UPDATE_USER)) {
            stm.setString(1, user.getUserName());
            stm.setString(2, user.getEmail());
            stm.setString(3, user.getPassword());
            stm.setString(4, user.getRoleDbValue());
            stm.setString(5, user.getGoogleId());
            stm.setInt(6, user.getAge());
            stm.setString(7, user.getName());
            stm.setString(8, user.getExpertise());
            stm.setInt(9, user.getUserId());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(int id) {
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stm = conn.prepareStatement(DELETE_USER_BY_ID)) {
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stm = conn.prepareStatement(LIST_ALL_USER); ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Helper method to map ResultSet to User object
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("UserID"));
        user.setUserName(rs.getString("Username"));
        user.setPassword(rs.getString("Password"));
        user.setEmail(rs.getString("Email"));
        String roleStr = rs.getString("Role");
        user.setRole(User.Role.valueOf(roleStr.toUpperCase()));
        user.setGoogleId(rs.getString("GoogleID"));
        user.setAge(rs.getInt("Age"));
        user.setName(rs.getString("Name"));
        user.setExpertise(rs.getString("Expertise"));
        return user;
    }

    @Override
    public User findById(int id) {
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stm = conn.prepareStatement(SELECT_USER_BY_ID)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User checkLoginByUsername(String username, String password) {
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stm = conn.prepareStatement(CHECK_LOGIN_BY_USERNAME_AND_PASSWORD)) {
            stm.setString(1, username);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro finding user by username and password: " + e.getMessage());
            e.printStackTrace();;
        }
        return null;
    }

    public User findByEmail(String email) {
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stm = conn.prepareStatement(CHECK_LOGIN_BY_EMAIL)) {
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public User findByGoogleId(String googleId) {
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stm = conn.prepareStatement(CHECK_LOGIN_BY_GOOGLE_ID)) {
            stm.setString(1, googleId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by Google ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void updateGoogleId(String googleId, String email) {
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stm = conn.prepareStatement(UPDATE_GOOGLE_ID)) {
            stm.setString(1, googleId);
            stm.setString(2, email);
            stm.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating Google ID: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        String testUsername = "john_doe";
        String testPassword = "hashed_password_1";
        User user = userDAO.checkLoginByUsername(testUsername, testPassword);
        System.out.println(user != null ? "Found user: " + user.getUserName() : "User not found");
    }

}
