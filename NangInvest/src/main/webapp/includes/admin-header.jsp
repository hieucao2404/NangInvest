<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ page
import="model.User" %>
<link
  rel="stylesheet"
  href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
/>

<header class="admin-header">
  <div class="admin-nav">
    <!-- Logo -->
    <div class="admin-logo">
      <a href="${pageContext.request.contextPath}/dashboard">
        <h2>NangInvest Admin</h2>
      </a>
    </div>

    <!-- Menu -->
    <nav class="admin-menu">
      <a href="${pageContext.request.contextPath}/dashboard" class="nav-item">
        <i class="fas fa-tachometer-alt"></i> Dashboard
      </a>
      <a
        href="${pageContext.request.contextPath}/courses?action=adminManage"
        class="nav-item"
      >
        <i class="fas fa-graduation-cap"></i> Courses
      </a>
      <a href="${pageContext.request.contextPath}/books" class="nav-item">
        <i class="fas fa-book"></i> Books
      </a>
      <a href="${pageContext.request.contextPath}/services" class="nav-item">
        <i class="fas fa-concierge-bell"></i> Services
      </a>
      <a
        href="${pageContext.request.contextPath}/admin/manageUsers.jsp"
        class="nav-item"
      >
        <i class="fas fa-users"></i> Users
      </a>
      <a
        href="${pageContext.request.contextPath}/admin/orders"
        class="nav-item"
      >
        <i class="fas fa-shopping-cart"></i> Orders
      </a>
      <a href="${pageContext.request.contextPath}/analytics" class="nav-item">
        <i class="fas fa-chart-line"></i> Analytics
      </a>
    </nav>

    <!-- User Menu -->
    <div class="admin-user-menu">
      <!-- User Info -->
      <div class="user-info">
        <span>Welcome, ${sessionScope.user.name}</span>
      </div>
      <!-- User Actions -->
      <div class="user-actions">
        <a
          href="${pageContext.request.contextPath}/user/homepage.jsp"
          class="btn-view"
        >
          <i class="fas fa-globe"></i> View Site
        </a>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
          <i class="fas fa-sign-out-alt"></i> Logout
        </a>
      </div>
    </div>
  </div>
</header>

<style>
  .admin-header {
    background: #2c3e50;
    padding: 1rem 0;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
    font-family: "Segoe UI", sans-serif;
  }

  .admin-nav {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 2rem;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .admin-logo a {
    color: white;
    text-decoration: none;
  }

  .admin-logo h2 {
    margin: 0;
    font-size: 1.6rem;
    font-weight: 700;
  }

  .admin-menu {
    display: flex;
    gap: 1.5rem;
  }

  .admin-menu a {
    color: #bdc3c7;
    text-decoration: none;
    padding: 0.5rem 0.8rem;
    border-radius: 4px;
    transition: all 0.2s ease;
    display: flex;
    align-items: center;
    gap: 6px;
  }

  .admin-menu a:hover,
  .admin-menu a.active {
    color: white;
    background-color: #34495e;
  }

  .admin-user-menu {
    display: flex;
    align-items: center;
    gap: 1rem;
    color: white;
  }

  .admin-user-menu .user-info {
    font-size: 0.95rem;
  }

  .admin-user-menu .btn-view,
  .admin-user-menu .btn-logout {
    text-decoration: none;
    padding: 6px 12px;
    border-radius: 4px;
    font-size: 0.9rem;
    display: inline-flex;
    align-items: center;
    gap: 6px;
    transition: background 0.2s ease;
  }

  .btn-view {
    background-color: #3498db;
    color: white;
  }

  .btn-view:hover {
    background-color: #2980b9;
  }

  .btn-logout {
    background-color: #e74c3c;
    color: white;
  }

  .btn-logout:hover {
    background-color: #c0392b;
  }
</style>
