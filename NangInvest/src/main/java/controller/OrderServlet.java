/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CartDAO;
import dao.CourseDAO;
import java.io.IOException;
import java.util.List;

import dao.OrderDAO;
import dao.UserCoursesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import model.Cart;
import model.Course;
import model.Order;
import model.User;

/**
 *
 * @author Admin
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/orders"})
public class OrderServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private CartDAO cartDAO;
    private CourseDAO courseDAO;
    private UserCoursesDAO userCoursesDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        cartDAO = new CartDAO();
        courseDAO = new CourseDAO();
        userCoursesDAO = new UserCoursesDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            // Not logged in, redirect to login
            response.sendRedirect(request.getContextPath() + "/public/login-register.jsp?redirect=/orders");
            return;
        }

        // Default action: list all orders for this user
        List<Order> orders = orderDAO.findByUserId(user.getUserId());
        request.setAttribute("orders", orders);

        // Forward to JSP to display orders
        request.getRequestDispatcher("/user/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void handleCheckout(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        try {
            List<Cart> cartItems = cartDAO.findByUserId(user.getUserId());
            if (cartItems.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/user/cart?error=empty_cart");
                return;
            }
            BigDecimal totalRevenue = BigDecimal.ZERO;
            List<Integer> courseIds = new ArrayList<>();
            for (Cart cartItem : cartItems) {
                Course course = courseDAO.findById(cartItem.getProductId()).orElse(null);
                if (course != null && !course.isFreeOfCharge()) {
                    BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());
                    totalRevenue = totalRevenue.add(course.getPrice().multiply(quantity));
                    courseIds.add(cartItem.getProductId());
                    
                    Order order = new Order(user.getUserId(), cartItem.getProductId(), "Completed", course.getPrice().multiply(quantity));
                    orderDAO.create(order);
                    
                    userCoursesDAO.enrollUserInCourse(user.getUserId(), cartItem.getProductId());
                }
            }

            new ai.AnalyticsPredictor().addRevenue(totalRevenue);

            //clear cart
            cartDAO.clearCartByUserId(user.getUserId());

            //create individual orders for each course
            response.sendRedirect(request.getContextPath() + "/user/order-confirmation.jsp?success=order_placed");
        }catch(Exception e){
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/user/cart?error=checkout_failed");
    }
}

/**
 * Returns a short description of the servlet.
 *
 * @return a String containing servlet description
 */
@Override
public String getServletInfo() {
        return "Handles order listing and checkout processing";
    }// </editor-fold>

}
