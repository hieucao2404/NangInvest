<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@page import="model.Book" %> <%
String role = (String) session.getAttribute("role"); if (role != null &&
role.equals("admin")) { %>
<jsp:include page="/includes/admin-header.jsp" />
<% } else if (role != null && role.equals("user")) { %>
<jsp:include page="/includes/user-header.jsp" />
<% } else { %>
<jsp:include page="/includes/public-header.jsp" />
<% } %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>
      <c:out value="${book != null ? (book.bookName != null ? book.bookName :
      'Book Detail') : 'Book Detail'}"/>
    </title>
    <style>
      .book-container {
        max-width: 700px;
        margin: 40px auto;
        padding: 30px;
        background: #fff;
        border-radius: 10px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
      }
      .book-title {
        font-size: 2rem;
        color: #667eea;
        margin-bottom: 10px;
      }
      .book-meta {
        color: #888;
        font-size: 0.95rem;
        margin-bottom: 20px;
      }
      .book-desc {
        font-size: 1.1rem;
        color: #333;
        line-height: 1.7;
        margin-bottom: 20px;
      }
      .book-rating {
        color: #f39c12;
        font-weight: bold;
      }
      .book-price {
        color: #27ae60;
        font-weight: bold;
      }
    </style>
  </head>
  <body>
    <div class="book-container">
      <c:choose>
        <c:when test="${book != null}">
          <div class="book-title">
            <c:out value="${book.bookName}" />
          </div>
          <div class="book-meta">
            <c:if test="${book.topic != null}">
              <strong>Topic:</strong> <c:out value="${book.topic}" /><br />
            </c:if>
            <c:if test="${book.rating != null}">
              <span class="book-rating"
                >Rating: <c:out value="${book.rating}" /></span
              ><br />
            </c:if>
          </div>
          <c:if test="${book.coverImage != null && !book.coverImage.isEmpty()}">
            <img
              src="${book.coverImage}"
              alt="Cover"
              style="
                max-width: 120px;
                max-height: 160px;
                border-radius: 6px;
                margin-bottom: 18px;
              "
            />
          </c:if>
          <div class="book-desc">
            <c:choose>
              <c:when
                test="${book.previewContent != null && !book.previewContent.isEmpty()}"
              >
                <c:out value="${book.previewContent}" />
              </c:when>
              <c:otherwise>
                <span style="color: #888">No description available.</span>
              </c:otherwise>
            </c:choose>
          </div>
          <c:if
            test="${book.affiliateLink != null && !book.affiliateLink.isEmpty()}"
          >
            <div style="margin-top: 18px">
              <a
                href="${book.affiliateLink}"
                class="affiliate-link"
                target="_blank"
                >Buy this book</a
              >
            </div>
          </c:if>
        </c:when>
        <c:otherwise>
          <h2 style="color: #c00">Book not found.</h2>
        </c:otherwise>
      </c:choose>
    </div>
  </body>
</html>
