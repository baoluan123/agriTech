<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${plant.id == null ? 'Thêm cây' : 'Sửa cây'} - AgriTech</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/plantedit.css">
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
            <h2><i class="fa-solid fa-leaf"></i> ${plant.id == null ? 'Thêm giống cây mới' : 'Cập nhật thông số kỹ thuật'}</h2>
        </div>
    </header>

    <div class="form-wrapper">
        <form action="${pageContext.request.contextPath}/admin/plants/save" method="POST" class="agri-form">
            
            <input type="hidden" name="id" value="${plant.id}">

            <!-- Tên cây -->
            <div class="form-group">
                <label for="namePlant"><i class="fa-solid fa-tag"></i> Tên giống cây</label>
                <input type="text" id="namePlant" name="namePlant" value="${plant.namePlant}" required placeholder="Ví dụ: Cây Ớt Chuông">
            </div>

            <!-- Các thông số kỹ thuật nông nghiệp -->
            <div class="form-row">
                <div class="form-group">
                    <label for="idealHumidity"><i class="fa-solid fa-droplet"></i> Độ ẩm lý tưởng (%)</label>
                    <input type="number" step="0.1" id="idealHumidity" name="idealHumidity" value="${plant.idealHumidity}" placeholder="Ví dụ: 75.5">
                </div>
                <div class="form-group">
                    <label for="waterFrequency"><i class="fa-solid fa-clock"></i> Tần suất tưới (Lần/Ngày)</label>
                    <input type="number" id="waterFrequency" name="waterFrequency" value="${plant.waterFrequency}" placeholder="Ví dụ: 2">
                </div>
            </div>

            <!-- Thông tin phân bón -->
            <div class="form-group">
                <label for="fertilizerInfo"><i class="fa-solid fa-flask"></i> Thông tin phân bón</label>
                <input type="text" id="fertilizerInfo" name="fertilizerInfo" value="${plant.fertilizerInfo}" placeholder="Ví dụ: NPK 20-20-15">
            </div>

            <!-- Mô tả chi tiết -->
            <div class="form-group">
                <label for="descriptionPlant"><i class="fa-solid fa-file-lines"></i> Mô tả chi tiết & Kỹ thuật chăm sóc</label>
                <textarea id="descriptionPlant" name="descriptionPlant" rows="5" placeholder="Nhập hướng dẫn chi tiết...">${plant.descriptionPlant}</textarea>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-submit">
                    <i class="fa-solid fa-circle-check"></i> Xác nhận lưu
                </button>
                <a href="${pageContext.request.contextPath}/admin/plants" class="btn-cancel">
                    Hủy bỏ
                </a>
            </div>
        </form>
    </div>
</div>
</body>
</html>