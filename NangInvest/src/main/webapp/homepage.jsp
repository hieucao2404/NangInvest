<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Nàng Invest - Investment Newsletter & Learning Platform</title>
    <style>
      /* Substack-inspired styling */
      body {
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", system-ui, sans-serif;
        margin: 0;
        padding: 0;
        background-color: #ffffff;
        color: #1a1a1a;
        line-height: 1.6;
      }
      
      .main-container {
        max-width: 680px;
        margin: 0 auto;
        padding: 0 2rem;
      }
      
      .hero-section {
        padding: 4rem 0 3rem 0;
        text-align: center;
        border-bottom: 1px solid #e1e5e9;
        margin-bottom: 3rem;
      }
      
      .hero-title {
        font-size: 3rem;
        font-weight: 700;
        margin: 0 0 1rem 0;
        color: #1a1a1a;
        line-height: 1.2;
      }
      
      .hero-subtitle {
        font-size: 1.25rem;
        color: #666;
        margin: 0 0 2rem 0;
        max-width: 600px;
        margin-left: auto;
        margin-right: auto;
      }
      
      .author-info {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 1rem;
        margin-bottom: 2rem;
        color: #666;
        font-size: 0.95rem;
      }
      
      .author-avatar {
        width: 24px;
        height: 24px;
        border-radius: 50%;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-weight: bold;
        font-size: 12px;
      }
      
      .subscribe-section {
        background: #f8f9fa;
        border: 1px solid #e1e5e9;
        border-radius: 8px;
        padding: 2rem;
        margin: 2rem 0;
        text-align: center;
      }
      
      .subscribe-form {
        display: flex;
        gap: 0.5rem;
        max-width: 400px;
        margin: 1rem auto 0 auto;
      }
      
      .email-input {
        flex: 1;
        padding: 0.75rem 1rem;
        border: 1px solid #d1d5db;
        border-radius: 6px;
        font-size: 1rem;
        outline: none;
      }
      
      .email-input:focus {
        border-color: #667eea;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
      }
      
      .subscribe-btn {
        background: #667eea;
        color: white;
        border: none;
        padding: 0.75rem 1.5rem;
        border-radius: 6px;
        font-size: 1rem;
        font-weight: 600;
        cursor: pointer;
        transition: background-color 0.2s;
      }
      
      .subscribe-btn:hover {
        background: #5a6fd8;
      }
      
      .content-section {
        padding: 2rem 0;
      }
      
      .section-title {
        font-size: 1.5rem;
        font-weight: 700;
        margin: 0 0 1.5rem 0;
        color: #1a1a1a;
      }
      
      .featured-content {
        display: grid;
        gap: 2rem;
        margin-bottom: 3rem;
      }
      
      .content-card {
        background: white;
        border: 1px solid #e1e5e9;
        border-radius: 8px;
        padding: 1.5rem;
        transition: box-shadow 0.2s;
      }
      
      .content-card:hover {
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }
      
      .content-title {
        font-size: 1.25rem;
        font-weight: 600;
        margin: 0 0 0.5rem 0;
        color: #1a1a1a;
      }
      
      .content-excerpt {
        color: #666;
        margin: 0 0 1rem 0;
      }
      
      .content-meta {
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-size: 0.9rem;
        color: #888;
      }
      
      .read-more {
        color: #667eea;
        text-decoration: none;
        font-weight: 500;
      }
      
      .read-more:hover {
        text-decoration: underline;
      }
      
      .features-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
        gap: 2rem;
        margin: 3rem 0;
      }
      
      .feature-card {
        text-align: center;
        padding: 2rem 1rem;
      }
      
      .feature-icon {
        font-size: 3rem;
        margin-bottom: 1rem;
        display: block;
      }
      
      .feature-title {
        font-size: 1.2rem;
        font-weight: 600;
        margin: 0 0 0.5rem 0;
        color: #1a1a1a;
      }
      
      .feature-description {
        color: #666;
        margin: 0;
      }
      
      .stats-section {
        background: #f8f9fa;
        border-radius: 8px;
        padding: 2rem;
        margin: 3rem 0;
        text-align: center;
      }
      
      .stats-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
        gap: 2rem;
        margin-top: 1rem;
      }
      
      .stat-item {
        text-align: center;
      }
      
      .stat-number {
        font-size: 2rem;
        font-weight: 700;
        color: #667eea;
        margin-bottom: 0.25rem;
      }
      
      .stat-label {
        color: #666;
        font-size: 0.9rem;
      }
      
      .recent-posts {
        border-top: 1px solid #e1e5e9;
        padding-top: 2rem;
        margin-top: 3rem;
      }
      
      .post-list {
        display: grid;
        gap: 1.5rem;
      }
      
      .post-item {
        padding-bottom: 1.5rem;
        border-bottom: 1px solid #f0f0f0;
      }
      
      .post-item:last-child {
        border-bottom: none;
      }
      
      .post-title {
        font-size: 1.1rem;
        font-weight: 600;
        margin: 0 0 0.5rem 0;
      }
      
      .post-title a {
        color: #1a1a1a;
        text-decoration: none;
      }
      
      .post-title a:hover {
        color: #667eea;
      }
      
      .post-excerpt {
        color: #666;
        font-size: 0.95rem;
        margin: 0 0 0.5rem 0;
      }
      
      .post-date {
        color: #888;
        font-size: 0.85rem;
      }
      
      @media (max-width: 768px) {
        .main-container {
          padding: 0 1rem;
        }
        
        .hero-title {
          font-size: 2rem;
        }
        
        .subscribe-form {
          flex-direction: column;
        }
        
        .features-grid {
          grid-template-columns: 1fr;
        }
      }
    </style>
  </head>
  <body>
    <!-- Role-based Header Selection -->
    <c:choose>
      <c:when test="${sessionScope.user != null && sessionScope.user.role == 'ADMIN'}">
        <%-- Admin Header --%>
        <%@include file="includes/admin-header.jsp" %>
      </c:when>
      <c:when test="${sessionScope.user != null && sessionScope.user.role == 'USER'}">
        <%-- User Header --%>
        <%@include file="includes/user-header.jsp" %>
      </c:when>
      <c:otherwise>
        <%-- Public Header for non-logged in users --%>
        <%@include file="includes/public-header.jsp" %>
      </c:otherwise>
    </c:choose>

    <main class="main-container">
      <!-- Hero Section -->
      <section class="hero-section">
        <h1 class="hero-title">Nàng Invest</h1>
        <p class="hero-subtitle">
          An investment newsletter that helps you understand yourself, understand the market, and build a clear investment strategy — from mindset to action.

        </p>
        
        <div class="author-info">
          <div class="author-avatar">Y</div>
          <span>By NangInvet</span>
          
        </div>
        
        <!-- Subscribe Section for non-users -->
        <c:if test="${sessionScope.user == null}">
          <div class="subscribe-section">
            <h3>Subscribe to get our latest investment insights</h3>
            <p>Join thousands of investors learning to make smarter financial decisions</p>
            <form class="subscribe-form" action="${pageContext.request.contextPath}/subcribe">
              <input type="email" class="email-input" placeholder="Enter your email" required>
              <button type="submit" class="subscribe-btn">Subscribe</button>
            </form>
            <small style="color: #888; margin-top: 1rem; display: block;">
              By subscribing, I agree to the Terms of Use and Privacy Policy
            </small>
          </div>
        </c:if>
        
        <!-- Welcome message for logged-in users -->
        <c:if test="${sessionScope.user != null}">
          <div class="subscribe-section">
            <h3>Welcome back, ${sessionScope.user.name != null ? sessionScope.user.name : sessionScope.user.userName}!</h3>
            <p>Continue your investment journey with our latest insights and tools</p>
            <div style="display: flex; gap: 1rem; justify-content: center; margin-top: 1rem;">
              <a href="${pageContext.request.contextPath}/dashboard" class="subscribe-btn" style="text-decoration: none;">Go to Dashboard</a>
              <a href="${pageContext.request.contextPath}/user/courses.jsp" class="read-more" style="padding: 0.75rem 1.5rem; border: 1px solid #667eea; border-radius: 6px; text-decoration: none;">Browse Courses</a>
            </div>
          </div>
        </c:if>
      </section>

      <!-- Platform Statistics -->
      <section class="stats-section">
        <h2 class="section-title">Platform Overview</h2>
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-number">1,000+</div>
            <div class="stat-label">Active Subscribers</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">50+</div>
            <div class="stat-label">Investment Courses</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">200+</div>
            <div class="stat-label">Books & Resources</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">24/7</div>
            <div class="stat-label">AI Support</div>
          </div>
        </div>
      </section>

      <!-- Featured Content -->
      <section class="content-section">
        <h2 class="section-title">What We Offer</h2>
        
        <div class="features-grid">
          <div class="feature-card">
            <span class="feature-icon">📊</span>
            <h3 class="feature-title">Investment Analytics</h3>
            <p class="feature-description">
              Deep market analysis and portfolio insights to help you make informed investment decisions
            </p>
          </div>
          
          <div class="feature-card">
            <span class="feature-icon">🎓</span>
            <h3 class="feature-title">Educational Courses</h3>
            <p class="feature-description">
              Comprehensive courses covering everything from basic investing to advanced trading strategies
            </p>
          </div>
          
          <div class="feature-card">
            <span class="feature-icon">📚</span>
            <h3 class="feature-title">Investment Library</h3>
            <p class="feature-description">
              Curated collection of books, reports, and research from leading investment experts
            </p>
          </div>
          
          <div class="feature-card">
            <span class="feature-icon">🤖</span>
            <h3 class="feature-title">AI Assistant</h3>
            <p class="feature-description">
              24/7 AI-powered investment advisor to answer your questions and provide personalized guidance
            </p>
          </div>
          
          <div class="feature-card">
            <span class="feature-icon">💼</span>
            <h3 class="feature-title">Consulting Services</h3>
            <p class="feature-description">
              One-on-one sessions with certified financial advisors for personalized investment strategies
            </p>
          </div>
          
          <div class="feature-card">
            <span class="feature-icon">�</span>
            <h3 class="feature-title">Market Insights</h3>
            <p class="feature-description">
              Real-time market data, trends analysis, and weekly investment newsletter
            </p>
          </div>
        </div>
      </section>

      <!-- Recent Content/Posts -->
      <section class="recent-posts">
        <h2 class="section-title">Latest Investment Insights</h2>
        
        <div class="post-list">
          <article class="post-item">
            <h3 class="post-title">
              <a href="#">Hiểu về tâm lý thị trường trong đầu tư chứng khoán</a>
            </h3>
            <p class="post-excerpt">
              Tâm lý đầu tư đóng vai trò quan trọng trong việc ra quyết định. Bài viết này sẽ giúp bạn 
              nhận diện và kiểm soát cảm xúc khi đầu tư...
            </p>
            <div class="post-date">3 days ago</div>
          </article>
          
          <article class="post-item">
            <h3 class="post-title">
              <a href="#">Xây dựng danh mục đầu tư đa dạng cho người mới bắt đầu</a>
            </h3>
            <p class="post-excerpt">
              Diversification là nguyên tắc cơ bản nhất trong đầu tư. Hướng dẫn chi tiết cách phân bổ 
              tài sản hiệu quả...
            </p>
            <div class="post-date">1 week ago</div>
          </article>
          
          <article class="post-item">
            <h3 class="post-title">
              <a href="#">Phân tích kỹ thuật vs Phân tích cơ bản: Nên chọn phương pháp nào?</a>
            </h3>
            <p class="post-excerpt">
              So sánh chi tiết hai phương pháp phân tích đầu tư phổ biến và cách áp dụng phù hợp 
              với từng nhà đầu tư...
            </p>
            <div class="post-date">2 weeks ago</div>
          </article>
          
          <article class="post-item">
            <h3 class="post-title">
              <a href="#">Chiến lược đầu tư dài hạn trong thời đại số</a>
            </h3>
            <p class="post-excerpt">
              Công nghệ thay đổi cách chúng ta đầu tư. Khám phá những chiến lược mới phù hợp 
              với kỷ nguyên digital...
            </p>
            <div class="post-date">3 weeks ago</div>
          </article>
        </div>
        
        <div style="text-align: center; margin-top: 2rem;">
          <a href="${pageContext.request.contextPath}/public/blog.jsp" class="read-more" 
             style="padding: 0.75rem 2rem; border: 1px solid #667eea; border-radius: 6px; text-decoration: none;">
            View All Posts
          </a>
        </div>
      </section>
    </main>

    <!-- AI Chat Widget for all users -->
    <%@include file="includes/ai-chat-widget.jsp" %>

    <script>
      // Add hover effects to feature cards
      document.addEventListener("DOMContentLoaded", function() {
        const featureCards = document.querySelectorAll(".feature-card");
        featureCards.forEach((card) => {
          card.addEventListener("mouseenter", function() {
            this.style.transform = "translateY(-10px)";
            this.style.boxShadow = "0 10px 25px rgba(0, 0, 0, 0.15)";
          });
          
          card.addEventListener("mouseleave", function() {
            this.style.transform = "translateY(0)";
            this.style.boxShadow = "none";
          });
        });
      });
    </script>
  </body>
</html>
