/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.OrderDAO;
import dao.UserCoursesDAO;
import dao.UserLessonsDAO;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.User;
import model.UserCourses;

/**
 *
 * @author Admin
 */
@WebServlet(name = "CourseProgressServlet", urlPatterns = {"/user/progress"})
public class CourseProgressServlet extends HttpServlet {
    private UserCoursesDAO userCoursesDAO;
    private OrderDAO orderDAO;
    private UserLessonsDAO userLessonsDAO;
    
      @Override
    public void init() throws ServletException {
        userCoursesDAO = new UserCoursesDAO();
        orderDAO = new OrderDAO();
        userLessonsDAO = new UserLessonsDAO();
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
            response.sendRedirect(request.getContextPath() + "/public/login-register.jsp?redirect=/user/progress");
            return;
        }
        
        List<UserCourses> progressList = userCoursesDAO.findByUserId(user.getUserId());
        request.setAttribute("progressList", progressList);
        request.getRequestDispatcher("/user/progress.jsp").forward(request,response);
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            response.sendRedirect(request.getContextPath() + "/public/login-register.jsp?redirect=/user/progress");
            return;
        }
        
        String action = request.getParameter("action");
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        if(!orderDAO.hasUserPurchasedProduct(user.getUserId(), courseId)){
            response.sendRedirect(request.getContextPath() + "/user/progress?error=notPurchased");
            return;
        }
        
        if("completeLesson".equals(action)){
            int lessonId = Integer.parseInt(request.getParameter("lessonId"));
            userLessonsDAO.markLessonCompleted(user.getUserId(), lessonId);
            userCoursesDAO.calculateAndUpdateProgress(user.getUserId(), courseId);
        }else{
            int progress = Integer.parseInt(request.getParameter("progress"));
            userCoursesDAO.updateProgress(user.getUserId(), courseId, progress);
        }
        response.sendRedirect(request.getContextPath() + "/user/progress");
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
