<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<link
  rel="stylesheet"
  href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
/>

<header class="user-header">
  <div class="user-nav">
    <!-- Logo -->
    <div class="user-logo">
      <a href="${pageContext.request.contextPath}/dashboard">
        <h2>NangInvest</h2>
      </a>
    </div>

    <!-- Menu -->
    <nav class="user-menu">
      <a href="${pageContext.request.contextPath}/courses" class="nav-item">
        <i class="fas fa-graduation-cap"></i> Courses
      </a>
      <a href="${pageContext.request.contextPath}/books" class="nav-item">
        <i class="fas fa-book"></i> Books
      </a>
      <a href="${pageContext.request.contextPath}/services" class="nav-item">
        <i class="fas fa-concierge-bell"></i> Services
      </a>
      <a href="${pageContext.request.contextPath}/blog" class="nav-item">
        <i class="fas fa-blog"></i> Blog
      </a>
    </nav>

    <!-- Actions -->
    <div class="user-actions">
      <a
        href="${pageContext.request.contextPath}/user/cart?action=view"
        class="cart-link"
      >
        <i class="fas fa-shopping-cart"></i>
        <c:if test="${cartCount > 0}">
          <span class="cart-count">${cartCount}</span>
        </c:if>
      </a>
      <a href="${pageContext.request.contextPath}/dashboard" class="nav-item">
        <i class="fas fa-user-circle"></i> Profile
      </a>
      <a
        href="${pageContext.request.contextPath}/logout"
        class="btn btn-sm logout-btn"
      >
        <i class="fas fa-sign-out-alt"></i> Logout
      </a>
    </div>
  </div>
</header>

<style>
  .user-header {
    background: #2c3e50;
    padding: 1rem 0;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    font-family: "Segoe UI", sans-serif;
  }

  .user-nav {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 2rem;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .user-logo a {
    color: white;
    text-decoration: none;
  }

  .user-logo h2 {
    margin: 0;
    font-size: 1.6rem;
    font-weight: 700;
  }

  .user-menu {
    display: flex;
    gap: 1.5rem;
  }

  .user-menu a,
  .user-actions a.nav-item {
    color: #bdc3c7;
    text-decoration: none;
    padding: 0.5rem 0.8rem;
    border-radius: 4px;
    transition: background 0.2s ease, color 0.2s ease;
  }

  .user-menu a:hover,
  .user-actions a.nav-item:hover {
    color: white;
    background-color: #34495e;
  }

  .user-actions {
    display: flex;
    align-items: center;
    gap: 1rem;
  }

  .cart-link {
    color: #bdc3c7;
    position: relative;
    font-size: 1.2rem;
    text-decoration: none;
  }

  .cart-link:hover {
    color: white;
  }

  .cart-count {
    position: absolute;
    top: -6px;
    right: -10px;
    background: #e74c3c;
    color: white;
    border-radius: 50%;
    font-size: 12px;
    padding: 2px 6px;
  }

  .logout-btn {
    background-color: #e74c3c;
    color: white;
    padding: 6px 12px;
    border-radius: 4px;
    text-decoration: none;
    transition: background 0.2s ease;
  }

  .logout-btn:hover {
    background-color: #c0392b;
  }
</style>
