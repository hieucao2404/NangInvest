<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Blogs - NangInvest Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-styles.css">
    <style>
        .blog-list-container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        
        .blog-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        
        .create-btn {
            background-color: #4e73df;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            text-decoration: none;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
        }
        
        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        th {
            background-color: #f8f9fc;
            font-weight: 600;
        }
        
        tr:hover {
            background-color: #f8f9fc;
        }
        
        .blog-image {
            width: 80px;
            height: 50px;
            object-fit: cover;
            border-radius: 4px;
        }
        
        .no-image {
            width: 80px;
            height: 50px;
            background-color: #f8f9fc;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 4px;
            color: #858796;
            font-size: 12px;
        }
        
        .blog-title {
            font-weight: 500;
            color: #333;
            max-width: 400px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        
        .topic-badge {
            display: inline-block;
            padding: 5px 10px;
            background-color: #e8f0fe;
            color: #4e73df;
            border-radius: 20px;
            font-size: 12px;
        }
        
        .action-buttons {
            display: flex;
            gap: 10px;
        }
        
        .view-btn, .edit-btn, .delete-btn {
            padding: 5px 10px;
            border-radius: 4px;
            text-decoration: none;
            font-size: 14px;
        }
        
        .view-btn {
            background-color: #f8f9fc;
            color: #5a5c69;
            border: 1px solid #ddd;
        }
        
        .edit-btn {
            background-color: #36b9cc;
            color: white;
            border: none;
        }
        
        .delete-btn {
            background-color: #e74a3b;
            color: white;
            border: none;
            cursor: pointer;
        }
        
        .pagination {
            display: flex;
            justify-content: center;
            margin-top: 20px;
        }
        
        .page-item {
            margin: 0 5px;
        }
        
        .page-link {
            padding: 8px 12px;
            border-radius: 4px;
            text-decoration: none;
            color: #4e73df;
            background-color: #f8f9fc;
        }
        
        .page-item.active .page-link {
            background-color: #4e73df;
            color: white;
        }
        
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.5);
            z-index: 1000;
            justify-content: center;
            align-items: center;
        }
        
        .modal-content {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            width: 400px;
            text-align: center;
        }
        
        .modal-buttons {
            display: flex;
            justify-content: center;
            gap: 10px;
            margin-top: 20px;
        }
        
        .confirm-btn {
            background-color: #e74a3b;
            color: white;
            border: none;
            padding: 8px 15px;
            border-radius: 4px;
            cursor: pointer;
        }
        
        .cancel-btn {
            background-color: #858796;
            color: white;
            border: none;
            padding: 8px 15px;
            border-radius: 4px;
            cursor: pointer;
        }
        
        .search-filter {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }
        
        .search-input {
            flex-grow: 1;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        
        .filter-select {
            min-width: 150px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
    </style>
</head>
<body>
    <jsp:include page="../includes/admin-header.jsp" />
    
    <div class="blog-list-container">
        <div class="blog-header">
            <h2>Manage Blogs</h2>
            <a href="${pageContext.request.contextPath}/admin/blogs?action=create" class="create-btn">Create New Blog</a>
        </div>
        
        <c:if test="${param.success != null}">
            <div class="alert alert-success">
                ${param.success}
            </div>
        </c:if>
        
        <c:if test="${param.error != null}">
            <div class="alert alert-danger">
                ${param.error}
            </div>
        </c:if>
        
        <div class="search-filter">
            <input type="text" id="searchInput" class="search-input" placeholder="Search blogs..." 
                   value="${param.search}">
            
            <select id="topicFilter" class="filter-select">
                <option value="">All Topics</option>
                <c:forEach items="${topics}" var="topicOption">
                    <option value="${topicOption}" ${param.topic eq topicOption ? 'selected' : ''}>
                        ${topicOption}
                    </option>
                </c:forEach>
            </select>
        </div>
        
        <table>
            <thead>
                <tr>
                    <th>Image</th>
                    <th>Title</th>
                    <th>Topic</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${blogs}" var="blog">
                    <tr>
                        <td>
                            <c:choose>
                                <c:when test="${blog.hasImage()}">
                                    <img src="${pageContext.request.contextPath}${blog.imageUrl}" alt="${blog.blogName}" class="blog-image">
                                </c:when>
                                <c:otherwise>
                                    <div class="no-image">No Image</div>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="blog-title">${blog.blogName}</div>
                        </td>
                        <td>
                            <span class="topic-badge">${blog.topic}</span>
                        </td>
                        <td>
                            <div class="action-buttons">
                                <a href="${pageContext.request.contextPath}/public/blog?id=${blog.blogId}" class="view-btn" target="_blank">View</a>
                                <a href="${pageContext.request.contextPath}/admin/blogs?action=edit&id=${blog.blogId}" class="edit-btn">Edit</a>
                                <button class="delete-btn" onclick="confirmDelete('${blog.blogId}', '${blog.blogName}')">Delete</button>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                
                <c:if test="${empty blogs}">
                    <tr>
                        <td colspan="4" style="text-align: center; padding: 30px;">
                            No blogs found. ${param.search != null || param.topic != null ? 'Try a different search or filter.' : 'Create your first blog!'}
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
        
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <div class="page-item">
                        <a href="${pageContext.request.contextPath}/admin/blogs?page=${currentPage - 1}${param.search != null ? '&search=' : ''}${param.topic != null ? '&topic=' : ''}" class="page-link">
                            Previous
                        </a>
                    </div>
                </c:if>
                
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <div class="page-item ${currentPage == i ? 'active' : ''}">
                        <a href="${pageContext.request.contextPath}/admin/blogs?page=${i}${param.search != null ? '&search=' : ''}${param.topic != null ? '&topic=' : ''}" class="page-link">
                            ${i}
                        </a>
                    </div>
                </c:forEach>
                
                <c:if test="${currentPage < totalPages}">
                    <div class="page-item">
                        <a href="${pageContext.request.contextPath}/admin/blogs?page=${currentPage + 1}${param.search != null ? '&search=' : ''}${param.topic != null ? '&topic=' : ''}" class="page-link">
                            Next
                        </a>
                    </div>
                </c:if>
            </div>
        </c:if>
    </div>
    
    <!-- Delete Confirmation Modal -->
    <div id="deleteModal" class="modal">
        <div class="modal-content">
            <h3>Confirm Delete</h3>
            <p>Are you sure you want to delete the blog "<span id="blogNameToDelete"></span>"?</p>
            <p>This action cannot be undone.</p>
            
            <div class="modal-buttons">
                <button id="confirmDeleteBtn" class="confirm-btn">Delete</button>
                <button onclick="closeModal()" class="cancel-btn">Cancel</button>
            </div>
        </div>
    </div>
    
    <script>
        // Search and filter functionality
        document.getElementById('searchInput').addEventListener('keyup', function(e) {
            if (e.key === 'Enter') {
                applyFilters();
            }
        });
        
        document.getElementById('topicFilter').addEventListener('change', function() {
            applyFilters();
        });
        
        function applyFilters() {
            const searchValue = document.getElementById('searchInput').value.trim();
            const topicValue = document.getElementById('topicFilter').value;
            let url = '${pageContext.request.contextPath}/admin/blogs?page=1';
            
            if (searchValue) {
                url += '&search=' + encodeURIComponent(searchValue);
            }
            
            if (topicValue) {
                url += '&topic=' + encodeURIComponent(topicValue);
            }
            
            window.location.href = url;
        }
        
        // Delete confirmation
        let blogIdToDelete;
        
        function confirmDelete(blogId, blogName) {
            blogIdToDelete = blogId;
            document.getElementById('blogNameToDelete').textContent = blogName;
            document.getElementById('deleteModal').style.display = 'flex';
        }
        
        function closeModal() {
            document.getElementById('deleteModal').style.display = 'none';
        }
        
        document.getElementById('confirmDeleteBtn').addEventListener('click', function() {
            window.location.href = '${pageContext.request.contextPath}/admin/blogs?action=delete&id=' + blogIdToDelete;
        });
        
        // Close modal when clicking outside
        window.addEventListener('click', function(event) {
            if (event.target === document.getElementById('deleteModal')) {
                closeModal();
            }
        });
    </script>
</body>
</html>
