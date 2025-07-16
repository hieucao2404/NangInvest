/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
 * Servlet for handling course-related operations Handles course browsing,
 * filtering, and cart operations
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
            case "add":
                handleAddCourse(request, response);
                break;
            case "edit":
                handleEditCourse(request, response);
                break;
            case "browse":
                handleBrowseCourses(request, response);
                break;
            case "search":
                handleSearchCourses(request, response);
                break;
            case "filter":
                handleFilterCourses(request, response);
                break;
            case "delete":
                handleDeleteCourse(request, response);
                break;
            case "adminManage":
                handleAdminManageCourses(request, response);
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
            case "saveCourse":
                handleSaveCourse(request, response);
                break;
            case "updateCourse":
                handleUpdateCourse(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/courses");
                break;
        }
    }

    /**
     * Handle deleting a course by ID and redirect to adminManage
     */
   private void handleDeleteCourse(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
    String courseIdStr = request.getParameter("courseId");
    String redirectUrl = request.getContextPath() + "/courses?action=adminManage";

    try {
        if (courseIdStr == null || courseIdStr.trim().isEmpty()) {
            log("Delete course failed: Course ID is missing or empty");
            request.getSession().setAttribute("error", "Course ID is missing or empty");
        } else {
            int courseId = Integer.parseInt(courseIdStr);
            log("Attempting to delete course with ID: " + courseId);
            // Clean up related data
            cartDAO.removeCartItemsByProductId(courseId);
          //  userCoursesDAO.removeEnrollmentsByCourseId(courseId);
            boolean deleted = courseDAO.deleteCourseById(courseId);
            if (deleted) {
                log("Course ID " + courseId + " deleted successfully");
                request.getSession().setAttribute("success", "Course deleted successfully");
            } else {
                log("Failed to delete course ID " + courseId + ": Course not found or already deleted");
                request.getSession().setAttribute("error", "Failed to delete course: Course not found or already deleted");
            }
        }
    } catch (NumberFormatException e) {
        log("Delete course failed: Invalid course ID format - " + courseIdStr, e);
        request.getSession().setAttribute("error", "Invalid course ID format");
    } catch (Exception e) {
        log("Error deleting course: " + e.getMessage(), e);
        request.getSession().setAttribute("error", "Error deleting course: " + e.getMessage());
    }
    log("Redirecting to: " + redirectUrl);
    response.sendRedirect(redirectUrl);
}

    private void handleEditCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String courseIdStr = request.getParameter("courseId");
        try {
            int courseId = Integer.parseInt(courseIdStr);
            Optional<Course> courseOptional = courseDAO.findById(courseId);
            if (!courseOptional.isPresent()) {
                request.setAttribute("error", "Course not found");
                request.getRequestDispatcher("/admin/manageCourses.jsp").forward(request, response);
                return;
            }
            request.setAttribute("course", courseOptional.get());
            request.getRequestDispatcher("/admin/forms/courseForm.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid course ID");
            request.getRequestDispatcher("/admin/manageCourses.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error fetching course");
            request.getRequestDispatcher("/admin/manageCourses.jsp").forward(request, response);
        }
    }

    private void handleUpdateCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String courseIdStr = request.getParameter("courseId");
        String courseName = request.getParameter("courseName");
        String priceStr = request.getParameter("price");
        String time = request.getParameter("time");
        String isFreeStr = request.getParameter("isFree");
        String imageUrl = request.getParameter("imageUrl");

        // Validate inputs
        if (courseIdStr == null || courseName == null || courseName.trim().isEmpty()) {
            request.setAttribute("error", "Course ID and name are required");
            request.getRequestDispatcher("/admin/forms/courseForm.jsp").forward(request, response);
            return;
        }

        try {
            int courseId = Integer.parseInt(courseIdStr);
            BigDecimal price;
            try {
                price = (priceStr != null && !priceStr.isEmpty()) ? new BigDecimal(priceStr) : BigDecimal.ZERO;
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid price format");
                request.getRequestDispatcher("/admin/forms/courseForm.jsp").forward(request, response);
                return;
            }
            Boolean isFree = (isFreeStr != null) ? Boolean.valueOf(isFreeStr) : false;

            // Create Course object using all fields constructor
            Course course = new Course(courseName, price, time, isFree, imageUrl);
            course.setCourseId(courseId); // Set ID for update

            // Update in DB
            courseDAO.save(course); // Uses JPA merge for existing entities
            request.setAttribute("success", "Course updated successfully");
            request.setAttribute("course", courseDAO.findById(courseId).orElse(course));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid course ID");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to update course: " + e.getMessage());
        }

        request.getRequestDispatcher("/admin/forms/courseForm.jsp").forward(request, response);
    }

    private void handleSaveCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String courseName = request.getParameter("courseName");
        String priceStr = request.getParameter("price");
        String time = request.getParameter("time");
        String isFreeStr = request.getParameter("isFree");
        String imageUrl = request.getParameter("imageUrl");

        // Validate inputs
        if (courseName == null || courseName.trim().isEmpty()) {
            request.setAttribute("error", "Course name is required");
            request.getRequestDispatcher("/admin/forms/courseForm.jsp").forward(request, response);
            return;
        }

        // Parse and validate
        BigDecimal price;
        try {
            price = (priceStr != null && !priceStr.isEmpty()) ? new BigDecimal(priceStr) : BigDecimal.ZERO;
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid price format");
            request.getRequestDispatcher("/admin/forms/courseForm.jsp").forward(request, response);
            return;
        }
        Boolean isFree = (isFreeStr != null) ? Boolean.valueOf(isFreeStr) : false;

        // Create Course object
        Course course = new Course(courseName, price, time, isFree, imageUrl);

        // Save to DB
        try {
            courseDAO.addCourse(course);
            request.setAttribute("success", "Course added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to add course: " + e.getMessage());
        }

        // Redirect to adminManage to show updated course list
        response.sendRedirect(request.getContextPath() + "/courses?action=adminManage");
    }

    private void handleAddCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the course form for GET requests
        request.getRequestDispatcher("/admin/forms/courseForm.jsp").forward(request, response);
    }

    private void handleAdminManageCourses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Course> courses = courseDAO.findAll();
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("/admin/manageCourses.jsp").forward(request, response);
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
