package controller;

import dao.UserCoursesDAO;
import dao.CourseDAO;
import model.Course;
import model.UserCourses;
import model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

@WebServlet(name = "MyCoursesServlet", urlPatterns = { "/user/myCourses" })
public class MyCoursesServlet extends HttpServlet {

  private static final Logger LOGGER = Logger.getLogger(MyCoursesServlet.class.getName());
  private static final long serialVersionUID = 1L;
  private static final String CONTENT_TYPE_JSON = "application/json";
  private static final String CHARSET_UTF8 = "UTF-8";
  private static final String PARAM_COURSE_ID = "courseId";
  private static final String PARAM_PROGRESS = "progress";
  private static final String PARAM_STATUS = "status";
  private static final String PARAM_ERROR = "error";

  private transient UserCoursesDAO userCoursesDAO;
  private transient CourseDAO courseDAO;
  private transient Gson gson;

  @Override
  public void init() throws ServletException {
    super.init();
    this.userCoursesDAO = new UserCoursesDAO();
    this.courseDAO = new CourseDAO();
    this.gson = new Gson();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession();
    User user = (User) session.getAttribute("user");

    if (user == null) {
      try {
        response.sendRedirect(request.getContextPath() + "/public/login-registers.jsp");
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Error redirecting to login", e);
        throw e;
      }
      return;
    }

    String action = request.getParameter("action");

    if (action == null) {
      // Forward to JSP page
      try {
        request.getRequestDispatcher("/user/myCourses.jsp").forward(request, response);
      } catch (ServletException | IOException e) {
        LOGGER.log(Level.SEVERE, "Error forwarding to myCourses.jsp", e);
        throw e;
      }
      return;
    }

    try {
      switch (action) {
        case "getCourses":
          handleGetCourses(response, user);
          break;
        case "getProgress":
          handleGetProgress(request, response, user);
          break;
        case "updateProgress":
          handleUpdateProgress(request, response, user);
          break;
        default:
          response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
      }
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error handling GET request", e);
      throw e;
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession();
    User user = (User) session.getAttribute("user");

    if (user == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String action = request.getParameter("action");

    if (action == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter required");
      return;
    }

    switch (action) {
      case "updateProgress":
        handleUpdateProgress(request, response, user);
        break;
      case "completeCourse":
        handleCompleteCourse(request, response, user);
        break;
      default:
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
    }
  }

  private void handleGetCourses(HttpServletResponse response, User user)
      throws IOException {

    try {
      List<UserCourses> userCourses = userCoursesDAO.findByUserId(user.getUserId());
      List<CourseWithProgress> coursesWithProgress = new ArrayList<>();

      for (UserCourses uc : userCourses) {
        Course course = courseDAO.findById(uc.getCourseId()).orElse(null);
        if (course != null) {
          CourseWithProgress cwp = new CourseWithProgress();
          cwp.id = Long.valueOf(course.getCourseId());
          cwp.title = course.getCourseName();
          cwp.description = ""; // Add description field to Course model if needed
          cwp.price = course.getPrice().doubleValue();
          cwp.imageUrl = course.getImageUrl();
          cwp.progress = uc.getProgress();
          cwp.status = determineStatus(uc);
          cwp.enrolledDate = uc.getEnrollmentDate();
          cwp.completedDate = uc.getCompletionDate();

          coursesWithProgress.add(cwp);
        }
      }

      response.setContentType(CONTENT_TYPE_JSON);
      response.setCharacterEncoding(CHARSET_UTF8);

      PrintWriter out = response.getWriter();
      out.print(gson.toJson(coursesWithProgress));
      out.flush();

    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error retrieving user courses", e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

      JsonObject error = new JsonObject();
      error.addProperty(PARAM_ERROR, "Failed to retrieve courses");

      response.setContentType(CONTENT_TYPE_JSON);
      PrintWriter out = response.getWriter();
      out.print(gson.toJson(error));
      out.flush();
    }
  }

  private void handleGetProgress(HttpServletRequest request, HttpServletResponse response, User user)
      throws IOException {

    try {
      String courseIdStr = request.getParameter(PARAM_COURSE_ID);
      if (courseIdStr == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course ID required");
        return;
      }

      Integer courseId = Integer.valueOf(courseIdStr);
      UserCourses userCourse = userCoursesDAO.findByUserAndCourse(user.getUserId(), courseId);

      JsonObject progressData = new JsonObject();
      if (userCourse != null) {
        progressData.addProperty(PARAM_PROGRESS, userCourse.getProgress());
        progressData.addProperty(PARAM_STATUS, determineStatus(userCourse));
        progressData.addProperty("enrolledDate", userCourse.getEnrollmentDate().toString());
        if (userCourse.getCompletionDate() != null) {
          progressData.addProperty("completedDate", userCourse.getCompletionDate().toString());
        }
      } else {
        progressData.addProperty(PARAM_PROGRESS, 0);
        progressData.addProperty(PARAM_STATUS, "not-enrolled");
      }

      response.setContentType(CONTENT_TYPE_JSON);
      response.setCharacterEncoding(CHARSET_UTF8);

      PrintWriter out = response.getWriter();
      out.print(gson.toJson(progressData));
      out.flush();

    } catch (NumberFormatException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid course ID");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error retrieving course progress", e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private void handleUpdateProgress(HttpServletRequest request, HttpServletResponse response, User user)
      throws IOException {

    try {
      String courseIdStr = request.getParameter(PARAM_COURSE_ID);
      String progressStr = request.getParameter(PARAM_PROGRESS);

      if (courseIdStr == null || progressStr == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course ID and progress required");
        return;
      }

      Integer courseId = Integer.valueOf(courseIdStr);
      int progress = Integer.parseInt(progressStr);

      // Validate progress range
      if (progress < 0 || progress > 100) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Progress must be between 0 and 100");
        return;
      }

      // For now, we'll create a simple implementation
      // In a real implementation, you'd find the UserCourses record
      UserCourses userCourse = new UserCourses(user.getUserId(), courseId);
      userCourse.setProgress(progress);

      // If progress is 100%, mark as completed
      if (progress == 100 && userCourse.getCompletionDate() == null) {
        userCourse.setCompletionDate(new java.sql.Timestamp(System.currentTimeMillis()));
      }

      // userCoursesDAO.update(userCourse); // Implement this method in DAO

      JsonObject result = new JsonObject();
      result.addProperty("success", true);
      result.addProperty(PARAM_PROGRESS, progress);
      result.addProperty(PARAM_STATUS, determineStatus(userCourse));

      response.setContentType(CONTENT_TYPE_JSON);
      response.setCharacterEncoding(CHARSET_UTF8);

      PrintWriter out = response.getWriter();
      out.print(gson.toJson(result));
      out.flush();

    } catch (NumberFormatException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid course ID or progress value");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error updating course progress", e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

      JsonObject error = new JsonObject();
      error.addProperty(PARAM_ERROR, "Failed to update progress");

      response.setContentType(CONTENT_TYPE_JSON);
      PrintWriter out = response.getWriter();
      out.print(gson.toJson(error));
      out.flush();
    }
  }

  private void handleCompleteCourse(HttpServletRequest request, HttpServletResponse response, User user)
      throws IOException {

    try {
      String courseIdStr = request.getParameter(PARAM_COURSE_ID);

      if (courseIdStr == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course ID required");
        return;
      }

      Integer courseId = Integer.valueOf(courseIdStr);
      // For now, we'll create a simple implementation
      UserCourses userCourse = new UserCourses(user.getUserId(), courseId);

      userCourse.setProgress(100);
      userCourse.setCompletionDate(new java.sql.Timestamp(System.currentTimeMillis()));

      // userCoursesDAO.update(userCourse); // Implement this method in DAO

      JsonObject result = new JsonObject();
      result.addProperty("success", true);
      result.addProperty("message", "Course completed successfully!");
      result.addProperty(PARAM_STATUS, "completed");

      response.setContentType(CONTENT_TYPE_JSON);
      response.setCharacterEncoding(CHARSET_UTF8);

      PrintWriter out = response.getWriter();
      out.print(gson.toJson(result));
      out.flush();

    } catch (NumberFormatException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid course ID");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error completing course", e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

      JsonObject error = new JsonObject();
      error.addProperty(PARAM_ERROR, "Failed to complete course");

      response.setContentType(CONTENT_TYPE_JSON);
      PrintWriter out = response.getWriter();
      out.print(gson.toJson(error));
      out.flush();
    }
  }

  private String determineStatus(UserCourses userCourse) {
    if (userCourse.getCompletionDate() != null) {
      return "completed";
    } else if (userCourse.getProgress() > 0) {
      return "in-progress";
    } else {
      return "enrolled";
    }
  }

  // Helper class for JSON serialization
  private static class CourseWithProgress {
    public Long id;
    public String title;
    public String description;
    public double price;
    public String imageUrl;
    public int progress;
    public String status;
    public java.sql.Timestamp enrolledDate;
    public java.sql.Timestamp completedDate;
  }
}
