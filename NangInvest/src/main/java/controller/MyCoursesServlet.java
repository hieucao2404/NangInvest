package controller;

import dao.UserCoursesDAO;
import dao.CourseDAO;
import model.Course;
import model.UserCourses;
import model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.OrderDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

@WebServlet(name = "MyCoursesServlet", urlPatterns = {"/user/myCourses"})
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
    private transient OrderDAO orderDAO;
    private transient Gson gson;
    private transient EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userCoursesDAO = new UserCoursesDAO();
        this.courseDAO = new CourseDAO();
        this.orderDAO = new OrderDAO();
        this.gson = new Gson();
        this.emf = Persistence.createEntityManagerFactory("NangInvestPU");
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
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                List<UserCourses> userCourses = userCoursesDAO.findByUserId(user.getUserId());
                List<Course> purchasedCourses = orderDAO.findPurchasedCoursesNotEnrolled(user.getUserId());
                request.setAttribute("userCourses", userCourses);
                request.setAttribute("purchasedCourses", purchasedCourses);
                em.getTransaction().commit();
                request.getRequestDispatcher("/user/myCourses.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                LOGGER.log(Level.SEVERE, "Error forwarding to myCourses.jsp", e);
                throw e;
            } finally {
                em.close();
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
            case "enrollCourse":
                handleEnrollCourse(request, response, user);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    private void handleGetCourses(HttpServletResponse response, User user)
            throws IOException {
        EntityManager em = emf.createEntityManager();

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

                    coursesWithProgress.add(cwp);
                }
            }

            em.getTransaction().commit();

            response.setContentType(CONTENT_TYPE_JSON);
            response.setCharacterEncoding(CHARSET_UTF8);

            PrintWriter out = response.getWriter();
            out.print(gson.toJson(coursesWithProgress));
            out.flush();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.log(Level.SEVERE, "Error retrieving user courses", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            JsonObject error = new JsonObject();
            error.addProperty(PARAM_ERROR, "Failed to retrieve courses");

            response.setContentType(CONTENT_TYPE_JSON);
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(error));
            out.flush();
        } finally {
            em.close();
        }
    }

    private void handleGetProgress(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {
        EntityManager em = emf.createEntityManager();
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

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.log(Level.SEVERE, "Error retrieving course progress", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            em.close();
        }
    }

    private void handleUpdateProgress(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            String courseIdStr = request.getParameter(PARAM_COURSE_ID);
            String progressStr = request.getParameter(PARAM_PROGRESS);

            if (courseIdStr == null || progressStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course ID and progress required");
                return;
            }

            Integer courseId = Integer.valueOf(courseIdStr);
            int progress = Integer.parseInt(progressStr);

            if (progress < 0 || progress > 100) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Progress must be between 0 and 100");
                return;
            }

            UserCourses userCourse = userCoursesDAO.findByUserAndCourse(user.getUserId(), courseId);
            if (userCourse == null) {
                userCourse = new UserCourses();
                userCourse.setUserId(user.getUserId());
                userCourse.setCourseId(courseId);
                userCourse.setEnrollmentDate(new Timestamp(System.currentTimeMillis()));
            }
            userCourse.setProgress(progress);

            if (progress == 100 && userCourse.getCompletionDate() == null) {
                userCourse.setCompletionDate(new Timestamp(System.currentTimeMillis()));
            }

            em.merge(userCourse);
            em.getTransaction().commit();

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
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.log(Level.SEVERE, "Error updating course progress", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            JsonObject error = new JsonObject();
            error.addProperty(PARAM_ERROR, "Failed to update progress");

            response.setContentType(CONTENT_TYPE_JSON);
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(error));
            out.flush();
        } finally {
            em.close();
        }
    }

    private void handleCompleteCourse(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            String courseIdStr = request.getParameter(PARAM_COURSE_ID);

            if (courseIdStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course ID required");
                return;
            }

            Integer courseId = Integer.valueOf(courseIdStr);
            UserCourses userCourse = userCoursesDAO.findByUserAndCourse(user.getUserId(), courseId);
            if (userCourse == null) {
                userCourse = new UserCourses();
                userCourse.setUserId(user.getUserId());
                userCourse.setCourseId(courseId);
                userCourse.setEnrollmentDate(new Timestamp(System.currentTimeMillis()));
            }
            userCourse.setProgress(100);
            userCourse.setCompletionDate(new Timestamp(System.currentTimeMillis()));

            em.merge(userCourse);
            em.getTransaction().commit();

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
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.log(Level.SEVERE, "Error completing course", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            JsonObject error = new JsonObject();
            error.addProperty(PARAM_ERROR, "Failed to complete course");

            response.setContentType(CONTENT_TYPE_JSON);
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(error));
            out.flush();
        } finally {
            em.close();
        }
    }

    private void handleEnrollCourse(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            String courseIdStr = request.getParameter(PARAM_COURSE_ID);
            if (courseIdStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course ID required");
                return;
            }

            Integer courseId = Integer.valueOf(courseIdStr);
            Course course = courseDAO.findById(courseId).orElse(null);
            if (course == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid course ID");
                return;
            }

            UserCourses userCourse = userCoursesDAO.findByUserAndCourse(user.getUserId(), courseId);
            if (userCourse != null) {
                response.sendRedirect(request.getContextPath() + "/user/myCourses?success=alreadyEnrolled");
                return;
            }

            userCoursesDAO.enrollUserInCourse(user.getUserId(), courseId);
            em.getTransaction().commit();

            response.sendRedirect(request.getContextPath() + "/user/myCourses?success=enrolled");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid course ID");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.log(Level.SEVERE, "Error enrolling course", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject error = new JsonObject();
            error.addProperty(PARAM_ERROR, "Failed to enroll course");
            response.setContentType(CONTENT_TYPE_JSON);
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(error));
            out.flush();
        } finally {
            em.close();
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
