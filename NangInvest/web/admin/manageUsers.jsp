<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.User" %>
<c:if test="${empty userList}">
    <c:redirect url="/users?action=list"/>
</c:if>

<!DOCTYPE html>
<html>
    <head>
        <title>User Management</title>
        <style>
            table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 30px;
            }
            table, th, td {
                border: 1px solid #999;
            }
            th, td {
                padding: 10px;
                text-align: center;
            }
            .actions form {
                display: inline;
            }
        </style>
    </head>
    <body>

        <h2>User Management</h2>

        <a href="${pageContext.request.contextPath}/users?action=add">
            <button>Add New User</button>
        </a>

        <br><br>

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
                                <button>Edit</button>
                            </a>
                            <form method="post" action="${pageContext.request.contextPath}/users">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${user.userId}">
                                <button type="submit" onclick="return confirm('Are you sure you want to delete this user?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <c:out value="${userList}" />

    </body>
</html>
