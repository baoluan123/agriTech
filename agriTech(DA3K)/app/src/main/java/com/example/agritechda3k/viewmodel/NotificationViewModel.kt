package com.example.agritechda3k.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agritechda3k.api.dto.NotificationDTO
import com.example.agritechda3k.database.repository.NotificationRepository
import com.example.agritechda3k.model.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {
    // 1. Luồng dữ liệu lịch sử thông báo (Lấy từ Room)
    // Khi Room có dữ liệu mới từ SSE, biến này sẽ tự cập nhật lên UI
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications
    // --- BỔ SUNG THÊM ĐỂ CHẠY VỚI MAIN ACTIVITY ---

    // 1. Biến quan sát tổng số tin chưa đọc (Dùng LiveData cho Activity dễ observe)
    private val _totalUnReadCount = MutableLiveData<Int>()
    val totalUnReadCount: LiveData<Int> get() = _totalUnReadCount
    // 2. Hàm gọi từ vòng lặp Polling (MainActivity)
    fun getTotalUnreadCount(userId: Long) {
        viewModelScope.launch {
            val count = repository.getTotalUnreadCount(userId) // Nhớ thêm hàm này vào Repo nhé
            _totalUnReadCount.postValue(count)
        }
    }
    // 3. Hàm dập Dialog khi người dùng nhấn "Đóng"
    fun markAllReadForUser(userId: Long) {
        viewModelScope.launch {
            repository.markAllRead(userId) // Gọi xuống Repo để báo Server/DB
            _totalUnReadCount.postValue(0) // Reset con số về 0 ngay để tắt Dialog
        }
    }
    // 2. Hàm lấy lịch sử theo từng cây
    fun loadHistory(plantUserId: Long) {
        viewModelScope.launch {
            repository.getHistory(plantUserId).collect {
                list->_notifications.value = list
            }
        }
        // Gọi thêm refresh để đồng bộ dữ liệu mới nhất từ Server về Room
        viewModelScope.launch {
            repository.refreshHistory(plantUserId)
        }
    }
    // 3. Hàm kích hoạt Real-time (SSE)
    fun startSse(userId:Long) {
        repository.startRealtimeNotifications(userId)
    }
    // 4. Hàm đánh dấu đã đọc
    fun markAsRead(notificationId: Long) {
        viewModelScope.launch {
            repository.markAsRead(notificationId)
        }
    }
    // 5. Quan trọng: Đóng kết nối khi ViewModel bị hủy để tránh rò rỉ bộ nhớ
    override fun onCleared() {
        super.onCleared()
        repository.stopRealtimeNotifications()
    }
}