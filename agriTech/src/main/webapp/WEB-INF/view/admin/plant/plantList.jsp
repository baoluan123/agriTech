<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh mục cây trồng - AgriTech</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/myPlant.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/plant.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <div class="sidebar">
        <!-- Sidebar logic -->
        <div class="logo">
            <i class="fa-solid fa-leaf"></i>
            <span>Agri-Tech</span>
        </div>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa-solid fa-house"></i> Tổng quan</a></li>
                
                <li><a href="${pageContext.request.contextPath}/admin/devices"><i class="fa-solid fa-microchip"></i> Quản lý thiết bị</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/users"><i class="fa-solid fa-users"></i> Quản lý người dùng</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/admin/plants"><i class="fa-solid fa-seedling"></i> Danh mục cây</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/logout" class="logout"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a></li>
            </ul>
        </nav>
    </div>

    <div class="main-content">
        <header>
            <div class="page-title">
                <h2><i class="fa-solid fa-book-open"></i> Thư viện cây trồng</h2>
                <p>Quản lý các loại giống cây hỗ trợ trong hệ thống Agri-Tech</p>
            </div>
            <button class="btn-add-new" onclick="window.location.href='${pageContext.request.contextPath}/admin/plants/add'">
    <i class="fa-solid fa-plus"></i> Thêm giống mới
</button>
        </header>

        <div class="plants-grid">
            <c:forEach items="${plantListDTO}" var="p">
                <div class="plant-card category-card">
                    <div class="plant-img-container">
                        <!-- Hiển thị ảnh thumbnail của giống cây -->
                        <img src="${p.thumbnailUrl}" alt="${p.namePlant}" onerror="this.src='https://via.placeholder.com/150?text=No+Image'">
                    </div>

                    <div class="plant-info">
                        <span class="plant-type">ID Giống: #PLANT-${p.id}</span>
                        <h3 class="custom-name">${p.namePlant}</h3>
                        
                        <div class="description-box">
                            <p>${p.descriptionPlant}</p>
                        </div>

                        <div class="detail-row">
                            <i class="fa-solid fa-circle-check"></i>
                            <span>Trạng thái: <strong>Khả dụng</strong></span>
                        </div>
                    </div>

                    <div class="card-actions">
                        <button class="btn-control btn-view" onclick="window.location.href='${pageContext.request.contextPath}/admin/plants/edit/${p.id}'"><i class="fa-solid fa-pen-to-square"></i> Chỉnh sửa</button>
                        <button class="btn-edit btn-delete-category" onclick="window.location.href='${pageContext.request.contextPath}/admin/plants/delete/${p.id}'"><i class="fa-solid fa-trash-can"></i> Xóa</button>
                    </div>
                </div>
            </c:forEach>
        </div>

        <c:if test="${empty plantListDTO}">
            <div class="empty-state">
                <i class="fa-solid fa-box-open"></i>
                <p>Thư viện hiện đang trống. Hãy thêm các loại cây trồng đầu tiên!</p>
            </div>
        </c:if>
    </div>
</body>
</html>