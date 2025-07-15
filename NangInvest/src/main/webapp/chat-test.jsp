<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Chat API Test</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  </head>
  <body>
    <h1>AI Chat API Test</h1>

    <div id="test-results">
      <h2>Testing Chat API...</h2>
      <div id="status">Loading...</div>
    </div>

    <div style="margin-top: 20px">
      <input
        type="text"
        id="test-message"
        placeholder="Type a test message"
        value="Hello AI"
      />
      <button onclick="testChat()">Test Chat</button>
    </div>

    <div
      id="response"
      style="
        margin-top: 20px;
        padding: 10px;
        background: #f0f0f0;
        min-height: 100px;
      "
    >
      Response will appear here...
    </div>

    <script>
      // Test different possible URLs
      const possibleUrls = [
        "/api/chat",
        "/NangInvest/api/chat",
        "<%=request.getContextPath()%>/api/chat",
        "./api/chat",
      ];

      function updateStatus(message) {
        document.getElementById("status").innerHTML = message;
      }

      function updateResponse(message) {
        document.getElementById("response").innerHTML =
          "<pre>" + message + "</pre>";
      }

      async function testUrl(url, message) {
        try {
          updateStatus("Testing URL: " + url);

          const formData = new FormData();
          formData.append("message", message);

          const response = await fetch(url, {
            method: "POST",
            body: formData,
            credentials: "same-origin",
          });

          const statusText = response.status + " " + response.statusText;
          updateStatus("URL: " + url + " - Status: " + statusText);

          if (response.ok) {
            const contentType = response.headers.get("content-type");
            let result;

            if (contentType && contentType.includes("application/json")) {
              result = await response.json();
              updateResponse(
                "SUCCESS with " +
                  url +
                  ":\n\n" +
                  JSON.stringify(result, null, 2)
              );
              return true;
            } else {
              result = await response.text();
              updateResponse("SUCCESS with " + url + ":\n\n" + result);
              return true;
            }
          } else {
            const errorText = await response.text();
            updateResponse(
              "FAILED with " + url + " (" + statusText + "):\n\n" + errorText
            );
            return false;
          }
        } catch (error) {
          updateResponse("ERROR with " + url + ":\n\n" + error.message);
          return false;
        }
      }

      async function testChat() {
        const message =
          document.getElementById("test-message").value || "Hello AI";

        updateStatus("Testing chat API...");
        updateResponse("Testing in progress...");

        for (const url of possibleUrls) {
          const success = await testUrl(url, message);
          if (success) {
            updateStatus("SUCCESS! Working URL: " + url);
            break;
          }
          await new Promise((resolve) => setTimeout(resolve, 1000)); // Wait 1 second between tests
        }
      }

      // Auto-test on page load
      window.onload = function () {
        setTimeout(testChat, 1000);
      };

      // Display context info
      updateStatus(
        "Context Path: <%=request.getContextPath()%><br>Servlet Path: <%=request.getServletPath()%><br>Request URL: <%=request.getRequestURL()%>"
      );
    </script>
  </body>
</html>
