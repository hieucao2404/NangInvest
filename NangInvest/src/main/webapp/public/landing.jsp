<%-- Document : landing Created on : Jul 11, 2025, 10:30:00 AM Author : Admin
Description: Landing page for visitors (before login) with English text --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>NangInvest - Smart Investing with Yen Pham</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/style.css"
    />
    <style>
      body {
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
          sans-serif;
        margin: 0;
        padding: 0;
        background-color: #f8f9fa;
        color: #333;
        line-height: 1.6;
      }
      .header {
        background: white;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        padding: 1rem 0;
        position: sticky;
        top: 0;
        z-index: 100;
      }
      .container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 20px;
      }
      .nav {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      .logo {
        font-size: 1.8rem;
        font-weight: bold;
        color: #2563eb;
      }
      .nav-links {
        display: flex;
        gap: 2rem;
        align-items: center;
      }
      .nav-links a {
        text-decoration: none;
        color: #4b5563;
        font-weight: 500;
        transition: color 0.2s;
      }
      .nav-links a:hover {
        color: #2563eb;
      }

      /* Hero section with author image */
      .hero-section {
        background: linear-gradient(135deg, #f0f4ff, #e0e7ff);
        padding: 60px 0 40px;
      }
      .hero-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        text-align: center;
      }
      .author-image {
        width: 180px;
        height: 180px;
        border-radius: 50%;
        object-fit: cover;
        margin-bottom: 24px;
        border: 4px solid white;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }
      .hero-title {
        font-size: 2.5rem;
        color: #1e3a8a;
        margin-bottom: 16px;
        font-weight: 700;
      }
      .hero-subtitle {
        font-size: 1.5rem;
        color: #4b5563;
        margin-bottom: 24px;
        max-width: 700px;
      }

      /* Social links */
      .social-links {
        display: flex;
        gap: 16px;
        margin: 24px 0;
        justify-content: center;
      }
      .social-link {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background-color: #fff;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #2563eb;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        transition: transform 0.2s, box-shadow 0.2s;
      }
      .social-link:hover {
        transform: translateY(-3px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }

      /* Content sections */
      .content-section {
        padding: 60px 0;
      }
      .section-title {
        text-align: center;
        font-size: 2rem;
        color: #1e3a8a;
        margin-bottom: 48px;
        position: relative;
      }
      .section-title:after {
        content: "";
        position: absolute;
        bottom: -12px;
        left: 50%;
        transform: translateX(-50%);
        width: 60px;
        height: 3px;
        background-color: #2563eb;
      }

      /* Blog cards */
      .blog-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
        gap: 32px;
        margin-top: 48px;
      }
      .blog-card {
        background: white;
        border-radius: 8px;
        overflow: hidden;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        transition: transform 0.3s, box-shadow 0.3s;
      }
      .blog-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12);
      }
      .blog-image {
        width: 100%;
        height: 200px;
        object-fit: cover;
      }
      .blog-content {
        padding: 24px;
      }
      .blog-date {
        font-size: 0.875rem;
        color: #6b7280;
        margin-bottom: 8px;
      }
      .blog-title {
        font-size: 1.25rem;
        color: #1e3a8a;
        margin-bottom: 12px;
        font-weight: 600;
        line-height: 1.4;
      }
      .blog-excerpt {
        font-size: 0.95rem;
        color: #4b5563;
        margin-bottom: 16px;
      }

      /* Newsletter section */
      .newsletter-section {
        background-color: #f3f4f6;
        padding: 60px 0;
        text-align: center;
      }
      .newsletter-container {
        max-width: 600px;
        margin: 0 auto;
      }
      .newsletter-title {
        font-size: 1.75rem;
        color: #1e3a8a;
        margin-bottom: 16px;
      }
      .newsletter-description {
        font-size: 1.1rem;
        color: #4b5563;
        margin-bottom: 24px;
      }
      .newsletter-form {
        display: flex;
        gap: 8px;
        margin-top: 24px;
      }
      .newsletter-input {
        flex: 1;
        padding: 12px 16px;
        border: 1px solid #d1d5db;
        border-radius: 6px;
        font-size: 1rem;
      }

      /* Buttons */
      .btn {
        display: inline-block;
        padding: 12px 24px;
        background: #2563eb;
        color: white;
        text-decoration: none;
        border-radius: 6px;
        font-weight: 500;
        transition: all 0.2s ease;
        border: none;
        cursor: pointer;
        font-family: inherit;
        font-size: 1rem;
      }
      .btn:hover {
        background: #1d4ed8;
        transform: translateY(-2px);
      }
      .btn-outline {
        background: transparent;
        border: 2px solid #2563eb;
        color: #2563eb;
      }
      .btn-outline:hover {
        background: #2563eb;
        color: white;
      }

      /* About section */
      .about-section {
        padding: 60px 0;
        background-color: white;
      }
      .about-container {
        display: flex;
        flex-direction: column;
        gap: 32px;
        max-width: 800px;
        margin: 0 auto;
        text-align: center;
      }
      .about-text {
        font-size: 1.1rem;
        color: #4b5563;
        line-height: 1.7;
      }

      /* Footer */
      .footer {
        background: #1f2937;
        color: white;
        text-align: center;
        padding: 40px 0;
      }
      .footer-links {
        display: flex;
        justify-content: center;
        gap: 24px;
        margin-bottom: 24px;
      }
      .footer-links a {
        color: #e5e7eb;
        text-decoration: none;
        transition: color 0.2s;
      }
      .footer-links a:hover {
        color: white;
        text-decoration: underline;
      }

      /* Responsive styles */
      @media (max-width: 768px) {
        .nav-links {
          gap: 1rem;
        }
        .hero-title {
          font-size: 2rem;
        }
        .hero-subtitle {
          font-size: 1.2rem;
        }
        .newsletter-form {
          flex-direction: column;
        }
        .blog-grid {
          grid-template-columns: 1fr;
        }
      }
    </style>
  </head>
  <body>
    <!-- Header Navigation -->
    <header class="header">
      <div class="container">
        <nav class="nav">
          <div class="logo">NangInvest</div>
          <div class="nav-links">
            <a href="${pageContext.request.contextPath}/public/blog.jsp">Blog</a>
            <a href="${pageContext.request.contextPath}/public/courses.jsp">Courses</a>
            <a href="${pageContext.request.contextPath}/homepage.jsp">Home</a>
            <a
              href="${pageContext.request.contextPath}/public/login-registers.jsp"
              class="btn"
              >Login</a
            >
          </div>
        </nav>
      </div>
    </header>

    <!-- Hero Section with Author Image -->
    <section class="hero-section">
      <div class="container hero-container">
        <img
          src="https://i0.wp.com/thepresentwriter.com/wp-content/uploads/2024/05/DSC01159-round.png"
          alt="Yen Pham"
          class="author-image"
        />
        <h1 class="hero-title">Hello! I'm Yen Pham</h1>
        <p class="hero-subtitle">
          A finance and investment expert with over 15 years of experience. I help
          you build a strong financial foundation through practical investment knowledge.
        </p>
        <div>
          <a
            href="${pageContext.request.contextPath}/public/courses.jsp"
            class="btn"
            >Explore Courses</a
          >
          <a
            href="${pageContext.request.contextPath}/public/blog.jsp"
            class="btn btn-outline"
            >Read Blogs</a
          >
        </div>

        <!-- Social Media Links -->
        <div class="social-links">
          <a href="#" class="social-link"><i>📘</i></a>
          <a href="#" class="social-link"><i>📱</i></a>
          <a href="#" class="social-link"><i>📺</i></a>
          <a href="#" class="social-link"><i>🎙️</i></a>
        </div>
      </div>
    </section>

    <!-- Latest Blog Posts -->
    <section class="content-section">
      <div class="container">
        <h2 class="section-title">Latest Blog Posts</h2>
        <div class="blog-grid">
          <c:choose>
            <c:when test="${not empty latestBlogs}">
              <c:forEach var="blog" items="${latestBlogs}">
                <div class="blog-card">
                  <img
                    src="${blog.imageUrl}"
                    alt="${blog.blogName}"
                    class="blog-image"
                  />
                  <div class="blog-content">
                    <span class="blog-topic">${blog.topic}</span>
                    <h3 class="blog-title">
                      <a
                        href="${pageContext.request.contextPath}/public/blogPost.jsp?blogId=${blog.blogId}"
                      >
                        ${blog.blogName}
                      </a>
                    </h3>
                    <p class="blog-excerpt">
                      <c:choose>
                        <c:when test="${not empty blog.detailedContent}">
                          ${fn:substring(blog.detailedContent.replaceAll("<[^>]*>", ""), 0, 120)}...
                        </c:when>
                        <c:otherwise>No preview available.</c:otherwise>
                      </c:choose>
                    </p>
                    <a
                      href="${pageContext.request.contextPath}/public/blogPost.jsp?blogId=${blog.blogId}"
                      class="btn btn-outline"
                      >Read More</a
                    >
                  </div>
                </div>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <div
                style="
                  text-align: center;
                  color: #888;
                  font-size: 1.1rem;
                  margin: 2rem 0;
                "
              >
                No blog articles found.
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </section>

    <!-- About Section -->
    <section class="about-section">
      <div class="container about-container">
        <h2 class="section-title">About NangInvest</h2>
        <p class="about-text">
          NangInvest is a platform for sharing practical investment knowledge and
          experience, founded by Yen Pham, a finance expert with over 15 years
          in investment consulting.
        </p>
        <p class="about-text">
          Our mission is to provide high-quality, easy-to-understand, and actionable
          investment education for everyone. We believe anyone can achieve financial
          freedom through learning and applying sound investment principles.
        </p>
        <a
          href="${pageContext.request.contextPath}/public/about.jsp"
          class="btn"
          >Learn More</a
        >
      </div>
    </section>

    <!-- Newsletter Section -->
    <section class="newsletter-section">
      <div class="container newsletter-container">
        <h2 class="newsletter-title">Weekly Investment Newsletter</h2>
        <p class="newsletter-description">
          Subscribe to our free newsletter for weekly market analysis, investment
          opportunities, and helpful financial tips from Yen Pham.
        </p>
        <form class="newsletter-form">
          <input
            type="email"
            placeholder="Your Email"
            class="newsletter-input"
            required
          />
          <button type="submit" class="btn">Subscribe</button>
        </form>
      </div>
    </section>

    <!-- Footer -->
    <footer class="footer">
      <div class="container">
        <div class="footer-links">
          <a href="${pageContext.request.contextPath}/public/about.jsp"
            >About Us</a
          >
          <a href="${pageContext.request.contextPath}/public/courses.jsp"
            >Courses</a
          >
          <a href="${pageContext.request.contextPath}/public/blog.jsp">Blog</a>
          <a href="${pageContext.request.contextPath}/public/contact.jsp"
            >Contact</a
          >
          <a href="${pageContext.request.contextPath}/public/terms.jsp"
            >Terms</a
          >
        </div>
        <p>© 2025 NangInvest. Elevate your investment knowledge.</p>
      </div>
    </footer>

    <!-- Include AI Chat Widget -->
    <%@include file="../includes/ai-chat-widget.jsp" %>

    <script>
      function openAIChat() {
        // Find the actual chat widget elements based on your HTML structure
        const chatWidget = document.querySelector("#ai-chat-widget");
        const chatBox = document.querySelector("#chat-box");
        const chatToggle = document.querySelector("#chat-toggle");
        const messageInput = document.querySelector("#message-input");

        if (chatWidget && chatBox) {
          console.log("Found chat elements, opening chat...");

          // Make sure the chat widget is visible
          chatWidget.style.display = "block";

          // Show the chat box (remove hidden class and set display)
          chatBox.classList.remove("hidden");
          chatBox.style.display = "flex";

          // Hide the toggle button since chat is now open
          if (chatToggle) {
            chatToggle.style.display = "none";
          }

          // Focus on the input field for immediate use
          if (messageInput) {
            setTimeout(() => {
              messageInput.focus();
            }, 100);
          }

          // Scroll to make sure chat is visible
          setTimeout(() => {
            chatWidget.scrollIntoView({
              behavior: "smooth",
              block: "nearest",
            });
          }, 200);
        } else {
          console.log("Chat elements not found, trying fallback...");

          // Fallback: scroll to bottom and try to click the toggle button
          window.scrollTo({
            top: document.body.scrollHeight,
            behavior: "smooth",
          });

          // Try to click the chat toggle button after scrolling
          setTimeout(() => {
            const toggleBtn = document.querySelector(
              "#chat-toggle, .chat-button"
            );
            if (toggleBtn) {
              console.log("Clicking toggle button...");
              toggleBtn.click();
            } else {
              console.log("No toggle button found");
            }
          }, 500);
        }
      }
    </script>
  </body>
</html>