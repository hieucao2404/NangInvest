<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>My Orders</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
  </head>
  <body>
    <div class="container mt-5">
      <h2>My Orders</h2>
      <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
      </c:if>
      <c:if test="${empty orders}">
        <p>No orders found.</p>
      </c:if>
      <c:if test="${not empty orders}">
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>Order ID</th>
              <th>Product ID</th>
              <th>Payment Status</th>
              <!-- Uncomment if total is added to Order entity -->
              <!-- <th>Total</th> -->
            </tr>
          </thead>
          <tbody>
            <c:forEach var="order" items="${orders}">
              <tr>
                <td>${order.orderId}</td>
                <td>${order.productId}</td>
                <td>${order.paymentStatus}</td>
                <!-- Uncomment if total is added to Order entity -->
                <!-- <td>${order.total}</td> -->
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </c:if>
      <a
        href="${pageContext.request.contextPath}/user/cart"
        class="btn btn-primary"
        >Back to Cart</a
      >
    </div>
  </body>
</html>
