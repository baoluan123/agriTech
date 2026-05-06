<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<java8:formatLocalDateTime value="${p.lastWatered}" pattern="dd/MM/yyyy HH:mm"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết vườn cây - AgriTech</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/myPlant.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

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

    <div class="main-content">
    <header>
        <div class="page-title">
            <a href="${pageContext.request.contextPath}/admin/users" class="btn-back">
                <i class="fa-solid fa-arrow-left"></i> Quay lại
            </a>
            <h2>Vườn cây của khách hàng: ${plants[0].fullName}</h2>
            <p>Mã người dùng: #USER-${plants[0].userId}</p>
        </div>
    </header>

    <div class="plants-grid">
        <c:forEach items="${plants}" var="p">
            <div class="plant-card">
                <div class="status-indicator ${p.status ? 'active' : 'inactive'}">
                    ${p.status ? 'Đang theo dõi' : 'Ngưng kết nối'}
                </div>
                
                <div class="plant-img-container">
                    <img src="${p.imageUrl}" alt="${p.plantName}">
                </div>

                <div class="plant-info">
                    <span class="plant-type">${p.plantName}</span>
                    <h3 class="custom-name">${p.customName}</h3>
                    
                    <div class="detail-row">
                        <i class="fa-solid fa-microchip"></i>
                        <span>Thiết bị: <strong>${p.deviceCode}</strong></span>
                    </div>

                    <div class="detail-row">
                        <i class="fa-solid fa-droplet"></i>
                        <span>Tưới lần cuối: 
                            <strong><fmt:formatDate value="${p.lastWatered}" pattern="dd/MM/yyyy HH:mm"/></strong>
                        </span>
                    </div>

                    <div class="fertilizer-box">
                        <i class="fa-solid fa-flask"></i>
                        <p>${p.fertilizerInfo}</p>
                    </div>
                </div>

                <div class="card-actions">
                    <button class="btn-control"><i class="fa-solid fa-faucet-drip"></i> Tưới ngay</button>
                    <button class="btn-edit"><i class="fa-solid fa-gear"></i> Cấu hình</button>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${empty plants}">
        <div class="empty-state">
            <i class="fa-solid fa-seedling"></i>
            <p>Người dùng này chưa có cây trồng nào trong hệ thống.</p>
        </div>
    </c:if>
</div>
</body>
</html>