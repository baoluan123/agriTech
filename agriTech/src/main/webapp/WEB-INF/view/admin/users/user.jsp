<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <!-- Thêm nếu cần định dạng ngày tháng/tiền tệ -->
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập hệ thống Agri-Tech</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/user.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<!-- Sidebar -->
    <div class="sidebar">
        <div class="logo">
            <i class="fa-solid fa-leaf"></i>
            <span>Agri-Tech</span>
        </div>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa-solid fa-house"></i> Tổng quan</a></li>
                <li ><a href="${pageContext.request.contextPath}/admin/devices"><i class="fa-solid fa-microchip"></i> Quản lý thiết bị</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/admin/users"><i class="fa-solid fa-users"></i></i> Quản lý người dùng</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/plants"><i class="fa-solid fa-seedling"></i> Danh mục cây</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/logout" class="logout"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a></li>
            </ul>
        </nav>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <header>
            <div class="page-title">
                <h2><i class="fa-solid fa-users-gear"></i> Hệ thống quản lý người dùng</h2>
            </div>
            <div class="user-info">
                <span>Chào, <strong>${adminUser.fullName}</strong></span>
                <span class="badge-admin">${adminUser.levelAdmin}</span>
            </div>
        </header>

        <div class="table-container">
            <div class="table-header">
                <h3>Danh sách khách hàng</h3>
                <div class="search-box">
                    <input type="text" placeholder="Tìm kiếm người dùng...">
                    <button class="btn-search"><i class="fa-solid fa-magnifying-glass"></i></button>
                </div>
            </div>

            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Họ tên</th>
                        <th>Số điện thoại</th>
                        <th>Địa chỉ</th>
                        <th style="text-align: center;">Vườn cây</th>
                        <th style="text-align: center;">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${userList}" var="user">
                        <tr>
                            <td>#${user.id}</td>
                            <td><strong>${user.account.fullName}</strong></td>
                            <td>${user.phone}</td>
                            <td>${user.address}</td>
                            <td style="text-align: center;">
                                <!-- Nút Xem Vườn: truyền accountId vào URL -->
                                <a href="${pageContext.request.contextPath}/admin/gardens/${user.id}" class="btn-action btn-garden">
                                    <i class="fa-solid fa-seedling"></i> Xem vườn
                                </a>
                            </td>
                            <td style="text-align: center;">
                                <button class="btn-icon btn-edit"><i class="fa-solid fa-pen-to-square"></i></button>
                                <button class="btn-icon btn-delete"><i class="fa-solid fa-trash"></i></button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty userList}">
                        <tr>
                            <td colspan="6" style="text-align: center; padding: 20px; color: #999;">Không có người dùng nào.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

</body>
</html>