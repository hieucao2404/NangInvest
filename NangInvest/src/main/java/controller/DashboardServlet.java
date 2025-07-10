/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import ai.AnalyticsPredictor;
import ai.ContentGenerator;
import ai.RecommendationEngine;
import com.google.gson.Gson;
import dao.CourseDAO;
import dao.OrderDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.User;

/**
 *
 * @author Admin
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
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
        //get comrehensive analytics
        Map<String, Object> analytics = analyticsPredictor.getPlatformAnalytics();

        //Get AI insights
        Map<String, Object> aiInsights = new HashMap<>();
        aiInsights.put("trendingTopics", analyticsPredictor.getTrendingTopics());
        aiInsights.put("churnRisk", aiInsights);
        aiInsights.put("coursePopularity", analyticsPredictor.predictCoursePopularity());
        aiInsights.put("marketingTimes", analyticsPredictor.predictOptimalMarketingTimes());

        //get content suggestion
        List<String> blogSuggestions = contentGenerator.generateBlogTopics("General Finance");

        //Prepare dashboard data
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
        //Get personalized recommendations
        Map<String, Object> recommendations = recommendationEngine.getPersonalizedContent(user);
        
        //Get user-specific analytics
        Map<String, Object> userAnalytics = new HashMap<>();
        userAnalytics.put("userInterests", analyticsPredictor.predictUserInterests(user.getUserId()));
        userAnalytics.put("orderHistory", orderDAO.findByUserId(user.getUserId()));
        userAnalytics.put("recommendedCourses", recommendations.get("recommendedCourses"));
        userAnalytics.put("recommendedBooks", recommendations.get("recommededBooks"));
        
        //Prepare dashboead data
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
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if(user == null){
            response.sendRedirect(request.getContextPath() + "/public/login.jsp");
            return;
        }
        
        String path = request.getServletPath();
        
        if(path.contains("/admin/") && user.getRole() == User.Role.ADMIN){
            handleAdminDashboard(request, response, user);
        }else if(path.contains("/user/") || path.equals("/dashboard")){
            handleUserDashboard(request, response, user);
        }else{
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Access denied");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
