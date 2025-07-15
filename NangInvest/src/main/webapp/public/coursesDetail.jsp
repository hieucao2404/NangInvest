<%-- Document : courses-detail Created on : Jun 9, 2025, 9:11:54 PM Author :
Admin --%> <%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@page import="model.Course" %>
<%@page import="dao.CourseDAO" %> <% String courseIdParam =
request.getParameter("courseId"); Course course = null; if (courseIdParam !=
null) { try { int courseId = Integer.parseInt(courseIdParam); course = new
CourseDAO().findById(courseId).orElse(null); } catch (Exception e) { // handle
error } } %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>
      <c:out value="${course != null ? course.title : 'Course Detail'}"/>
    </title>
    <style>
      .course-container {
        max-width: 700px;
        margin: 40px auto;
        padding: 30px;
        background: #fff;
        border-radius: 10px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
      }
      .course-title {
        font-size: 2rem;
        color: #667eea;
        margin-bottom: 10px;
      }
      .course-meta {
        color: #888;
        font-size: 0.95rem;
        margin-bottom: 20px;
      }
      .course-desc {
        font-size: 1.1rem;
        color: #333;
        line-height: 1.7;
        margin-bottom: 20px;
      }
      .course-price {
        color: #27ae60;
        font-weight: bold;
      }
    </style>
  </head>
  <body>
    <div class="course-container">
      <c:choose>
        <c:when test="${course != null}">
          <div class="course-title">
            ${course.title != null ? course.title : course.courseName}
          </div>
          <div class="course-meta">
            <strong>Topic:</strong> ${course.topic != null ? course.topic :
            'General'}<br />
            <c:if test="${course.price != null}"
              ><span class="course-price">Price: $${course.price}</span><br
            /></c:if>
            <c:if test="${course.isFree == true}"
              ><span class="course-price">Free Course</span><br
            /></c:if>
          </div>
          <div class="course-desc">${course.description}</div>
        </c:when>
        <c:otherwise>
          <h2 style="color: #c00">Course not found.</h2>
        </c:otherwise>
      </c:choose>
    </div>
  </body>
</html>
