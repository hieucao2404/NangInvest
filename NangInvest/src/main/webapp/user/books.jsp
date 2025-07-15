<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Investment Books - NangInvest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/catalog-styles.css">
</head>
<body>
    <%@include file="../includes/user-header.jsp" %>
    
    <div class="container">
        <div class="page-header">
            <h1>Investment Books</h1>
            <p>Discover the best books to enhance your investment knowledge</p>
        </div>

        <!-- Success Messages -->
        <c:if test="${param.added == 'true'}">
            <div class="alert alert-success">Book added to cart successfully!</div>
        </c:if>

        <!-- Books Grid -->
        <div class="catalog-grid">
            <c:forEach var="book" items="${books}">
                <div class="catalog-card">
                    <div class="card-image">
                        <c:if test="${not empty book.coverImage}">
                            <img src="${book.coverImage}" alt="${book.bookName}">
                        </c:if>
                        <c:if test="${empty book.coverImage}">
                            <div class="no-image">📚</div>
                        </c:if>
                        
                        <c:if test="${book.isPreviewAvailable}">
                            <div class="preview-badge">Preview Available</div>
                        </c:if>
                    </div>
                    
                    <div class="card-content">
                        <h3 class="card-title">${book.bookName}</h3>
                        
                        <c:if test="${not empty book.topic}">
                            <span class="topic-tag">${book.topic}</span>
                        </c:if>
                        
                        <c:if test="${not empty book.rating}">
                            <div class="rating">
                                <span class="rating-stars">★★★★★</span>
                                <span class="rating-value">${book.rating}/5</span>
                            </div>
                        </c:if>
                        
                        <c:if test="${not empty book.previewContent && book.isPreviewAvailable}">
                            <p class="preview-text">${book.previewContent.substring(0, Math.min(100, book.previewContent.length()))}...</p>
                        </c:if>
                    </div>
                    
                    <div class="card-actions">
                        <c:if test="${book.isPreviewAvailable}">
                            <button class="btn btn-secondary" onclick="showPreview(${book.bookId})">
                                Preview
                            </button>
                        </c:if>
                        
                        <c:if test="${not empty book.affiliateLink}">
                            <a href="${book.affiliateLink}" target="_blank" class="btn btn-primary">
                                View on Store
                            </a>
                        </c:if>
                        
                        <form style="display: inline;" action="${pageContext.request.contextPath}/user/cart" method="post">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="${book.bookId}">
                            <input type="hidden" name="type" value="book">
                            <input type="hidden" name="redirect" value="${pageContext.request.requestURL}">
                            <button type="submit" class="btn btn-accent">
                                Add to Cart
                            </button>
                        </form>
                    </div>
                </div>
            </c:forEach>
            
            <c:if test="${empty books}">
                <div class="no-content">
                    <h3>No books available yet</h3>
                    <p>Check back soon for investment books and resources!</p>
                </div>
            </c:if>
        </div>
    </div>

    <!-- Preview Modal -->
    <div id="previewModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closePreview()">&times;</span>
            <div id="previewContent"></div>
        </div>
    </div>

    <%@include file="../includes/ai-chat-widget.jsp" %>

    <script>
        function showPreview(bookId) {
            // You would fetch preview content via AJAX
            // For now, showing a placeholder
            const modal = document.getElementById('previewModal');
            const content = document.getElementById('previewContent');
            content.innerHTML = `
                <h2>Book Preview</h2>
                <p>Loading preview content for book ID: ${bookId}</p>
                <p>This would show the actual book preview content...</p>
            `;
            modal.style.display = 'block';
        }

        function closePreview() {
            document.getElementById('previewModal').style.display = 'none';
        }

        // Close modal when clicking outside
        window.onclick = function(event) {
            const modal = document.getElementById('previewModal');
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }
    </script>
</body>
</html>