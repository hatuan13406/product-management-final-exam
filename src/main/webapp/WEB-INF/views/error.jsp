<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Lỗi cơ sở dữ liệu | Final Exam</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>
<main class="container narrow error-page">
    <section class="panel form-panel">
        <p class="eyebrow danger-text">KHÔNG THỂ KẾT NỐI</p>
        <h1>Đã xảy ra lỗi</h1>
        <div class="validation-error"><c:out value="${errorMessage}" /></div>
        <a class="button primary" href="${pageContext.request.contextPath}/products">Thử lại</a>
    </section>
</main>
</body>
</html>
