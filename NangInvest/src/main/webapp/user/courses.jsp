<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Investment Courses - NangInvest</title>
    <style>
        /* Modern Substack-inspired course catalog styling */
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", system-ui, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
            color: #1a1a1a;
            line-height: 1.6;
        }

        .courses-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 2rem;
        }

        .courses-header {
            background: white;
            border-radius: 12px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            text-align: center;
        }

        .courses-title {
            font-size: 2.5rem;
            font-weight: 700;
            margin: 0 0 0.5rem 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .courses-subtitle {
            font-size: 1.1rem;
            color: #666;
            margin: 0 0 1.5rem 0;
        }

        .search-filter-section {
            background: white;
            border-radius: 12px;
            padding: 1.5rem;
            margin-bottom: 2rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        }

        .search-form {
            display: flex;
            gap: 1rem;
            margin-bottom: 1rem;
            flex-wrap: wrap;
        }

        .search-input {
            flex: 1;
            min-width: 300px;
            padding: 0.75rem;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 1rem;
        }

        .search-btn {
            padding: 0.75rem 1.5rem;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 600;
            transition: transform 0.2s;
        }

        .search-btn:hover {
            transform: translateY(-1px);
        }

        .filter-tabs {
            display: flex;
            gap: 0.5rem;
            flex-wrap: wrap;
        }

        .filter-tab {
            padding: 0.5rem 1rem;
            background: #f8f9fa;
            border: 1px solid #ddd;
            border-radius: 20px;
            text-decoration: none;
            color: #666;
            font-weight: 500;
            transition: all 0.2s;
        }

        .filter-tab.active,
        .filter-tab:hover {
            background: #667eea;
            color: white;
            border-color: #667eea;
        }

        .course-stats {
            display: flex;
            gap: 1rem;
            margin-top: 1rem;
            font-size: 0.9rem;
            color: #666;
        }

        .courses-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 2rem;
            margin-bottom: 2rem;
        }

        .course-card {
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            transition: all 0.3s ease;
            border: 1px solid #eee;
        }

        .course-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
        }

        .course-image {
            width: 100%;
            height: 200px;
            background: linear-gradient(135deg, #f7fafc 0%, #edf2f7 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 3rem;
            color: #667eea;
            position: relative;
        }

        .course-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .course-badge {
            position: absolute;
            top: 12px;
            right: 12px;
            padding: 0.25rem 0.75rem;
            border-radius: 12px;
            font-size: 0.75rem;
            font-weight: 600;
            text-transform: uppercase;
        }

        .badge-free {
            background: #48bb78;
            color: white;
        }

        .badge-paid {
            background: #ed8936;
            color: white;
        }

        .badge-enrolled {
            background: #4299e1;
            color: white;
        }

        .course-content {
            padding: 1.5rem;
        }

        .course-title {
            font-size: 1.25rem;
            font-weight: 700;
            margin: 0 0 0.75rem 0;
            color: #1a1a1a;
        }

        .course-meta {
            display: flex;
            align-items: center;
            gap: 1rem;
            margin-bottom: 1rem;
            font-size: 0.9rem;
            color: #666;
        }

        .course-price {
            font-size: 1.5rem;
            font-weight: 700;
            color: #667eea;
            margin-bottom: 1rem;
        }

        .course-price.free {
            color: #48bb78;
        }

        .course-actions {
            display: flex;
            gap: 0.75rem;
        }

        .btn {
            padding: 0.75rem 1.25rem;
            border-radius: 6px;
            font-weight: 600;
            text-decoration: none;
            text-align: center;
            cursor: pointer;
            border: none;
            transition: all 0.2s;
            flex: 1;
            font-size: 0.9rem;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
        }

        .btn-success {
            background: #48bb78;
            color: white;
        }

        .btn-info {
            background: #4299e1;
            color: white;
        }

        .btn-disabled {
            background: #e2e8f0;
            color: #a0aec0;
            cursor: not-allowed;
        }

        .progress-container {
            margin-bottom: 1rem;
        }

        .progress-bar {
            width: 100%;
            background: #e0e0e0;
            border-radius: 5px;
            overflow: hidden;
        }

        .progress {
            height: 20px;
            background: #27ae60;
            text-align: center;
            color: white;
            line-height: 20px;
            transition: width 0.3s ease;
        }

        .alert {
            padding: 1rem;
            border-radius: 6px;
            margin-bottom: 1rem;
            font-weight: 500;
        }

        .alert-success {
            background: #f0fff4;
            color: #22543d;
            border: 1px solid #9ae6b4;
        }

        .alert-error {
            background: #fed7d7;
            color: #742a2a;
            border: 1px solid #feb2b2;
        }

        .empty-state {
            text-align: center;
            padding: 3rem;
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        }

        .empty-state-icon {
            font-size: 4rem;
            margin-bottom: 1rem;
            opacity: 0.5;
        }

        .cart-widget {
            position: fixed;
            top: 100px;
            right: 20px;
            background: white;
            border-radius: 50px;
            padding: 0.75rem 1.25rem;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            z-index: 1000;
            transition: all 0.3s ease;
        }

        .cart-widget:hover {
            transform: scale(1.05);
        }

        .cart-widget a {
            text-decoration: none;
            color: #667eea;
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        @media (max-width: 768px) {
            .courses-container {
                padding: 1rem;
            }

            .courses-grid {
                grid-template-columns: 1fr;
                gap: 1rem;
            }

            .search-form {
                flex-direction: column;
            }

            .search-input {
                min-width: auto;
            }

            .filter-tabs {
                justify-content: center;
            }

            .cart-widget {
                position: relative;
                top: auto;
                right: auto;
                margin-bottom: 1rem;
                display: inline-block;
            }
        }
    </style>
</head>
<body>
    <%@include file="../includes/user-header.jsp" %>

    <main class="courses-container">
        <!-- Page Header -->
        <section class="courses-header">
            <h1 class="courses-title">Investment Courses</h1>
            <p class="courses-subtitle">
                Master the art of investing with our comprehensive course library
            </p>
        </section>

        <!-- Alert Messages -->
        <c:if test="${param.success == 'added_to_cart'}">
            <div class="alert alert-success">
                ✅ Course added to cart successfully!
            </div>
        </c:if>
        <c:if test="${param.success == 'enrolled'}">
            <div class="alert alert-success">
                ✅ Successfully enrolled in free course!
            </div>
        </c:if>
        <c:if test="${param.error == 'already_enrolled'}">
            <div class="alert alert-error">
                ⚠️ You are already enrolled in this course.
            </div>
        </c:if>
        <c:if test="${param.error == 'already_in_cart'}">
            <div class="alert alert-error">
                ⚠️ This course is already in your cart.
            </div>
        </c:if>
        <c:if test="${param.error == 'already_purchased'}">
            <div class="alert alert-error">
                ⚠️ You have already purchased this course.
            </div>
        </c:if>
        <c:if test="${param.error == 'invalid_course'}">
            <div class="alert alert-error">
                ⚠️ Invalid course selected.
            </div>
        </c:if>
        <c:if test="${param.error == 'course_not_found'}">
            <div class="alert alert-error">
                ⚠️ Course not found.
            </div>
        </c:if>
        <c:if test="${param.error == 'invalid_course_id'}">
            <div class="alert alert-error">
                ⚠️ Invalid course ID.
            </div>
        </c:if>
        <c:if test="${param.error == 'add_to_cart_failed'}">
            <div class="alert alert-error">
                ⚠️ Failed to add course to cart.
            </div>
        </c:if>

        <!-- Search and Filter Section -->
        <section class="search-filter-section">
            <!-- Search Form -->
            <form action="${pageContext.request.contextPath}/courses" method="get" class="search-form">
                <input type="hidden" name="action" value="search">
                <input type="text" name="query" class="search-input" 
                       placeholder="Search courses..." 
                       value="${searchQuery}">
                <button type="submit" class="search-btn">🔍 Search</button>
            </form>

            <!-- Filter Tabs -->
            <div class="filter-tabs">
                <a href="${pageContext.request.contextPath}/courses" 
                   class="filter-tab ${empty currentFilter ? 'active' : ''}">
                    All Courses
                </a>
                <a href="${pageContext.request.contextPath}/courses?filter=free" 
                   class="filter-tab ${currentFilter == 'free' ? 'active' : ''}">
                    Free Courses
                </a>
                <a href="${pageContext.request.contextPath}/courses?filter=paid" 
                   class="filter-tab ${currentFilter == 'paid' ? 'active' : ''}">
                    Premium Courses
                </a>
            </div>

            <!-- Course Statistics -->
            <div class="course-stats">
                <span>📚 ${totalCourses} Total Courses</span>
                <span>🆓 ${freeCourses} Free</span>
                <span>💎 ${paidCourses} Premium</span>
                <c:if test="${not empty searchQuery}">
                    <span>🔍 ${fn:length(courses)} Results for "${searchQuery}"</span>
                </c:if>
            </div>
        </section>

        <!-- Cart Widget (for logged-in users) -->
        <c:if test="${sessionScope.user != null && cartCount > 0}">
            <div class="cart-widget">
                <a href="${pageContext.request.contextPath}/user/cart">
                    🛒 Cart (${cartCount})
                </a>
            </div>
        </c:if>

        <!-- Courses Grid -->
        <c:choose>
            <c:when test="${not empty courses}">
                <section class="courses-grid">
                    <c:forEach var="course" items="${courses}">
                        <div class="course-card">
                            <!-- Course Image -->
                            <div class="course-image">
                                <c:choose>
                                    <c:when test="${not empty course.imageUrl}">
                                        <img src="${course.imageUrl}" alt="${course.courseName}">
                                    </c:when>
                                    <c:otherwise>
                                        📖
                                    </c:otherwise>
                                </c:choose>
                                
                                <!-- Course Badge -->
                                <c:set var="isEnrolled" value="${sessionScope.user != null && userCoursesDAO.isUserEnrolledInCourse(sessionScope.user.userId, course.courseId)}"/>
                                <c:set var="hasPurchased" value="${sessionScope.user != null && orderDAO.hasUserPurchasedProduct(sessionScope.user.userId, course.courseId)}"/>
                                <c:choose>
                                    <c:when test="${isEnrolled}">
                                        <span class="course-badge badge-enrolled">Enrolled</span>
                                    </c:when>
                                    <c:when test="${course.isFreeOfCharge()}">
                                        <span class="course-badge badge-free">Free</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="course-badge badge-paid">Premium</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <!-- Course Content -->
                            <div class="course-content">
                                <h3 class="course-title">${course.courseName}</h3>
                                
                                <div class="course-meta">
                                    <c:if test="${not empty course.time}">
                                        <span>⏱️ ${course.time}</span>
                                    </c:if>
                                    <span>📊 Beginner to Advanced</span>
                                </div>

                                <!-- Course Price -->
                                <div class="course-price ${course.isFreeOfCharge() ? 'free' : ''}">
                                    <c:choose>
                                        <c:when test="${course.isFreeOfCharge()}">
                                            Free
                                        </c:when>
                                        <c:otherwise>
                                            $<fmt:formatNumber value="${course.price}" pattern="#,##0.00"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <!-- Progress Bar for Enrolled Courses -->
                                <c:if test="${isEnrolled}">
                                    <c:set var="progress" value="${userCoursesDAO.findByUserAndCourse(sessionScope.user.userId, course.courseId).progress}"/>
                                    <div class="progress-container">
                                        <div class="progress-bar">
                                            <div class="progress" style="width: ${progress}%">${progress}%</div>
                                        </div>
                                    </div>
                                </c:if>

                                <!-- Course Actions -->
                                <div class="course-actions">
                                    <c:choose>
                                        <c:when test="${sessionScope.user == null}">
                                            <a href="${pageContext.request.contextPath}/public/login-registers.jsp?redirect=/courses" 
                                               class="btn btn-primary">
                                                Login to Enroll
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="inCart" value="false"/>
                                            <c:forEach var="cartItem" items="${cartItems}">
                                                <c:if test="${cartItem.productId == course.courseId}">
                                                    <c:set var="inCart" value="true"/>
                                                </c:if>
                                            </c:forEach>
                                            <c:choose>
                                                <c:when test="${isEnrolled}">
                                                    <a href="${pageContext.request.contextPath}/course?courseId=${course.courseId}" 
                                                       class="btn btn-info">
                                                        ✅ Enrolled - View Course
                                                    </a>
                                                </c:when>
                                                <c:when test="${hasPurchased && !isEnrolled}">
                                                    <form action="${pageContext.request.contextPath}/courses" method="post" style="width: 100%;">
                                                        <input type="hidden" name="action" value="addToCart">
                                                        <input type="hidden" name="courseId" value="${course.courseId}">
                                                        <button type="submit" class="btn btn-disabled" disabled>
                                                            ✅ Purchased
                                                        </button>
                                                    </form>
                                                </c:when>
                                                <c:when test="${inCart}">
                                                    <a href="${pageContext.request.contextPath}/user/cart" 
                                                       class="btn btn-success">
                                                        🛒 In Cart - Checkout
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <form action="${pageContext.request.contextPath}/courses" method="post" style="width: 100%;">
                                                        <input type="hidden" name="action" value="addToCart">
                                                        <input type="hidden" name="courseId" value="${course.courseId}">
                                                        <button type="submit" class="btn btn-primary">
                                                            <c:choose>
                                                                <c:when test="${course.isFreeOfCharge()}">🆓 Enroll Now</c:when>
                                                                <c:otherwise>🛒 Add to Cart</c:otherwise>
                                                            </c:choose>
                                                        </button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </section>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon">📚</div>
                    <h2>No Courses Found</h2>
                    <p>
                        <c:choose>
                            <c:when test="${not empty searchQuery}">
                                No courses match your search for "${searchQuery}". Try different keywords or browse all courses.
                            </c:when>
                            <c:otherwise>
                                No courses are available at the moment. Check back soon for new content!
                            </c:otherwise>
                        </c:choose>
                    </p>
                    <c:if test="${not empty searchQuery}">
                        <a href="${pageContext.request.contextPath}/courses" class="btn btn-primary">
                            Browse All Courses
                        </a>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <%@include file="../includes/ai-chat-widget.jsp" %>

    <script>
        // Add interactive features
        document.addEventListener('DOMContentLoaded', function() {
            // Animate course cards on scroll
            const observerOptions = {
                threshold: 0.1,
                rootMargin: '0px 0px -50px 0px'
            };

            const observer = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        entry.target.style.opacity = '0';
                        entry.target.style.transform = 'translateY(20px)';
                        entry.target.style.transition = 'all 0.6s ease';
                        
                        setTimeout(() => {
                            entry.target.style.opacity = '1';
                            entry.target.style.transform = 'translateY(0)';
                        }, 100);
                        
                        observer.unobserve(entry.target);
                    }
                });
            }, observerOptions);

            // Observe all course cards
            document.querySelectorAll('.course-card').forEach(card => {
                observer.observe(card);
            });

            // Add loading states to buttons
            document.querySelectorAll('form button').forEach(button => {
                button.addEventListener('click', function() {
                    this.style.opacity = '0.7';
                    this.innerHTML = '⏳ Processing...';
                });
            });

            // Auto-hide alerts after 5 seconds
            setTimeout(() => {
                document.querySelectorAll('.alert').forEach(alert => {
                    alert.style.transition = 'all 0.5s ease';
                    alert.style.opacity = '0';
                    alert.style.transform = 'translateY(-20px)';
                    setTimeout(() => alert.remove(), 500);
                });
            }, 5000);
        });
    </script>
</body>
</html>