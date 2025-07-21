<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Complete Payment - NangInvest</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@include file="../includes/user-header.jsp" %>

    <div class="container mt-5">
        <h2>Complete Your Payment</h2>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <p>Please scan the QR code below to make a bank transfer for your order.</p>
        <div class="payment-details">
            <p><strong>Amount:</strong> $<fmt:formatNumber value="${totalAmount}" pattern="#,##0.00"/></p>
            <p><strong>Order IDs:</strong> ${orderIds}</p>
            <p><strong>Bank Account:</strong> NangInvest Bank, Account #1234567890</p>
            <p><strong>Instructions:</strong> Transfer the exact amount to the account above, including the order IDs in the transfer note. Payment will be verified within 24 hours.</p>
        </div>
        <c:if test="${not empty qrCodePath}">
            <img src="${pageContext.request.contextPath}/${qrCodePath}" alt="Payment QR Code" class="img-fluid" style="max-width: 200px;"/>
        </c:if>
        <div class="mt-3">
            <a href="${pageContext.request.contextPath}/orders" class="btn btn-primary">View Orders</a>
            <a href="${pageContext.request.contextPath}/user/cart" class="btn btn-secondary">Back to Cart</a>
        </div>
    </div>

    <%@include file="../includes/ai-chat-widget.jsp" %>
</body>
</html>