<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<style>
  .public-header {
    background: white;
    border-bottom: 1px solid #e1e5e9;
    padding: 0;
    position: sticky;
    top: 0;
    z-index: 1000;
  }

  .header-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 1rem 2rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .logo-section {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .logo-section h1 {
    margin: 0;
    font-size: 1.5rem;
    font-weight: 700;
    color: #1a1a1a;
  }

  .logo-icon {
    width: 32px;
    height: 32px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-weight: bold;
    font-size: 18px;
  }

  .nav-menu {
    display: flex;
    gap: 2rem;
    align-items: center;
  }

  .nav-link {
    color: #666;
    text-decoration: none;
    font-weight: 500;
    transition: color 0.2s;
  }

  .nav-link:hover {
    color: #1a1a1a;
    text-decoration: none;
  }

  .auth-buttons {
    display: flex;
    gap: 1rem;
    align-items: center;
  }

  .btn-login {
    color: #667eea;
    text-decoration: none;
    font-weight: 500;
    padding: 0.5rem 1rem;
    border-radius: 6px;
    transition: background-color 0.2s;
  }

  .btn-login:hover {
    background: #f8f9fa;
    text-decoration: none;
  }

  .btn-signup {
    background: #667eea;
    color: white;
    text-decoration: none;
    padding: 0.75rem 1.5rem;
    border-radius: 6px;
    font-weight: 500;
    transition: background-color 0.2s;
  }

  .btn-signup:hover {
    background: #5a6fd8;
    text-decoration: none;
  }

  @media (max-width: 768px) {
    .header-container {
      padding: 1rem;
      flex-wrap: wrap;
    }

    .nav-menu {
      gap: 1rem;
    }

    .auth-buttons {
      margin-top: 0.5rem;
    }
  }
</style>

<header class="public-header">
  <div class="header-container">
    <div class="logo-section">
      <div class="logo-icon">N</div>
      <h1>Nàng Invest</h1>
    </div>

    <nav class="nav-menu">
      <a href="${pageContext.request.contextPath}/" class="nav-link">Home</a>
      <a
        href="${pageContext.request.contextPath}/public/about.jsp"
        class="nav-link"
        >About</a
      >
      <a
        href="${pageContext.request.contextPath}/public/courses.jsp"
        class="nav-link"
        >Courses</a
      >
      <a
        href="${pageContext.request.contextPath}/public/books.jsp"
        class="nav-link"
        >Books</a
      >
      <a
        href="${pageContext.request.contextPath}/public/blog"
        class="nav-link"
        >Blog</a
      >
    </nav>

    <div class="auth-buttons">
      <a
        href="${pageContext.request.contextPath}/public/login-registers.jsp"
        class="btn-login"
        >Sign in</a
      >
      <a
        href="${pageContext.request.contextPath}/public/register.jsp"
        class="btn-signup"
        >Subscribe</a
      >
    </div>
  </div>
</header>
