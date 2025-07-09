package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;

import dao.BlogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Blog;

/**
 * REST API for Blog data
 */
@WebServlet("/api/blogs")
public class BlogRestServlet extends HttpServlet {

  private final BlogDAO blogDAO = new BlogDAO();
  private final Gson gson = new Gson();

  /**
   * GET method to retrieve blog data
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    try (PrintWriter out = response.getWriter()) {
      // Check if a specific blog ID is requested
      String blogIdParam = request.getParameter("id");

      if (blogIdParam != null && !blogIdParam.isEmpty()) {
        // Get a specific blog
        try {
          int blogId = Integer.parseInt(blogIdParam);
          blogDAO.findById(blogId)
              .ifPresentOrElse(
                  blog -> out.print(gson.toJson(blog)),
                  () -> {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(gson.toJson("Blog not found"));
                  });
        } catch (NumberFormatException e) {
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          out.print(gson.toJson("Invalid blog ID format"));
        }
      } else {
        // Get all blogs or filter by topic
        String topicParam = request.getParameter("topic");

        // Pagination parameters
        int page = 0;
        int size = 10;

        try {
          String pageParam = request.getParameter("page");
          if (pageParam != null && !pageParam.isEmpty()) {
            page = Integer.parseInt(pageParam);
          }

          String sizeParam = request.getParameter("size");
          if (sizeParam != null && !sizeParam.isEmpty()) {
            size = Integer.parseInt(sizeParam);
          }
        } catch (NumberFormatException e) {
          // Use default values if parsing fails
        }

        List<Blog> blogs;
        if (topicParam != null && !topicParam.isEmpty()) {
          blogs = blogDAO.findBlogsByTopicPaginated(topicParam, page, size);
        } else {
          blogs = blogDAO.findBlogsPaginated(page, size);
        }

        out.print(gson.toJson(blogs));
      }
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().print(gson.toJson("Error retrieving blog data: " + e.getMessage()));
    }
  }

  /**
   * POST method to create a new blog
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    try {
      // Parse JSON request body into Blog object
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = request.getReader().readLine()) != null) {
        sb.append(line);
      }

      Blog newBlog = gson.fromJson(sb.toString(), Blog.class);
      Blog savedBlog = blogDAO.save(newBlog);

      response.setStatus(HttpServletResponse.SC_CREATED);
      response.getWriter().print(gson.toJson(savedBlog));
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().print(gson.toJson("Error creating blog: " + e.getMessage()));
    }
  }
}
