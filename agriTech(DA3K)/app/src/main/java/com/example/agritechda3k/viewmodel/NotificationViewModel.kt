package com.example.agritechda3k.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agritechda3k.api.dto.NotificationDTO
import com.example.agritechda3k.database.repository.NotificationRepository
import com.example.agritechda3k.model.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {
    // LiveData chứa số lượng thông báo chưa đọc (để hiện Badge hoặc Pop-up)
    val totalUnReadCount = MutableLiveData<Long>() // Dùng cho Activity
    // LiveData chứa danh sách lịch sử (để hiện RecyclerView)
    val notificationList = MutableLiveData<List<NotificationDTO>>()
    // Hàm gọi check số lượng (dùng cho Polling Real-time) *** coi lai
//    fun checkUnreadCount(plantUserId:Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val count = repository.fetchUnreadCount(plantUserId)
//            unReadCount.postValue(count?:0)
//        }
//    }
        // Hàm lấy toàn bộ lịch sử
        fun fetchAllHistory(plantUserId: Long) {
            viewModelScope.launch(Dispatchers.IO) {
                // Hàm này chỉ lo "kéo" data mới từ Server về nạp vào Room
                repository.refreshHistory(plantUserId)
            }
        }
        fun maskRead(notificationId : Long) {
            viewModelScope.launch(Dispatchers.IO) {

                repository.markAsRead(notificationId)
                // Sau khi mark read, có thể giảm unReadCount xuống để tắt Pop-up ngay lập tức
                totalUnReadCount.postValue(0L)
            }
        }
        // Hàm cho MainActivity gọi them ham nay
        fun checkTotalUnread(userId: Long) {
            viewModelScope.launch(Dispatchers.IO) {
                val count = repository.fetchTotalUnread(userId)
                totalUnReadCount.postValue(count ?: 0)
            }
        }

    //them thang nay
    fun getLocalHistory(plantUserId: Long): LiveData<List<Notification>> {
        return repository.getNotificationsFromRoom(plantUserId)
    }
}