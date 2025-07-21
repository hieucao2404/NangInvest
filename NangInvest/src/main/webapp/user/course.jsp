<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${course.courseName} - NangInvest</title>
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", system-ui, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
            color: #1a1a1a;
            line-height: 1.6;
        }
        .course-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 2rem;
        }
        .course-header {
            background: white;
            border-radius: 12px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        }
        .course-title {
            font-size: 2rem;
            font-weight: 700;
            margin: 0 0 1rem 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
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
            background: #48bb78;
            text-align: center;
            color: white;
            line-height: 20px;
            transition: width 0.3s ease;
        }
        .lessons-grid {
            display: grid;
            gap: 1rem;
        }
        .lesson-card {
            background: white;
            border-radius: 8px;
            padding: 1rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .lesson-title {
            font-size: 1.1rem;
            font-weight: 600;
        }
        .btn {
            padding: 0.5rem 1rem;
            border-radius: 6px;
            font-weight: 600;
            text-decoration: none;
            text-align: center;
            cursor: pointer;
            border: none;
            transition: all 0.2s;
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
        .alert {
            padding: 1rem;
            border-radius: 6px;
            margin-bottom: 1rem;
            font-weight: 500;
        }
        .alert-error {
            background: #fed7d7;
            color: #742a2a;
            border: 1px solid #feb2b2;
        }
        @media (max-width: 768px) {
            .course-container {
                padding: 1rem;
            }
            .lesson-card {
                flex-direction: column;
                gap: 1rem;
                text-align: center;
            }
        }
    </style>
</head>
<body>
    <%@include file="../includes/user-header.jsp" %>
    <main class="course-container">
        <section class="course-header">
            <h1 class="course-title">${course.courseName}</h1>
            <div class="progress-container">
                <div class="progress-bar">
                    <div class="progress" style="width: ${progress}%">${progress}%</div>
                </div>
            </div>
        </section>
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ⚠️ ${error}
            </div>
        </c:if>
        <section class="lessons-grid">
            <c:forEach var="lesson" items="${lessons}">
                <div class="lesson-card">
                    <span class="lesson-title">${lesson.title}</span>
                    <c:set var="isCompleted" value="${userLessonsDAO.isLessonCompleted(sessionScope.user.userId, lesson.lessonId)}"/>
                    <c:choose>
                        <c:when test="${isCompleted}">
                            <span class="btn btn-success">✅ Completed</span>
                        </c:when>
                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/user/progress" method="post">
                                <input type="hidden" name="action" value="completeLesson">
                                <input type="hidden" name="courseId" value="${course.courseId}">
                                <input type="hidden" name="lessonId" value="${lesson.lessonId}">
                                <button type="submit" class="btn btn-primary">Complete Lesson</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>
        </section>
    </main>
    <%@include file="../includes/ai-chat-widget.jsp" %>
</body>
</html>