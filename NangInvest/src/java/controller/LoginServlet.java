/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.GeneralSecurityException;
import java.util.Collections;
import model.User;

/**
 *
 * @author Admin
 */
@WebServlet(name = "LoginServlet", urlPatterns = { "/login", "/login/google" })
public class LoginServlet extends HttpServlet {

    UserDAO userDAO = new UserDAO();
    private static final String CLIENT_ID = "422211950963-r094spj3shieq20gkajosg6lplpppehv.apps.googleusercontent.com"; // google
    // oath
    // client
    // id

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/login/google".equals(path)) {
            String oathRrl = "https://accounts.google.com/o/oauth2/v2/auth?"
                    + "client_id=" + CLIENT_ID
                    + "&redirect_uri=" + request.getContextPath() + "/login/google"
                    + "&response_type=code"
                    + "&scope=email%20profile%20openid"
                    + "&access_type=offline";
            response.sendRedirect(oathRrl);
        } else {
            request.getRequestDispatcher("public/login.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        if ("/login".equals(path)) {
            String loginType = request.getParameter("loginType");
            if ("username".equals(loginType)) {
                String username = request.getParameter("username").trim();
                String password = request.getParameter("password").trim();
                System.out.println("Username from form: '" + username + "'");
                System.out.println("Password from form: '" + password + "'");
                User user = userDAO.checkLoginByUsername(username, password);
                if (user != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    response.sendRedirect(request.getContextPath() + "/public/homepage.jsp");
                } else {
                    request.setAttribute("error", "Invalid username or password");
                    request.getRequestDispatcher("public/login.jsp").forward(request, response);
                }
            } else if ("/login/google".equals(path)) {
                String idTokenString = request.getParameter("id_token");
                if (idTokenString != null) {
                    try {
                        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                                GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
                                .setAudience(Collections.singletonList(CLIENT_ID)).build();
                        GoogleIdToken idToken = verifier.verify(idTokenString);
                        if (idToken != null) {
                            GoogleIdToken.Payload payload = idToken.getPayload();
                            String email = payload.getEmail();
                            String name = (String) payload.get("name");

                            User user = userDAO.findByEmail(email);
                            if (user == null) {
                                // create new user
                                user = new User();
                                user.setUserName(email.split("@")[0]);
                                user.setEmail(email);
                                user.setName(name);
                                user.setRole(User.Role.USER);
                                userDAO.addUser(user);
                            }

                            HttpSession session = request.getSession();
                            session.setAttribute("user", user);
                            response.sendRedirect(request.getContextPath() + "/users?action=list");
                        } else {
                            request.setAttribute("error", "Invalid Google ID token");
                            request.getRequestDispatcher("public/login.jsp").forward(request, response);
                        }
                    } catch (GeneralSecurityException | IOException e) {
                        request.setAttribute("error", "Error verifying Google token: " + e.getMessage());
                        request.getRequestDispatcher("/public/login.jsp").forward(request, response);
                    }
                }
            }

        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
