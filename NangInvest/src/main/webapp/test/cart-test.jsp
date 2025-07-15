<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Cart Test - NangInvest</title>
    <style>
      body {
        font-family: Arial, sans-serif;
        padding: 20px;
      }
      .test-section {
        margin: 20px 0;
        padding: 15px;
        border: 1px solid #ddd;
      }
      .success {
        color: green;
      }
      .error {
        color: red;
      }
      .info {
        color: blue;
      }
    </style>
  </head>
  <body>
    <h1>Cart Functionality Test</h1>

    <div class="test-section">
      <h3>Test Actions</h3>
      <p class="info">Use these links to test cart functionality:</p>

      <div style="margin: 10px 0">
        <a href="${pageContext.request.contextPath}/courses" target="_blank">
          → Go to Courses Page (Browse & Add to Cart)
        </a>
      </div>

      <div style="margin: 10px 0">
        <a href="${pageContext.request.contextPath}/user/cart" target="_blank">
          → View Cart
        </a>
      </div>

      <div style="margin: 10px 0">
        <a
          href="${pageContext.request.contextPath}/user/myCourses"
          target="_blank"
        >
          → My Courses (After Purchase)
        </a>
      </div>
    </div>

    <div class="test-section">
      <h3>Test Workflow</h3>
      <ol>
        <li>
          <strong>Browse Courses:</strong> Go to courses page and browse
          available courses
        </li>
        <li><strong>Add to Cart:</strong> Click "Add to Cart" on any course</li>
        <li>
          <strong>View Cart:</strong> Click cart icon or go to cart page to see
          added items
        </li>
        <li>
          <strong>Update Quantities:</strong> Test quantity increase/decrease
          buttons
        </li>
        <li><strong>Remove Items:</strong> Test remove item functionality</li>
        <li><strong>Checkout:</strong> Proceed to checkout (if implemented)</li>
        <li>
          <strong>View My Courses:</strong> After purchase, check My Courses
          page
        </li>
      </ol>
    </div>

    <div class="test-section">
      <h3>Expected Results</h3>
      <ul>
        <li class="success">✓ Courses page loads with available courses</li>
        <li class="success">
          ✓ Cart icon shows item count after adding courses
        </li>
        <li class="success">
          ✓ Cart page displays course names, prices, and images
        </li>
        <li class="success">✓ Quantity controls work properly</li>
        <li class="success">✓ Remove item button works</li>
        <li class="success">✓ Total price calculates correctly</li>
        <li class="success">✓ Clear cart functionality works</li>
        <li class="success">✓ Cart persists items across sessions</li>
      </ul>

      <h4>✅ Current Implementation Status:</h4>
      <ul>
        <li class="info">
          📍 <strong>CoursesServlet:</strong> /courses - Browse and add to cart
        </li>
        <li class="info">
          📍 <strong>CartServlet:</strong> /user/cart - View and manage cart
        </li>
        <li class="info">
          📍 <strong>MyCoursesServlet:</strong> /user/myCourses - View enrolled
          courses
        </li>
        <li class="info">
          🔧 <strong>Cart Operations:</strong> Add, Remove, Update Quantity,
          Clear Cart
        </li>
        <li class="info">
          💾 <strong>Data Structure:</strong> CartItemWithDetails for full
          course info
        </li>
      </ul>
    </div>

    <div class="test-section">
      <h3>Quick Debug Info</h3>
      <p>
        <strong>User ID:</strong> ${sessionScope.user != null ?
        sessionScope.user.userId : 'Not logged in'}
      </p>
      <p><strong>Session ID:</strong> ${pageContext.session.id}</p>
      <p><strong>Context Path:</strong> ${pageContext.request.contextPath}</p>
      <p><strong>Current URL:</strong> ${pageContext.request.requestURL}</p>

      <h4>🔗 Direct URLs to Test:</h4>
      <ul class="info">
        <li>
          <strong>This Test Page:</strong>
          http://localhost:8081/NangInvest/test/cart-test.jsp
        </li>
        <li>
          <strong>Courses Page:</strong>
          http://localhost:8081${pageContext.request.contextPath}/courses
        </li>
        <li>
          <strong>Cart Page:</strong>
          http://localhost:8081${pageContext.request.contextPath}/user/cart
        </li>
        <li>
          <strong>My Courses:</strong>
          http://localhost:8081${pageContext.request.contextPath}/user/myCourses
        </li>
      </ul>
    </div>

    <div class="test-section">
      <h3>Manual SQL Test</h3>
      <p class="info">
        You can also manually test by running these SQL queries:
      </p>
      <pre>
-- Check if courses exist
SELECT * FROM Courses LIMIT 5;

-- Check user's cart (replace YOUR_USER_ID with actual user ID)
SELECT c.ID, c.UserID, c.CourseID, c.Quantity, co.Title, co.Price 
FROM Cart c 
JOIN Courses co ON c.CourseID = co.ID 
WHERE c.UserID = YOUR_USER_ID;

-- Check user's enrolled courses
SELECT uc.*, c.Title 
FROM UserCourses uc 
JOIN Courses c ON uc.CourseID = c.ID 
WHERE uc.UserID = YOUR_USER_ID;

-- Example: Check all cart items
SELECT c.*, co.Title, co.Price, co.Description 
FROM Cart c 
JOIN Courses co ON c.CourseID = co.ID 
ORDER BY c.UserID, c.ID;
        </pre
      >
    </div>
  </body>
</html>
