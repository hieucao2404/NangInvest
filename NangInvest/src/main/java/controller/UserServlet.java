/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import model.User;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UserServlet", urlPatterns = { "/users", "/users/*" })
public class UserServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                listUsers(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                listUsers(request, response);
                break;
            case "add":
                addUser(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            case "edit":
                editUser(request, response);
                break;
            case "register":
                registerUser(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // Show user list
    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> users = userDAO.findAll();
        request.setAttribute("userList", users);
        request.getRequestDispatcher("admin/manageUsers.jsp").forward(request, response);
    }

    // Show add user form
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("admin/userForm.jsp").forward(request, response);
    }

    // Show edit user form
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Optional<User> userOpt = userDAO.findById(id);
        if (userOpt.isPresent()) {
            request.setAttribute("user", userOpt.get());
            request.getRequestDispatcher("admin/userForm.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }

    // Add user
    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User user = extractUserFromRequest(request);
        userDAO.save(user);
        response.sendRedirect(request.getContextPath() + "/users?action=list");
    }

    // Update user
    private void editUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User user = extractUserFromRequest(request);
        user.setUserId(Integer.parseInt(request.getParameter("id")));
        userDAO.update(user);
        response.sendRedirect(request.getContextPath() + "/users?action=list");
    }

    // Delete user
    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deleteById(id);
        response.sendRedirect(request.getContextPath() + "/users?action=list");
    }

    // Helper to extract user from request
    private User extractUserFromRequest(HttpServletRequest request) {
        User user = new User();
        user.setUserName(request.getParameter("username"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        user.setRole(User.Role.USER);
        user.setGoogleId(null);
        user.setAge(Integer.parseInt(request.getParameter("age")));
        user.setName(request.getParameter("name"));
        user.setExpertise(request.getParameter("expertise"));
        return user;
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = extractUserFromRequest(request);
        // check for duplicate
        if (userDAO.existsByUsername(user.getUserName()) || userDAO.existsByEmail(user.getEmail())) {
            request.setAttribute("error", "Username or email already in use");
            request.getRequestDispatcher("/public/login.jsp").forward(request, response);
        }
        userDAO.save(user);

        request.setAttribute("success", "Signup successful! Please login.");
        request.getRequestDispatcher("/public/login.jsp").forward(request, response);
    }
}
