package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SubcribeServlet", urlPatterns = { "/api/subscribe" })
public class SubcribeServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String email = request.getParameter("email");
    if (email == null || email.trim().isEmpty()) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email is required.");
      return;
    }
    // Insert into DB (replace with your connection details)
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_db", "your_user",
        "your_password")) {
      String sql = "INSERT INTO subscribers (email, subscribed_at) VALUES (?, ?)";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, email);
        stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        stmt.executeUpdate();
      }
      response.setContentType("text/html;charset=UTF-8");
      response.getWriter().write(
          "<div style='padding:2rem;text-align:center;'>Thank you for subscribing! Check your inbox for updates.</div>");
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Subscription failed: " + e.getMessage());
    }
  }
}
