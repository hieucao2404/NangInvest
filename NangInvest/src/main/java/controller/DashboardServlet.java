/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import ai.AnalyticsPredictor;
import ai.ContentGenerator;
import ai.RecommendationEngine;
import dao.CourseDAO;
import dao.OrderDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author Admin
 */
@WebServlet(name = "DashboardServlet", urlPatterns = { "/dashboard" })
public class DashboardServlet extends HttpServlet {

    private final AnalyticsPredictor analyticsPredictor;
    private final RecommendationEngine recommendationEngine;
    private final ContentGenerator contentGenerator;
    private final UserDAO userDAO;
    private final CourseDAO courseDAO;
    private final OrderDAO orderDAO;
    private final Gson gson;

    public DashboardServlet() {
        this.analyticsPredictor = new AnalyticsPredictor();
        this.recommendationEngine = new RecommendationEngine();
        this.contentGenerator = new ContentGenerator();
        this.userDAO = new UserDAO();
        this.courseDAO = new CourseDAO();
        this.orderDAO = new OrderDAO();
        this.gson = new Gson();
    }

    private void handleAdminDashboard(HttpServletRequest request, HttpServletResponse response, User admin)
            throws ServletException, IOException {
        // get comrehensive analytics
        Map<String, Object> analytics = analyticsPredictor.getPlatformAnalytics();

        // Get AI insights
        Map<String, Object> aiInsights = new HashMap<>();
        aiInsights.put("trendingTopics", analyticsPredictor.getTrendingTopics());
        aiInsights.put("churnRisk", aiInsights);
        aiInsights.put("coursePopularity", analyticsPredictor.predictCoursePopularity());
        aiInsights.put("marketingTimes", analyticsPredictor.predictOptimalMarketingTimes());

        // get content suggestion
        List<String> blogSuggestions = contentGenerator.generateBlogTopics("General Finance");

        // Prepare dashboard data
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("analytics", analytics);
        dashboardData.put("aiInsights", aiInsights);
        dashboardData.put("blogSuggestions", blogSuggestions);
        dashboardData.put("admin", admin);

        request.setAttribute("dashboardData", dashboardData);
        request.getRequestDispatcher("/admin/analytics.jsp").forward(request, response);

    }

    private void handleUserDashboard(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Get personalized recommendations
        Map<String, Object> recommendations = recommendationEngine.getPersonalizedContent(user);

        // Defensive: ensure all lists are non-null and all keys used in JSP are present
        List<?> enrolledCourses = (List<?>) recommendations.get("enrolledCourses");
        if (enrolledCourses == null) {
            enrolledCourses = new java.util.ArrayList<>();
            recommendations.put("enrolledCourses", enrolledCourses);
        }
        // Also ensure recommendations always has enrolledCourses
        if (!recommendations.containsKey("enrolledCourses")) {
            recommendations.put("enrolledCourses", new java.util.ArrayList<>());
        }
        List<?> recommendedCourses = (List<?>) recommendations.get("recommendedCourses");
        if (recommendedCourses == null) {
            recommendedCourses = new java.util.ArrayList<>();
            recommendations.put("recommendedCourses", recommendedCourses);
        }
        List<?> recommendedBooks = (List<?>) recommendations.get("recommendedBooks");
        if (recommendedBooks == null) {
            recommendedBooks = new java.util.ArrayList<>();
            recommendations.put("recommendedBooks", recommendedBooks);
        }

        List<?> userInterests = analyticsPredictor.predictUserInterests(user.getUserId());
        if (userInterests == null)
            userInterests = new java.util.ArrayList<>();
        List<?> orderHistory = orderDAO.findByUserId(user.getUserId());
        if (orderHistory == null)
            orderHistory = new java.util.ArrayList<>();

        // Defensive: ensure purchasedBooks and completedCourses are always present
        List<?> purchasedBooks = null;
        if (recommendations.containsKey("purchasedBooks")) {
            purchasedBooks = (List<?>) recommendations.get("purchasedBooks");
        }
        if (purchasedBooks == null)
            purchasedBooks = new java.util.ArrayList<>();

        List<?> completedCourses = null;
        if (recommendations.containsKey("completedCourses")) {
            completedCourses = (List<?>) recommendations.get("completedCourses");
        }
        if (completedCourses == null)
            completedCourses = new java.util.ArrayList<>();

        // Get user-specific analytics
        Map<String, Object> userAnalytics = new HashMap<>();
        userAnalytics.put("userInterests", userInterests);
        userAnalytics.put("orderHistory", orderHistory);
        userAnalytics.put("recommendedCourses", recommendedCourses);
        userAnalytics.put("recommendedBooks", recommendedBooks);
        userAnalytics.put("purchasedBooks", purchasedBooks);
        userAnalytics.put("completedCourses", completedCourses);

        // Prepare dashboard data
        Map<String, Object> dashboardData = new HashMap<>();    
        dashboardData.put("user", user);
        dashboardData.put("recommendations", recommendations);
        dashboardData.put("userAnalytics", userAnalytics);

        request.setAttribute("dashboardData", dashboardData);
        request.getRequestDispatcher("/user/dashboard.jsp").forward(request, response);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DashboardServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DashboardServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/public/login-registers.jsp");
            return;
        }

        String path = request.getServletPath();

        // Role-based routing for /dashboard endpoint
        if (path.equals("/dashboard")) {
            if (user.getRole() == User.Role.ADMIN) {
                handleAdminDashboard(request, response, user);
            } else {
                handleUserDashboard(request, response, user);
            }
        } else if (path.contains("/admin/") && user.getRole() == User.Role.ADMIN) {
            handleAdminDashboard(request, response, user);
        } else if (path.contains("/user/")) {
            handleUserDashboard(request, response, user);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            doGet(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
