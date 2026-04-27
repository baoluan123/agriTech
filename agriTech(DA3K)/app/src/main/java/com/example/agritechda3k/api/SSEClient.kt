package com.example.agritechda3k.api

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.agritechda3k.R
import com.example.agritechda3k.activity.MainActivity
import com.example.agritechda3k.api.dto.NotificationDTO
import com.example.agritechda3k.database.dao.NotificationDao
import com.example.agritechda3k.mapper.Notification.toEntityss
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.util.concurrent.TimeUnit


class SSEClient(
    private val context: Context,
    private val notificationDao: NotificationDao // Để lưu tin vào Room ngay khi nhận
) {
    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS) // Giữ kết nối vĩnh viễn
        .build()
    private var eventSource: EventSource? = null
    private val gson = Gson()
    fun startListening(userId: Long) {
        val request = Request.Builder()
            .url("http://192.168.1.3:8080/api/notifications/subscribe/$userId")
            .header("Accept", "text/event-stream")
            .build()
        val listener = object : EventSourceListener() {
            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                // 1. Nhận JSON từ Spring Boot
                val dto = gson.fromJson(data, NotificationDTO::class.java)
                // 2. Mapping sang Entity (Dùng cái Extension function ông vừa viết)
                val entity = dto.toEntityss()
                // 3. Lưu vào Room (Chạy trong Coroutine vì Room không cho chạy Main Thread)
                CoroutineScope(Dispatchers.IO).launch {
                    notificationDao.insertNotification(entity)
                }
                // 4. Bắn Notification lên thanh trạng thái điện thoại (Ting ting)
                showSystemNotification(dto)
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                Log.e("SSE", "Mất kết nối! Thử lại sau 5s...")
                // Logic reconnect có thể đặt ở đây
            }
        }
        eventSource = EventSources.createFactory(client).newEventSource(request, listener)
    }
    fun stopListening() {
        eventSource?.cancel()
    }
    private fun showSystemNotification(dto: NotificationDTO) {
        // Code hiển thị Notification trên Android (NotificationChannel...)
        val channelId = "agri_tech_notifications"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // 1. Tạo Channel (Bắt buộc cho Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Cảnh báo cây trồng",
                NotificationManager.IMPORTANCE_HIGH // Hiện banner và phát tiếng
            ).apply {
                description = "Thông báo về độ ẩm và lịch tưới"
            }
            notificationManager.createNotificationChannel(channel)
        }
        // 2. Tạo Intent để khi nhấn vào thông báo sẽ mở App
        // Ông có thể truyền plantUserId vào đây để Fragment nhận diện và mở đúng chậu cây
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("PLANT_USER_ID", dto.plantUserId)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // 3. Xây dựng nội dung thông báo
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notifications) // Thay bằng icon lá cây của ông
            .setContentTitle(dto.title)
            .setContentText(dto.message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // Nhấn vào là tự mất thông báo
            .setContentIntent(pendingIntent)
            .build()

        // 4. Hiển thị (Dùng ID của Notification để tránh đè nhau)
        notificationManager.notify(dto.id.toInt(), notification)
    }
}