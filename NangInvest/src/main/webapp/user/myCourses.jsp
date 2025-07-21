<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>My Courses - NangInvest</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-styles.css" />
        <style>
            .courses-container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
            }
            .section-card {
                background: white;
                border-radius: 10px;
                padding: 25px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                margin-bottom: 20px;
            }
            .section-title {
                color: #333;
                margin-bottom: 20px;
                padding-bottom: 10px;
                border-bottom: 2px solid #eee;
            }
            .course-item {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 15px 0;
                border-bottom: 1px solid #eee;
            }
            .course-item:last-child {
                border-bottom: none;
            }
            .course-item h4 {
                margin: 0;
                color: #333;
            }
            .course-item a {
                color: #667eea;
                text-decoration: none;
            }
            .course-item a:hover {
                text-decoration: underline;
            }
            .progress-bar {
                width: 200px;
                height: 8px;
                background: #eee;
                border-radius: 4px;
                overflow: hidden;
            }
            .progress-fill {
                height: 100%;
                background: linear-gradient(90deg, #4caf50, #45a049);
                transition: width 0.3s ease;
            }
            .no-courses {
                text-align: center;
                padding: 20px 0;
                color: #666;
            }
            .no-courses a {
                color: #667eea;
                text-decoration: none;
            }
            .no-courses a:hover {
                text-decoration: underline;
            }
            .success-message, .error-message {
                padding: 10px;
                border-radius: 5px;
                margin-bottom: 20px;
                text-align: center;
            }
            .success-message {
                color: #155724;
                background: #d4edda;
            }
            .error-message {
                color: #721c24;
                background: #f8d7da;
            }
            .enroll-btn {
                padding: 8px 16px;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-weight: bold;
            }
            .enroll-btn:hover {
                background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
            }
            @media (max-width: 768px) {
                .courses-container {
                    padding: 15px;
                }
                .course-item {
                    flex-direction: column;
                    align-items: flex-start;
                    gap: 10px;
                }
                .progress-bar {
                    width: 100%;
                }
            }
        </style>
    </head>
    <body>
        <%@include file="../includes/user-header.jsp" %>
        <div class="courses-container">
            <!-- Enrolled Courses -->
            <div class="section-card">
                <h2 class="section-title">📚 My Enrolled Courses</h2>
                <c:if test="${not empty param.success and param.success == 'enrolled'}">
                    <div class="success-message">Successfully enrolled in the course!</div>
                </c:if>
                <c:if test="${not empty param.success and param.success == 'alreadyEnrolled'}">
                    <div class="error-message">Course already enrolled!</div>
                </c:if>
                <c:choose>
                    <c:when test="${not empty userCourses}">
                        <c:forEach var="userCourse" items="${userCourses}">
                            <div class="course-item">
                                <div>
                                    <h4>
                                        <a href="${pageContext.request.contextPath}/course?courseId=${userCourse.course.courseId}">
                                            ${userCourse.course.courseName}
                                        </a>
                                    </h4>
                                    <c:if test="${userCourse.course.time != null}">
                                        <p style="margin: 5px 0; color: #666"><strong>Duration:</strong> ${userCourse.course.time}</p>
                                    </c:if>
                                    <c:if test="${userCourse.course.price != null}">
                                        <p style="margin: 5px 0; color: #666">
                                            <strong>Price:</strong>
                                            <c:choose>
                                                <c:when test="${userCourse.course.isFree == true}">Free</c:when>
                                                <c:otherwise>$${userCourse.course.price}</c:otherwise>
                                            </c:choose>
                                        </p>
                                    </c:if>
                                    <c:if test="${userCourse.enrollmentDate != null}">
                                        <p style="margin: 5px 0; color: #666"><strong>Enrolled:</strong> <fmt:formatDate value="${userCourse.enrollmentDate}" pattern="MMM dd, yyyy"/></p>
                                    </c:if>
                                    <c:if test="${userCourse.completionDate != null}">
                                        <p style="margin: 5px 0; color: #666"><strong>Completed:</strong> <fmt:formatDate value="${userCourse.completionDate}" pattern="MMM dd, yyyy"/></p>
                                    </c:if>
                                </div>
                                <div>
                                    <p style="margin: 5px 0; color: #666">Progress: <fmt:formatNumber value="${userCourse.progress}" maxFractionDigits="0"/>%</p>
                                    <div class="progress-bar">
                                        <div class="progress-fill" style="width: ${userCourse.progress}%"></div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="no-courses">
                            <span style="font-size: 2rem; display: block; margin-bottom: 10px">🎓</span>
                            <p>No courses enrolled</p>
                            <a href="${pageContext.request.contextPath}/courses">Browse courses</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <!-- Purchased but Not Enrolled Courses -->
            <div class="section-card">
                <h2 class="section-title">🛒 Purchased Courses Ready to Enroll</h2>
                <c:choose>
                    <c:when test="${not empty purchasedCourses}">
                        <c:forEach var="course" items="${purchasedCourses}">
                            <div class="course-item">
                                <div>
                                    <h4>${course.courseName}</h4>
                                    <c:if test="${course.time != null}">
                                        <p style="margin: 5px 0; color: #666"><strong>Duration:</strong> ${course.time}</p>
                                    </c:if>
                                    <c:if test="${course.price != null}">
                                        <p style="margin: 5px 0; color: #666">
                                            <strong>Price:</strong>
                                            <c:choose>
                                                <c:when test="${course.isFree == true}">Free</c:when>
                                                <c:otherwise>$${course.price}</c:otherwise>
                                            </c:choose>
                                        </p>
                                    </c:if>
                                </div>
                                <div>
                                    <form action="${pageContext.request.contextPath}/user/myCourses" method="post">
                                        <input type="hidden" name="action" value="enrollCourse">
                                        <input type="hidden" name="courseId" value="${course.courseId}">
                                        <button type="submit" class="enroll-btn">Enroll Now</button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="no-courses">
                            <span style="font-size: 2rem; display: block; margin-bottom: 10px">🛒</span>
                            <p>No purchased courses available to enroll</p>
                            <a href="${pageContext.request.contextPath}/courses">Browse courses</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <%@include file="../includes/ai-chat-widget.jsp" %>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const progressBars = document.querySelectorAll(".progress-fill");
                progressBars.forEach((bar) => {
                    const width = bar.style.width;
                    bar.style.width = "0%";
                    setTimeout(() => {
                        bar.style.width = width;
                    }, 500);
                });
            });
        </script>
    </body>
</html>