<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@page
import="model.User"%>

<header class="admin-header">
  <div class="admin-nav">
    <div class="admin-logo">
      <a href="${pageContext.request.contextPath}/dashboard">
        <h2>NangInvest Admin</h2>
      </a>
    </div>

    <nav class="admin-menu">
      <a href="${pageContext.request.contextPath}/dashboard" class="nav-item">
        <i class="icon-dashboard"></i> Dashboard
      </a>
      <a
        href="${pageContext.request.contextPath}/admin/courses"
        class="nav-item"
      >
        <i class="icon-courses"></i> Courses
      </a>
      <a href="${pageContext.request.contextPath}/admin/books" class="nav-item">
        <i class="icon-books"></i> Books
      </a>
      <a
        href="${pageContext.request.contextPath}/admin/services"
        class="nav-item"
      >
        <i class="icon-services"></i> Services
      </a>
      <a href="${pageContext.request.contextPath}/admin/users" class="nav-item">
        <i class="icon-users"></i> Users
      </a>
      <a
        href="${pageContext.request.contextPath}/admin/analytics"
        class="nav-item"
      >
        <i class="icon-analytics"></i> Analytics
      </a>
    </nav>

    <div class="admin-user-menu">
      <div class="user-info">
        <span>Welcome, ${sessionScope.user.name}</span>
      </div>
      <div class="user-actions">
        <a
          href="${pageContext.request.contextPath}/user/homepage.jsp"
          class="btn btn-sm"
          >View Site</a
        >
        <a
          href="${pageContext.request.contextPath}/logout"
          class="btn btn-sm btn-danger"
          >Logout</a
        >
      </div>
    </div>
  </div>
</header>

<style>
  .admin-header {
    background: #2c3e50;
    padding: 1rem 0;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
    font-size: 1.5rem;
    font-weight: 700;
  }

  .admin-menu {
    display: flex;
    gap: 2rem;
  }

  .admin-menu a {
    color: #bdc3c7;
    text-decoration: none;
    padding: 0.5rem 1rem;
    border-radius: 4px;
    transition: all 0.2s;
  }

  .admin-menu a:hover,
  .admin-menu a.active {
    color: white;
    background: #34495e;
  }

  .admin-actions {
    display: flex;
    align-items: center;
    gap: 1rem;
    color: white;
  }
</style>
