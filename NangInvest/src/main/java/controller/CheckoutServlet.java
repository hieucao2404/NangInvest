package controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import dao.CartDAO;
import dao.CourseDAO;
import dao.OrderDAO;
import dao.UserCoursesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import model.Cart;
import model.Course;
import model.Order;
import model.User;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/user/checkout"})
public class CheckoutServlet extends HttpServlet {
    private CartDAO cartDAO;
    private CourseDAO courseDAO;
    private OrderDAO orderDAO;
    private UserCoursesDAO userCoursesDAO;

    @Override
    public void init() throws ServletException {
        cartDAO = new CartDAO();
        courseDAO = new CourseDAO();
        orderDAO = new OrderDAO();
        userCoursesDAO = new UserCoursesDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("CheckoutServlet doGet invoked, redirecting to cart");
        response.sendRedirect(request.getContextPath() + "/user/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("CheckoutServlet doPost invoked");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            System.out.println("User not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/public/login-register.jsp?redirect=/user/checkout");
            return;
        }

        try {
            List<Cart> cartItems = cartDAO.findByUserId(user.getUserId());
            System.out.println("Cart items found: " + cartItems.size());
            if (cartItems.isEmpty()) {
                System.out.println("Empty cart, redirecting to cart");
                response.sendRedirect(request.getContextPath() + "/user/cart?error=empty_cart");
                return;
            }

            BigDecimal totalRevenue = BigDecimal.ZERO;
            List<Integer> courseIds = new ArrayList<>();
            List<BigDecimal> itemTotals = new ArrayList<>();
            List<Integer> orderIds = new ArrayList<>();
            for (Cart cartItem : cartItems) {
                Course course = courseDAO.findById(cartItem.getProductId()).orElse(null);
                if (course != null && !course.isFreeOfCharge()) {
                    BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());
                    BigDecimal itemTotal = course.getPrice().multiply(quantity);
                    totalRevenue = totalRevenue.add(itemTotal);
                    courseIds.add(cartItem.getProductId());
                    itemTotals.add(itemTotal);
                }
            }

            if (courseIds.isEmpty()) {
                System.out.println("No chargeable items in cart");
                response.sendRedirect(request.getContextPath() + "/user/cart?error=no_chargeable_items");
                return;
            }

            for (int i = 0; i < courseIds.size(); i++) {
                Integer courseId = courseIds.get(i);
                Course course = courseDAO.findById(courseId).orElse(null);
                if (course != null) {
                    System.out.println("Creating order for courseId: " + courseId);
                    Order order = new Order(user.getUserId(), courseId, "Pending", itemTotals.get(i));
                    int orderId = orderDAO.create(order);
                    System.out.println("Order created with orderId: " + orderId);
                    orderIds.add(orderId);
                }
            }

            // Generate QR code using ZXing
            String qrContent = String.format("Bank: NangInvest Bank\nAccount: 1234567890\nAmount: $%s\nOrder IDs: %s",
                    totalRevenue, String.join(",", orderIds.stream().map(String::valueOf).toList()));
            String qrFilePath = "qr_codes/payment_" + user.getUserId() + "_" + System.currentTimeMillis() + ".png";
            File qrFile = new File(getServletContext().getRealPath("/") + qrFilePath);
            qrFile.getParentFile().mkdirs();
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);
            MatrixToImageWriter.writeToFile(matrix, "PNG", qrFile);
            // Update analytics (optional, comment out if ai.AnalyticsPredictor is unavailable)
            try {
                new ai.AnalyticsPredictor().addRevenue(totalRevenue);
                System.out.println("Analytics updated with total: " + totalRevenue);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Analytics update failed: " + e.getMessage());
            }

            // Clear cart
            cartDAO.clearCartByUserId(user.getUserId());
            System.out.println("Cart cleared for user: " + user.getUserId());

            // Forward to payment.jsp
            request.setAttribute("totalAmount", totalRevenue);
            request.setAttribute("orderIds", String.join(",", orderIds.stream().map(String::valueOf).toList()));
            request.setAttribute("qrCodePath", qrFilePath);
            System.out.println("Forwarding to payment.jsp with QR code: " + qrFilePath);
            request.getRequestDispatcher("/user/payment.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Checkout failed: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/user/cart?error=checkout_failed");
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles checkout with QR code payment";
    }
}