<%-- Document : analytics Created on : Jun 9, 2025, 9:16:26 PM Author : Admin
Updated on: Jul 13, 2025 --%> <%@page contentType="text/html"
pageEncoding="UTF-8"%> <%@taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%> <%@taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt"%> <%@taglib prefix="fn"
uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Analytics Dashboard - NangInvest Admin</title>
    <style>
      /* Admin Dashboard styling similar to homepage */
      body {
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", system-ui,
          sans-serif;
        margin: 0;
        padding: 0;
        background-color: #f8f9fa;
        color: #1a1a1a;
        line-height: 1.6;
      }

      .dashboard-container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 2rem;
      }

      .dashboard-header {
        background: white;
        border-radius: 12px;
        padding: 2rem;
        margin-bottom: 2rem;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
      }

      .dashboard-title {
        font-size: 2.5rem;
        font-weight: 700;
        margin: 0 0 0.5rem 0;
        color: #1a1a1a;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
      }

      .dashboard-subtitle {
        font-size: 1.1rem;
        color: #666;
        margin: 0;
      }

      .stats-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 1.5rem;
        margin-bottom: 2rem;
      }

      .stat-card {
        background: white;
        border-radius: 12px;
        padding: 1.5rem;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        border-left: 4px solid;
        transition: transform 0.2s, box-shadow 0.2s;
      }

      .stat-card:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      }

      .stat-card.primary {
        border-left-color: #667eea;
      }
      .stat-card.success {
        border-left-color: #48bb78;
      }
      .stat-card.warning {
        border-left-color: #ed8936;
      }
      .stat-card.info {
        border-left-color: #4299e1;
      }

      .stat-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 1rem;
      }

      .stat-icon {
        font-size: 2rem;
        opacity: 0.7;
      }

      .stat-number {
        font-size: 2.5rem;
        font-weight: 700;
        margin: 0;
        color: #1a1a1a;
      }

      .stat-label {
        color: #666;
        font-size: 0.9rem;
        margin: 0;
      }

      .stat-change {
        font-size: 0.85rem;
        margin-top: 0.5rem;
      }

      .stat-change.positive {
        color: #48bb78;
      }
      .stat-change.negative {
        color: #f56565;
      }

      .dashboard-grid {
        display: grid;
        grid-template-columns: 2fr 1fr;
        gap: 2rem;
        margin-bottom: 2rem;
      }

      .chart-section {
        background: white;
        border-radius: 12px;
        padding: 2rem;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
      }

      .section-title {
        font-size: 1.5rem;
        font-weight: 600;
        margin: 0 0 1.5rem 0;
        color: #1a1a1a;
      }

      .chart-placeholder {
        height: 300px;
        background: linear-gradient(135deg, #f7fafc 0%, #edf2f7 100%);
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #666;
        font-size: 1.1rem;
        border: 2px dashed #cbd5e0;
      }

      .insights-section {
        background: white;
        border-radius: 12px;
        padding: 2rem;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
      }

      .insight-item {
        background: #f7fafc;
        border-radius: 8px;
        padding: 1rem;
        margin-bottom: 1rem;
        border-left: 3px solid #667eea;
      }

      .insight-item:last-child {
        margin-bottom: 0;
      }

      .insight-title {
        font-weight: 600;
        color: #1a1a1a;
        margin: 0 0 0.5rem 0;
      }

      .insight-description {
        color: #666;
        margin: 0;
        font-size: 0.9rem;
      }

      .content-suggestions {
        background: white;
        border-radius: 12px;
        padding: 2rem;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        margin-bottom: 2rem;
      }

      .suggestion-list {
        list-style: none;
        padding: 0;
        margin: 0;
      }

      .suggestion-item {
        background: #f7fafc;
        border-radius: 8px;
        padding: 1rem;
        margin-bottom: 0.5rem;
        border-left: 3px solid #48bb78;
        cursor: pointer;
        transition: background-color 0.2s;
      }

      .suggestion-item:hover {
        background: #edf2f7;
      }

      .suggestion-item:last-child {
        margin-bottom: 0;
      }

      .recent-activity {
        background: white;
        border-radius: 12px;
        padding: 2rem;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
      }

      .activity-list {
        list-style: none;
        padding: 0;
        margin: 0;
      }

      .activity-item {
        display: flex;
        align-items: center;
        padding: 1rem 0;
        border-bottom: 1px solid #edf2f7;
      }

      .activity-item:last-child {
        border-bottom: none;
      }

      .activity-icon {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-size: 1.2rem;
        margin-right: 1rem;
      }

      .activity-content {
        flex: 1;
      }

      .activity-title {
        font-weight: 600;
        margin: 0 0 0.25rem 0;
        color: #1a1a1a;
      }

      .activity-description {
        color: #666;
        font-size: 0.9rem;
        margin: 0;
      }

      .activity-time {
        color: #999;
        font-size: 0.8rem;
      }

      .action-buttons {
        display: flex;
        gap: 1rem;
        margin-top: 2rem;
      }

      .btn {
        padding: 0.75rem 1.5rem;
        border-radius: 6px;
        font-weight: 600;
        text-decoration: none;
        transition: all 0.2s;
        border: none;
        cursor: pointer;
        font-size: 0.95rem;
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
        background: white;
        color: #667eea;
        border: 1px solid #667eea;
      }

      .btn-secondary:hover {
        background: #667eea;
        color: white;
      }

      .quick-actions {
        background: white;
        border-radius: 12px;
        padding: 2rem;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        margin-bottom: 2rem;
      }

      .quick-actions-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 1rem;
      }

      .quick-action {
        background: #f7fafc;
        border-radius: 8px;
        padding: 1.5rem;
        text-align: center;
        text-decoration: none;
        color: #1a1a1a;
        transition: all 0.2s;
        border: 1px solid #edf2f7;
      }

      .quick-action:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        background: white;
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
        .dashboard-container {
          padding: 1rem;
        }

        .dashboard-grid {
          grid-template-columns: 1fr;
        }

        .stats-grid {
          grid-template-columns: 1fr;
        }

        .quick-actions-grid {
          grid-template-columns: 1fr;
        }
      }
    </style>
  </head>
  <body>
    <!-- Admin Header -->
    <%@include file="../includes/admin-header.jsp" %>

    <main class="dashboard-container">
      <!-- Dashboard Header -->
      <section class="dashboard-header">
        <h1 class="dashboard-title">Analytics Dashboard</h1>
        <p class="dashboard-subtitle">
          Welcome back, ${sessionScope.user.name != null ?
          sessionScope.user.name : sessionScope.user.userName}! Here's what's
          happening with NangInvest today.
        </p>
      </section>

      <!-- Key Statistics -->
      <section class="stats-grid">
        <div class="stat-card primary">
          <div class="stat-header">
            <div>
              <h3 class="stat-number">
                ${dashboardData.analytics.totalUsers != null ?
                dashboardData.analytics.totalUsers : '1,234'}
              </h3>
              <p class="stat-label">Total Users</p>
            </div>
            <span class="stat-icon">👥</span>
          </div>
          <p class="stat-change positive">↗ +12% from last month</p>
        </div>

        <div class="stat-card success">
          <div class="stat-header">
            <div>
              <h3 class="stat-number">
                ${dashboardData.analytics.totalRevenue != null ?
                dashboardData.analytics.totalRevenue : '$45,678'}
              </h3>
              <p class="stat-label">Revenue</p>
            </div>
            <span class="stat-icon">💰</span>
          </div>
          <p class="stat-change positive">↗ +18% from last month</p>
        </div>

        <div class="stat-card warning">
          <div class="stat-header">
            <div>
              <h3 class="stat-number">
                ${dashboardData.analytics.activeCourses != null ?
                dashboardData.analytics.activeCourses : '56'}
              </h3>
              <p class="stat-label">Active Courses</p>
            </div>
            <span class="stat-icon">📚</span>
          </div>
          <p class="stat-change positive">↗ +3 new this week</p>
        </div>

        <div class="stat-card info">
          <div class="stat-header">
            <div>
              <h3 class="stat-number">
                ${dashboardData.analytics.blogPosts != null ?
                dashboardData.analytics.blogPosts : '128'}
              </h3>
              <p class="stat-label">Blog Posts</p>
            </div>
            <span class="stat-icon">📝</span>
          </div>
          <p class="stat-change positive">↗ +5 this week</p>
        </div>
      </section>

      <!-- Quick Actions -->
      <section class="quick-actions">
        <h2 class="section-title">Quick Actions</h2>
        <div class="quick-actions-grid">
          <a
            href="${pageContext.request.contextPath}/admin/blogForm.jsp"
            class="quick-action"
          >
            <span class="quick-action-icon">✍️</span>
            <h3 class="quick-action-title">Create Blog Post</h3>
          </a>
          <a
            href="${pageContext.request.contextPath}/admin/blog_list.jsp"
            class="quick-action"
          >
            <span class="quick-action-icon">📋</span>
            <h3 class="quick-action-title">Manage Blogs</h3>
          </a>
          <a
            href="${pageContext.request.contextPath}/admin/users.jsp"
            class="quick-action"
          >
            <span class="quick-action-icon">👤</span>
            <h3 class="quick-action-title">User Management</h3>
          </a>
          <a
            href="${pageContext.request.contextPath}/admin/courses.jsp"
            class="quick-action"
          >
            <span class="quick-action-icon">🎓</span>
            <h3 class="quick-action-title">Course Management</h3>
          </a>
        </div>
      </section>

      <!-- Main Dashboard Content -->
      <div class="dashboard-grid">
        <!-- Analytics Chart -->
        <section class="chart-section">
          <h2 class="section-title">Platform Performance</h2>
          <div class="chart-placeholder">
            📊 Interactive charts will be displayed here
            <br /><small
              >Integration with Chart.js or similar library needed</small
            >
          </div>
        </section>

        <!-- AI Insights -->
        <section class="insights-section">
          <h2 class="section-title">AI Insights</h2>
          <c:choose>
            <c:when test="${not empty dashboardData.aiInsights}">
              <c:if test="${not empty dashboardData.aiInsights.trendingTopics}">
                <div class="insight-item">
                  <h3 class="insight-title">Trending Topics</h3>
                  <p class="insight-description">
                    <c:forEach
                      items="${dashboardData.aiInsights.trendingTopics}"
                      var="topic"
                      varStatus="status"
                    >
                      ${topic}<c:if test="${!status.last}">, </c:if>
                    </c:forEach>
                  </p>
                </div>
              </c:if>
              <c:if
                test="${not empty dashboardData.aiInsights.coursePopularity}"
              >
                <div class="insight-item">
                  <h3 class="insight-title">Course Performance</h3>
                  <p class="insight-description">
                    AI predicts these courses will perform well this month.
                  </p>
                </div>
              </c:if>
            </c:when>
            <c:otherwise>
              <div class="insight-item">
                <h3 class="insight-title">Market Trends</h3>
                <p class="insight-description">
                  Investment education is showing 23% growth this quarter.
                </p>
              </div>
              <div class="insight-item">
                <h3 class="insight-title">User Engagement</h3>
                <p class="insight-description">
                  Blog posts about "Portfolio Diversification" are trending.
                </p>
              </div>
              <div class="insight-item">
                <h3 class="insight-title">Optimal Posting</h3>
                <p class="insight-description">
                  Best time to publish: Tuesday 10 AM and Thursday 2 PM.
                </p>
              </div>
            </c:otherwise>
          </c:choose>
        </section>
      </div>

      <!-- Content Suggestions -->
      <section class="content-suggestions">
        <h2 class="section-title">AI-Powered Content Suggestions</h2>
        <ul class="suggestion-list">
          <c:choose>
            <c:when test="${not empty dashboardData.blogSuggestions}">
              <c:forEach
                items="${dashboardData.blogSuggestions}"
                var="suggestion"
              >
                <li class="suggestion-item">${suggestion}</li>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <li class="suggestion-item">
                The Psychology of Investment Decisions: Understanding Market
                Behavior
              </li>
              <li class="suggestion-item">
                ESG Investing in 2025: Sustainable Finance Trends
              </li>
              <li class="suggestion-item">
                Cryptocurrency vs Traditional Assets: A Balanced Portfolio
                Approach
              </li>
              <li class="suggestion-item">
                Tax-Efficient Investment Strategies for Young Professionals
              </li>
              <li class="suggestion-item">
                Market Volatility: How to Stay Calm During Economic Uncertainty
              </li>
            </c:otherwise>
          </c:choose>
        </ul>
      </section>

      <!-- Recent Activity -->
      <section class="recent-activity">
        <h2 class="section-title">Recent Activity</h2>
        <ul class="activity-list">
          <li class="activity-item">
            <div class="activity-icon">👤</div>
            <div class="activity-content">
              <h3 class="activity-title">New User Registration</h3>
              <p class="activity-description">
                John Doe signed up for the investment course
              </p>
            </div>
            <span class="activity-time">2 hours ago</span>
          </li>
          <li class="activity-item">
            <div class="activity-icon">📝</div>
            <div class="activity-content">
              <h3 class="activity-title">Blog Post Published</h3>
              <p class="activity-description">
                "Market Analysis Q3 2025" went live
              </p>
            </div>
            <span class="activity-time">4 hours ago</span>
          </li>
          <li class="activity-item">
            <div class="activity-icon">💰</div>
            <div class="activity-content">
              <h3 class="activity-title">Course Purchase</h3>
              <p class="activity-description">
                Advanced Portfolio Management course sold
              </p>
            </div>
            <span class="activity-time">6 hours ago</span>
          </li>
          <li class="activity-item">
            <div class="activity-icon">🎓</div>
            <div class="activity-content">
              <h3 class="activity-title">Course Completion</h3>
              <p class="activity-description">
                Sarah completed "Investment Basics"
              </p>
            </div>
            <span class="activity-time">1 day ago</span>
          </li>
        </ul>
      </section>

      <!-- Action Buttons -->
      <div class="action-buttons">
        <a
          href="${pageContext.request.contextPath}/admin/blogForm.jsp"
          class="btn btn-primary"
          >Create New Content</a
        >
        <a
          href="${pageContext.request.contextPath}/admin/blog_list.jsp"
          class="btn btn-secondary"
          >Manage Content</a
        >
        <a
          href="${pageContext.request.contextPath}/public/blog.jsp"
          class="btn btn-secondary"
          >View Public Blog</a
        >
      </div>
    </main>

    <!-- AI Chat Widget -->
    <%@include file="../includes/ai-chat-widget.jsp" %>

    <script>
      // Add interactive elements
      document.addEventListener("DOMContentLoaded", function () {
        // Animate stat cards on load
        const statCards = document.querySelectorAll(".stat-card");
        statCards.forEach((card, index) => {
          setTimeout(() => {
            card.style.opacity = "0";
            card.style.transform = "translateY(20px)";
            card.style.transition = "all 0.5s ease";

            setTimeout(() => {
              card.style.opacity = "1";
              card.style.transform = "translateY(0)";
            }, 100);
          }, index * 100);
        });

        // Add click handlers for suggestion items
        const suggestionItems = document.querySelectorAll(".suggestion-item");
        suggestionItems.forEach((item) => {
          item.addEventListener("click", function () {
            const title = this.textContent;
            if (
              confirm(`Would you like to create a blog post about: "${title}"?`)
            ) {
              window.location.href =
                "${pageContext.request.contextPath}/admin/blogForm.jsp?suggested=" +
                encodeURIComponent(title);
            }
          });
        });

        // Refresh data every 5 minutes
        setInterval(() => {
          console.log("Refreshing dashboard data...");
          // Implement AJAX refresh here if needed
        }, 300000);
      });
    </script>
  </body>
</html>
