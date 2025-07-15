<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="user-header">
  <div class="user-nav">
    <div class="user-logo">
      <a href="${pageContext.request.contextPath}/dashboard">NangInvest</a>
    </div>

    <div class="user-menu">
      <a href="${pageContext.request.contextPath}/user/courses.jsp">Courses</a>
      <a href="${pageContext.request.contextPath}/user/books.jsp">Books</a>
      <a href="${pageContext.request.contextPath}/user/services.jsp"
        >Services</a
      >
      <a href="${pageContext.request.contextPath}/user/blog">Blog</a>
    </div>

    <div class="user-actions">
      <a
        href="${pageContext.request.contextPath}/user/cart?action=view"
        class="cart-link"
      >
        <span class="cart-icon">🛒</span>
        <span class="cart-count">${cartCount > 0 ? cartCount : ''}</span>
      </a>
      <a href="${pageContext.request.contextPath}/dashboard">Profile</a>
      <a
        href="${pageContext.request.contextPath}/logout"
        class="btn btn-outline btn-sm"
        >Logout</a
      >
    </div>
  </div>
</nav>
