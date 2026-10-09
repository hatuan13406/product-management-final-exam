<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${editing ? 'Sửa sản phẩm' : 'Thêm sản phẩm'} | Final Exam</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>
<header class="app-header">
    <div class="container header-inner">
        <a class="logo" href="${pageContext.request.contextPath}/products"><span class="logo-icon">◈</span> Product<span>Hub</span></a>
        <span class="header-label">Quản lý sản phẩm</span>
    </div>
</header>
<main class="container narrow">
    <a class="back" href="${pageContext.request.contextPath}/products">← Quay về danh sách</a>
    <section class="panel form-panel">
        <p class="eyebrow">${editing ? 'CHỈNH SỬA' : 'THÊM MỚI'}</p>
        <h1>${editing ? 'Cập nhật sản phẩm' : 'Thêm sản phẩm mới'}</h1>
        <p class="muted">Nhập đầy đủ tên, giá và số lượng sản phẩm.</p>

        <c:if test="${not empty error}">
            <div class="validation-error" role="alert"><c:out value="${error}" /></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/products" accept-charset="UTF-8">
            <input type="hidden" name="action" value="${editing ? 'edit' : 'create'}">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
            <c:if test="${editing}"><input type="hidden" name="id" value="${product.id}"></c:if>

            <label for="name">Tên sản phẩm <span class="required">*</span></label>
            <input type="text" id="name" name="name" maxlength="150" required
                   value="${fn:escapeXml(formName)}" placeholder="Ví dụ: Laptop Dell Inspiron">

            <div class="two-col">
                <div>
                    <label for="price">Giá (VNĐ) <span class="required">*</span></label>
                    <input type="number" id="price" name="price" min="0" max="9999999999.99"
                           step="0.01" inputmode="decimal" required
                           value="${fn:escapeXml(formPrice)}" placeholder="Ví dụ: 12990000">
                    <div class="hint">Giá không âm, tối đa 2 chữ số thập phân.</div>
                </div>
                <div>
                    <label for="quantity">Số lượng <span class="required">*</span></label>
                    <input type="number" id="quantity" name="quantity" min="0" max="2147483647"
                           step="1" inputmode="numeric" required
                           value="${fn:escapeXml(formQuantity)}" placeholder="Ví dụ: 20">
                    <div class="hint">Số nguyên lớn hơn hoặc bằng 0.</div>
                </div>
            </div>

            <div class="form-actions">
                <button class="button primary" type="submit">${editing ? 'Lưu thay đổi' : 'Thêm sản phẩm'}</button>
                <a class="button neutral" href="${pageContext.request.contextPath}/products">Hủy bỏ</a>
            </div>
        </form>
    </section>
</main>
</body>
</html>
