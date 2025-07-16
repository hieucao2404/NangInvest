<%-- Document : blog-post Created on : Jun 9, 2025, 9:11:44 PM Author : Admin
--%> <%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@page import="model.Blog" %> <%@page
import="dao.BlogDAO" %> <% String blogIdParam = request.getParameter("blogId");
Blog blog = null; if (blogIdParam != null) { try { int blogId =
Integer.parseInt(blogIdParam); blog = new
BlogDAO().findById(blogId).orElse(null); } catch (Exception e) { // handle error
} } %>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>
      <c:out value="${blog != null ? blog.blogName : 'Blog Post'}"/>
    </title>
    <style>
      .blog-container {
        max-width: 800px;
        margin: 40px auto;
        padding: 30px;
        background: #fff;
        border-radius: 10px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
      }
      .blog-title {
        font-size: 2.2rem;
        color: #667eea;
        margin-bottom: 10px;
      }
      .blog-meta {
        color: #888;
        font-size: 0.95rem;
        margin-bottom: 25px;
      }
      .blog-content {
        font-size: 1.15rem;
        color: #333;
        line-height: 1.7;
      }
    </style>
  </head>
  <body>
    <!-- Role-based Header Selection -->
    <c:choose>
      <c:when
        test="${sessionScope.user != null && sessionScope.user.role == 'ADMIN'}"
      >
        <%-- Admin Header --%> <%@include file="../includes/admin-header.jsp" %>
      </c:when>
      <c:when
        test="${sessionScope.user != null && sessionScope.user.role == 'USER'}"
      >
        <%-- User Header --%> <%@include file="../includes/user-header.jsp" %>
      </c:when>
      <c:otherwise>
        <%-- Public Header for non-logged in users --%> <%@include
        file="../includes/public-header.jsp" %>
      </c:otherwise>
    </c:choose>
    <div class="blog-container">
      <c:choose>
        <c:when test="${blog != null}">
          <div class="blog-title">${blog.blogName}</div>
          <div class="blog-meta">Topic: <strong>${blog.topic}</strong></div>
          <div class="blog-content">${blog.detailedContent}</div>
        </c:when>
        <c:otherwise>
          <h2 style="color: #c00">Blog post not found.</h2>
        </c:otherwise>
      </c:choose>
    </div>
  </body>
</html>
