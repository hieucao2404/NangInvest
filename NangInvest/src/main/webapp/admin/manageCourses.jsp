<%-- Document : manageCourses Created on : Jun 9, 2025, 9:13:24 PM Author :
Admin --%> <%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Manage Courses - Admin</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/admin-styles.css"
    />
    <style>
      body {
        font-family: "Segoe UI", Arial, sans-serif;
        background: #f8f9fa;
      }
      .container {
        max-width: 1100px;
        margin: 40px auto;
        background: #fff;
        border-radius: 12px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.07);
        padding: 2rem 2.5rem;
      }
      h2 {
        font-size: 2rem;
        font-weight: 700;
        color: #4e73df;
        margin-bottom: 2rem;
      }
      .action-bar {
        display: flex;
        justify-content: flex-end;
        margin-bottom: 1.5rem;
      }
      .btn {
        padding: 0.7rem 1.5rem;
        border-radius: 6px;
        font-weight: 600;
        font-size: 1rem;
        border: none;
        cursor: pointer;
        transition: box-shadow 0.2s;
      }
      .btn-primary {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: #fff;
      }
      .btn-primary:hover {
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
      }
      table {
        width: 100%;
        border-collapse: collapse;
        margin-bottom: 2rem;
      }
      th,
      td {
        padding: 1rem;
        text-align: left;
      }
      th {
        background: #f3f4f6;
        color: #333;
        font-weight: 600;
      }
      tr {
        border-bottom: 1px solid #e5e7eb;
      }
      tr:last-child {
        border-bottom: none;
      }
      .course-actions {
        display: flex;
        gap: 0.5rem;
      }
      .btn-edit {
        background: #f8f9fc;
        color: #4e73df;
        border: 1px solid #4e73df;
      }
      .btn-edit:hover {
        background: #4e73df;
        color: #fff;
      }
      .btn-delete {
        background: #f8d7da;
        color: #c82333;
        border: 1px solid #c82333;
      }
      .btn-delete:hover {
        background: #c82333;
        color: #fff;
      }
      .no-courses {
        text-align: center;
        color: #888;
        font-size: 1.1rem;
        margin: 2rem 0;
      }
    </style>
  </head>
  <body>
    <%@include file="../includes/admin-header.jsp" %>
    <div class="container">
      <h2>Manage Courses</h2>
      <div class="action-bar">
        <a
          href="${pageContext.request.contextPath}/courses?action=add"
          class="btn btn-primary"
          >Add New Course</a
        >
      </div>
      <c:choose>
        <c:when test="${not empty courses}">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Price</th>
                <th>Duration</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="course" items="${courses}">
                <tr>
                  <td>${course.courseId}</td>
                  <td>${course.courseName}</td>
                  <td>
                    <c:choose>
                      <c:when test="${course.isFree}">Free</c:when>
                      <c:otherwise>$${course.price}</c:otherwise>
                    </c:choose>
                  </td>
                  <td>${course.time}</td>
                  <td>
                    <c:choose>
                      <c:when test="${course.isFree}">Free</c:when>
                      <c:otherwise>Premium</c:otherwise>
                    </c:choose>
                  </td>
                  <td class="course-actions">
                    <a
                      href="${pageContext.request.contextPath}/courses?action=edit&courseId=${course.courseId}"
                      class="btn btn-sm btn-warning btn-edit"
                      >Edit</a
                    >
                    <a
                      href="${pageContext.request.contextPath}/courses?action=delete&courseId=${course.courseId}"
                      class="btn btn-delete"
                      onclick="return confirm('Are you sure you want to delete this course?');"
                      >Delete</a
                    >
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:when>
        <c:otherwise>
          <div class="no-courses">No courses found.</div>
        </c:otherwise>
      </c:choose>
    </div>
  </body>
</html>
