<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${blog != null ? 'Edit' : 'Create'} Blog - NangInvest Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-styles.css">
    <script src="https://cdn.tiny.cloud/1/gpkprdx5nugaq7fmkg1lybjs1pclj1iqd9jilbsguq2twmpu/tinymce/6/tinymce.min.js" referrerpolicy="origin"></script>
    <style>
        .blog-form-container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-label {
            display: block;
            margin-bottom: 5px;
            font-weight: 600;
        }
        
        .form-control {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
        }
        
        .btn-primary {
            background-color: #4e73df;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        
        .btn-secondary {
            background-color: #858796;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
        }
        
        .btn-container {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }
        
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        
        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .current-image {
            max-width: 300px;
            margin-top: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        
        .image-preview-container {
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <jsp:include page="../../includes/admin-header.jsp" />
    
    <div class="blog-form-container">
        <h2>${blog != null ? 'Edit' : 'Create'} Blog Post</h2>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                ${error}
            </div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/admin/blogs" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="${blog != null ? 'update' : 'create'}">
            <c:if test="${blog != null}">
                <input type="hidden" name="blogId" value="${blog.blogId}">
            </c:if>
            
            <div class="form-group">
                <label for="blogName" class="form-label">Blog Title*</label>
                <input type="text" id="blogName" name="blogName" class="form-control" 
                       value="${blog != null ? blog.blogName : ''}" required>
            </div>
            
            <div class="form-group">
                <label for="topic" class="form-label">Topic*</label>
                <select id="topic" name="topic" class="form-control" required>
                    <option value="" disabled ${blog == null ? 'selected' : ''}>Select a topic</option>
                    <c:forEach items="${topics}" var="topicOption">
                        <option value="${topicOption}" ${blog != null && blog.topic == topicOption ? 'selected' : ''}>
                            ${topicOption}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-group">
                <label for="blogImage" class="form-label">Blog Image</label>
                <input type="file" id="blogImage" name="blogImage" class="form-control" accept="image/*">
                
                <c:if test="${blog != null && blog.imageUrl != null}">
                    <div class="image-preview-container">
                        <p>Current Image:</p>
                        <img src="${pageContext.request.contextPath}${blog.imageUrl}" alt="Blog Image" class="current-image">
                    </div>
                </c:if>
            </div>
            
            <div class="form-group">
                <label for="detailedContent" class="form-label">Blog Content*</label>
                <textarea id="detailedContent" name="detailedContent" class="form-control">
                    ${blog != null ? blog.detailedContent : ''}
                </textarea>
            </div>
            
            <div class="btn-container">
                <button type="submit" class="btn-primary">${blog != null ? 'Update' : 'Create'} Blog</button>
                <a href="${pageContext.request.contextPath}/admin/blogs" class="btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
    
    <script>
        tinymce.init({
            selector: '#detailedContent',
            height: 500,
            plugins: 'anchor autolink charmap codesample emoticons image link lists media searchreplace table visualblocks wordcount',
            toolbar: 'undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table | align lineheight | numlist bullist indent outdent | emoticons charmap | removeformat',
            menubar: 'file edit view insert format tools table help',
            content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }'
        });
        
        // Image preview
        document.getElementById('blogImage').addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    let previewContainer = document.querySelector('.image-preview-container');
                    
                    if (!previewContainer) {
                        previewContainer = document.createElement('div');
                        previewContainer.className = 'image-preview-container';
                        document.getElementById('blogImage').parentNode.appendChild(previewContainer);
                    }
                    
                    previewContainer.innerHTML = `
                        <p>Preview:</p>
                        <img src="${e.target.result}" alt="Preview" class="current-image">
                    `;
                };
                reader.readAsDataURL(file);
            }
        });
    </script>
</body>
</html>
