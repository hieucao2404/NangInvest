<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart - NangInvest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart-styles.css">
</head>
<body>
    <%@include file="../includes/user-header.jsp" %>
    
    <div class="container">
        <div class="page-header">
            <h1>Shopping Cart</h1>
            <p>Review your selected courses and books</p>
        </div>

        <!-- Success and Error Messages -->
        <c:if test="${param.updated == 'true'}">
            <div class="alert alert-success">Cart updated successfully!</div>
        </c:if>
        <c:if test="${param.removed == 'true'}">
            <div class="alert alert-success">Item removed from cart!</div>
        </c:if>
        <c:if test="${param.cleared == 'true'}">
            <div class="alert alert-success">Cart cleared successfully!</div>
        </c:if>
        <c:if test="${param.error == 'remove_failed'}">
            <div class="alert alert-warning">Failed to remove item - No matching cart item found.</div>
        </c:if>
        <c:if test="${param.error == 'remove_error'}">
            <div class="alert alert-danger">Error removing item. Please try again later.</div>
        </c:if>

        <div class="cart-container">
            <c:if test="${not empty cartItems}">
                <div class="cart-items">
                    <c:forEach var="item" items="${cartItems}">
    <div class="cart-item">
        <div class="item-details">
            <div class="item-image">
                <c:choose>
                    <c:when test="${not empty item.course.imageUrl}">
                        <img src="${item.course.imageUrl}" alt="${item.course.courseName}" style="width:100%;height:100%;object-fit:cover;">
                    </c:when>
                    <c:otherwise>
                        <div class="placeholder-image">📚</div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="item-info">
                <h3>${item.course.courseName}</h3>
                <p class="item-type">Course</p>
                <p class="item-price">
                    <c:choose>
                        <c:when test="${item.course.isFree}">
                            Free
                        </c:when>
                        <c:otherwise>
                            $<fmt:formatNumber value="${item.course.price}" pattern="#,##0.00"/>
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
        </div>
        
        <div class="item-quantity">
            <span>Quantity:</span>
            <div class="quantity-controls">
                <button onclick="updateQuantity('<c:out value="${item.cartItem.cartId}"/>', '${item.cartItem.quantity - 1}')" 
                        ${item.cartItem.quantity <= 1 ? 'disabled' : ''}>-</button>
                <span class="quantity-value">${item.cartItem.quantity}</span>
                <button onclick="updateQuantity('<c:out value="${item.cartItem.cartId}"/>', '${item.cartItem.quantity + 1}')">+</button>
            </div>
        </div>
        
        <div class="item-actions">
            <button class="btn btn-danger btn-sm" 
                    onclick="removeItem('<c:out value="${item.cartItem.cartId}"/>')">
                Remove
            </button>
        </div>
    </div>
</c:forEach>
                </div>

                <div class="cart-summary">
                    <div class="summary-content">
                        <h3>Order Summary</h3>
                        <div class="summary-row">
                            <span>Items (${cartCount}):</span>
                            <span>$<fmt:formatNumber value="${totalPrice}" pattern="#,##0.00"/></span>
                        </div>
                        <div class="summary-row">
                            <span>Shipping:</span>
                            <span>Free</span>
                        </div>
                        <div class="summary-row total">
                            <span>Total:</span>
                            <span>$<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
                        </div>
                        
                        <div class="cart-actions">
                            <a href="${pageContext.request.contextPath}/user/checkout.jsp" 
                               class="btn btn-primary btn-block">
                                Proceed to Checkout
                            </a>
                            <button onclick="clearCart()" class="btn btn-secondary btn-block">
                                Clear Cart
                            </button>
                            <a href="${pageContext.request.contextPath}/courses" 
                               class="btn btn-outline btn-block">
                                Continue Shopping
                            </a>
                        </div>
                    </div>
                </div>
            </c:if>

            <c:if test="${empty cartItems}">
                <div class="empty-cart">
                    <div class="empty-cart-icon">🛒</div>
                    <h2>Your cart is empty</h2>
                    <p>Discover our courses and books to start your investment learning journey!</p>
                    <div class="empty-cart-actions">
                        <a href="${pageContext.request.contextPath}/courses" class="btn btn-primary">
                            Browse Courses
                        </a>
                        <a href="${pageContext.request.contextPath}/books" class="btn btn-secondary">
                            Browse Books
                        </a>
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    <%@include file="../includes/ai-chat-widget.jsp" %>

    <script>
        function updateQuantity(cartId, newQuantity) {
            if (newQuantity < 1) return;
            
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/user/cart';
            
            form.innerHTML = `
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="cartId" value="${cartId}">
                <input type="hidden" name="quantity" value="${newQuantity}">
            `;
            
            document.body.appendChild(form);
            form.submit();
        }

        function removeItem(cartId) {
    console.log("removeItem called with cartId:", cartId);
    if (confirm('Are you sure you want to remove this item from your cart?')) {
        try {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/user/cart';

            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'remove';

            const cartIdInput = document.createElement('input');
            cartIdInput.type = 'hidden';
            cartIdInput.name = 'cartId';
            cartIdInput.value = cartId;

            form.appendChild(actionInput);
            form.appendChild(cartIdInput);
            document.body.appendChild(form);
            console.log("Submitting form for cartId:", cartId);
            form.submit();
        } catch (error) {
            console.error("Error submitting remove form:", error);
            alert("Failed to remove item. Please try again.");
        }
    }
}

        function clearCart() {
            if (confirm('Are you sure you want to clear your entire cart?')) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '${pageContext.request.contextPath}/user/cart';
                form.innerHTML = '<input type="hidden" name="action" value="clear">';
                document.body.appendChild(form);
                form.submit();
            }
        }
    </script>
</body>
</html>