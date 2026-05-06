<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý thiết bị ESP32 - AgriTech</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/myPlant.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <div class="sidebar">
        <!-- Sidebar giữ nguyên, active mục Quản lý thiết bị -->
        <div class="logo">
            <i class="fa-solid fa-leaf"></i>
            <span>Agri-Tech</span>
        </div>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa-solid fa-house"></i> Tổng quan</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/admin/devices"><i class="fa-solid fa-microchip"></i> Quản lý thiết bị</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/users"><i class="fa-solid fa-users"></i></i> Quản lý người dùng</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/plants"><i class="fa-solid fa-seedling"></i> Danh mục cây</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/logout" class="logout"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a></li>
        </nav>
    </div>

    <div class="main-content">
        <header>
            <div class="page-title">
                <h2><i class="fa-solid fa-server"></i> Danh sách thiết bị hệ thống</h2>
                <p>Tổng số thiết bị đang hoạt động: ${listDevice.size()}</p>
            </div>
        </header>

        <div class="plants-grid">
            <c:forEach items="${listDevice}" var="d">
                <div class="plant-card device-card">
                    <!-- Trạng thái Online/Offline -->
                    <div class="status-indicator ${d.status.toLowerCase() == 'online' ? 'active' : 'inactive'}">
                        <i class="fa-solid fa-signal"></i> ${d.status}
                    </div>
                    
                    <!-- <div class="plant-img-container device-img">
                        <i class="fa-solid fa-microchip"></i>
                    </div> -->

                    <div class="plant-info">
                        <span class="plant-type">Mã thiết bị</span>
                        <h3 class="custom-name">${d.deviceCode}</h3>
                        
                        <div class="detail-row">
                            <i class="fa-solid fa-calendar-check"></i>
                            <span>Ngày đăng ký: 
                                <strong><fmt:parseDate value="${d.createdAt}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                                <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy"/></strong>
                            </span>
                        </div>

                        <div class="fertilizer-box info-box">
                            <i class="fa-solid fa-circle-info"></i>
                            <p>Thiết bị đang quản lý hệ thống tưới và cảm biến môi trường.</p>
                        </div>
                    </div>

                    <div class="card-actions">
                        <button class="btn-control"><i class="fa-solid fa-tower-broadcast"></i> Kiểm tra</button>
                        <button class="btn-edit btn-danger"><i class="fa-solid fa-power-off"></i> Ngắt</button>
                    </div>
                </div>
            </c:forEach>
        </div>

        <c:if test="${empty listDevice}">
            <div class="empty-state">
                <i class="fa-solid fa-microchip"></i>
                <p>Chưa có thiết bị nào được đăng ký trên hệ thống.</p>
            </div>
        </c:if>
    </div>
</body>
</html>