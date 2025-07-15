<%-- Document : book-detail Created on : Jun 9, 2025, 9:12:02 PM Author : Admin
--%> <%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@page import="model.Book" %> <%@page
import="dao.BooksDAO" %> <% String bookIdParam = request.getParameter("bookId");
Book book = null; if (bookIdParam != null) { try { int bookId =
Integer.parseInt(bookIdParam); book = new
BooksDAO().findById(bookId).orElse(null); } catch (Exception e) { // handle
error } } %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title><c:out value="${book != null ? book.title : 'Book Detail'}"/></title>
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
            ${book.title != null ? book.title : book.bookName}
          </div>
          <div class="book-meta">
            <strong>Author:</strong> ${book.author}<br />
            <strong>Topic:</strong> ${book.topic}<br />
            <c:if test="${book.rating != null}"
              ><span class="book-rating">Rating: ${book.rating}</span><br
            /></c:if>
            <c:if test="${book.price != null}"
              ><span class="book-price">Price: $${book.price}</span><br
            /></c:if>
          </div>
          <div class="book-desc">${book.description}</div>
        </c:when>
        <c:otherwise>
          <h2 style="color: #c00">Book not found.</h2>
        </c:otherwise>
      </c:choose>
    </div>
  </body>
</html>
