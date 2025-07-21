/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.List;

import dao.BooksDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Book;

@WebServlet(name = "BookServlet", urlPatterns = { "/books" })
public class BookServlet extends HttpServlet {
    private BooksDAO booksDAO;

    @Override
    public void init() throws ServletException {
        booksDAO = new BooksDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null)
            action = "list";

        switch (action) {
            case "view":
                handleViewBook(request, response);
                break;
            case "search":
                handleSearchBooks(request, response);
                break;
            case "filter":
                handleFilterBooks(request, response);
                break;
            case "add":
                showBookForm(request, response);
                break;
            case "edit":
                showEditBookForm(request, response);
                break;
            default:
                handleListBooks(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null)
            action = "list";

        switch (action) {
            case "save":
                handleSaveBook(request, response);
                break;
            case "update":
                handleUpdateBook(request, response);
                break;
            case "delete":
                handleDeleteBook(request, response);
                break;
            default:
                handleListBooks(request, response);
                break;
        }
    }

    private void handleListBooks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Book> books = booksDAO.findAll();
        request.setAttribute("books", books);
        request.getRequestDispatcher("/public/books.jsp").forward(request, response);
    }

    private void handleViewBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                Book book = booksDAO.findById(id).orElse(null);
                if (book != null) {
                    request.setAttribute("book", book);
                    request.getRequestDispatcher("/public/bookDetail.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        response.sendRedirect(request.getContextPath() + "/books");
    }

    private void handleSearchBooks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("query");
        List<Book> books;
        if (query != null && !query.trim().isEmpty()) {
            books = booksDAO.findByNamePattern(query.trim());
        } else {
            books = booksDAO.findAll();
        }
        request.setAttribute("books", books);
        request.setAttribute("searchQuery", query);
        request.getRequestDispatcher("/public/books.jsp").forward(request, response);
    }

    private void handleFilterBooks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String topic = request.getParameter("topic");
        List<Book> books;
        if (topic != null && !topic.trim().isEmpty()) {
            books = booksDAO.findByTopic(topic.trim());
        } else {
            books = booksDAO.findAll();
        }
        request.setAttribute("books", books);
        request.setAttribute("filterTopic", topic);
        request.getRequestDispatcher("/public/books.jsp").forward(request, response);
    }

    private void showBookForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/admin/forms/bookForm.jsp").forward(request, response);
    }

    private void showEditBookForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                Book book = booksDAO.findById(id).orElse(null);
                if (book != null) {
                    request.setAttribute("book", book);
                    request.getRequestDispatcher("/admin/forms/bookForm.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        response.sendRedirect(request.getContextPath() + "/books");
    }

    private void handleSaveBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookName = request.getParameter("bookName");
        String topic = request.getParameter("topic");
        String affiliateLink = request.getParameter("affiliateLink");
        String coverImage = request.getParameter("coverImage");
        String previewContent = request.getParameter("previewContent");
        String ratingStr = request.getParameter("rating");
        String isPreviewAvailableStr = request.getParameter("isPreviewAvailable");

        Book book = new Book();
        book.setBookName(bookName);
        book.setTopic(topic);
        book.setAffiliateLink(affiliateLink);
        book.setCoverImage(coverImage);
        book.setPreviewContent(previewContent);
        book.setIsPreviewAvailable("on".equals(isPreviewAvailableStr) || "true".equals(isPreviewAvailableStr));
        try {
            if (ratingStr != null && !ratingStr.isEmpty()) {
                book.setRating(new java.math.BigDecimal(ratingStr));
            }
        } catch (NumberFormatException ignored) {
        }

        booksDAO.save(book);
        response.sendRedirect(request.getContextPath() + "/books");
    }

    private void handleUpdateBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("bookId");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/books");
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            Book book = booksDAO.findById(id).orElse(null);
            if (book == null) {
                response.sendRedirect(request.getContextPath() + "/books");
                return;
            }
            book.setBookName(request.getParameter("bookName"));
            book.setTopic(request.getParameter("topic"));
            book.setAffiliateLink(request.getParameter("affiliateLink"));
            book.setCoverImage(request.getParameter("coverImage"));
            book.setPreviewContent(request.getParameter("previewContent"));
            String ratingStr = request.getParameter("rating");
            if (ratingStr != null && !ratingStr.isEmpty()) {
                try {
                    book.setRating(new java.math.BigDecimal(ratingStr));
                } catch (NumberFormatException ignored) {
                }
            }
            String isPreviewAvailableStr = request.getParameter("isPreviewAvailable");
            book.setIsPreviewAvailable("on".equals(isPreviewAvailableStr) || "true".equals(isPreviewAvailableStr));
            booksDAO.save(book);
        } catch (NumberFormatException ignored) {
        }
        response.sendRedirect(request.getContextPath() + "/books");
    }

    private void handleDeleteBook(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idStr = request.getParameter("bookId");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                booksDAO.deleteById(id);
            } catch (NumberFormatException ignored) {
            }
        }
        response.sendRedirect(request.getContextPath() + "/books");
    }
}
