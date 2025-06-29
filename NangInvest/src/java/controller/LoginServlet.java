/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.UserDAO;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GoogleAccount;
import util.CookieUtil;
import model.User;
import model.User.Role;
import org.apache.http.client.fluent.Request;
import util.Constants;

/**
 *
 * @author Admin
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/login/google"})
public class LoginServlet extends HttpServlet {

    UserDAO userDAO = new UserDAO();

    // oath
    // client
    // id
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code != null && !code.isEmpty()) {
            try {
                System.out.println("Received code" + code);
                String accessToken = getToken(code);
                System.out.println("AccToken" + accessToken);
                GoogleAccount acc = getUserInfo(accessToken);
                System.out.println(acc);
                // check if exist
                User user = userDAO.findByEmail(acc.getEmail());
                if (user == null) {
                    user = new User();
                    user.setEmail(acc.getEmail());
                    user.setName(acc.getName());
                    user.setUserName(acc.getEmail().split("@")[0]);
                    user.setPassword(null); //gg user
                    user.setRole(Role.USER);
                    user.setGoogleId(acc.getGoogleId());
                    userDAO.addUser(user);

                }
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                // add cookie
                CookieUtil.addCookie(response, "username", user.getUserName(), 3600);
                CookieUtil.addCookie(response, "role", user.getRole().toString(), 3600);

                response.sendRedirect(request.getContextPath() + "/public/homepage.jsp");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/public/login.jsp?error=google_login_failed");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/public/login.jsp");
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
                User user = userDAO.checkLoginByUsername(username, password);
                if (user != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    //cookie
                    CookieUtil.addCookie(response, "username", user.getUserName(), 3600);
                    CookieUtil.addCookie(response, "role", user.getRole().toString(), 3600);

                    String role = user.getRole().toString();
                    if ("ADMIN".equals(role)) {
                        response.sendRedirect(request.getContextPath() + "/admin/manageUsers.jsp");
                    } else if ("USER".equals(role)) {
                        response.sendRedirect(request.getContextPath() + "/public/homepage.jsp");
                    }
                } else {
                    request.setAttribute("error", "Invalid username or password");
                    request.getRequestDispatcher("public/login.jsp").forward(request, response);
                }
            }
        }

    }

    public static String getToken(final String code) throws ClientProtocolException, IOException {
        String response = Request.Post(Constants.GOOGLE_LINK_GET_TOKEN).bodyForm(Form.form().add("client_id", Constants.GOOGLE_CLIENT_ID)
                .add("client_secret", Constants.GOOGLE_CLIENT_SECRET)
                .add("redirect_uri", Constants.GOOGLE_REDIRECT_URI).add("code", code)
                .add("grant_type", Constants.GOOGLE_GRANT_TYPE).build())
                .execute().returnContent().asString();
        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
        return accessToken;
    }

    public static GoogleAccount getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = Constants.GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        GoogleAccount gga = new Gson().fromJson(response, GoogleAccount.class);
        return gga;
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
