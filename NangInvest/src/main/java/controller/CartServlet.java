package controller;

import dao.CartDAO;
import dao.CourseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.Course;
import model.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CartServlet", urlPatterns = { "/user/cart" })
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CART_JSP = "/user/cart.jsp";
    private static final String ERROR_ATTR = "error";
    private static final String LOGIN_REDIRECT = "/public/login-register.jsp";

    private transient CartDAO cartDAO;
    private transient CourseDAO courseDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        cartDAO = new CartDAO();
        courseDAO = new CourseDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("CartServlet doGet invoked"); // Debug
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            System.out.println("User not logged in, redirecting to login"); // Debug
            response.sendRedirect(request.getContextPath() + LOGIN_REDIRECT);
            return;
        }

        try {
            loadCartData(request, user.getUserId());
            System.out.println("Forwarding to cart.jsp"); // Debug
            request.getRequestDispatcher(CART_JSP).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading cart: " + e.getMessage()); // Debug
            request.setAttribute(ERROR_ATTR, "Error loading cart: " + e.getMessage());
            request.getRequestDispatcher(CART_JSP).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("CartServlet doPost invoked"); // Debug
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            System.out.println("User not logged in, redirecting to login"); // Debug
            response.sendRedirect(request.getContextPath() + LOGIN_REDIRECT + "?redirect=/user/cart");
            return;
        }

        String action = request.getParameter("action");
        System.out.println("Action received: " + action); // Debug
        if (action == null) {
            System.out.println("No action parameter, redirecting to cart"); // Debug
            response.sendRedirect(request.getContextPath() + "/user/cart?error=invalid_action");
            return;
        }

        try {
            switch (action) {
                case "update":
                    System.out.println("Handling update action"); // Debug
                    handleUpdateQuantity(request);
                    response.sendRedirect(request.getContextPath() + "/user/cart?updated=true");
                    break;
                case "remove":
                    System.out.println("Handling remove action"); // Debug
                    handleRemoveItem(request);
                    response.sendRedirect(request.getContextPath() + "/user/cart?removed=true");
                    break;
                case "clear":
                    System.out.println("Handling clear action"); // Debug
                    handleClearCart(user.getUserId());
                    response.sendRedirect(request.getContextPath() + "/user/cart?cleared=true");
                    break;
                case "checkout":
                    System.out.println("Handling checkout, forwarding to /user/checkout"); // Debug
                    handleCheckout(user, request, response);
                    return; // Exit after forward
                default:
                    System.out.println("Invalid action: " + action); // Debug
                    response.sendRedirect(request.getContextPath() + "/user/cart?error=invalid_action");
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error processing action: " + e.getMessage()); // Debug
            response.sendRedirect(request.getContextPath() + "/user/cart?error=operation_failed");
        }
    }

    private void handleCheckout(User user, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Forwarding to /user/checkout for user: " + user.getUserId()); // Debug
        request.getRequestDispatcher("/user/checkout").forward(request, response);
    }

    private void loadCartData(HttpServletRequest request, int userId) {
        try {
            List<Cart> cartItems = cartDAO.findByUserId(userId);
            List<CartItemWithDetails> cartItemsWithDetails = new ArrayList<>();
            BigDecimal totalPrice = BigDecimal.ZERO;

            for (Cart cartItem : cartItems) {
                Course course = courseDAO.findById(cartItem.getProductId()).orElse(null);
                if (course != null) {
                    CartItemWithDetails itemWithDetails = new CartItemWithDetails();
                    itemWithDetails.setCartItem(cartItem);
                    itemWithDetails.setCourse(course);
                    cartItemsWithDetails.add(itemWithDetails);
                    if (!course.isFreeOfCharge()) {
                        BigDecimal itemTotal = course.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                        totalPrice = totalPrice.add(itemTotal);
                    }
                }
            }

            request.setAttribute("cartItems", cartItemsWithDetails);
            request.setAttribute("totalPrice", totalPrice);
            request.setAttribute("cartCount", cartItems.size());
            System.out.println("Cart data loaded: " + cartItems.size() + " items, total: " + totalPrice); // Debug
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading cart data: " + e.getMessage()); // Debug
            request.setAttribute(ERROR_ATTR, "Error loading cart data: " + e.getMessage());
            request.setAttribute("cartItems", new ArrayList<>());
            request.setAttribute("totalPrice", BigDecimal.ZERO);
            request.setAttribute("cartCount", 0);
        }
    }

    private void handleUpdateQuantity(HttpServletRequest request) {
        try {
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            if (quantity > 0) {
                cartDAO.updateQuantity(cartId, quantity);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid quantity or cart ID", e);
        }
    }

    private void handleRemoveItem(HttpServletRequest request) {
        try {
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            System.out.println("Removing cartId: " + cartId); // Debug
            boolean removed = cartDAO.removeCartItemById(cartId);
            if (!removed) {
                request.setAttribute(ERROR_ATTR, "Failed to remove item - No matching cart item found.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid cart ID", e);
        }
    }

    private void handleClearCart(int userId) {
        cartDAO.clearCartByUserId(userId);
    }

    public static class CartItemWithDetails {
        private Cart cartItem;
        private Course course;

        public Cart getCartItem() {
            return cartItem;
        }

        public void setCartItem(Cart cartItem) {
            this.cartItem = cartItem;
        }

        public Course getCourse() {
            return course;
        }

        public void setCourse(Course course) {
            this.course = course;
        }
    }
}