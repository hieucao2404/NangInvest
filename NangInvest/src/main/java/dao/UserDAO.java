/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import java.util.Optional;
import model.User;

/**
 * User-specific DAO extending the generic DAO
 */
public class UserDAO extends GenericDAOImpl<User, Integer> {

    // Custom methods specific to User entity
    public Optional<User> findByUsername(String username) {
        String jpql = "SELECT u FROM User u WHERE u.userName = ?1";
        return findSingleByQuery(jpql, username);
    }

    public Optional<User> findByEmail(String email) {
        String jpql = "SELECT u FROM User u WHERE u.email = ?1";
        return findSingleByQuery(jpql, email);
    }

    public Optional<User> findByGoogleId(String googleId) {
        String jpql = "SELECT u FROM User u WHERE u.googleId = ?1";
        return findSingleByQuery(jpql, googleId);
    }

    public List<User> findByRole(User.Role role) {
        String jpql = "SELECT u FROM User u WHERE u.role = ?1";
        return findByQuery(jpql, role);
    }

    public boolean existsByUsername(String username) {
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.userName = ?1";
        return countByQuery(jpql, username) > 0;
    }

    public boolean existsByEmail(String email) {
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = ?1";
        return countByQuery(jpql, email) > 0;
    }

    public User checkLoginByUsername(String username, String password) {
        String jpql = "SELECT u FROM User u WHERE u.userName = ?1 AND u.password = ?2";
        Optional<User> userOpt = findSingleByQuery(jpql, username, password);
        return userOpt.orElse(null);
    }
}
