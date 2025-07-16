<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Form</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-styles.css">
    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background: #f8f9fa;
        }
        .form-container {
            max-width: 500px;
            margin: 40px auto;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.07);
            padding: 2rem 2.5rem;
        }
        h2 {
            font-size: 1.7rem;
            font-weight: 700;
            color: #4e73df;
            margin-bottom: 1.5rem;
        }
        .form-group {
            margin-bottom: 1.2rem;
        }
        label {
            display: block;
            font-weight: 500;
            margin-bottom: 0.5rem;
            color: #333;
        }
        input, select {
            width: 100%;
            padding: 0.7rem;
            border-radius: 6px;
            border: 1px solid #ddd;
            font-size: 1rem;
        }
        .form-actions {
            display: flex;
            gap: 1rem;
            margin-top: 1.5rem;
        }
        .btn {
            padding: 0.7rem 1.5rem;
            border-radius: 6px;
            font-weight: 600;
            font-size: 1rem;
            border: none;
            cursor: pointer;
            transition: box-shadow 0.2s;
        }
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: #fff;
        }
        .btn-primary:hover {
            box-shadow: 0 4px 12px rgba(102,126,234,0.2);
        }
        .btn-secondary {
            background: #f8f9fc;
            color: #4e73df;
            border: 1px solid #4e73df;
        }
        .btn-secondary:hover {
            background: #4e73df;
            color: #fff;
        }
        .error {
            color: #e74a3b;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h2>${user != null ? 'Edit User' : 'Add New User'}</h2>
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        <form method="post" action="${pageContext.request.contextPath}/users">
            <input type="hidden" name="action" value="${user != null ? 'edit' : 'add'}" />
            <c:if test="${user != null}">
                <input type="hidden" name="id" value="${user.userId}" />
            </c:if>
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" value="${user.userName}" required />
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" value="${user.email}" required />
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" value="${user != null ? user.password : ''}" required />
            </div>
            <div class="form-group">
                <label for="role">Role</label>
                <select id="role" name="role">
                    <option value="USER" ${user != null && user.role == 'USER' ? 'selected' : ''}>User</option>
                    <option value="ADMIN" ${user != null && user.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
                </select>
            </div>
            <div class="form-group">
                <label for="age">Age</label>
                <input type="number" id="age" name="age" value="${user.age}" min="0" required />
            </div>
            <div class="form-group">
                <label for="name">Name</label>
                <input type="text" id="name" name="name" value="${user.name}" required />
            </div>
            <div class="form-group">
                <label for="expertise">Expertise</label>
                <input type="text" id="expertise" name="expertise" value="${user.expertise}" />
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">${user != null ? 'Update User' : 'Add User'}</button>
                <a href="${pageContext.request.contextPath}/users?action=list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</body>
</html>
