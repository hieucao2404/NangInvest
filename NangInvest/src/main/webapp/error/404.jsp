<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Page Not Found - NangInvest</title>
    <style>
      body {
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
          sans-serif;
        background: linear-gradient(135deg, #2563eb, #3b82f6);
        color: white;
        margin: 0;
        padding: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        min-height: 100vh;
      }
      .error-container {
        text-align: center;
        max-width: 600px;
        padding: 2rem;
      }
      .error-code {
        font-size: 6rem;
        font-weight: bold;
        margin-bottom: 1rem;
      }
      .error-message {
        font-size: 1.5rem;
        margin-bottom: 2rem;
      }
      .error-description {
        font-size: 1rem;
        opacity: 0.9;
        margin-bottom: 2rem;
      }
      .btn {
        display: inline-block;
        padding: 1rem 2rem;
        background: white;
        color: #2563eb;
        text-decoration: none;
        border-radius: 8px;
        font-weight: 600;
        transition: transform 0.2s;
      }
      .btn:hover {
        transform: scale(1.05);
      }
    </style>
  </head>
  <body>
    <div class="error-container">
      <div class="error-code">404</div>
      <div class="error-message">Page Not Found</div>
      <div class="error-description">
        Sorry, the page you are looking for doesn't exist or has been moved.
      </div>
      <a href="${pageContext.request.contextPath}/" class="btn">
        🏠 Return Home
      </a>
    </div>
  </body>
</html>
