<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.User" %>
<c:if test="${empty userList}">
    <c:redirect url="/users?action=list"/>
</c:if>

<!DOCTYPE html>
<html>
<%@include file="../includes/admin-header.jsp" %>
    <head>
        <title>User Management</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-styles.css">
        <style>
            body {
                font-family: 'Segoe UI', Arial, sans-serif;
                background: #f8f9fa;
                margin: 0;
                padding: 0;
            }
            .container {
                max-width: 1100px;
                margin: 40px auto;
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.07);
                padding: 2rem 2.5rem;
            }
            h2 {
                font-size: 2rem;
                font-weight: 700;
                margin-bottom: 1.5rem;
                color: #4e73df;
            }
            .add-btn {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: #fff;
                border: none;
                border-radius: 6px;
                padding: 0.7rem 1.5rem;
                font-weight: 600;
                font-size: 1rem;
                cursor: pointer;
                margin-bottom: 2rem;
                transition: box-shadow 0.2s;
            }
            .add-btn:hover {
                box-shadow: 0 4px 12px rgba(102,126,234,0.2);
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 30px;
                background: #fff;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 1px 4px rgba(0,0,0,0.04);
            }
            th {
                background: #f8f9fc;
                font-weight: 600;
                color: #4e73df;
                padding: 14px 10px;
            }
            td {
                padding: 12px 10px;
                text-align: center;
                color: #333;
            }
            tr:nth-child(even) {
                background: #f4f6fb;
            }
            .actions {
                display: flex;
                gap: 8px;
                justify-content: center;
            }
            .action-btn {
                padding: 6px 14px;
                border-radius: 4px;
                font-size: 0.95rem;
                font-weight: 500;
                border: none;
                cursor: pointer;
                transition: background 0.2s;
            }
            .edit-btn {
                background: #36b9cc;
                color: #fff;
            }
            .edit-btn:hover {
                background: #2c9faf;
            }
            .delete-btn {
                background: #e74a3b;
                color: #fff;
            }
            .delete-btn:hover {
                background: #c0392b;
            }
            .view-btn {
                background: #f8f9fc;
                color: #4e73df;
                border: 1px solid #4e73df;
            }
            .view-btn:hover {
                background: #4e73df;
                color: #fff;
            }
        </style>
    </head>
    <body>

    <div class="container">
        <h2>User Management</h2>
        <a href="${pageContext.request.contextPath}/users?action=add">
            <button class="add-btn">+ Add New User</button>
        </a>

        <table>
            <thead>
                <tr>
                    <th>UserID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Age</th>
                    <th>Name</th>
                    <th>Expertise</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${userList}">
                    <tr>
                        <td>${user.userId}</td>
                        <td>${user.userName}</td>
                        <td>${user.email}</td>
                        <td>${user.role}</td>
                        <td>${user.age}</td>
                        <td>${user.name}</td>
                        <td>${user.expertise}</td>
                        <td class="actions">
                            <a href="${pageContext.request.contextPath}/users?action=edit&id=${user.userId}">
                                <button class="action-btn edit-btn">Edit</button>
                            </a>
                            <form method="post" action="${pageContext.request.contextPath}/users">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${user.userId}">
                                <button type="submit" class="action-btn delete-btn" onclick="return confirm('Are you sure you want to delete this user?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    </body>
</html>
