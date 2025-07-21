<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="model.Book" %>
<%@ page import="java.util.List" %>
<%
    String role = (String) session.getAttribute("role");
    if (role != null && role.equals("admin")) {
%>
    <jsp:include page="/includes/admin-header.jsp" />
<%
    } else if (role != null && role.equals("user")) {
%>
    <jsp:include page="/includes/user-header.jsp" />
<%
    } else {
%>
    <jsp:include page="/includes/public-header.jsp" />
<%
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Books</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f8f9fa; margin: 0; }
        .container { max-width: 1000px; margin: 40px auto; background: #fff; border-radius: 8px; box-shadow: 0 2px 8px #0001; padding: 32px; }
        h1 { text-align: center; margin-bottom: 32px; }
        table { width: 100%; border-collapse: collapse; margin-top: 16px; }
        th, td { padding: 12px 10px; border-bottom: 1px solid #e0e0e0; text-align: left; }
        th { background: #f1f1f1; }
        tr:hover { background: #f9f9f9; }
        .cover-img { max-width: 60px; max-height: 80px; border-radius: 4px; }
        .affiliate-link { color: #007bff; text-decoration: underline; }
        .preview-badge { background: #28a745; color: #fff; padding: 2px 8px; border-radius: 12px; font-size: 0.85em; margin-left: 8px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>All Books</h1>
        <table>
            <thead>
                <tr>
                    <th>Cover</th>
                    <th>Title</th>
                    <th>Topic</th>
                    <th>Rating</th>
                    <th>Preview</th>
                    <th>Affiliate Link</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Book> books = (List<Book>) request.getAttribute("books");
                    if (books != null && !books.isEmpty()) {
                        for (Book book : books) {
                %>
                <tr>
                    <td>
                        <% if (book.getCoverImage() != null && !book.getCoverImage().trim().isEmpty()) { %>
                            <img src="<%= book.getCoverImage() %>" alt="Cover" class="cover-img" />
                        <% } else { %>
                            <span style="color:#bbb;">No Image</span>
                        <% } %>
                    </td>
                    <td>
                        <a href="<%= request.getContextPath() + "/books?action=view&id=" + book.getBookId() %>"><%= book.getBookName() %></a>
                    </td>
                    <td><%= book.getTopic() != null ? book.getTopic() : "-" %></td>
                    <td><%= book.getFormattedRating() %></td>
                    <td>
                        <% if (book.hasPreview()) { %>
                            <span class="preview-badge">Available</span>
                        <% } else { %>
                            <span style="color:#bbb;">No</span>
                        <% } %>
                    </td>
                    <td>
                        <% if (book.hasAffiliateLink()) { %>
                            <a href="<%= book.getAffiliateLink() %>" class="affiliate-link" target="_blank">Buy</a>
                        <% } else { %>
                            <span style="color:#bbb;">-</span>
                        <% } %>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr><td colspan="6" style="text-align:center; color:#888;">No books found.</td></tr>
                <% } %>
            </tbody>
        </table>
    </div>
</body>
</html>
