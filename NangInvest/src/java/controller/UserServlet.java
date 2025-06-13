/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import model.User;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UserServlet", urlPatterns = {"/users", "/users/*"})
public class UserServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

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
        if (action == null) action = "list";

        switch (action) {
            case "add":
                addUser(request, response);
                break;
            case "edit":
                updateUser(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // Show user list
    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> users = userDAO.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/views/userList.jsp").forward(request, response);
    }

    // Show add user form
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/userForm.jsp").forward(request, response);
    }

    // Show edit user form
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User user = userDAO.findById(id);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/userForm.jsp").forward(request, response);
    }

    // Add user
    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User user = extractUserFromRequest(request);
        userDAO.addUser(user);
        response.sendRedirect(request.getContextPath() + "/users?action=list");
    }

    // Update user
    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User user = extractUserFromRequest(request);
        user.setUserId(Integer.parseInt(request.getParameter("id")));
        userDAO.updateUser(user);
        response.sendRedirect(request.getContextPath() + "/users?action=list");
    }

    // Delete user
    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deleteUser(id);
        response.sendRedirect(request.getContextPath() + "/users?action=list");
    }

    // Helper to extract user from request
    private User extractUserFromRequest(HttpServletRequest request) {
        User user = new User();
        user.setUserName(request.getParameter("username"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        user.setRole(User.Role.valueOf(request.getParameter("role").toUpperCase()));
        user.setGoogleId(request.getParameter("googleId"));
        user.setAge(Integer.parseInt(request.getParameter("age")));
        user.setName(request.getParameter("name"));
        user.setExpertise(request.getParameter("expertise"));
        return user;
    }
}
