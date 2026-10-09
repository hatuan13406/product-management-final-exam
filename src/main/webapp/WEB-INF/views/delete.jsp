<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Xác nhận xóa sản phẩm | Final Exam</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>
<header class="app-header">
    <div class="container header-inner">
        <a class="logo" href="${pageContext.request.contextPath}/products"><span class="logo-icon">◈</span> Product<span>Hub</span></a>
    </div>
</header>
<main class="container narrow">
    <a class="back" href="${pageContext.request.contextPath}/products">← Quay về danh sách</a>
    <section class="panel form-panel">
        <p class="eyebrow danger-text">XÁC NHẬN XÓA</p>
        <h1>Bạn muốn xóa sản phẩm này?</h1>
        <p class="muted">Thao tác xóa không thể hoàn tác. Vui lòng kiểm tra thông tin trước khi xác nhận.</p>
        <dl class="details">
            <div><dt>ID</dt><dd>#<c:out value="${product.id}" /></dd></div>
            <div><dt>Tên sản phẩm</dt><dd><c:out value="${product.name}" /></dd></div>
            <div><dt>Giá</dt><dd><c:out value="${product.price}" /> VNĐ</dd></div>
            <div><dt>Số lượng</dt><dd><c:out value="${product.quantity}" /></dd></div>
        </dl>
        <form action="${pageContext.request.contextPath}/products" method="post">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="id" value="${product.id}">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
            <div class="form-actions">
                <button type="submit" class="button danger">Đồng ý xóa</button>
                <a class="button neutral" href="${pageContext.request.contextPath}/products">Hủy bỏ</a>
            </div>
        </form>
    </section>
</main>
</body>
</html>
