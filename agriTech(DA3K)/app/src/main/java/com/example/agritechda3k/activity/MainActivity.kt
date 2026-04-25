package com.example.agritechda3k.activity

import android.content.Intent
import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.agritechda3k.R
import com.example.agritechda3k.api.RetrofitClient
import com.example.agritechda3k.api.service.NotificationApi
import com.example.agritechda3k.database.DatabaseSetup
import com.example.agritechda3k.database.repository.NotificationRepository
import com.example.agritechda3k.databinding.ActivityMainBinding
import com.example.agritechda3k.viewmodel.NotificationViewModel
import com.example.agritechda3k.viewmodelfactory.NotificationViewModelFactory
import kotlinx.coroutines.launch
// Sửa lại import ở đầu file
import android.os.Handler
import android.os.Looper
import com.google.android.material.dialog.MaterialAlertDialogBuilder // Để hiện thông báo đẹp


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val notificationViewModel: NotificationViewModel by viewModels() {
        val api = RetrofitClient.createService(NotificationApi::class.java)
        val dao = DatabaseSetup.getDatabase(this).notificationDao()
        val repository = NotificationRepository(api,dao)
        NotificationViewModelFactory(repository)
    }
    //moi***
    private var isDialogShowing = false // Cờ kiểm soát Dialog
    private val handler = Handler(Looper.getMainLooper())
    private val checkNotificationRunnable = object : Runnable{
        override fun run() {
            // Lấy userId từ Auth session (Ví dụ id = 1) // doi sau test cung
            val currentUserId = 3L

            // Check TỔNG cả vườn
            // Nếu Dialog đang hiện thì tạm dừng check để tránh đè nhau
            if (!isDialogShowing) {
                notificationViewModel.checkTotalUnread(currentUserId)
            }
            handler.postDelayed(this, 10000) // Để 5 giây cho ổn định ông nhé
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment =supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottom = binding.bottom
        bottom.setupWithNavController(navController)

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                val db = DatabaseSetup.getDatabase(this@MainActivity)
                db.authDao().clearAuth()
                val intent = Intent(this@MainActivity, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        notificationViewModel.totalUnReadCount.observe(this) { count ->
            // Chỉ hiện Dialog khi có thông báo và chưa có cái Dialog nào đang mở
            if(count > 0 && !isDialogShowing) {
                // Nếu có thông báo mới (chưa đọc), hiện màn hình nhỏ ngay

                showWateringAlert()
            }
        }
        // BẮT ĐẦU VÒNG LẶP CHECK KHI MỞ APP
        handler.post(checkNotificationRunnable)

    }
    private fun showWateringAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle("canh bao")
            .setMessage("cay cua ban dang can duoc cham soc")
            .setCancelable(false) // Bắt người dùng phải tương tác
            .setPositiveButton("Xem lịch sử") {dialog, _ ->
                isDialogShowing = false
                // Lệnh chuyển hướng fragment của ông ở đây
                dialog.dismiss()
            }
            .setNegativeButton("Đóng"){dialog, _ ->
                isDialogShowing = false
                // Mẹo: Nên gọi markAsRead cho thông báo mới nhất ở đây
                // để lần check sau count sẽ về 0
                dialog.dismiss()
            }
            .show()
    }
    override fun onDestroy() {
        super.onDestroy()
        // CỰC KỲ QUAN TRỌNG: Dừng vòng lặp khi tắt Activity để tránh tốn pin và lỗi bộ nhớ
        handler.removeCallbacks(checkNotificationRunnable)
    }
}