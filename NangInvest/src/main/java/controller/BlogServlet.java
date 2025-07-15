/*
 * Blog controller handling blog-related operations
 */
package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.BlogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.Blog;
import model.User;
import util.FileUploadUtil;

/**
 * BlogServlet handles all blog-related operations:
 * - Listing blogs (public and admin view)
 * - Viewing single blog posts
 * - Creating new blog posts (admin)
 * - Editing blog posts (admin)
 * - Deleting blog posts (admin)
 * 
 * @author Admin
 */
@WebServlet(name = "BlogServlet", urlPatterns = { "/blogs", "/blog", "/admin/blogs/*", "/public/blog", "/user/blog" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 20 // 20MB
)
public class BlogServlet extends HttpServlet {

    private final BlogDAO blogDAO = new BlogDAO();
    private static final int BLOGS_PER_PAGE = 6;

    /**
     * Handles the HTTP <code>GET</code> method.
     * Routes to different methods based on the path and parameters.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getServletPath();
        String action = request.getParameter("action");

        // Check if user is admin for admin-only actions
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        boolean isAdmin = user != null && user.getRole() == User.Role.ADMIN;

        // Handle specific blog routes
        if (pathInfo.contains("/admin/blogs")) {
            if (!isAdmin) {
                response.sendRedirect(request.getContextPath() + "/public/blog");
                return;
            }

            if (action == null) {
                // Admin blog list
                listBlogsAdmin(request, response);
            } else if (action.equals("create")) {
                // Show blog creation form
                showBlogForm(request, response, null);
            } else if (action.equals("edit")) {
                // Show blog edit form
                String blogIdStr = request.getParameter("id");
                if (blogIdStr != null && !blogIdStr.isEmpty()) {
                    try {
                        int blogId = Integer.parseInt(blogIdStr);
                        Optional<Blog> blog = blogDAO.findById(blogId);
                        if (blog.isPresent()) {
                            showBlogForm(request, response, blog.get());
                        } else {
                            response.sendRedirect(request.getContextPath() + "/admin/blogs?error=Blog+not+found");
                        }
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath() + "/admin/blogs?error=Invalid+blog+ID");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/blogs?error=No+blog+ID+provided");
                }
            } else if (action.equals("delete")) {
                // Handle delete request (could be GET from admin panel)
                deleteBlog(request, response);
            }
        } else if (pathInfo.contains("/blog") || pathInfo.contains("/public/blog") || pathInfo.contains("/user/blog")) {
            // Public blog routes
            String blogIdStr = request.getParameter("id");

            if (blogIdStr != null && !blogIdStr.isEmpty()) {
                // View specific blog post
                viewBlog(request, response, blogIdStr);
            } else {
                // List all blogs (public view)
                listBlogsPublic(request, response);
            }
        } else {
            // Default to public blog list
            listBlogsPublic(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * Used for blog creation, updates, and deletion.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is admin
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        boolean isAdmin = user != null && user.getRole() == User.Role.ADMIN;

        if (!isAdmin) {
            response.sendRedirect(request.getContextPath() + "/public/blog?error=Unauthorized");
            return;
        }

        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "create":
                    createBlog(request, response);
                    break;
                case "update":
                    updateBlog(request, response);
                    break;
                case "delete":
                    deleteBlog(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/blogs?error=Invalid+action");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/blogs?error=No+action+specified");
        }
    }

    /**
     * List blogs for public view with pagination, filtering, and search
     */
    private void listBlogsPublic(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String topic = request.getParameter("topic");
        String search = request.getParameter("search");
        String pageParam = request.getParameter("page");
        int page = 1;
        int blogsPerPage = 6;

        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1)
                    page = 1;
            } catch (NumberFormatException e) {
                System.err.println("Invalid page parameter: " + pageParam);
                page = 1;
            }
        }
        int offset = (page - 1) * blogsPerPage;

        List<Blog> blogs = new ArrayList<>();
        Blog featuredBlog = null;
        List<String> topics = new ArrayList<>();
        int totalPages = 0;
        String error = null;

        try {
            System.out.println(
                    "listBlogsPublic: topic=" + topic + ", search=" + search + ", page=" + page + ", offset=" + offset);
            if (search != null && !search.trim().isEmpty()) {
                blogs = blogDAO.findByBlogNameContainingPaginated(search, offset, blogsPerPage);
                long totalBlogs = blogDAO.getBlogCountByNameContaining(search);
                totalPages = (int) Math.ceil((double) totalBlogs / blogsPerPage);
            } else if (topic != null && !topic.trim().isEmpty()) {
                blogs = blogDAO.findBlogsByTopicPaginated(topic, offset, blogsPerPage);
                long totalBlogs = blogDAO.getBlogCountByTopic(topic);
                totalPages = (int) Math.ceil((double) totalBlogs / blogsPerPage);
            } else {
                blogs = blogDAO.findBlogsPaginated(offset, blogsPerPage);
                long totalBlogs = blogDAO.getBlogCount();
                totalPages = (int) Math.ceil((double) totalBlogs / blogsPerPage);
            }
            featuredBlog = blogs.isEmpty() ? null : blogs.get(0);
            topics = blogDAO.findAllTopics();
            System.out.println("listBlogsPublic: Found " + blogs.size() + " blogs, totalPages=" + totalPages
                    + ", topics=" + topics);
        } catch (Exception e) {
            System.err.println("Error in listBlogsPublic: " + e.getMessage());
            e.printStackTrace();
            error = "Failed to load blogs: " + e.getMessage();
        }

        request.setAttribute("blogs", blogs);
        request.setAttribute("featuredBlog", featuredBlog);
        request.setAttribute("topics", topics);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("error", error);
        request.getRequestDispatcher("/public/blog.jsp").forward(request, response);
    }



    /**
     * List blogs for admin view with pagination
     */
    private void listBlogsAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int pageNumber = getPageNumber(request);
        int offset = (pageNumber - 1) * BLOGS_PER_PAGE;

        // Get all blogs for admin with pagination
        List<Blog> blogs = blogDAO.findBlogsPaginated(offset, BLOGS_PER_PAGE);
        long totalBlogs = blogDAO.getBlogCount();
        int totalPages = (int) Math.ceil((double) totalBlogs / BLOGS_PER_PAGE);

        // Get all topics for filter dropdown
        List<String> topics = blogDAO.findAllTopics();

        // Set request attributes
        request.setAttribute("blogs", blogs);
        request.setAttribute("topics", topics);
        request.setAttribute("currentPage", pageNumber);
        request.setAttribute("totalPages", totalPages);

        // Forward to admin blog list page
        request.getRequestDispatcher("/admin/blog_list.jsp").forward(request, response);
    }

    /**
     * View a single blog post
     */
    private void viewBlog(HttpServletRequest request, HttpServletResponse response, String blogIdStr)
            throws ServletException, IOException {

        try {
            int blogId = Integer.parseInt(blogIdStr);
            Optional<Blog> blogOptional = blogDAO.findById(blogId);

            if (blogOptional.isPresent()) {
                Blog blog = blogOptional.get();
                request.setAttribute("blog", blog);

                // Get related blogs with same topic (up to 3)
                if (blog.getTopic() != null && !blog.getTopic().isEmpty()) {
                    List<Blog> relatedBlogs = blogDAO.findByTopic(blog.getTopic());
                    // Remove the current blog from related blogs
                    relatedBlogs.removeIf(b -> b.getBlogId().equals(blog.getBlogId()));
                    // Limit to 3 related blogs
                    if (relatedBlogs.size() > 3) {
                        relatedBlogs = relatedBlogs.subList(0, 3);
                    }
                    request.setAttribute("relatedBlogs", relatedBlogs);
                }

                request.getRequestDispatcher("/public/blogPost.jsp").forward(request, response);
            } else {
                // Blog not found
                response.sendRedirect(request.getContextPath() + "/public/blog?error=Blog+not+found");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/public/blog?error=Invalid+blog+ID");
        }
    }

    /**
     * Show blog creation/edit form
     */
    private void showBlogForm(HttpServletRequest request, HttpServletResponse response, Blog blog)
            throws ServletException, IOException {

        // Get all topics for dropdown
        List<String> topics = blogDAO.findAllTopics();
        request.setAttribute("topics", topics);

        if (blog != null) {
            // Edit mode
            request.setAttribute("blog", blog);
            request.setAttribute("formAction", "update");
            request.setAttribute("formTitle", "Edit Blog Post");
        } else {
            // Create mode
            request.setAttribute("formAction", "create");
            request.setAttribute("formTitle", "Create New Blog Post");
        }

        request.getRequestDispatcher("/admin/forms/blogForm.jsp").forward(request, response);
    }

    /**
     * Create a new blog post
     */
    private void createBlog(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get blog data from form
        String blogName = request.getParameter("blogName");
        String topic = request.getParameter("topic");
        String detailedContent = request.getParameter("detailedContent");

        // Validate required fields
        if (blogName == null || blogName.trim().isEmpty() || detailedContent == null
                || detailedContent.trim().isEmpty()) {
            request.setAttribute("error", "Blog name and content are required");
            showBlogForm(request, response, null);
            return;
        }

        // Check if blog name already exists
        if (blogDAO.existsByBlogName(blogName)) {
            request.setAttribute("error", "A blog with this name already exists");
            showBlogForm(request, response, null);
            return;
        }

        // Handle image upload
        String imageUrl = null;
        Part filePart = request.getPart("blogImage");
        if (filePart != null && filePart.getSize() > 0) {
            imageUrl = FileUploadUtil.saveFile(filePart, getServletContext().getRealPath("/images/blogs"));
        }

        // Create blog object
        Blog blog = new Blog(blogName, topic, imageUrl, detailedContent);

        // Save to database
        blogDAO.save(blog);

        // Redirect to admin blogs list
        response.sendRedirect(request.getContextPath() + "/admin/blogs?success=Blog+created+successfully");
    }

    /**
     * Update an existing blog post
     */
    private void updateBlog(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get blog data from form
        String blogIdStr = request.getParameter("blogId");
        String blogName = request.getParameter("blogName");
        String topic = request.getParameter("topic");
        String detailedContent = request.getParameter("detailedContent");

        // Validate required fields
        if (blogIdStr == null || blogName == null || blogName.trim().isEmpty() || detailedContent == null
                || detailedContent.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/blogs?error=Missing+required+fields");
            return;
        }

        try {
            int blogId = Integer.parseInt(blogIdStr);
            Optional<Blog> blogOptional = blogDAO.findById(blogId);

            if (blogOptional.isPresent()) {
                Blog blog = blogOptional.get();

                // Check if blog name exists and is different from current blog
                if (!blog.getBlogName().equals(blogName) && blogDAO.existsByBlogName(blogName)) {
                    request.setAttribute("error", "A blog with this name already exists");
                    request.setAttribute("blog", blog);
                    showBlogForm(request, response, blog);
                    return;
                }

                // Update blog data
                blog.setBlogName(blogName);
                blog.setTopic(topic);
                blog.setDetailedContent(detailedContent);

                // Handle image upload
                Part filePart = request.getPart("blogImage");
                if (filePart != null && filePart.getSize() > 0) {
                    String imageUrl = FileUploadUtil.saveFile(filePart,
                            getServletContext().getRealPath("/images/blogs"));
                    blog.setImageUrl(imageUrl);
                }

                // Update in database
                blogDAO.update(blog);

                // Redirect to admin blogs list
                response.sendRedirect(request.getContextPath() + "/admin/blogs?success=Blog+updated+successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/blogs?error=Blog+not+found");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/blogs?error=Invalid+blog+ID");
        }
    }

    /**
     * Delete a blog post
     */
    private void deleteBlog(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String blogIdStr = request.getParameter("id");

        if (blogIdStr != null && !blogIdStr.isEmpty()) {
            try {
                int blogId = Integer.parseInt(blogIdStr);
                Optional<Blog> blogOptional = blogDAO.findById(blogId);

                if (blogOptional.isPresent()) {
                    Blog blogToDelete = blogOptional.get();
                    blogDAO.delete(blogToDelete);
                    response.sendRedirect(request.getContextPath() + "/admin/blogs?success=Blog+deleted+successfully");
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/blogs?error=Blog+not+found");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/blogs?error=Invalid+blog+ID");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/blogs?error=No+blog+ID+provided");
        }
    }

    /**
     * Helper method to get page number from request
     */
    private int getPageNumber(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        int page = 1; // Default to first page

        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                // Ignore and use default
            }
        }

        return page;
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Blog Servlet handling all blog-related operations";
    }
}
