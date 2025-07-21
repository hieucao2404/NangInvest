<%-- 
    Document   : order-confirmation
    Created on : Jul 18, 2025, 11:04:47 AM
    Author     : Admin
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Order Confirmation</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"><!--  -->
    </head>
    <body>
    <div class="container mt-5">
        <h2>Order Confirmation</h2>
        <c:if test="${param.success == 'order_placed'}">
            <div class="alert alert-success">Your order has been placed successfully!</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <a href="${pageContext.request.contextPath}/orders" class="btn btn-primary">View Orders</a>
        <a href="${pageContext.request.contextPath}/user/cart" class="btn btn-secondary">Back to Cart</a>
    </div>
</body>
</html> 