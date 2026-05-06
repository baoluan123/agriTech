<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <!-- Thêm nếu cần định dạng ngày tháng/tiền tệ -->
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Agri-Tech Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin-style.css">
    <!-- Icon từ FontAwesome để làm Menu cho đẹp -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <!-- Sidebar bên trái -->
    <div class="sidebar">
        <div class="logo">
            <i class="fa-solid fa-leaf"></i>
            <span>Agri-Tech</span>
        </div>
        <nav>
            <ul>
                
                <li class="active"><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa-solid fa-house"></i> Tổng quan</a></li>
                <li ><a href="${pageContext.request.contextPath}/admin/devices"><i class="fa-solid fa-microchip"></i> Quản lý thiết bị</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/users"><i class="fa-solid fa-users"></i></i> Quản lý người dùng</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/plants"><i class="fa-solid fa-seedling"></i> Danh mục cây</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/logout" class="logout"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a></li>
            </ul>
        </nav>
    </div>

    <!-- Nội dung chính bên phải -->
    <div class="main-content">
        <header>
            <div class="user-info">
                <span>Chào, <strong>${adminUser.fullName}</strong></span>
                <span class="badge">${adminUser.levelAdmin}</span>
            </div>
        </header>

        <section class="dashboard-cards">
            <!-- Card Nhiệt độ -->
            <div class="card card-temp">
                <div class="card-icon"><i class="fa-solid fa-temperature-half"></i></div>
                <div class="card-data">
                    <h3>Nhiệt độ</h3>
                    <p class="value">28°C</p>
                </div>
            </div>

            <!-- Card Độ ẩm -->
            <div class="card card-humidity">
                <div class="card-icon"><i class="fa-solid fa-droplet"></i></div>
                <div class="card-data">
                    <h3>Độ ẩm đất</h3>
                    <p class="value">65%</p>
                </div>
            </div>

            <!-- Card Trạng thái bơm -->
            <div class="card card-pump">
                <div class="card-icon"><i class="fa-solid fa-faucet-drip"></i></div>
                <div class="card-data">
                    <h3>Máy bơm</h3>
                    <p class="status on">ĐANG CHẠY</p>
                </div>
            </div>
        </section>

        <!-- Bảng dữ liệu chi tiết -->
        <div class="data-table-container">
            <h3>Lịch sử hoạt động thiết bị</h3>
            <table>
                <thead>
                    <tr>
                        <th>Thời gian</th>
                        <th>Thiết bị</th>
                        <th>Hành động</th>
                        <th>Trạng thái</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>20:30 - 06/05/2026</td>
                        <td>Máy bơm 01</td>
                        <td>Bật tự động</td>
                        <td><span class="tag success">Thành công</span></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

</body>
</html>