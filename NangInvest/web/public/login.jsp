<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Login</title>

        <!-- Google Identity Services -->
        <script src="https://accounts.google.com/gsi/client" async defer></script>

        <meta name="google-signin-client_id"
              content="422211950963-r094spj3shieq20gkajosg6lplpppehv.apps.googleusercontent.com">


        <!--        Style the page      -->

        <style>
            .form-container {
                max-width: 400px;
                margin: auto;
                padding: 20px;
                border: 1px solid #ccc;
                border-radius: 8px;
            }
            .toggle-buttons {
                text-align: center;
                margin-bottom: 10px;
            }
            .toggle-buttons button {
                margin: 5px;
                padding: 10px 20px;
                cursor: pointer;
            }
            .hidden {
                display: none;
            }
        </style>
    </head>


    <body>
        <!--- form for to choose -->
        <div class="form-container">
            <div class="toggle-buttons">
                <button onclick="showLogin()">Login</button>
                <button onclick="showSignup()">Sign Up</button>
            </div>


            <!--<!-- Login form -->
            <h2>Login</h2>
            <form id ="loginForm" method="post" action="login">
                <input type="hidden" name="loginType" value="username" />
                <label>Username:</label>
                <input type="text" name="username" required /><br/>
                <label>Password:</label>
                <input type="password" name="password" required /><br/>
                <input type="submit" value="Login" />
            </form>

            <!<!-- Signup form -->
            <form id="signupForm" class="hidden" method="post" action="users?action=register">
                <label>Full Name:</label>
                <input type="text" name="name" required /><br/>

                <label>Username:</label>
                <input type="text" name="username" required /><br/>

                <label>Email:</label>
                <input type="email" name="email" required /><br/>

                <label>Password:</label>
                <input type="password" name="password" required /><br/>

                <label>Age:</label>
                <input type="number" name="age" min="1" required /><br/>

                <label>Expertise:</label>
                <input type="text" name="expertise" placeholder="e.g., Finance, Marketing" required /><br/>

                <input type="submit" value="Register" />
            </form>


            <hr/>

            <!-- ✅ Google Login Button -->
            <a href="https://accounts.google.com/o/oauth2/v2/auth?scope=email%20profile&redirect_uri=http://localhost:8081/NangInvest/login/google&response_type=code&client_id=422211950963-r094spj3shieq20gkajosg6lplpppehv.apps.googleusercontent.com&prompt=consent&access_type=offline">
                Login with Google
            </a>

            <% String error = (String) request.getAttribute("error");
                if (error != null) {%>
            <div style="color:red;"><%= error%></div>
            <% }%>

        </div>

        <!<!-- JS for toggle -->
        <script>
            function showLogin() {
                document.getElementById("loginForm").classList.remove("hidden");
                document.getElementById("signupForm").classList.add("hidden");
            }
            
            function showSignup(){
                document.getElementById("signupForm").classList.remove("hidden");
                document.getElementById("loginForm").classList.add("hidden");
            }
        </script>




        <!-- ✅ Script to handle Google Login -->
        <script>
            function handleCredentialResponse(response) {
                // Send the ID token to your backend
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = 'login';

                const tokenInput = document.createElement('input');
                tokenInput.type = 'hidden';
                tokenInput.name = 'credential';
                tokenInput.value = response.credential;

                const typeInput = document.createElement('input');
                typeInput.type = 'hidden';
                typeInput.name = 'loginType';
                typeInput.value = 'google';

                form.appendChild(tokenInput);
                form.appendChild(typeInput);

                document.body.appendChild(form);
                form.submit();
            }
        </script>


    </body>
</html>
