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
    <style>
      .modern-form {
        background: #fff;
        border-radius: 12px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.07);
        padding: 2rem 2.5rem;
        max-width: 600px;
        margin: 2rem auto;
      }
      .modern-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 1.5rem;
      }
      .form-group.modern-group {
        display: flex;
        flex-direction: column;
        margin-bottom: 1.2rem;
      }
      .form-group.full-width {
        grid-column: 1 / span 2;
      }
      label {
        font-weight: 600;
        margin-bottom: 0.5rem;
        color: #4e73df;
        font-size: 1rem;
      }
      input, select {
        padding: 0.7rem 1rem;
        border-radius: 6px;
        border: 1px solid #e5e7eb;
        font-size: 1rem;
        background: #f8f9fa;
        transition: border-color 0.2s;
      }
      input:focus, select:focus {
        border-color: #4e73df;
        outline: none;
      }
      .form-actions.modern-actions {
        display: flex;
        gap: 1rem;
        justify-content: flex-end;
        margin-top: 2rem;
      }
      .btn-primary {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: #fff;
        border: none;
        padding: 0.7rem 1.5rem;
        border-radius: 6px;
        font-weight: 600;
        font-size: 1rem;
        cursor: pointer;
        transition: box-shadow 0.2s;
      }
      .btn-primary:hover {
        box-shadow: 0 4px 12px rgba(102,126,234,0.2);
      }
      .btn-secondary {
        background: #f8f9fc;
        color: #4e73df;
        border: 1px solid #4e73df;
        padding: 0.7rem 1.5rem;
        border-radius: 6px;
        font-weight: 600;
        font-size: 1rem;
        cursor: pointer;
        transition: background 0.2s, color 0.2s;
      }
      .btn-secondary:hover {
        background: #4e73df;
        color: #fff;
      }
      .image-preview.modern-preview {
        margin-top: 1rem;
        text-align: center;
      }
      .image-preview img {
        max-width: 180px;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.08);
      }
      .alert-error {
        background: #f8d7da;
        color: #c82333;
        border-radius: 6px;
        padding: 1rem;
        margin-bottom: 1.5rem;
        font-weight: 500;
        text-align: center;
      }
      .modern-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 2rem;
      }
      .modern-header h1 {
        font-size: 2rem;
        font-weight: 700;
        color: #4e73df;
        margin: 0;
      }
    </style>
</head>
<body>
    <%@include file="../../includes/admin-header.jsp" %>
    
    <div class="admin-container">
        <div class="admin-content">
            <div class="modern-form">
                <div class="page-header modern-header">
                    <h1><span class="icon-course"></span> ${isEdit ? 'Edit' : 'Add New'} Course</h1>
                    <a href="${pageContext.request.contextPath}/courses?action=adminManage" class="btn btn-secondary">
                        <i class="icon-back"></i> Back to Courses
                    </a>
                </div>

                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/courses" method="post" class="admin-form">
                    <input type="hidden" name="action" value="${isEdit ? 'updateCourse' : 'saveCourse'}">
                    <c:if test="${isEdit}">
                        <input type="hidden" name="courseId" value="${course.courseId}">
                    </c:if>

                    <div class="form-grid modern-grid">
                        <div class="form-group modern-group">
                            <label for="courseName"><span class="icon-title"></span> Course Name *</label>
                            <input type="text" id="courseName" name="courseName" 
                                   value="${course.courseName}" required maxlength="200" placeholder="Enter course name">
                        </div>

                        <div class="form-group modern-group">
                            <label for="time"><span class="icon-clock"></span> Duration</label>
                            <input type="text" id="time" name="time" 
                                   value="${course.time}" placeholder="e.g., 4 weeks, 20 hours">
                        </div>

                        <div class="form-group modern-group">
                            <label for="isFree"><span class="icon-type"></span> Course Type</label>
                            <select id="isFree" name="isFree" onchange="togglePriceField()">
                                <option value="true" ${course.isFree ? 'selected' : ''}>Free</option>
                                <option value="false" ${!course.isFree ? 'selected' : ''}>Premium</option>
                            </select>
                        </div>

                        <div class="form-group modern-group" id="priceGroup" style="display: ${course.isFree ? 'none' : 'block'}">
                            <label for="price"><span class="icon-money"></span> Price ($)</label>
                            <input type="number" id="price" name="price" 
                                   value="${course.price}" min="0" step="0.01" placeholder="Enter price">
                        </div>

                        <div class="form-group full-width modern-group">
                            <label for="imageUrl"><span class="icon-image"></span> Course Image URL</label>
                            <input type="url" id="imageUrl" name="imageUrl" 
                                   value="${course.imageUrl}" placeholder="https://example.com/image.jpg">
                            <c:if test="${not empty course.imageUrl}">
                                <div class="image-preview modern-preview">
                                    <img src="${course.imageUrl}" alt="Course Preview" id="imagePreview">
                                </div>
                            </c:if>
                        </div>
                    </div>

                    <div class="form-actions modern-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="icon-save"></i> ${isEdit ? 'Update' : 'Create'} Course
                        </button>
                        <a href="${pageContext.request.contextPath}/courses?action=adminManage" class="btn btn-secondary">Cancel</a>
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