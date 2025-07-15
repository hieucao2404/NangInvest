<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${isEdit ? 'Edit' : 'Add'} Course - NangInvest Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/form-styles.css">
</head>
<body>
    <%@include file="../../includes/admin-header.jsp" %>
    
    <div class="admin-container">
        <div class="admin-content">
            <div class="page-header">
                <h1>${isEdit ? 'Edit' : 'Add New'} Course</h1>
                <a href="${pageContext.request.contextPath}/admin/courses" class="btn btn-secondary">
                    <i class="icon-back"></i> Back to Courses
                </a>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>

            <div class="form-container">
                <form action="${pageContext.request.contextPath}/admin/courses" method="post" class="admin-form">
                    <input type="hidden" name="action" value="${isEdit ? 'update' : 'create'}">
                    <c:if test="${isEdit}">
                        <input type="hidden" name="courseId" value="${course.courseId}">
                    </c:if>

                    <div class="form-grid">
                        <div class="form-group">
                            <label for="courseName">Course Name *</label>
                            <input type="text" id="courseName" name="courseName" 
                                   value="${course.courseName}" required maxlength="200">
                        </div>

                        <div class="form-group">
                            <label for="time">Duration</label>
                            <input type="text" id="time" name="time" 
                                   value="${course.time}" placeholder="e.g., 4 weeks, 20 hours">
                        </div>

                        <div class="form-group">
                            <label for="isFree">Course Type</label>
                            <select id="isFree" name="isFree" onchange="togglePriceField()">
                                <option value="true" ${course.isFree ? 'selected' : ''}>Free</option>
                                <option value="false" ${!course.isFree ? 'selected' : ''}>Premium</option>
                            </select>
                        </div>

                        <div class="form-group" id="priceGroup" style="display: ${course.isFree ? 'none' : 'block'}">
                            <label for="price">Price ($)</label>
                            <input type="number" id="price" name="price" 
                                   value="${course.price}" min="0" step="0.01">
                        </div>

                        <div class="form-group full-width">
                            <label for="imageUrl">Course Image URL</label>
                            <input type="url" id="imageUrl" name="imageUrl" 
                                   value="${course.imageUrl}" placeholder="https://example.com/image.jpg">
                            <c:if test="${not empty course.imageUrl}">
                                <div class="image-preview">
                                    <img src="${course.imageUrl}" alt="Course Preview" id="imagePreview">
                                </div>
                            </c:if>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="icon-save"></i> ${isEdit ? 'Update' : 'Create'} Course
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/courses" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        function togglePriceField() {
            const isFree = document.getElementById('isFree').value === 'true';
            const priceGroup = document.getElementById('priceGroup');
            priceGroup.style.display = isFree ? 'none' : 'block';
            
            if (isFree) {
                document.getElementById('price').value = '';
            }
        }

        // Image preview functionality
        document.getElementById('imageUrl').addEventListener('input', function(e) {
            const imageUrl = e.target.value;
            const preview = document.getElementById('imagePreview');
            
            if (imageUrl) {
                if (!preview) {
                    const previewDiv = document.createElement('div');
                    previewDiv.className = 'image-preview';
                    previewDiv.innerHTML = '<img src="' + imageUrl + '" alt="Course Preview" id="imagePreview">';
                    e.target.parentNode.appendChild(previewDiv);
                } else {
                    preview.src = imageUrl;
                }
            } else if (preview) {
                preview.parentNode.remove();
            }
        });
    </script>
</body>
</html>