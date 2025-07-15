<%-- 
    Document   : blog
    Created on : Jun 9, 2025, 9:08:53 PM
    Author     : Admin
    Updated on: Jul 15, 2025
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
        /* Substack-inspired styling similar to homepage */
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
            text-decoration: none;
            display: inline-flex;
            align-items: center;
        }
        
        .subscribe-btn:hover {
            background: #5a6fd8;
        }
        
        .blog-filters {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 30px;
            flex-wrap: wrap;
            gap: 1rem;
        }
        
        .blog-categories {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }
        
        .category-tag {
            display: inline-block;
            padding: 6px 14px;
            background: #f8f9fa;
            border: 1px solid #e1e5e9;
            border-radius: 20px;
            color: #667eea;
            font-size: 0.9rem;
            cursor: pointer;
            transition: all 0.2s ease;
            text-decoration: none;
        }
        
        .category-tag:hover, .category-tag.active {
            background: #667eea;
            color: white;
            border-color: #667eea;
        }
        
        .blog-search {
            position: relative;
            width: 100%;
            max-width: 300px;
        }
        
        .search-input {
            width: 100%;
            padding: 10px 15px;
            padding-right: 40px;
            border: 1px solid #e1e5e9;
            border-radius: 25px;
            font-size: 0.95rem;
            outline: none;
            transition: border-color 0.3s;
        }
        
        .search-input:focus {
            border-color: #667eea;
        }
        
        .search-icon {
            position: absolute;
            right: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #999;
            cursor: pointer;
        }
        
        .blog-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(330px, 1fr));
            gap: 30px;
        }
        
        .blog-card {
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            transition: transform 0.3s, box-shadow 0.3s;
        }
        
        .blog-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px rgba(0,0,0,0.1);
        }
        
        .blog-image {
            height: 200px;
            background-size: cover;
            background-position: center;
        }
        
        .blog-content {
            padding: 25px;
        }
        
        .blog-topic {
            display: inline-block;
            padding: 4px 12px;
            background: #f0f0f0;
            border-radius: 15px;
            color: #666;
            font-size: 0.8rem;
            margin-bottom: 10px;
        }
        
        .blog-name {
            font-size: 1.25rem;
            font-weight: 600;
            margin-bottom: 12px;
            line-height: 1.4;
            color: #333;
        }
        
        .blog-name a {
            color: #333;
            text-decoration: none;
        }
        
        .blog-name a:hover {
            color: #667eea;
        }
        
        .blog-excerpt {
            color: #666;
            font-size: 0.95rem;
            margin-bottom: 15px;
            line-height: 1.6;
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
            color: #999;
            font-size: 0.85rem;
        }
        
        .blog-read-more {
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
            display: inline-flex;
            align-items: center;
            gap: 5px;
        }
        
        .blog-read-more:hover {
            text-decoration: underline;
        }
        
        .blog-featured {
            margin-bottom: 50px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 15px;
            overflow: hidden;
            display: grid;
            grid-template-columns: 1fr 1fr;
        }
        
        .featured-image {
            height: 100%;
            min-height: 350px;
            background-size: cover;
            background-position: center;
        }
        
        .featured-content {
            padding: 40px;
            color: white;
        }
        
        .featured-tag {
            display: inline-block;
            background: rgba(255,255,255,0.2);
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.85rem;
            margin-bottom: 15px;
        }
        
        .featured-title {
            font-size: 2rem;
            font-weight: 700;
            margin-bottom: 15px;
            line-height: 1.3;
        }
        
        .featured-excerpt {
            margin-bottom: 25px;
            opacity: 0.9;
            line-height: 1.6;
        }
        
        .featured-button {
            display: inline-block;
            background: white;
            color: #667eea;
            padding: 12px 25px;
            border-radius: 25px;
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
            margin: 3rem 0;
            gap: 5px;
        }
        
        .page-link {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 40px;
            height: 40px;
            border: 1px solid #e1e5e9;
            border-radius: 5px;
            color: #666;
            text-decoration: none;
            transition: all 0.2s;
        }
        
        .page-link:hover, .page-link.active {
            background: #667eea;
            color: white;
            border-color: #667eea;
        }
        
        .newsletter-section {
            background: #f8f9fa;
            padding: 50px;
            border-radius: 10px;
            margin-top: 60px;
            text-align: center;
        }
        
        .newsletter-title {
            font-size: 1.5rem;
            margin-bottom: 15px;
            color: #333;
        }
        
        .newsletter-description {
            color: #666;
            max-width: 600px;
            margin: 0 auto 25px;
        }
        
        .newsletter-form {
            display: flex;
            max-width: 500px;
            margin: 0 auto;
            gap: 10px;
        }
        
        .newsletter-input {
            flex: 1;
            padding: 12px 20px;
            border: 1px solid #ddd;
            border-radius: 25px;
            outline: none;
            font-size: 1rem;
        }
        
        .newsletter-input:focus {
            border-color: #667eea;
        }
        
        .newsletter-button {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 12px 25px;
            border-radius: 25px;
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
                font-size: 2rem;
            }
            
            .subscribe-form {
                flex-direction: column;
            }
            
            .blog-filters {
                flex-direction: column;
                align-items: flex-start;
                gap: 15px;
            }
            
            .blog-search {
                width: 100%;
                max-width: none;
            }
            
            .newsletter-form {
                flex-direction: column;
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
    <!-- Include the appropriate header based on user role -->
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
        <!-- Hero Section -->
        <section class="hero-section">
            <h1 class="hero-title">Investment Insights</h1>
            <p class="hero-subtitle">
                Explore our latest articles on investment strategies, market trends, financial education, 
                and expert insights to help you make informed financial decisions.
            </p>
            
            <div class="author-info">
                <div class="author-avatar">Y</div>
                <span>By Yến Phạm & NangInvest Team</span>
                <span>•</span>
                <span>Updated daily</span>
            </div>
            
            <!-- Subscribe Section for non-users -->
            <c:if test="${sessionScope.user == null}">
                <div class="subscribe-section">
                    <h3>Subscribe to get our latest investment insights</h3>
                    <p>Join thousands of investors learning to make smarter financial decisions</p>
                    <form class="subscribe-form" action="${pageContext.request.contextPath}/public/register.jsp">
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
                    <p>Continue exploring our investment insights and educational content</p>
                    <div style="display: flex; gap: 1rem; justify-content: center; margin-top: 1rem;">
                        <a href="${pageContext.request.contextPath}/dashboard" class="subscribe-btn">Go to Dashboard</a>
                        <a href="${pageContext.request.contextPath}/user/courses.jsp" class="blog-read-more" 
                           style="padding: 0.75rem 1.5rem; border: 1px solid #667eea; border-radius: 6px; text-decoration: none;">Browse Courses</a>
                    </div>
                </div>
            </c:if>
        </section>

        <!-- Blog Filters -->
        <div class="blog-filters">
            <div class="blog-categories">
                <a href="${pageContext.request.contextPath}/public/blog" class="category-tag ${empty param.topic ? 'active' : ''}">All</a>
                <c:forEach items="${topics}" var="topic">
                    <a href="${pageContext.request.contextPath}/public/blog?topic=${topic}" class="category-tag ${param.topic eq topic ? 'active' : ''}">${topic}</a>
                </c:forEach>
                <!-- Default categories if no dynamic data available -->
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

        <!-- Featured Blog Post -->
        <c:if test="${not empty featuredBlog}">
            <section class="blog-featured">
                <div class="featured-image" style="background-image: url('${featuredBlog.imageUrl != null ? featuredBlog.imageUrl : pageContext.request.contextPath.concat('/images/default-blog.jpg')}')"></div>
                <div class="featured-content">
                    <span class="featured-tag">${featuredBlog.topic}</span>
                    <h2 class="featured-title">${featuredBlog.blogName}</h2>
                    <p class="featured-excerpt">
                        ${fn:substring(featuredBlog.detailedContent.replaceAll("<[^>]*>", ""), 0, 150)}...
                    </p>
                    <a href="${pageContext.request.contextPath}/blog?id=${featuredBlog.blogId}" class="featured-button">Read Article</a>
                </div>
            </section>
        </c:if>

        <!-- Blog Posts -->
        <section>
            <h2 class="section-title">Latest Articles</h2>
            
            <!-- Debug Output -->
            <p>Debug: blogs size = ${fn:length(blogs)}</p>
            <p>Debug: blogs = ${blogs}</p>
            <p>Debug: featuredBlog = ${featuredBlog != null ? featuredBlog.blogName : 'null'}</p>
            <p>Debug: topics = ${topics}</p>
            <p>Debug: currentPage = ${currentPage}, totalPages = ${totalPages}</p>
            
            <!-- Error Handling -->
            <c:if test="${not empty error}">
                <div style="text-align: center; color: red; font-size: 1.1rem; margin: 2rem 0;">
                    ${error}
                </div>
            </c:if>
            
            <div class="blog-grid">
                <c:choose>
                    <c:when test="${not empty blogs}">
                        <c:forEach items="${blogs}" var="blog">
                            <article class="blog-card">
                                <span class="blog-topic">${blog.topic}</span>
                                <h3 class="blog-name">
                                    <a href="${pageContext.request.contextPath}/blog?id=${blog.blogId}">${blog.blogName}</a>
                                </h3>
                                <p class="blog-excerpt">
                                    ${fn:substring(blog.detailedContent.replaceAll("<[^>]*>", ""), 0, 150)}...
                                </p>
                                <div class="blog-meta">
                                    <span>Published</span>
                                    <a href="${pageContext.request.contextPath}/blog?id=${blog.blogId}" class="blog-read-more">
                                        Read More →
                                    </a>
                                </div>
                            </article>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div style="text-align:center; color:#888; font-size:1.1rem; margin:2rem 0;">No blog articles found.</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
        
        <!-- Pagination -->
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

        <!-- Admin Blog Management Button -->
        <c:if test="${sessionScope.user != null && sessionScope.user.role == 'ADMIN'}">
            <div style="text-align: center; margin: 2rem 0;">
                <a href="${pageContext.request.contextPath}/admin/blog_list.jsp" class="subscribe-btn" style="background: #764ba2; color: white; padding: 0.75rem 2rem; border-radius: 8px; font-size: 1rem; font-weight: 600; text-decoration: none;">Manage Blogs</a>
            </div>
        </c:if>
        
        <!-- Newsletter Section (for public users only) -->
        <c:if test="${sessionScope.user == null}">
            <section class="newsletter-section">
                <h2 class="newsletter-title">Subscribe to Our Investment Newsletter</h2>
                <p class="newsletter-description">
                    Get the latest investment insights, market analysis, and financial tips delivered directly to your inbox.
                </p>
                <form class="newsletter-form" action="${pageContext.request.contextPath}/subscribe" method="POST">
                    <input type="email" class="newsletter-input" placeholder="Your email address" required>
                    <button type="submit" class="newsletter-button">Subscribe</button>
                </form>
            </section>
        </c:if>
    </main>
    
    <!-- AI Chat Widget -->
    <%@include file="../includes/ai-chat-widget.jsp" %>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Blog card hover effect
            const blogCards = document.querySelectorAll('.blog-card');
            blogCards.forEach(card => {
                card.addEventListener('mouseenter', () => {
                    card.style.transform = 'translateY(-10px)';
                });
                
                card.addEventListener('mouseleave', () => {
                    card.style.transform = 'translateY(0)';
                });
            });
            
            // Search icon click handler
            const searchIcon = document.querySelector('.search-icon');
            if (searchIcon) {
                searchIcon.addEventListener('click', function() {
                    this.closest('form').submit();
                });
            }
        });
    </script>
</body>
</html>