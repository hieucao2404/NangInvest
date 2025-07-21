<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Admin - Manage Orders - NangInvest</title>
  </head>
  <body>
    <%@ include file="/includes/admin-header.jsp" %>
    <h2>Pending Orders</h2>
    <c:if test="${not empty pendingOrders}">
      <table border="1">
        <tr>
          <th>Order ID</th>
          <th>User ID</th>
          <th>Course ID</th>
          <th>Amount</th>
          <th>Status</th>
          <th>Action</th>
        </tr>
        <c:forEach var="order" items="${pendingOrders}">
          <tr>
            <td>${order.orderId}</td>
            <td>${order.userId}</td>
            <td>${order.productId}</td>
            <td>${order.total}</td>
            <td>${order.paymentStatus}</td>
            <td>
              <form
                action="${pageContext.request.contextPath}/admin/orders"
                method="post"
              >
                <input type="hidden" name="action" value="confirm" />
                <input type="hidden" name="orderId" value="${order.orderId}" />
                <button type="submit">Confirm</button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
    <c:if test="${empty pendingOrders}">
      <p>No pending orders.</p>
    </c:if>
  </body>
</html>
