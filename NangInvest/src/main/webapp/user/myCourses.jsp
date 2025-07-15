<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@taglib prefix="fn"
uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>My Courses - NangInvest</title>
    <style>
      /* Modern Substack-inspired my courses styling */
      body {
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", system-ui,
          sans-serif;
        margin: 0;
        padding: 0;
        background-color: #f8f9fa;
        color: #1a1a1a;
        line-height: 1.6;
      }

      .my-courses-container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 2rem;
      }

      .page-header {
        background: white;
        border-radius: 12px;
        padding: 2rem;
        margin-bottom: 2rem;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        text-align: center;
      }

      .page-title {
        font-size: 2.5rem;
        font-weight: 700;
        margin: 0 0 0.5rem 0;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
      }

      .page-subtitle {
        font-size: 1.1rem;
        color: #666;
        margin: 0 0 1.5rem 0;
      }

      .stats-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 1rem;
        margin-bottom: 2rem;
      }

      .stat-card {
        background: white;
        border-radius: 12px;
        padding: 1.5rem;
        text-align: center;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        border-left: 4px solid;
      }

      .stat-card.enrolled {
        border-left-color: #4299e1;
      }

      .stat-card.completed {
        border-left-color: #48bb78;
      }

      .stat-card.in-progress {
        border-left-color: #ed8936;
      }

      .stat-number {
        font-size: 2rem;
        font-weight: 700;
        color: #1a1a1a;
        margin: 0;
      }

      .stat-label {
        color: #666;
        font-size: 0.9rem;
        margin: 0.5rem 0 0 0;
      }

      .courses-section {
        background: white;
        border-radius: 12px;
        padding: 2rem;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
      }

      .section-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 2rem;
      }

      .section-title {
        font-size: 1.5rem;
        font-weight: 600;
        color: #1a1a1a;
        margin: 0;
      }

      .courses-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
        gap: 2rem;
      }

      .course-card {
        background: #f8f9fa;
        border-radius: 12px;
        overflow: hidden;
        transition: all 0.3s ease;
        border: 1px solid #eee;
      }

      .course-card:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
        background: white;
      }

      .course-image {
        width: 100%;
        height: 180px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 3rem;
        color: white;
        position: relative;
      }

      .course-image img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .course-progress {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        height: 4px;
        background: rgba(255, 255, 255, 0.3);
      }

      .progress-bar {
        height: 100%;
        background: #48bb78;
        transition: width 0.3s ease;
      }

      .course-content {
        padding: 1.5rem;
      }

      .course-title {
        font-size: 1.2rem;
        font-weight: 700;
        margin: 0 0 0.75rem 0;
        color: #1a1a1a;
      }

      .course-meta {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 1rem;
        font-size: 0.85rem;
        color: #666;
      }

      .course-status {
        padding: 0.25rem 0.75rem;
        border-radius: 12px;
        font-size: 0.75rem;
        font-weight: 600;
        text-transform: uppercase;
      }

      .status-enrolled {
        background: #e6f3ff;
        color: #0066cc;
      }

      .status-in-progress {
        background: #fff5e6;
        color: #cc6600;
      }

      .status-completed {
        background: #e6ffe6;
        color: #008000;
      }

      .course-actions {
        display: flex;
        gap: 0.75rem;
        margin-top: 1rem;
      }

      .btn {
        padding: 0.75rem 1.25rem;
        border-radius: 6px;
        font-weight: 600;
        text-decoration: none;
        text-align: center;
        cursor: pointer;
        border: none;
        transition: all 0.2s;
        flex: 1;
        font-size: 0.9rem;
      }

      .btn-primary {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
      }

      .btn-primary:hover {
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
      }

      .btn-secondary {
        background: #6c757d;
        color: white;
      }

      .btn-success {
        background: #48bb78;
        color: white;
      }

      .alert {
        padding: 1rem;
        border-radius: 6px;
        margin-bottom: 1rem;
        font-weight: 500;
      }

      .alert-success {
        background: #f0fff4;
        color: #22543d;
        border: 1px solid #9ae6b4;
      }

      .empty-state {
        text-align: center;
        padding: 3rem;
        color: #666;
      }

      .empty-state-icon {
        font-size: 4rem;
        margin-bottom: 1rem;
        opacity: 0.5;
      }

      .quick-actions {
        background: white;
        border-radius: 12px;
        padding: 1.5rem;
        margin-bottom: 2rem;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
      }

      .quick-actions-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 1rem;
      }

      .quick-action {
        background: #f8f9fa;
        border-radius: 8px;
        padding: 1.5rem;
        text-align: center;
        text-decoration: none;
        color: #1a1a1a;
        transition: all 0.2s;
        border: 1px solid #eee;
      }

      .quick-action:hover {
        background: white;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }

      .quick-action-icon {
        font-size: 2rem;
        margin-bottom: 0.5rem;
        display: block;
      }

      .quick-action-title {
        font-weight: 600;
        margin: 0;
      }

      @media (max-width: 768px) {
        .my-courses-container {
          padding: 1rem;
        }

        .courses-grid {
          grid-template-columns: 1fr;
          gap: 1rem;
        }

        .stats-grid {
          grid-template-columns: 1fr;
        }

        .quick-actions-grid {
          grid-template-columns: 1fr;
        }

        .section-header {
          flex-direction: column;
          gap: 1rem;
          text-align: center;
        }
      }
    </style>
  </head>
  <body>
    <%@include file="../includes/user-header.jsp" %>

    <main class="my-courses-container">
      <!-- Page Header -->
      <section class="page-header">
        <h1 class="page-title">My Courses</h1>
        <p class="page-subtitle">
          Track your learning progress and continue your investment education
          journey
        </p>
      </section>

      <!-- Success Messages -->
      <c:if test="${param.success == 'enrolled'}">
        <div class="alert alert-success">
          ✅ Successfully enrolled in course! Start learning now.
        </div>
      </c:if>

      <!-- Quick Actions -->
      <section class="quick-actions">
        <div class="quick-actions-grid">
          <a
            href="${pageContext.request.contextPath}/courses"
            class="quick-action"
          >
            <span class="quick-action-icon">🔍</span>
            <h3 class="quick-action-title">Browse Courses</h3>
          </a>
          <a
            href="${pageContext.request.contextPath}/user/cart"
            class="quick-action"
          >
            <span class="quick-action-icon">🛒</span>
            <h3 class="quick-action-title">My Cart</h3>
          </a>
          <a
            href="${pageContext.request.contextPath}/public/blog.jsp"
            class="quick-action"
          >
            <span class="quick-action-icon">📚</span>
            <h3 class="quick-action-title">Learning Resources</h3>
          </a>
        </div>
      </section>

      <!-- Course Statistics -->
      <section class="stats-grid">
        <div class="stat-card enrolled">
          <h3 class="stat-number" id="enrolledCount">0</h3>
          <p class="stat-label">Enrolled Courses</p>
        </div>
        <div class="stat-card in-progress">
          <h3 class="stat-number" id="inProgressCount">0</h3>
          <p class="stat-label">In Progress</p>
        </div>
        <div class="stat-card completed">
          <h3 class="stat-number" id="completedCount">0</h3>
          <p class="stat-label">Completed</p>
        </div>
      </section>

      <!-- My Courses Section -->
      <section class="courses-section">
        <div class="section-header">
          <h2 class="section-title">My Enrolled Courses</h2>
        </div>

        <!-- Check if user is logged in -->
        <c:choose>
          <c:when test="${sessionScope.user == null}">
            <div class="empty-state">
              <div class="empty-state-icon">🔐</div>
              <h3>Please Log In</h3>
              <p>You need to be logged in to view your courses.</p>
              <a
                href="${pageContext.request.contextPath}/public/login-register.jsp"
                class="btn btn-primary"
              >
                Login Now
              </a>
            </div>
          </c:when>
          <c:otherwise>
            <!-- Course Grid (will be populated by JavaScript) -->
            <div class="courses-grid" id="coursesGrid">
              <!-- Courses will be loaded here -->
            </div>

            <!-- Empty State (initially hidden) -->
            <div class="empty-state" id="emptyState" style="display: none">
              <div class="empty-state-icon">📚</div>
              <h3>No Courses Yet</h3>
              <p>
                You haven't enrolled in any courses yet. Start your learning
                journey today!
              </p>
              <a
                href="${pageContext.request.contextPath}/courses"
                class="btn btn-primary"
              >
                Browse Courses
              </a>
            </div>
          </c:otherwise>
        </c:choose>
      </section>
    </main>

    <%@include file="../includes/ai-chat-widget.jsp" %>

    <script>
      // Pass contextPath from JSP to JS
      var contextPath = "${pageContext.request.contextPath}";

      // Sample course data (in production, this would come from the server)
      const sampleCourses = [
        {
          id: 1,
          title: "Investment Fundamentals",
          progress: 65,
          status: "in-progress",
          image: null,
          enrolledDate: "2025-01-01",
        },
        {
          id: 2,
          title: "Portfolio Management Basics",
          progress: 100,
          status: "completed",
          image: null,
          enrolledDate: "2025-01-15",
        },
        {
          id: 3,
          title: "Risk Assessment Strategies",
          progress: 25,
          status: "in-progress",
          image: null,
          enrolledDate: "2025-02-01",
        },
      ];

      document.addEventListener("DOMContentLoaded", function () {
        <c:if test="${sessionScope.user != null}">loadUserCourses();</c:if>;
      });

      function loadUserCourses() {
        // Fetch actual user courses from the server
        fetch(contextPath + "/user/myCourses?action=getCourses")
          .then((response) => {
            if (!response.ok) {
              throw new Error("Network response was not ok");
            }
            return response.json();
          })
          .then((courses) => {
            displayCourses(courses);
            updateStats(courses);
          })
          .catch((error) => {
            console.error("Error loading courses:", error);
            // Fall back to sample data for demonstration
            displayCourses(sampleCourses);
            updateStats(sampleCourses);
          });
      }

      function displayCourses(courses) {
        const coursesGrid = document.getElementById("coursesGrid");
        const emptyState = document.getElementById("emptyState");

        if (!courses || courses.length === 0) {
          coursesGrid.style.display = "none";
          emptyState.style.display = "block";
          return;
        }

        coursesGrid.innerHTML = "";
        emptyState.style.display = "none";

        courses.forEach((course) => {
          const courseCard = createCourseCard(course);
          coursesGrid.appendChild(courseCard);
        });
      }

      function createCourseCard(course) {
        var card = document.createElement("div");
        card.className = "course-card";

        var statusClass = getStatusClass(course.status);
        var statusText = getStatusText(course.status);

        var imageHtml = course.image
          ? '<img src="' + course.image + '" alt="' + course.title + '">'
          : "📖";

        var progressBarHtml =
          '<div class="course-progress">' +
          '<div class="progress-bar" style="width: ' +
          course.progress +
          '%"></div>' +
          "</div>";

        var actionsHtml =
          (course.status === "completed"
            ? '<a href="' +
              contextPath +
              "/course/view?id=" +
              course.id +
              '" class="btn btn-success">✅ Review Course</a>'
            : '<a href="' +
              contextPath +
              "/course/learn?id=" +
              course.id +
              '" class="btn btn-primary">📚 Continue Learning</a>') +
          '<a href="' +
          contextPath +
          "/course/details?id=" +
          course.id +
          '" class="btn btn-secondary">📊 View Details</a>';

        card.innerHTML =
          '<div class="course-image">' +
          imageHtml +
          progressBarHtml +
          "</div>" +
          '<div class="course-content">' +
          '<h3 class="course-title">' +
          course.title +
          "</h3>" +
          '<div class="course-meta">' +
          "<span>Progress: " +
          course.progress +
          "%</span>" +
          '<span class="course-status ' +
          statusClass +
          '">' +
          statusText +
          "</span>" +
          "</div>" +
          '<div class="course-actions">' +
          actionsHtml +
          "</div>" +
          "</div>";

        return card;
      }

      function getStatusClass(status) {
        const statusMap = {
          enrolled: "status-enrolled",
          "in-progress": "status-in-progress",
          completed: "status-completed",
        };
        return statusMap[status] || "status-enrolled";
      }

      function getStatusText(status) {
        const statusMap = {
          enrolled: "Enrolled",
          "in-progress": "In Progress",
          completed: "Completed",
        };
        return statusMap[status] || "Enrolled";
      }

      function updateStats(courses) {
        const stats = {
          enrolled: courses.length,
          inProgress: courses.filter((c) => c.status === "in-progress").length,
          completed: courses.filter((c) => c.status === "completed").length,
        };

        // Animate numbers
        animateNumber("enrolledCount", stats.enrolled);
        animateNumber("inProgressCount", stats.inProgress);
        animateNumber("completedCount", stats.completed);
      }

      function animateNumber(elementId, targetValue) {
        const element = document.getElementById(elementId);
        const startValue = 0;
        const duration = 1000;
        const startTime = Date.now();

        function update() {
          const elapsed = Date.now() - startTime;
          const progress = Math.min(elapsed / duration, 1);
          const currentValue = Math.floor(
            startValue + (targetValue - startValue) * progress
          );

          element.textContent = currentValue;

          if (progress < 1) {
            requestAnimationFrame(update);
          }
        }

        requestAnimationFrame(update);
      }

      function showEmptyState() {
        document.getElementById("coursesGrid").style.display = "none";
        document.getElementById("emptyState").style.display = "block";
      }
    </script>
  </body>
</html>
