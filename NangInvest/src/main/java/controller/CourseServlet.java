/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CourseDAO;
import dao.UserCoursesDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import model.User;

/**
 *
 * @author Admin
 */
@WebServlet(name = "CourseServlet", urlPatterns = {"/course"})
public class CourseServlet extends HttpServlet {

   private CourseDAO courseDAO = new CourseDAO();
    private UserCoursesDAO userCoursesDAO = new UserCoursesDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseIdStr = request.getParameter("courseId");
        try {
            int courseId = Integer.parseInt(courseIdStr);
            Course course = courseDAO.findById(courseId).orElse(null);
            if (course == null) {
                request.setAttribute("error", "Course not found");
                request.getRequestDispatcher("/error/error.jsp").forward(request, response);
                return;
            }
            User user = (User) request.getSession().getAttribute("user");
            if (user != null && userCoursesDAO.isUserEnrolledInCourse(user.getUserId(), courseId)) {
                request.setAttribute("course", course);
                request.setAttribute("progress", userCoursesDAO.findByUserAndCourse(user.getUserId(), courseId).getProgress());
                request.setAttribute("lessons", courseDAO.getLessonsByCourseId(courseId));
                request.getRequestDispatcher("/user/course.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/courses");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid course ID");
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
        }
    }
}
