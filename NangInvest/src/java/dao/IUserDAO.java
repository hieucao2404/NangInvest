
package dao;

import java.util.List;

import model.User;

public interface IUserDAO {
  void addUser(User user);

  User findByUsername(String username);

  User findById(int id);

  void updateUser(User user);

  void deleteUser(int id);

  List<User> getAllUsers();
}