<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý sản phẩm | Final Exam</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>
<header class="app-header">
    <div class="container header-inner">
        <a class="logo" href="${pageContext.request.contextPath}/products"><span class="logo-icon">◈</span> Product<span>Hub</span></a>
        <span class="header-label">JSP • Servlet • JDBC • MySQL</span>
    </div>
</header>
<main class="container">
    <div class="page-top">
        <div>
            <p class="eyebrow">TRANG QUẢN TRỊ</p>
            <h1>Quản lý sản phẩm</h1>
            <p class="muted">Theo dõi danh sách, giá bán và số lượng sản phẩm của bạn.</p>
        </div>
        <a class="button primary" href="${pageContext.request.contextPath}/products?action=create">+ Thêm sản phẩm</a>
    </div>

    <c:if test="${param.notice eq 'created'}">
        <div class="notification">✓ Đã thêm sản phẩm mới thành công.</div>
    </c:if>
    <c:if test="${param.notice eq 'updated'}">
        <div class="notification">✓ Đã cập nhật thông tin sản phẩm.</div>
    </c:if>
    <c:if test="${param.notice eq 'deleted'}">
        <div class="notification">✓ Đã xóa sản phẩm thành công.</div>
    </c:if>

    <section class="panel">
        <div class="panel-header">
            <div>
                <h2>Danh sách sản phẩm</h2>
                <p class="muted">Có <strong><c:out value="${products.size()}" /></strong> sản phẩm trong hệ thống.</p>
            </div>
            <span class="pill">Dữ liệu MySQL</span>
        </div>
        <div class="table-scroll">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên sản phẩm</th>
                    <th>Giá (VNĐ)</th>
                    <th>Số lượng</th>
                    <th class="actions-header">Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="product" items="${products}">
                    <tr>
                        <td class="id">#<c:out value="${product.id}" /></td>
                        <td class="product-name"><c:out value="${product.name}" /></td>
                        <td class="price"><c:out value="${product.price}" /></td>
                        <td><span class="quantity"><c:out value="${product.quantity}" /></span></td>
                        <td class="actions">
                            <a class="edit-link" href="${pageContext.request.contextPath}/products?action=edit&amp;id=${product.id}">Sửa</a>
                            <a class="delete-link" href="${pageContext.request.contextPath}/products?action=delete&amp;id=${product.id}">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty products}">
                    <tr>
                        <td colspan="5" class="empty">Chưa có sản phẩm nào. Hãy nhấn “Thêm sản phẩm” để bắt đầu.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </section>
    <footer>Bài kiểm tra cuối module • Phát triển ứng dụng Web với JSP &amp; Servlet</footer>
</main>
</body>
</html>
