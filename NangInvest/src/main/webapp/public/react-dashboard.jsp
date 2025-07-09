<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>NangInvest - React Dashboard</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/style.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/responsive.css"
    />
    <style>
      /* Additional styles for React components */
      .blog-card {
        border: 1px solid #ddd;
        border-radius: 8px;
        padding: 15px;
        margin-bottom: 20px;
        transition: transform 0.3s ease;
      }

      .blog-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
      }

      .blog-image {
        max-width: 100%;
        height: auto;
        border-radius: 4px;
        margin: 10px 0;
      }

      .blog-topic {
        background-color: #f0f8ff;
        display: inline-block;
        padding: 3px 10px;
        border-radius: 20px;
        font-size: 0.9em;
      }

      .read-more {
        display: inline-block;
        margin-top: 10px;
        padding: 5px 15px;
        background-color: #007bff;
        color: white;
        text-decoration: none;
        border-radius: 4px;
      }
    </style>
  </head>
  <body>
    <!-- Header section -->
    <jsp:include page="header.jsp" />

    <div class="container">
      <h1>NangInvest React Integration</h1>

      <!-- React App Mount Point -->
      <div id="react-blog-dashboard"></div>
    </div>

    <!-- Footer section -->
    <jsp:include page="footer.jsp" />

    <!-- Option 1: Direct script loading (for development) -->
    <!--
    <script src="https://unpkg.com/react@19/umd/react.development.js"></script>
    <script src="https://unpkg.com/react-dom@19/umd/react-dom.development.js"></script>
    <script src="${pageContext.request.contextPath}/js/react-build/bundle.js"></script>
    -->

    <!-- Option 2: Webpack bundle (for production) -->
    <script src="${pageContext.request.contextPath}/js/react-build/bundle.js"></script>

    <!-- Custom initialization script to mount React on a specific element -->
    <script>
      document.addEventListener("DOMContentLoaded", function () {
        // Initialize React components if needed
      });
    </script>
  </body>
</html>
