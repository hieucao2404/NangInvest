<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Dashboard - NangInvest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-styles.css" />
    <style>
      .dashboard-container { max-width: 1200px; margin: 0 auto; padding: 20px; }
      .section-card {
        background: white; border-radius: 10px; padding: 25px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      }
      .section-title { color: #333; margin-bottom: 20px; padding-bottom: 10px; border-bottom: 2px solid #eee; }
      .action-link {
        display: inline-block; margin-top: 20px; padding: 10px 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white; border-radius: 5px; text-decoration: none; font-weight: bold;
      }
      .action-link:hover { background: linear-gradient(135deg, #764ba2 0%, #667eea 100%); }
      @media (max-width: 768px) {
        .dashboard-container { padding: 15px; }
      }
    </style>
  </head>
  <body>
    <%@include file="../includes/user-header.jsp" %>
    <div class="dashboard-container">
      <!-- User Profile Section -->
      <div class="section-card">
        <h2 class="section-title">👤 Your Profile</h2>
        <div style="display: grid; grid-template-columns: 1fr 2fr; gap: 30px; align-items: start;">
          <div style="text-align: center">
            <div style="width: 120px; height: 120px; border-radius: 50%; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); display: flex; align-items: center; justify-content: center; margin: 0 auto 20px auto; color: white; font-size: 3rem; font-weight: bold;">
              ${fn:substring(dashboardData.user.name != null ? dashboardData.user.name : dashboardData.user.userName, 0, 1)}
            </div>
            <h3 style="margin: 0 0 5px 0; color: #333">${dashboardData.user.name != null ? dashboardData.user.name : dashboardData.user.userName}</h3>
            <p style="color: #666; margin: 0; text-transform: capitalize">${dashboardData.user.role}</p>
          </div>
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px">
            <div>
              <h4 style="color: #667eea; margin: 0 0 10px 0">Contact Information</h4>
              <p style="margin: 5px 0"><strong>Email:</strong> ${dashboardData.user.email}</p>
              <p style="margin: 5px 0"><strong>Username:</strong> ${dashboardData.user.userName}</p>
              <c:if test="${dashboardData.user.age != null}">
                <p style="margin: 5px 0"><strong>Age:</strong> ${dashboardData.user.age}</p>
              </c:if>
            </div>
            <div>
              <h4 style="color: #667eea; margin: 0 0 10px 0">Professional Info</h4>
              <c:choose>
                <c:when test="${dashboardData.user.expertise != null && !empty dashboardData.user.expertise}">
                  <p style="margin: 5px 0"><strong>Expertise:</strong> ${dashboardData.user.expertise}</p>
                </c:when>
                <c:otherwise>
                  <p style="margin: 5px 0; color: #999">No expertise specified</p>
                </c:otherwise>
              </c:choose>
              <p style="margin: 5px 0"><strong>Member Since:</strong> 2024</p>
              <p style="margin: 5px 0"><strong>User ID:</strong> #${dashboardData.user.userId}</p>
            </div>
          </div>
        </div>
        <a href="${pageContext.request.contextPath}/user/myCourses" class="action-link">View My Courses</a>
      </div>
      <!-- AI Chat Widget -->
      <%@include file="../includes/ai-chat-widget.jsp" %>
    </div>
  </body>
</html>