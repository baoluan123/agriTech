package com.example.agritechda3k.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.R
import com.example.agritechda3k.databinding.ItemNotificationBinding
import com.example.agritechda3k.model.Notification
import com.example.agritechda3k.R

class NotificationAdapter(
    private var notifications: List<Notification>,
    private val onItemClick: (Notification) -> Unit
): RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    inner class NotificationViewHolder(val binding: ItemNotificationBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(notif: Notification) {
            binding.apply{
                tvNotifTitle.text = notif.title
                tvNotifMessage.text = notif.message
                tvNotifTime.text = notif.createdAt
                // Đổi màu icon cho dễ phân biệt: Cảnh báo (Đỏ) - Hệ thống (Xanh)
                if (notif.title.contains("Cảnh báo", ignoreCase = true)) {
                    viewStatusIndicator.setBackgroundResource(R.drawable.shape_circle_red)
                } else {
                    viewStatusIndicator.setBackgroundResource(R.drawable.shape_circle_green)
                }
                // Nếu đã đọc thì cho mờ đi một chút cho chuyên nghiệp
                root.alpha = if (notif.isRead) 0.5f else 1.0f
                // Click vào xem (nếu cần)
                root.setOnClickListener { onItemClick(notif) }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NotificationViewHolder(binding)
    }
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }
    override fun getItemCount(): Int = notifications.size
    // Hàm này ông gọi từ Fragment để cập nhật list mới
    fun setData(newList: List<Notification>) {
        this.notifications = newList
        notifyDataSetChanged()
    }
}