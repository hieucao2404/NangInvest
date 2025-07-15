package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

/**
 * CartServlet handles cart operations including viewing, updating, and removing
 * items
 */
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

    /**
     * Handles GET requests to display cart contents
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + LOGIN_REDIRECT);
            return;
        }

        try {
            // Load cart data with course details
            loadCartData(request, user.getUserId());

            // Forward to cart.jsp
            request.getRequestDispatcher(CART_JSP).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute(ERROR_ATTR, "Error loading cart: " + e.getMessage());
            request.getRequestDispatcher(CART_JSP).forward(request, response);
        }
    }

    /**
     * Handles POST requests for cart operations (update, remove, clear)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + LOGIN_REDIRECT);
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("update".equals(action)) {
                handleUpdateQuantity(request);
            } else if ("remove".equals(action)) {
                handleRemoveItem(request);
            } else if ("clear".equals(action)) {
                handleClearCart(user.getUserId());
            }

            // Redirect to cart page to show updated cart
            response.sendRedirect(request.getContextPath() + "/user/cart");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/user/cart?error=operation_failed");
        }
    }

    /**
     * Load cart data with course details for display
     */
    private void loadCartData(HttpServletRequest request, int userId) {
        try {
            List<Cart> cartItems = cartDAO.findByUserId(userId);
            List<CartItemWithDetails> cartItemsWithDetails = new ArrayList<>();
            BigDecimal totalPrice = BigDecimal.ZERO;

            for (Cart cartItem : cartItems) {
                // Assuming productId refers to courseId for courses
                var courseOpt = courseDAO.findById(cartItem.getProductId());
                if (courseOpt.isPresent()) {
                    Course course = courseOpt.get();
                    CartItemWithDetails itemWithDetails = new CartItemWithDetails();
                    itemWithDetails.setCartItem(cartItem);
                    itemWithDetails.setCourse(course);
                    cartItemsWithDetails.add(itemWithDetails);

                    // Calculate total price
                    BigDecimal itemTotal = course.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                    totalPrice = totalPrice.add(itemTotal);
                }
            }

            request.setAttribute("cartItems", cartItemsWithDetails);
            request.setAttribute("totalPrice", totalPrice);
            request.setAttribute("cartCount", cartItems.size());

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute(ERROR_ATTR, "Error loading cart data: " + e.getMessage());
            request.setAttribute("cartItems", new ArrayList<>());
            request.setAttribute("totalPrice", BigDecimal.ZERO);
            request.setAttribute("cartCount", 0);
        }
    }

    /**
     * Handle updating item quantity in cart
     */
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

    /**
     * Handle removing item from cart
     */
    private void handleRemoveItem(HttpServletRequest request) {
        try {
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            System.out.println("Removing cartId: " + cartId); // Debug log
            boolean removed = cartDAO.removeCartItemById(cartId);
            if (!removed) {
                request.setAttribute(ERROR_ATTR, "Failed to remove item - No matching cart item found.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid cart ID", e);
        }
    }

    /**
     * Handle clearing all items from cart
     */
    private void handleClearCart(int userId) {
        cartDAO.clearCartByUserId(userId);
    }

    /**
     * Inner class to hold cart item with course details
     */
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
