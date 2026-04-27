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
import com.example.agritechda3k.api.SSEClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder // Để hiện thông báo đẹp


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    // Khai báo ID ở một nơi duy nhất để dễ sửa (Sau này lấy từ Login)
    private val currentUserId: Long = 1L
    private val notificationViewModel: NotificationViewModel by viewModels() {
        val api = RetrofitClient.createService(NotificationApi::class.java)
        val dao = DatabaseSetup.getDatabase(this).notificationDao()
        // 1. Khởi tạo SSEClient tại đây
        val sseClient = SSEClient(
            this, dao)
            val repository = NotificationRepository(api,dao,sseClient)
        NotificationViewModelFactory(repository)
    }
    //moi***
    private var isDialogShowing = false // Cờ kiểm soát Dialog
    private val handler = Handler(Looper.getMainLooper())
    private val checkNotificationRunnable = object : Runnable{
        override fun run() {

            // Check TỔNG cả vườn
            // Nếu Dialog đang hiện thì tạm dừng check để tránh đè nhau
            if (!isDialogShowing) {
                notificationViewModel.getTotalUnreadCount(currentUserId)
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
        // --- SETUP NAVIGATION ---
        val navHostFragment =supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottom = binding.bottom
        bottom.setupWithNavController(navController)
        // --- LOGIC THÔNG BÁO ---
        handleNotificationIntent(intent) // Xử lý khi nhấn từ thanh trạng thái
        // --- LOGIC THÔNG BÁO ---
        // A. Chạy máy nghe Real-time (SSE)
        notificationViewModel.startSse(currentUserId)
        // B. Lắng nghe con số từ ViewModel để hiện Dialog
        notificationViewModel.totalUnReadCount.observe(this) { count ->
            if (count > 0 && !isDialogShowing) {
                showWateringAlert()
            }
        }


        // BẮT ĐẦU VÒNG LẶP CHECK KHI MỞ APP
        handler.post(checkNotificationRunnable)
        // --- LOGIC LOGOUT ---
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

    }

    private fun handleNotificationIntent(intent: Intent?) {
        val plantId = intent?.getLongExtra("PLANT_USER_ID", -1L) ?: -1L
        if (plantId != -1L) {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val bundle = Bundle().apply { putLong("plantUserId", plantId) }
            navHostFragment.navController.navigate(R.id.historyFragment, bundle)
        }
    }

    private fun showWateringAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle("canh bao")
            .setIcon(R.drawable.ic_notifications)
            .setMessage("cay cua ban dang can duoc cham soc")
            .setCancelable(false) // Bắt người dùng phải tương tác
            .setPositiveButton("Xem lịch sử") {dialog, _ ->
                isDialogShowing = false
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                navHostFragment.navController.navigate(R.id.historyFragment)
                dialog.dismiss()
            }
            .setNegativeButton("Đóng"){dialog, _ ->
                // Đóng xong thì mark read để reset count, tránh Dialog hiện lại liên tục
                notificationViewModel.markAllReadForUser(currentUserId)
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