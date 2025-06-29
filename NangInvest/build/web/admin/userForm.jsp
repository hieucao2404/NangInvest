<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>${user != null ? "Edit User" : "Add User"}</title>
    <style>
        form {
            max-width: 500px;
            margin: 0 auto;
        }
        label {
            display: block;
            margin-top: 10px;
        }
        input, select {
            width: 100%;
            padding: 6px;
            margin-top: 5px;
        }
        button {
            margin-top: 15px;
            padding: 8px 16px;
        }
    </style>
</head>
<body>

    <h2>${user != null ? "Edit User" : "Add New User"}</h2>

    <form method="post" action="${pageContext.request.contextPath}/users">
        <input type="hidden" name="action" value="${user != null ? "edit" : "add"}" />
        <c:if test="${user != null}">
            <input type="hidden" name="id" value="${user.userId}" />
        </c:if>

        <label>Username:</label>
        <input type="text" name="username" value="${user != null ? user.userName : ''}" required />

        <label>Email:</label>
        <input type="email" name="email" value="${user != null ? user.email : ''}" required />

        <label>Password:</label>
        <input type="password" name="password" value="${user != null ? user.password : ''}" required />

        <label>Age:</label>
        <input type="number" name="age" value="${user != null ? user.age : ''}" min="0" required />

        <label>Full Name:</label>
        <input type="text" name="name" value="${user != null ? user.name : ''}" />

        <label>Expertise:</label>
        <input type="text" name="expertise" value="${user != null ? user.expertise : ''}" />

        <!-- Optional: allow changing role (if needed) -->
        
        <label>Role:</label>
        <select name="role">
            <option value="USER" ${user != null && user.role == 'USER' ? 'selected' : ''}>User</option>
            <option value="ADMIN" ${user != null && user.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
            <option value="AFFILIATE" ${user != null && user.role == 'AFFILIATE' ? 'selected' : ''}>Affiliate</option>
        </select>
        

        <button type="submit">${user != null ? "Update User" : "Add User"}</button>
    </form>

    <br>
    <a href="${pageContext.request.contextPath}/users?action=list">
        <button>Back to User List</button>
    </a>

</body>
</html>
