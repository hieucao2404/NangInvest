<%-- 
    Document   : login
    Created on : Jun 9, 2025, 9:00:52 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Login</title>
        <script src="https://accounts.google.com/gsi/client" async defer></script>
    </head>
    <body>
        <h2>Login</h2>
        <form  method="post" action="login">
            <input type="hidden" name="loginType" value="username" />
            <label>Username:</label>
            <input type="text" name="username" required /><br/>
            <label>Password:</label>
            <input type="password" name="password" required /><br/>
            <input type="submit" value="Login" />
        </form>

        <hr/>

        <div id="g_id_onload"
             data-client_id="422211950963-r094spj3shieq20gkajosg6lplpppehv.apps.googleusercontent.com"
             data-login_uri="${pageContext.request.contextPath}/login"
             data-auto_prompt="false"
             data-callback="handleCredentialResponse">
        </div>
        <div class="g_id_signin"
             data-type="standard"
             data-size="large"
             data-theme="outline"
             data-text="sign_in_with"
             data-shape="rectangular"
             data-logo_alignment="left">
        </div>

        <script>
            function handleCredentialResponse(response) {
                // Send the ID token to your servlet
                var form = document.createElement('form');
                form.method = 'POST';
                form.action = '${pageContext.request.contextPath}/login';

                var loginType = document.createElement('input');
                loginType.type = 'hidden';
                loginType.name = 'loginType';
                loginType.value = 'google';
                form.appendChild(loginType);

                var idToken = document.createElement('input');
                idToken.type = 'hidden';
                idToken.name = 'id_token';
                idToken.value = response.credential;
                form.appendChild(idToken);

                document.body.appendChild(form);
                form.submit();
            }
        </script>

        <% String error = (String) request.getAttribute("error");
           if (error != null) { %>
            <div style="color:red;"><%= error %></div>
        <% } %>
    </body>
</html>
