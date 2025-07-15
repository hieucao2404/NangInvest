/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.List;

import dao.CartDAO;
import dao.CourseDAO;
import dao.OrderDAO;
import dao.UserCoursesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.Course;
import model.User;

/**
 * Servlet for handling course-related operations
 * Handles course browsing, filtering, and cart operations
 * 
 * @author Admin
 */
@WebServlet(name = "CoursesServlet", urlPatterns = { "/courses" })
public class CoursesServlet extends HttpServlet {

    private CourseDAO courseDAO;
    private CartDAO cartDAO;
    private UserCoursesDAO userCoursesDAO;
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        courseDAO = new CourseDAO();
        cartDAO = new CartDAO();
        userCoursesDAO = new UserCoursesDAO();
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "browse";
        }

        switch (action) {
            case "browse":
                handleBrowseCourses(request, response);
                break;
            case "search":
                handleSearchCourses(request, response);
                break;
            case "filter":
                handleFilterCourses(request, response);
                break;
            default:
                handleBrowseCourses(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/courses");
            return;
        }

        switch (action) {
            case "addToCart":
                handleAddToCart(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/courses");
                break;
        }
    }

    /**
     * Handle course browsing with optional filtering
     */
    private void handleBrowseCourses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String filter = request.getParameter("filter");
            String priceRange = request.getParameter("priceRange");
            String sortBy = request.getParameter("sortBy");

            List<Course> courses;

            // Apply filters
            if ("free".equals(filter)) {
                courses = courseDAO.findFreeCourses();
            } else if ("paid".equals(filter)) {
                courses = courseDAO.findPaidCourses();
            } else {
                courses = courseDAO.findAll();
            }

            // Apply price range filter if specified
            if (priceRange != null && !priceRange.isEmpty()) {
                // Price range filtering logic can be added here
                // For now, we'll use the existing courses list
            }

            // Get user enrollment status for each course
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null) {
                // Mark which courses the user is already enrolled in
                for (Course course : courses) {
                    boolean isEnrolled = userCoursesDAO.isUserEnrolledInCourse(user.getUserId(), course.getCourseId());
                    boolean hasPurchased = orderDAO.hasUserPurchasedProduct(user.getUserId(), course.getCourseId());

                    // Add these as request attributes for individual courses
                    // We'll handle this in the JSP by checking each course
                }

                // Get cart information
                List<Cart> cartItems = cartDAO.findByUserId(user.getUserId());
                long cartCount = cartDAO.getCartItemCountByUserId(user.getUserId());

                request.setAttribute("cartItems", cartItems);
                request.setAttribute("cartCount", cartCount);
            }

            // Get course statistics
            long totalCourses = courseDAO.count();
            long freeCourses = courseDAO.countFreeCourses();
            long paidCourses = courseDAO.countPaidCourses();

            request.setAttribute("courses", courses);
            request.setAttribute("totalCourses", totalCourses);
            request.setAttribute("freeCourses", freeCourses);
            request.setAttribute("paidCourses", paidCourses);
            request.setAttribute("currentFilter", filter);
            request.setAttribute("currentSort", sortBy);

            request.getRequestDispatcher("/user/courses.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading courses: " + e.getMessage());
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
        }
    }

    /**
     * Handle course search
     */
    private void handleSearchCourses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String searchQuery = request.getParameter("query");

            List<Course> courses;
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                courses = courseDAO.searchCoursesByName(searchQuery.trim());
            } else {
                courses = courseDAO.findAll();
            }

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null) {
                List<Cart> cartItems = cartDAO.findByUserId(user.getUserId());
                long cartCount = cartDAO.getCartItemCountByUserId(user.getUserId());

                request.setAttribute("cartItems", cartItems);
                request.setAttribute("cartCount", cartCount);
            }

            request.setAttribute("courses", courses);
            request.setAttribute("searchQuery", searchQuery);
            request.setAttribute("isSearch", true);

            request.getRequestDispatcher("/user/courses.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error searching courses: " + e.getMessage());
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
        }
    }

    /**
     * Handle adding course to cart
     */
    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/public/login-registers.jsp?redirect=/courses");
            return;
        }

        try {
            String courseIdParam = request.getParameter("courseId");
            if (courseIdParam == null || courseIdParam.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/courses?error=invalid_course");
                return;
            }

            Integer courseId = Integer.parseInt(courseIdParam);

            // Check if course exists
            Course course = courseDAO.findById(courseId).orElse(null);
            if (course == null) {
                response.sendRedirect(request.getContextPath() + "/courses?error=course_not_found");
                return;
            }

            // Check if user already enrolled
            boolean isEnrolled = userCoursesDAO.isUserEnrolledInCourse(user.getUserId(), courseId);
            if (isEnrolled) {
                response.sendRedirect(request.getContextPath() + "/courses?error=already_enrolled");
                return;
            }

            // Check if user already purchased
            boolean hasPurchased = orderDAO.hasUserPurchasedProduct(user.getUserId(), courseId);
            if (hasPurchased) {
                response.sendRedirect(request.getContextPath() + "/courses?error=already_purchased");
                return;
            }

            // Check if course is free - if free, enroll directly
            if (course.isFreeOfCharge()) {
                userCoursesDAO.enrollUserInCourse(user.getUserId(), courseId);
                response.sendRedirect(request.getContextPath() + "/user/myCourses.jsp?success=enrolled");
                return;
            }

            // Check if already in cart
            boolean inCart = cartDAO.existsByUserIdAndProductId(user.getUserId(), courseId);
            if (inCart) {
                response.sendRedirect(request.getContextPath() + "/courses?error=already_in_cart");
                return;
            }

            // Add to cart
            cartDAO.addOrUpdateCartItem(user.getUserId(), courseId, 1);
            response.sendRedirect(request.getContextPath() + "/courses?success=added_to_cart");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/courses?error=invalid_course_id");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/courses?error=add_to_cart_failed");
        }
    }

    /**
     * Handle course filtering
     */
    private void handleFilterCourses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Redirect to browse with filter parameters
        String filter = request.getParameter("type");
        String priceRange = request.getParameter("priceRange");
        String sortBy = request.getParameter("sortBy");

        StringBuilder redirectUrl = new StringBuilder(request.getContextPath() + "/courses?");

        if (filter != null && !filter.isEmpty()) {
            redirectUrl.append("filter=").append(filter).append("&");
        }
        if (priceRange != null && !priceRange.isEmpty()) {
            redirectUrl.append("priceRange=").append(priceRange).append("&");
        }
        if (sortBy != null && !sortBy.isEmpty()) {
            redirectUrl.append("sortBy=").append(sortBy);
        }

        response.sendRedirect(redirectUrl.toString());
    }
}
