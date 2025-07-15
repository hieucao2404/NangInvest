<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@taglib prefix="fn"
uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Dashboard - NangInvest</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/user-styles.css"
    />
    <style>
      .dashboard-container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 20px;
      }
      .welcome-section {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        padding: 30px;
        border-radius: 15px;
        margin-bottom: 30px;
        text-align: center;
      }
      .quick-actions {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 20px;
        margin-bottom: 30px;
      }
      .action-card {
        background: white;
        border-radius: 10px;
        padding: 25px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        transition: transform 0.3s ease, box-shadow 0.3s ease;
        text-decoration: none;
        color: inherit;
      }
      .action-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
        text-decoration: none;
      }
      .action-card .icon {
        font-size: 3rem;
        margin-bottom: 15px;
        display: block;
      }
      .action-card h3 {
        margin: 0 0 10px 0;
        color: #333;
      }
      .action-card p {
        color: #666;
        margin: 0;
      }
      .dashboard-sections {
        display: grid;
        grid-template-columns: 2fr 1fr;
        gap: 30px;
        margin-bottom: 30px;
      }
      .section-card {
        background: white;
        border-radius: 10px;
        padding: 25px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      }
      .section-title {
        color: #333;
        margin-bottom: 20px;
        padding-bottom: 10px;
        border-bottom: 2px solid #eee;
      }
      .progress-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 15px 0;
        border-bottom: 1px solid #eee;
      }
      .progress-item:last-child {
        border-bottom: none;
      }
      .progress-bar {
        width: 100%;
        height: 8px;
        background: #eee;
        border-radius: 4px;
        overflow: hidden;
        margin-top: 5px;
      }
      .progress-fill {
        height: 100%;
        background: linear-gradient(90deg, #4caf50, #45a049);
        transition: width 0.3s ease;
      }
      .recommendation-item {
        padding: 15px 0;
        border-bottom: 1px solid #eee;
      }
      .recommendation-item:last-child {
        border-bottom: none;
      }
      .recommendation-item h4 {
        margin: 0 0 5px 0;
        color: #333;
      }
      .recommendation-item p {
        margin: 0;
        color: #666;
        font-size: 0.9rem;
      }
      .stats-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 20px;
        margin-bottom: 30px;
      }
      .stat-card {
        background: white;
        padding: 20px;
        border-radius: 10px;
        text-align: center;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      }
      .stat-number {
        font-size: 2rem;
        font-weight: bold;
        color: #667eea;
        margin-bottom: 5px;
      }
      .stat-label {
        color: #666;
        font-size: 0.9rem;
      }
      @media (max-width: 768px) {
        .dashboard-sections {
          grid-template-columns: 1fr;
        }
        .quick-actions {
          grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        }
      }
    </style>
  </head>
  <body>
    <%@include file="../includes/user-header.jsp" %>
    <div class="dashboard-container">
      <!-- ...existing code... -->
      <!-- Welcome Section -->
      <div class="welcome-section">
        <h1>
          Welcome back, ${dashboardData.user.name != null ?
          dashboardData.user.name : dashboardData.user.userName}!
        </h1>
        <p>
          Ready to continue your investment journey? Let's explore what's new
          for you today.
        </p>
      </div>
      <!-- User Profile Section -->
      <div class="section-card" style="margin-bottom: 30px">
        <h2 class="section-title">👤 Your Profile</h2>
        <div
          style="
            display: grid;
            grid-template-columns: 1fr 2fr;
            gap: 30px;
            align-items: start;
          "
        >
          <div style="text-align: center">
            <div
              style="
                width: 120px;
                height: 120px;
                border-radius: 50%;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 20px auto;
                color: white;
                font-size: 3rem;
                font-weight: bold;
              "
            >
              ${fn:substring(dashboardData.user.name != null ?
              dashboardData.user.name : dashboardData.user.userName, 0, 1)}
            </div>
            <h3 style="margin: 0 0 5px 0; color: #333">
              ${dashboardData.user.name != null ? dashboardData.user.name :
              dashboardData.user.userName}
            </h3>
            <p style="color: #666; margin: 0; text-transform: capitalize">
              ${dashboardData.user.role}
            </p>
          </div>
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px">
            <div>
              <h4 style="color: #667eea; margin: 0 0 10px 0">
                Contact Information
              </h4>
              <p style="margin: 5px 0">
                <strong>Email:</strong> ${dashboardData.user.email}
              </p>
              <p style="margin: 5px 0">
                <strong>Username:</strong> ${dashboardData.user.userName}
              </p>
              <c:if test="${dashboardData.user.age != null}">
                <p style="margin: 5px 0">
                  <strong>Age:</strong> ${dashboardData.user.age}
                </p>
              </c:if>
            </div>
            <div>
              <h4 style="color: #667eea; margin: 0 0 10px 0">
                Professional Info
              </h4>
              <c:choose>
                <c:when
                  test="${dashboardData.user.expertise != null && !empty dashboardData.user.expertise}"
                >
                  <p style="margin: 5px 0">
                    <strong>Expertise:</strong> ${dashboardData.user.expertise}
                  </p>
                </c:when>
                <c:otherwise>
                  <p style="margin: 5px 0; color: #999">
                    No expertise specified
                  </p>
                </c:otherwise>
              </c:choose>
              <p style="margin: 5px 0"><strong>Member Since:</strong> 2024</p>
              <p style="margin: 5px 0">
                <strong>User ID:</strong> #${dashboardData.user.userId}
              </p>
            </div>
          </div>
        </div>
      </div>
      <!-- Quick Actions -->
      <!-- ...existing code... -->
      <div class="quick-actions">
        <a
          href="${pageContext.request.contextPath}/homepage.jsp"
          class="action-card"
        >
          <span class="icon">🏠</span>
          <h3>Homepage</h3>
          <p>Return to main user homepage</p>
        </a>
        <a
          href="${pageContext.request.contextPath}/user/myCourses.jsp"
          class="action-card"
        >
          <span class="icon">📚</span>
          <h3>Courses</h3>
          <p>Explore investment courses</p>
        </a>
        <a
          href="${pageContext.request.contextPath}/user/myServices.jsp"
          class="action-card"
        >
          <span class="icon">💼</span>
          <h3>Services</h3>
          <p>Financial consulting services</p>
        </a>
        <a
          href="${pageContext.request.contextPath}/user/myBooks.jsp"
          class="action-card"
        >
          <span class="icon">📖</span>
          <h3>Books</h3>
          <p>Browse investment books</p>
        </a>
      </div>
      <!-- Stats Overview -->
      <!-- ...existing code... -->
      <div class="stats-grid">
        <!-- ...existing code... -->
        <!-- Total Orders -->
        <div class="stat-card">
          <div class="stat-number">
            <c:choose>
              <c:when
                test="${not empty dashboardData && not empty dashboardData.userAnalytics && not empty dashboardData.userAnalytics.orderHistory}"
              >
                ${dashboardData.userAnalytics.orderHistory.size()}
              </c:when>
              <c:otherwise>0</c:otherwise>
            </c:choose>
          </div>
          <div class="stat-label">Total Orders</div>
          <small style="color: #999">Investment purchases</small>
        </div>
        <!-- Enrolled Courses -->
        <div class="stat-card">
          <div class="stat-number">
            <c:choose>
              <c:when
                test="${not empty dashboardData && not empty dashboardData.recommendations && not empty dashboardData.recommendations.enrolledCourses}"
              >
                ${dashboardData.recommendations.enrolledCourses.size()}
              </c:when>
              <c:otherwise>0</c:otherwise>
            </c:choose>
          </div>
          <div class="stat-label">Enrolled Courses</div>
          <small style="color: #999">Active learning</small>
        </div>
        <!-- Completed Courses -->
        <div class="stat-card">
          <div class="stat-number">
            <c:choose>
              <c:when
                test="${not empty dashboardData && not empty dashboardData.userAnalytics && not empty dashboardData.userAnalytics.completedCourses}"
              >
                ${dashboardData.userAnalytics.completedCourses.size()}
              </c:when>
              <c:otherwise>0</c:otherwise>
            </c:choose>
          </div>
          <div class="stat-label">Completed Courses</div>
          <small style="color: #999">Achievements</small>
        </div>
        <!-- Overall Progress -->
        <div class="stat-card">
          <div class="stat-number">
            <c:choose>
              <c:when
                test="${not empty dashboardData && not empty dashboardData.userAnalytics && dashboardData.userAnalytics.totalProgress != null}"
              >
                ${dashboardData.userAnalytics.totalProgress}%
              </c:when>
              <c:otherwise>75%</c:otherwise>
            </c:choose>
          </div>
          <div class="stat-label">Overall Progress</div>
          <small style="color: #999">Learning journey</small>
        </div>
        <!-- Books Owned -->
        <div class="stat-card">
          <div class="stat-number">
            <c:choose>
              <c:when
                test="${not empty dashboardData && not empty dashboardData.userAnalytics && not empty dashboardData.userAnalytics.purchasedBooks}"
              >
                ${dashboardData.userAnalytics.purchasedBooks.size()}
              </c:when>
              <c:otherwise>0</c:otherwise>
            </c:choose>
          </div>
          <div class="stat-label">Books Owned</div>
          <small style="color: #999">Knowledge library</small>
        </div>
        <!-- Interest Areas -->
        <div class="stat-card">
          <div class="stat-number">
            <c:choose>
              <c:when
                test="${not empty dashboardData && not empty dashboardData.userAnalytics && not empty dashboardData.userAnalytics.userInterests}"
              >
                ${dashboardData.userAnalytics.userInterests.size()}
              </c:when>
              <c:otherwise>3</c:otherwise>
            </c:choose>
          </div>
          <div class="stat-label">Interest Areas</div>
          <small style="color: #999">Focus topics</small>
        </div>
      </div>
      <!-- Main Dashboard Sections -->
      <!-- ...existing code... -->
      <div class="dashboard-sections">
        <!-- User History Section -->
        <div class="section-card">
          <h2 class="section-title">🕒 Your Activity History</h2>
          <div
            style="
              display: grid;
              grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
              gap: 20px;
            "
          >
            <!-- Orders Section -->
            <div>
              <h3 style="color: #667eea">📋 Recent Orders</h3>
              <c:choose>
                <c:when
                  test="${not empty dashboardData.userAnalytics.orderHistory}"
                >
                  <c:forEach
                    var="order"
                    items="${dashboardData.userAnalytics.orderHistory}"
                    end="3"
                  >
                    <div class="recommendation-item">
                      <h4>Order #${order.orderId}</h4>
                      <p><strong>Product ID:</strong> ${order.productId}</p>
                      <p>
                        <strong>Status:</strong>
                        <span
                          style="padding: 2px 8px; border-radius: 12px; font-size: 0.8rem; background: ${order.paymentStatus == 'Completed' ? '#d4edda' : order.paymentStatus == 'Pending' ? '#fff3cd' : '#f8d7da'}; color: ${order.paymentStatus == 'Completed' ? '#155724' : order.paymentStatus == 'Pending' ? '#856404' : '#721c24'};"
                        >
                          ${order.paymentStatus}
                        </span>
                      </p>
                      <c:if test="${order.user != null}">
                        <p><strong>User:</strong> ${order.user.userName}</p>
                      </c:if>
                    </div>
                  </c:forEach>
                </c:when>
                <c:otherwise>
                  <div style="text-align: center; padding: 20px 0; color: #666">
                    <span
                      style="
                        font-size: 2rem;
                        display: block;
                        margin-bottom: 10px;
                      "
                      >📦</span
                    >
                    <p>No orders yet</p>
                    <a
                      href="${pageContext.request.contextPath}/user/courses.jsp"
                      style="color: #667eea; text-decoration: none"
                      >Start shopping</a
                    >
                  </div>
                </c:otherwise>
              </c:choose>
            </div>
            <!-- Enrolled Courses -->
            <div>
              <h3 style="color: #667eea">📚 Your Courses</h3>
              <c:choose>
                <c:when
                  test="${not empty dashboardData.recommendations.enrolledCourses}"
                >
                  <c:forEach
                    var="course"
                    items="${dashboardData.recommendations.enrolledCourses}"
                    end="3"
                  >
                    <div class="recommendation-item">
                      <h4>
                        ${course.courseName != null ? course.courseName :
                        course.title}
                      </h4>
                      <c:if test="${course.time != null}">
                        <p><strong>Duration:</strong> ${course.time}</p>
                      </c:if>
                      <c:if test="${course.price != null}">
                        <p>
                          <strong>Price:</strong>
                          <c:choose
                            ><c:when test="${course.isFree == true}"
                              >Free</c:when
                            ><c:otherwise
                              >$${course.price}</c:otherwise
                            ></c:choose
                          >
                        </p>
                      </c:if>
                    </div>
                  </c:forEach>
                </c:when>
                <c:otherwise>
                  <div style="text-align: center; padding: 20px 0; color: #666">
                    <span
                      style="
                        font-size: 2rem;
                        display: block;
                        margin-bottom: 10px;
                      "
                      >🎓</span
                    >
                    <p>No courses enrolled</p>
                    <a
                      href="${pageContext.request.contextPath}/user/courses.jsp"
                      style="color: #667eea; text-decoration: none"
                      >Browse courses</a
                    >
                  </div>
                </c:otherwise>
              </c:choose>
            </div>
            <!-- Purchased Books -->
            <div>
              <h3 style="color: #667eea">📖 Your Books</h3>
              <c:choose>
                <c:when
                  test="${not empty dashboardData.userAnalytics.purchasedBooks}"
                >
                  <c:forEach
                    var="book"
                    items="${dashboardData.userAnalytics.purchasedBooks}"
                    end="3"
                  >
                    <div class="recommendation-item">
                      <h4>${book.title}</h4>
                      <p><strong>Author:</strong> ${book.author}</p>
                      <c:if test="${book.price != null}"
                        ><p><strong>Price:</strong> $${book.price}</p></c:if
                      >
                    </div>
                  </c:forEach>
                </c:when>
                <c:otherwise>
                  <div style="text-align: center; padding: 20px 0; color: #666">
                    <span
                      style="
                        font-size: 2rem;
                        display: block;
                        margin-bottom: 10px;
                      "
                      >📚</span
                    >
                    <p>No books purchased</p>
                    <a
                      href="${pageContext.request.contextPath}/user/books.jsp"
                      style="color: #667eea; text-decoration: none"
                      >Browse books</a
                    >
                  </div>
                </c:otherwise>
              </c:choose>
            </div>
          </div>
        </div>
        <!-- Recommendations Section -->
        <div class="section-card">
          <h2 class="section-title">💡 Personalized Recommendations</h2>
          <h3 style="color: #667eea; margin-bottom: 15px">
            📚 Recommended Courses
          </h3>
          <c:choose>
            <c:when
              test="${not empty dashboardData.userAnalytics.recommendedCourses}"
            >
              <c:forEach
                var="course"
                items="${dashboardData.userAnalytics.recommendedCourses}"
              >
                <div class="recommendation-item">
                  <h4>
                    <a
                      href="${pageContext.request.contextPath}/public/coursesDetail.jsp?courseId=${course.courseId}"
                      style="color: #667eea; text-decoration: none"
                    >
                      ${course.title != null ? course.title : course.courseName}
                    </a>
                  </h4>
                  <p>${course.description != null ? course.description : ''}</p>
                </div>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <div style="text-align: center; color: #666; padding: 20px 0">
                <span
                  style="font-size: 2rem; display: block; margin-bottom: 10px"
                  >📚</span
                >
                <p>
                  No recommended courses yet.
                  <a
                    href="${pageContext.request.contextPath}/user/courses.jsp"
                    style="color: #667eea; text-decoration: underline"
                  >
                    Browse courses
                  </a>
                  to get started!
                </p>
              </div>
            </c:otherwise>
          </c:choose>
          <h3 style="color: #667eea; margin: 25px 0 15px 0">
            📖 Recommended Books
          </h3>
          <c:choose>
            <c:when
              test="${not empty dashboardData.userAnalytics.recommendedBooks}"
            >
              <c:forEach
                var="book"
                items="${dashboardData.userAnalytics.recommendedBooks}"
              >
                <div class="recommendation-item">
                  <h4>
                    <a
                      href="${pageContext.request.contextPath}/public/bookDetail.jsp?bookId=${book.bookId}"
                      style="color: #667eea; text-decoration: none"
                    >
                      ${book.title != null ? book.title : book.bookName}
                    </a>
                  </h4>
                  <p>
                    ${book.author != null ? book.author : ''}
                    <c:if test="${book.description != null}"
                      >- ${book.description}</c:if
                    >
                  </p>
                </div>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <div style="text-align: center; color: #666; padding: 20px 0">
                <span
                  style="font-size: 2rem; display: block; margin-bottom: 10px"
                  >📖</span
                >
                <p>
                  No recommended books yet.
                  <a
                    href="${pageContext.request.contextPath}/user/books.jsp"
                    style="color: #667eea; text-decoration: underline"
                  >
                    Browse books
                  </a>
                  to get started!
                </p>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
      <!-- Recent Activity -->
      <div class="section-card">
        <h2 class="section-title">🕒 Your Investment Portfolio</h2>
        <div
          style="
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
          "
        >
          <!-- Orders Section -->
          <div>
            <h3 style="color: #667eea">📋 Recent Orders</h3>
            <c:choose>
              <c:when
                test="${not empty dashboardData.userAnalytics.orderHistory}"
              >
                <c:forEach
                  var="order"
                  items="${dashboardData.userAnalytics.orderHistory}"
                  end="3"
                >
                  <div class="recommendation-item">
                    <h4>Order #${order.orderId}</h4>
                    <p><strong>Product ID:</strong> ${order.productId}</p>
                    <p>
                      <strong>Status:</strong>
                      <span
                        style="
                          padding: 2px 8px;
                          border-radius: 12px;
                          font-size: 0.8rem;
                          background: ${order.paymentStatus == 'Completed' ? '#d4edda' : order.paymentStatus == 'Pending' ? '#fff3cd' : '#f8d7da'};
                          color: ${order.paymentStatus == 'Completed' ? '#155724' : order.paymentStatus == 'Pending' ? '#856404' : '#721c24'};
                        "
                      >
                        ${order.paymentStatus}
                      </span>
                    </p>
                    <c:if test="${order.user != null}">
                      <p><strong>User:</strong> ${order.user.userName}</p>
                    </c:if>
                  </div>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <div style="text-align: center; padding: 20px 0; color: #666">
                  <span
                    style="font-size: 2rem; display: block; margin-bottom: 10px"
                    >📦</span
                  >
                  <p>No orders yet</p>
                  <a
                    href="${pageContext.request.contextPath}/user/courses.jsp"
                    style="color: #667eea; text-decoration: none"
                  >
                    Start shopping
                  </a>
                </div>
              </c:otherwise>
            </c:choose>
          </div>
          <!-- Enrolled Courses -->
          <div>
            <h3 style="color: #667eea">📚 Your Courses</h3>
            <c:choose>
              <c:when
                test="${not empty dashboardData.recommendations.enrolledCourses}"
              >
                <c:forEach
                  var="course"
                  items="${dashboardData.recommendations.enrolledCourses}"
                  end="3"
                >
                  <div class="recommendation-item">
                    <h4>
                      ${course.courseName != null ? course.courseName :
                      course.title}
                    </h4>
                    <c:if test="${course.time != null}">
                      <p><strong>Duration:</strong> ${course.time}</p>
                    </c:if>
                    <c:if test="${course.price != null}">
                      <p>
                        <strong>Price:</strong>
                        <c:choose>
                          <c:when test="${course.isFree == true}">Free</c:when>
                          <c:otherwise>$${course.price}</c:otherwise>
                        </c:choose>
                      </p>
                    </c:if>
                    <c:if test="${course.progress != null}">
                      <div style="margin-top: 10px">
                        <small style="color: #666"
                          >Progress: ${course.progress}%</small
                        >
                        <div
                          class="progress-bar"
                          style="
                            width: 100%;
                            height: 6px;
                            background: #eee;
                            border-radius: 3px;
                            margin-top: 3px;
                          "
                        >
                          <div
                            class="progress-fill"
                            style="width: ${course.progress}%; height: 100%; background: #4CAF50; border-radius: 3px;"
                          ></div>
                        </div>
                      </div>
                    </c:if>
                  </div>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <div style="text-align: center; padding: 20px 0; color: #666">
                  <span
                    style="font-size: 2rem; display: block; margin-bottom: 10px"
                    >🎓</span
                  >
                  <p>No courses enrolled</p>
                  <a
                    href="${pageContext.request.contextPath}/user/courses.jsp"
                    style="color: #667eea; text-decoration: none"
                  >
                    Browse courses
                  </a>
                </div>
              </c:otherwise>
            </c:choose>
          </div>
          <!-- User Interests & Services -->
          <div>
            <h3 style="color: #667eea">💡 Your Interests</h3>
            <c:choose>
              <c:when
                test="${not empty dashboardData.userAnalytics.userInterests}"
              >
                <div style="margin-bottom: 15px">
                  <c:forEach
                    var="interest"
                    items="${dashboardData.userAnalytics.userInterests}"
                  >
                    <span
                      style="
                        background: linear-gradient(135deg, #667eea, #764ba2);
                        color: white;
                        padding: 5px 12px;
                        border-radius: 15px;
                        margin: 3px;
                        display: inline-block;
                        font-size: 0.85rem;
                      "
                    >
                      ${interest}
                    </span>
                  </c:forEach>
                </div>
              </c:when>
              <c:otherwise>
                <div style="margin-bottom: 15px">
                  <span
                    style="
                      background: #f0f0f0;
                      padding: 5px 10px;
                      border-radius: 15px;
                      margin: 5px;
                      display: inline-block;
                      font-size: 0.9rem;
                    "
                  >
                    Financial Planning
                  </span>
                  <span
                    style="
                      background: #f0f0f0;
                      padding: 5px 10px;
                      border-radius: 15px;
                      margin: 5px;
                      display: inline-block;
                      font-size: 0.9rem;
                    "
                  >
                    Investment Strategy
                  </span>
                  <span
                    style="
                      background: #f0f0f0;
                      padding: 5px 10px;
                      border-radius: 15px;
                      margin: 5px;
                      display: inline-block;
                      font-size: 0.9rem;
                    "
                  >
                    Risk Management
                  </span>
                </div>
              </c:otherwise>
            </c:choose>
            <h4 style="color: #667eea; margin: 15px 0 10px 0">
              🛠️ Services Available
            </h4>
            <div class="recommendation-item">
              <h4>Personal Financial Consulting</h4>
              <p>One-on-one investment advice</p>
            </div>
            <div class="recommendation-item">
              <h4>Portfolio Analysis</h4>
              <p>Professional portfolio review</p>
            </div>
          </div>
          <!-- Book Recommendations -->
          <div>
            <h3 style="color: #667eea">📖 Your Books</h3>
            <c:choose>
              <c:when
                test="${not empty dashboardData.userAnalytics.purchasedBooks}"
              >
                <c:forEach
                  var="book"
                  items="${dashboardData.userAnalytics.purchasedBooks}"
                  end="3"
                >
                  <div class="recommendation-item">
                    <h4>${book.title}</h4>
                    <p><strong>Author:</strong> ${book.author}</p>
                    <c:if test="${book.price != null}">
                      <p><strong>Price:</strong> $${book.price}</p>
                    </c:if>
                  </div>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <div style="text-align: center; padding: 20px 0; color: #666">
                  <span
                    style="font-size: 2rem; display: block; margin-bottom: 10px"
                    >📚</span
                  >
                  <p>No books purchased</p>
                  <a
                    href="${pageContext.request.contextPath}/user/books.jsp"
                    style="color: #667eea; text-decoration: none"
                  >
                    Browse books
                  </a>
                </div>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </div>
      <!-- AI Chat Widget -->
      <!-- ...existing code... -->
      <%@include file="../includes/ai-chat-widget.jsp" %>
      <script>
        document.addEventListener("DOMContentLoaded", function () {
          // Animate progress bars
          const progressBars = document.querySelectorAll(".progress-fill");
          progressBars.forEach((bar) => {
            const width = bar.style.width;
            bar.style.width = "0%";
            setTimeout(() => {
              bar.style.width = width;
            }, 500);
          });
          // Add hover effects to action cards
          const actionCards = document.querySelectorAll(".action-card");
          actionCards.forEach((card) => {
            card.addEventListener("mouseenter", function () {
              this.style.background =
                "linear-gradient(135deg, #667eea 0%, #764ba2 100%)";
              this.style.color = "white";
            });
            card.addEventListener("mouseleave", function () {
              this.style.background = "white";
              this.style.color = "inherit";
            });
          });
        });
      </script>
    </div>
  </body>
</html>
