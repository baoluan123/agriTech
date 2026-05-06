<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <!-- Thêm nếu cần định dạng ngày tháng/tiền tệ -->
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập hệ thống Agri-Tech</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <div class="login-container">
        <div class="login-box">
            <div class="login-header">
                <i class="fa-solid fa-leaf"></i>
                <h1>Agri-Tech Admin</h1>
                <p>Hệ thống quản lý vườn thông minh</p>
            </div>

            <!-- Hiển thị thông báo lỗi từ Controller nếu có -->
            <c:if test="${not empty error}">
                <div class="alert-error">
                    <i class="fa-solid fa-circle-exclamation"></i> ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/admin/login" method="POST">
                <div class="input-group">
                    <label for="username">Tên đăng nhập</label>
                    <div class="input-wrapper">
                        <i class="fa-solid fa-user"></i>
                        <input type="text" id="username" name="username" placeholder="Nhập tài khoản" required>
                    </div>
                </div>

                <div class="input-group">
                    <label for="pass">Mật khẩu</label>
                    <div class="input-wrapper">
                        <i class="fa-solid fa-lock"></i>
                        <input type="password" id="pass" name="pass" placeholder="Nhập mật khẩu" required>
                    </div>
                </div>

                <button type="submit" class="btn-login">Đăng Nhập</button>
            </form>

            <div class="login-footer">
                <p>&copy; 2026 Agri-Tech Project - Đà Nẵng</p>
            </div>
        </div>
    </div>

</body>
</html>