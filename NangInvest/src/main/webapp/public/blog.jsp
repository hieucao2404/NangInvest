<%-- 
    Document   : blog
    Created on : Jun 9, 2025, 9:08:53 PM
    Author     : Admin
    Updated on : Jul 15, 2025
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Investment Insights - NangInvest Blog</title>
        <style>
            :root {
                --primary-color: #667eea;
                --secondary-color: #764ba2;
                --text-dark: #1a1a1a;
                --text-gray: #666;
                --bg-light: #f8f9fa;
                --border-color: #e1e5e9;
            }

            body {
                font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", system-ui, sans-serif;
                margin: 0;
                padding: 0;
                background-color: #ffffff;
                color: var(--text-dark);
                line-height: 1.6;
            }

            .main-container {
                max-width: 720px;
                margin: 0 auto;
                padding: 0 1.5rem;
            }

            .hero-section {
                padding: 3.5rem 0;
                text-align: center;
                border-bottom: 1px solid var(--border-color);
            }

            .hero-title {
                font-size: 2.5rem;
                font-weight: 700;
                margin: 0 0 0.5rem;
                color: var(--text-dark);
            }

            .hero-subtitle {
                font-size: 1.125rem;
                color: var(--text-gray);
                max-width: 600px;
                margin: 0 auto 1.5rem;
            }

            .author-info {
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 0.75rem;
                color: var(--text-gray);
                font-size: 0.9rem;
            }

            .author-avatar {
                width: 28px;
                height: 28px;
                border-radius: 50%;
                background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
                display: flex;
                align-items: center;
                justify-content: center;
                color: white;
                font-weight: 600;
                font-size: 0.75rem;
            }

            .subscribe-section {
                background: var(--bg-light);
                border: 1px solid var(--border-color);
                border-radius: 8px;
                padding: 1.5rem;
                margin: 2rem 0;
                text-align: center;
            }

            .subscribe-form {
                display: flex;
                gap: 0.5rem;
                max-width: 400px;
                margin: 1rem auto 0;
            }

            .email-input {
                flex: 1;
                padding: 0.75rem;
                border: 1px solid var(--border-color);
                border-radius: 6px;
                font-size: 0.95rem;
                outline: none;
            }

            .email-input:focus {
                border-color: var(--primary-color);
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
            }

            .subscribe-btn {
                background: var(--primary-color);
                color: white;
                border: none;
                padding: 0.75rem 1.5rem;
                border-radius: 6px;
                font-size: 0.95rem;
                font-weight: 600;
                cursor: pointer;
                transition: background-color 0.2s;
            }

            .subscribe-btn:hover {
                background: #5a6fd8;
            }

            .blog-filters {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 2rem;
                flex-wrap: wrap;
                gap: 1rem;
            }

            .blog-categories {
                display: flex;
                gap: 0.5rem;
                flex-wrap: wrap;
            }

            .category-tag {
                padding: 0.5rem 1rem;
                background: var(--bg-light);
                border: 1px solid var(--border-color);
                border-radius: 20px;
                color: var(--primary-color);
                font-size: 0.85rem;
                text-decoration: none;
                transition: all 0.2s;
            }

            .category-tag:hover, .category-tag.active {
                background: var(--primary-color);
                color: white;
                border-color: var(--primary-color);
            }

            .blog-search {
                position: relative;
                max-width: 280px;
                width: 100%;
            }

            .search-input {
                width: 100%;
                padding: 0.75rem 2.5rem 0.75rem 1rem;
                border: 1px solid var(--border-color);
                border-radius: 20px;
                font-size: 0.9rem;
                outline: none;
            }

            .search-input:focus {
                border-color: var(--primary-color);
            }

            .search-icon {
                position: absolute;
                right: 1rem;
                top: 50%;
                transform: translateY(-50%);
                color: var(--text-gray);
                cursor: pointer;
            }

            .blog-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
                gap: 1.5rem;
                margin-bottom: 2rem;
            }

            .blog-card {
                background: white;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
                transition: transform 0.3s, box-shadow 0.3s;
            }

            .blog-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
            }

            .blog-image {
                height: 180px;
                background-size: cover;
                background-position: center;
            }

            .blog-content {
                padding: 1.25rem;
            }

            .blog-topic {
                display: inline-block;
                padding: 0.25rem 0.75rem;
                background: var(--bg-light);
                border-radius: 12px;
                color: var(--text-gray);
                font-size: 0.8rem;
                margin-bottom: 0.5rem;
            }

            .blog-name {
                font-size: 1.2rem;
                font-weight: 600;
                margin-bottom: 0.75rem;
                color: var(--text-dark);
            }

            .blog-name a {
                color: var(--text-dark);
                text-decoration: none;
            }

            .blog-name a:hover {
                color: var(--primary-color);
            }

            .blog-excerpt {
                color: var(--text-gray);
                font-size: 0.9rem;
                margin-bottom: 1rem;
                line-height: 1.5;
                display: -webkit-box;
                -webkit-line-clamp: 3;
                line-clamp: 3;
                -webkit-box-orient: vertical;
                overflow: hidden;
            }

            .blog-meta {
                display: flex;
                justify-content: space-between;
                align-items: center;
                color: var(--text-gray);
                font-size: 0.85rem;
            }

            .blog-read-more {
                color: var(--primary-color);
                text-decoration: none;
                font-weight: 500;
            }

            .blog-read-more:hover {
                text-decoration: underline;
            }

            .blog-featured {
                margin-bottom: 3rem;
                background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
                border-radius: 12px;
                display: grid;
                grid-template-columns: 1fr 1fr;
                overflow: hidden;
            }

            .featured-image {
                height: 100%;
                min-height: 320px;
                background-size: cover;
                background-position: center;
            }

            .featured-content {
                padding: 2rem;
                color: white;
            }

            .featured-tag {
                background: rgba(255, 255, 255, 0.2);
                padding: 0.3rem 0.9rem;
                border-radius: 15px;
                font-size: 0.85rem;
                margin-bottom: 1rem;
            }

            .featured-title {
                font-size: 1.75rem;
                font-weight: 700;
                margin-bottom: 0.75rem;
            }

            .featured-excerpt {
                font-size: 0.95rem;
                margin-bottom: 1.5rem;
                opacity: 0.9;
            }

            .featured-button {
                background: white;
                color: var(--primary-color);
                padding: 0.75rem 1.5rem;
                border-radius: 20px;
                font-weight: 600;
                text-decoration: none;
                transition: transform 0.2s;
            }

            .featured-button:hover {
                transform: translateY(-2px);
            }

            .pagination-section {
                display: flex;
                justify-content: center;
                gap: 0.5rem;
                margin: 2rem 0;
            }

            .page-link {
                display: flex;
                align-items: center;
                justify-content: center;
                width: 36px;
                height: 36px;
                border: 1px solid var(--border-color);
                border-radius: 6px;
                color: var(--text-gray);
                text-decoration: none;
                transition: all 0.2s;
            }

            .page-link:hover, .page-link.active {
                background: var(--primary-color);
                color: white;
                border-color: var(--primary-color);
            }

            .newsletter-section {
                background: var(--bg-light);
                padding: 2.5rem;
                border-radius: 10px;
                margin-top: 3rem;
                text-align: center;
            }

            .newsletter-title {
                font-size: 1.5rem;
                margin-bottom: 0.75rem;
                color: var(--text-dark);
            }

            .newsletter-description {
                color: var(--text-gray);
                max-width: 500px;
                margin: 0 auto 1.5rem;
            }

            .newsletter-form {
                display: flex;
                max-width: 450px;
                margin: 0 auto;
                gap: 0.5rem;
            }

            .newsletter-input {
                flex: 1;
                padding: 0.75rem 1rem;
                border: 1px solid var(--border-color);
                border-radius: 20px;
                font-size: 0.95rem;
                outline: none;
            }

            .newsletter-input:focus {
                border-color: var(--primary-color);
            }

            .newsletter-button {
                background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
                color: white;
                border: none;
                padding: 0.75rem 1.5rem;
                border-radius: 20px;
                font-weight: 600;
                cursor: pointer;
                transition: transform 0.2s;
            }

            .newsletter-button:hover {
                transform: translateY(-2px);
            }

            @media (max-width: 768px) {
                .main-container {
                    padding: 0 1rem;
                }

                .hero-title {
                    font-size: 1.75rem;
                }

                .subscribe-form, .newsletter-form {
                    flex-direction: column;
                }

                .blog-filters {
                    flex-direction: column;
                    align-items: flex-start;
                }

                .blog-search {
                    max-width: 100%;
                }

                .blog-featured {
                    grid-template-columns: 1fr;
                }

                .featured-image {
                    height: 200px;
                }
            }
        </style>
    </head>
    <body>
        <c:choose>
            <c:when test="${sessionScope.user != null && sessionScope.user.role == 'ADMIN'}">
                <%@include file="../includes/admin-header.jsp" %>
            </c:when>
            <c:when test="${sessionScope.user != null && sessionScope.user.role == 'USER'}">
                <%@include file="../includes/user-header.jsp" %>
            </c:when>
            <c:otherwise>
                <%@include file="../includes/public-header.jsp" %>
            </c:otherwise>
        </c:choose>

        <main class="main-container">
            <section class="hero-section">
                <h1 class="hero-title">Investment Insights</h1>
                <p class="hero-subtitle">
                    Discover expert articles on investment strategies, market trends, and financial education.
                </p>
                <div class="author-info">
                    <div class="author-avatar">Y</div>
                    <span>By Yến Phạm & NangInvest Team</span>
                    <span> • </span>
                    <span>Updated daily</span>
                </div>

                <c:if test="${sessionScope.user == null}">
                    <div class="subscribe-section">
                        <h3>Subscribe for Investment Insights</h3>
                        <p>Join thousands learning smarter financial decisions.</p>
                        <form class="subscribe-form" action="${pageContext.request.contextPath}/public/register.jsp">
                            <input type="email" class="email-input" placeholder="Enter your email" required>
                            <button type="submit" class="subscribe-btn">Subscribe</button>
                        </form>
                        <small style="color: var(--text-gray); margin-top: 0.75rem; display: block;">
                            By subscribing, I agree to the Terms of Use and Privacy Policy
                        </small>
                    </div>
                </c:if>

                <c:if test="${sessionScope.user != null}">
                    <div class="subscribe-section">
                        <h3>Welcome back, ${sessionScope.user.name != null ? sessionScope.user.name : sessionScope.user.userName}!</h3>
                        <p>Explore our latest investment insights.</p>
                        <div style="display: flex; gap: 0.75rem; justify-content: center; margin-top: 1rem;">
                            <a href="${pageContext.request.contextPath}/dashboard" class="subscribe-btn">Go to Dashboard</a>
                            <a href="${pageContext.request.contextPath}/user/courses.jsp" class="blog-read-more" style="padding: 0.75rem 1.5rem; border: 1px solid var(--primary-color); border-radius: 6px; text-decoration: none;">Browse Courses</a>
                        </div>
                    </div>
                </c:if>
            </section>

            <div class="blog-filters">
                <div class="blog-categories">
                    <a href="${pageContext.request.contextPath}/public/blog" class="category-tag ${empty param.topic ? 'active' : ''}">All</a>
                    <c:forEach items="${topics}" var="topic">
                        <a href="${pageContext.request.contextPath}/public/blog?topic=${topic}" class="category-tag ${param.topic eq topic ? 'active' : ''}">${topic}</a>
                    </c:forEach>
                    <c:if test="${empty topics}">
                        <a href="${pageContext.request.contextPath}/public/blog?topic=Investing" class="category-tag ${param.topic eq 'Investing' ? 'active' : ''}">Investing</a>
                        <a href="${pageContext.request.contextPath}/public/blog?topic=Market Analysis" class="category-tag ${param.topic eq 'Market Analysis' ? 'active' : ''}">Market Analysis</a>
                        <a href="${pageContext.request.contextPath}/public/blog?topic=Financial Planning" class="category-tag ${param.topic eq 'Financial Planning' ? 'active' : ''}">Financial Planning</a>
                        <a href="${pageContext.request.contextPath}/public/blog?topic=Investment Strategy" class="category-tag ${param.topic eq 'Investment Strategy' ? 'active' : ''}">Investment Strategy</a>
                        <a href="${pageContext.request.contextPath}/public/blog?topic=Personal Finance" class="category-tag ${param.topic eq 'Personal Finance' ? 'active' : ''}">Personal Finance</a>
                    </c:if>
                </div>
                <div class="blog-search">
                    <form action="${pageContext.request.contextPath}/public/blog" method="GET">
                        <input type="text" name="search" class="search-input" placeholder="Search articles..." value="${param.search}">
                        <span class="search-icon">🔍</span>
                    </form>
                </div>
            </div>

            <c:if test="${not empty featuredBlog}">
                <section class="blog-featured">
                    <div class="featured-image" style="background-image: url('${featuredBlog.imageUrl != null ? featuredBlog.imageUrl : pageContext.request.contextPath.concat('/images/default-blog.jpg')}')"></div>
                    <div class="featured-content">
                        <span class="featured-tag">${featuredBlog.topic}</span>
                        <h2 class="featured-title">${featuredBlog.blogName}</h2>
                        <p class="featured-excerpt">${fn:substring(featuredBlog.detailedContent.replaceAll("<[^>]*>", ""), 0, 120)}...</p>
                        <a href="${pageContext.request.contextPath}/blog?id=${featuredBlog.blogId}" class="featured-button">Read Article</a>
                    </div>
                </section>
            </c:if>

            <section>
                <h2 class="section-title">Latest Articles</h2>
                <div class="blog-grid">
                    <c:choose>
                        <c:when test="${not empty blogs}">
                            <c:forEach items="${blogs}" var="blog">
                                <article class="blog-card">
                                    <div class="blog-image" style="background-image: url('${blog.imageUrl != null ? blog.imageUrl : pageContext.request.contextPath.concat('/images/default-blog.jpg')}')"></div>
                                    <div class="blog-content">
                                        <span class="blog-topic">${blog.topic}</span>
                                        <h3 class="blog-name">
                                            <a href="${pageContext.request.contextPath}/blog?id=${blog.blogId}">${blog.blogName}</a>
                                        </h3>
                                        <p class="blog-excerpt">${fn:substring(blog.detailedContent.replaceAll("<[^>]*>", ""), 0, 120)}...</p>
                                        <div class="blog-meta">
                                            <span>Published</span>
                                            <a href="${pageContext.request.contextPath}/blog?id=${blog.blogId}" class="blog-read-more">Read More →</a>
                                        </div>
                                    </div>
                                </article>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div style="text-align: center; color: var(--text-gray); font-size: 1rem; margin: 2rem 0;">No blog articles found.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>

            <div class="pagination-section">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/public/blog?page=${currentPage - 1}${not empty param.topic ? '&topic='.concat(param.topic) : ''}${not empty param.search ? '&search='.concat(param.search) : ''}" class="page-link">‹</a>
                </c:if>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="${pageContext.request.contextPath}/public/blog?page=${i}${not empty param.topic ? '&topic='.concat(param.topic) : ''}${not empty param.search ? '&search='.concat(param.search) : ''}" class="page-link ${i == currentPage ? 'active' : ''}">${i}</a>
                </c:forEach>
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/public/blog?page=${currentPage + 1}${not empty param.topic ? '&topic='.concat(param.topic) : ''}${not empty param.search ? '&search='.concat(param.search) : ''}" class="page-link">›</a>
                </c:if>
            </div>

            <c:if test="${sessionScope.user != null && sessionScope.user.role == 'ADMIN'}">
                <div style="text-align: center; margin: 2rem 0;">
                    <a href="${pageContext.request.contextPath}/admin/blog_list.jsp" class="subscribe-btn" style="background: var(--secondary-color);">Manage Blogs</a>
                </div>
            </c:if>

            <c:if test="${sessionScope.user == null}">
                <section class="newsletter-section">
                    <h2 class="newsletter-title">Join Our Investment Newsletter</h2>
                    <p class="newsletter-description">Get the latest investment insights and financial tips in your inbox.</p>
                    <form class="newsletter-form" action="${pageContext.request.contextPath}/subscribe" method="POST">
                        <input type="email" class="newsletter-input" placeholder="Your email address" required>
                        <button type="submit" class="newsletter-button">Subscribe</button>
                    </form>
                </section>
            </c:if>
        </main>

        <%@include file="../includes/ai-chat-widget.jsp" %>

        <script>
            document.addEventListener('DOMContentLoaded', () => {
                const blogCards = document.querySelectorAll('.blog-card');
                blogCards.forEach(card => {
                    card.addEventListener('mouseenter', () => card.style.transform = 'translateY(-5px)');
                    card.addEventListener('mouseleave', () => card.style.transform = 'translateY(0)');
                });

                const searchIcon = document.querySelector('.search-icon');
                if (searchIcon) {
                    searchIcon.addEventListener('click', () => searchIcon.closest('form').submit());
                }
            });
        </script>
    </body>
</html>