<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@page
import="model.User"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Login | NangInvest</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <script src="https://accounts.google.com/gsi/client" async defer></script>
    <meta
      name="google-signin-client_id"
      content="422211950963-r094spj3shieq20gkajosg6lplpppehv.apps.googleusercontent.com"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/public-headbar.css"
    />
    <style>
      /* Your existing CSS remains unchanged */
      body {
        background: linear-gradient(120deg, #f8fafc 0%, #e0e7ff 100%);
        font-family: "Segoe UI", Arial, sans-serif;
        margin: 0;
        padding: 0;
      }
      .public-headbar {
        background: #1e293b;
        color: #fff;
        padding: 0.5rem 0;
        box-shadow: 0 2px 8px rgba(30, 41, 59, 0.08);
      }
      .public-headbar .container {
        display: flex;
        align-items: center;
        justify-content: space-between;
        max-width: 900px;
        margin: 0 auto;
        padding: 0 1rem;
      }
      .public-headbar .logo {
        font-size: 1.5rem;
        font-weight: bold;
        letter-spacing: 1px;
      }
      .public-headbar nav a {
        color: #fff;
        text-decoration: none;
        margin-left: 2rem;
        font-size: 1rem;
        transition: color 0.2s;
      }
      .public-headbar nav a:hover {
        color: #38bdf8;
      }
      .form-container {
        max-width: 400px;
        margin: 2rem auto;
        padding: 2rem 2rem 1.5rem 2rem;
        background: #fff;
        border-radius: 12px;
        box-shadow: 0 4px 24px rgba(30, 41, 59, 0.08);
      }
      .toggle-buttons {
        text-align: center;
        margin-bottom: 1.5rem;
      }
      .toggle-buttons button {
        margin: 0 8px;
        padding: 10px 32px;
        border: none;
        border-radius: 6px;
        background: #1e293b;
        color: #fff;
        font-size: 1rem;
        cursor: pointer;
        transition: background 0.2s;
      }
      .toggle-buttons button:hover {
        background: #38bdf8;
      }
      h2 {
        text-align: center;
        color: #1e293b;
        margin-bottom: 1rem;
      }
      label {
        display: block;
        margin-top: 1rem;
        margin-bottom: 0.3rem;
        color: #334155;
        font-weight: 500;
      }
      input[type="text"],
      input[type="email"],
      input[type="password"],
      input[type="number"] {
        width: 100%;
        padding: 10px;
        border: 1px solid #cbd5e1;
        border-radius: 6px;
        margin-bottom: 0.8rem;
        font-size: 1rem;
        background: #f1f5f9;
      }
      input[type="submit"] {
        width: 100%;
        padding: 12px;
        background: #38bdf8;
        color: #fff;
        border: none;
        border-radius: 6px;
        font-size: 1.1rem;
        font-weight: bold;
        cursor: pointer;
        margin-top: 1.2rem;
        transition: background 0.2s;
      }
      input[type="submit"]:hover {
        background: #1e293b;
      }
      .hidden {
        display: none;
      }
      .google-login {
        display: block;
        width: 100%;
        text-align: center;
        margin: 1.5rem 0 0.5rem 0;
        padding: 12px;
        background: #fff;
        border: 1px solid #38bdf8;
        border-radius: 6px;
        color: #1e293b;
        font-weight: 500;
        text-decoration: none;
        font-size: 1rem;
        transition: background 0.2s, color 0.2s;
      }
      .google-login:hover {
        background: #38bdf8;
        color: #fff;
      }
      .form-container hr {
        margin: 2rem 0 1rem 0;
        border: none;
        border-top: 1px solid #e2e8f0;
      }
      .form-container .error {
        color: #ef4444;
        text-align: center;
        margin-top: 1rem;
      }
      .form-container .success {
        color: #22c55e;
        text-align: center;
        margin-top: 1rem;
      }
    </style>
  </head>
  <div class="public-headbar">
    <div class="container">
      <span class="logo">NangInvest</span>
      <nav>
        <a href="${pageContext.request.contextPath}/">Home</a>
        <a href="${pageContext.request.contextPath}/courses">Courses</a>
        <a href="${pageContext.request.contextPath}/books">Books</a>
        <a href="${pageContext.request.contextPath}/about">About</a>
      </nav>
    </div>
  </div>

  <% String rememberedUser = ""; try { rememberedUser =
  util.CookieUtil.getCookieValue(request, "rememberedUser"); if (rememberedUser
  == null) { rememberedUser = ""; } } catch (Exception e) { rememberedUser = "";
  }%>
  <body>
    <div class="form-container">
      <div class="toggle-buttons">
        <button onclick="showLogin()">Login</button>
        <button onclick="showSignup()">Sign Up</button>
      </div>

      <h2>Login</h2>
      <form
        id="loginForm"
        method="post"
        action="${pageContext.request.contextPath}/login"
      >
        <input type="hidden" name="loginType" value="username" />
        <label>Username:</label>
        <input
          type="text"
          name="username"
          value="<%= rememberedUser%>"
          required
        /><br />
        <label>Password:</label>
        <input type="password" name="password" required /><br />
        <label> <input type="checkbox" name="rememberMe" /> Remember Me </label>
        <input type="submit" value="Login" />
      </form>

      <form
        id="signupForm"
        class="hidden"
        method="post"
        action="${pageContext.request.contextPath}/users?action=register"
      >
        <label>Full Name:</label>
        <input type="text" name="name" required /><br />
        <label>Username:</label>
        <input type="text" name="username" required /><br />
        <label>Email:</label>
        <input type="email" name="email" required /><br />
        <label>Password:</label>
        <input type="password" name="password" required /><br />
        <label>Age:</label>
        <input type="number" name="age" min="1" required /><br />
        <label>Expertise:</label>
        <input
          type="text"
          name="expertise"
          placeholder="e.g., Finance, Marketing"
          required
        /><br />
        <input type="submit" value="Register" />
      </form>

      <hr />

      <a
        href="https://accounts.google.com/o/oauth2/v2/auth?scope=email%20profile&redirect_uri=http://localhost:8081/NangInvest/login/google&response_type=code&client_id=422211950963-r094spj3shieq20gkajosg6lplpppehv.apps.googleusercontent.com&prompt=consent&access_type=offline"
        class="google-login"
      >
        Login with Google
      </a>

      <% String error = (String) request.getAttribute("error"); if (error !=
      null) {%>
      <div class="error"><%= error%></div>
      <% } String success = (String) request.getAttribute("success"); if
      (success != null) {%>
      <div class="success"><%= success%></div>
      <% }%>
    </div>

    <script>
      function showLogin() {
        document.getElementById("loginForm").classList.remove("hidden");
        document.getElementById("signupForm").classList.add("hidden");
      }

      function showSignup() {
        document.getElementById("signupForm").classList.remove("hidden");
        document.getElementById("loginForm").classList.add("hidden");
      }
    </script>

    <script>
      function handleCredentialResponse(response) {
        const form = document.createElement("form");
        form.method = "POST";
        form.action = "<%= request.getContextPath()%>/login";

        const tokenInput = document.createElement("input");
        tokenInput.type = "hidden";
        tokenInput.name = "credential";
        tokenInput.value = response.credential;

        const typeInput = document.createElement("input");
        typeInput.type = "hidden";
        typeInput.name = "loginType";
        typeInput.value = "google";

        form.appendChild(tokenInput);
        form.appendChild(typeInput);

        document.body.appendChild(form);
        form.submit();
      }
    </script>
  </body>
</html>
