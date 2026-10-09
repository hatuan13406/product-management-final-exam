package com.codegym.web;

import com.codegym.dao.JdbcProductDAO;
import com.codegym.dao.ProductDAO;
import com.codegym.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

/**
 * Product web controller: GET renders pages; POST mutates the database.
 * This application is a classroom assignment, not a public multi-user system.
 */
@WebServlet(name = "ProductServlet", urlPatterns = "/products")
public class ProductServlet extends HttpServlet {
    private ProductDAO dao;

    @Override
    public void init() {
        dao = new JdbcProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("X-Content-Type-Options", "nosniff");
        ensureCsrfToken(request);
        String action = param(request, "action");
        try {
            switch (action) {
                case "create" -> showProductForm(request, response, new Product(), false);
                case "edit" -> showExistingForm(request, response, true);
                case "delete" -> showExistingForm(request, response, false);
                case "", "list" -> listProducts(request, response);
                default -> response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Trang không tồn tại");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        } catch (SQLException e) {
            showDatabaseError(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setHeader("X-Content-Type-Options", "nosniff");
        if (!validCsrfToken(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Phiên làm việc không hợp lệ. Hãy tải lại trang và thử lại.");
            return;
        }
        try {
            switch (param(request, "action")) {
                case "create" -> saveProduct(request, response, false);
                case "edit" -> saveProduct(request, response, true);
                case "delete" -> deleteProduct(request, response);
                default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Thao tác không hợp lệ");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        } catch (SQLException e) {
            showDatabaseError(request, response, e);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        request.setAttribute("products", dao.findAll());
        forward(request, response, "/WEB-INF/views/list.jsp");
    }

    private void showExistingForm(HttpServletRequest request, HttpServletResponse response,
                                  boolean editing)
            throws SQLException, ServletException, IOException {
        int id = parseId(request.getParameter("id"));
        Optional<Product> found = dao.findById(id);
        if (found.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm");
            return;
        }
        if (editing) {
            showProductForm(request, response, found.get(), true);
        } else {
            request.setAttribute("product", found.get());
            forward(request, response, "/WEB-INF/views/delete.jsp");
        }
    }

    private void saveProduct(HttpServletRequest request, HttpServletResponse response,
                             boolean editing)
            throws SQLException, ServletException, IOException {
        int id = editing ? parseId(request.getParameter("id")) : 0;
        String name = param(request, "name").strip();
        String priceText = param(request, "price").strip();
        String quantityText = param(request, "quantity").strip();

        Product draft = new Product();
        draft.setId(id);
        draft.setName(name);
        request.setAttribute("formName", name);
        request.setAttribute("formPrice", priceText);
        request.setAttribute("formQuantity", quantityText);

        String error = null;
        BigDecimal price = null;
        int quantity = 0;
        if (name.isBlank() || name.length() > 150) {
            error = "Tên sản phẩm phải có từ 1 đến 150 ký tự.";
        } else {
            try {
                price = new BigDecimal(priceText);
                if (price.compareTo(BigDecimal.ZERO) < 0
                        || price.scale() > 2 || price.precision() - price.scale() > 10) {
                    error = "Giá phải từ 0 đến 9.999.999.999,99 và có tối đa 2 chữ số thập phân.";
                }
            } catch (NumberFormatException e) {
                error = "Giá sản phẩm phải là số hợp lệ, ví dụ 150000.00.";
            }
        }
        if (error == null) {
            try {
                quantity = Integer.parseInt(quantityText);
                if (quantity < 0) error = "Số lượng không được âm.";
            } catch (NumberFormatException e) {
                error = "Số lượng phải là số nguyên không âm.";
            }
        }
        if (error != null) {
            request.setAttribute("error", error);
            showProductForm(request, response, draft, editing);
            return;
        }

        Product product = new Product(id, name, price, quantity);
        if (editing) {
            if (dao.findById(id).isEmpty() || !dao.update(product)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm");
                return;
            }
            redirectToList(request, response, "updated");
        } else {
            dao.insert(product);
            redirectToList(request, response, "created");
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = parseId(request.getParameter("id"));
        if (!dao.deleteById(id)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm");
            return;
        }
        redirectToList(request, response, "deleted");
    }

    private void showProductForm(HttpServletRequest request, HttpServletResponse response,
                                 Product product, boolean editing)
            throws ServletException, IOException {
        request.setAttribute("editing", editing);
        request.setAttribute("product", product);
        if (request.getAttribute("formName") == null) {
            request.setAttribute("formName", product.getName() == null ? "" : product.getName());
        }
        if (request.getAttribute("formPrice") == null) {
            request.setAttribute("formPrice", product.getPrice() == null
                    ? "" : product.getPrice().toPlainString());
        }
        if (request.getAttribute("formQuantity") == null) {
            request.setAttribute("formQuantity", editing ? String.valueOf(product.getQuantity()) : "");
        }
        forward(request, response, "/WEB-INF/views/form.jsp");
    }

    private void showDatabaseError(HttpServletRequest request, HttpServletResponse response,
                                   SQLException ex)
            throws ServletException, IOException {
        getServletContext().log("MySQL product management failure", ex);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        request.setAttribute("errorMessage", "Không thể truy cập cơ sở dữ liệu. "
                + "Hãy kiểm tra MySQL, database.sql và các biến môi trường PRODUCT_DB_*. ");
        forward(request, response, "/WEB-INF/views/error.jsp");
    }

    private static int parseId(String input) {
        int id = Integer.parseInt(input == null ? "" : input.strip());
        if (id <= 0) throw new NumberFormatException("ID must be positive");
        return id;
    }

    private static String param(HttpServletRequest request, String key) {
        String result = request.getParameter(key);
        return result == null ? "" : result;
    }

    private static void ensureCsrfToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("csrfToken") == null) {
            session.setAttribute("csrfToken", UUID.randomUUID().toString());
        }
    }

    private static boolean validCsrfToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null
                && session.getAttribute("csrfToken") instanceof String
                && session.getAttribute("csrfToken").equals(request.getParameter("csrfToken"));
    }

    private static void forward(HttpServletRequest request, HttpServletResponse response,
                                String jsp) throws ServletException, IOException {
        request.getRequestDispatcher(jsp).forward(request, response);
    }

    private static void redirectToList(HttpServletRequest request,
                                       HttpServletResponse response, String notice)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/products?notice=" + notice);
    }
}
