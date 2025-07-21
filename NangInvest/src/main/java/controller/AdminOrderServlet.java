package controller;

import java.io.IOException;
import java.util.List;

import dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.User;

@WebServlet(name = "AdminOrderServlet", urlPatterns = { "/admin/orders" })
public class AdminOrderServlet extends HttpServlet {
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() != User.Role.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/public/login-register.jsp?redirect=/admin/orders");
            return;
        }

        List<Order> pendingOrders = orderDAO.findPendingOrders();
        request.setAttribute("pendingOrders", pendingOrders);
        request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() != User.Role.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/public/login-register.jsp?redirect=/admin/orders");
            return;
        }

        String action = request.getParameter("action");
        if ("confirm".equals(action)) {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            orderDAO.updatePaymentStatus(orderId, "Completed");
        }
        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}