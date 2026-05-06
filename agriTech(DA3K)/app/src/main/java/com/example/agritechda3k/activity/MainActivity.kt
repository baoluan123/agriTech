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
    private val database by lazy { DatabaseSetup.getDatabase(this).authDao() }

    private var currentUserId: Long = -1L

    private val notificationViewModel: NotificationViewModel by viewModels {
        val api = RetrofitClient.createService(NotificationApi::class.java)
        val dao = DatabaseSetup.getDatabase(this).notificationDao()
        val sseClient = SSEClient(this, dao)
        val repository = NotificationRepository(api, dao, sseClient)
        NotificationViewModelFactory(repository)
    }

    private var isDialogShowing = false
    private val handler = Handler(Looper.getMainLooper())

    // Vòng lặp check thông báo mỗi 10 giây
    private val checkNotificationRunnable = object : Runnable {
        override fun run() {
            if (!isDialogShowing && currentUserId != -1L) {
                notificationViewModel.getTotalUnreadCount(currentUserId)
            }
            handler.postDelayed(this, 10000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // 1. Setup giao diện (Insets, Navigation)
        setupBasicUI()

        // 2. LOGIC QUAN TRỌNG: Lấy ID từ Room trước khi làm mọi thứ khác
        lifecycleScope.launch {
            val idFromDb = database.getLoggedInId()
            if (idFromDb != null) {
                currentUserId = idFromDb
                // Chỉ khi có ID mới khởi tạo các dịch vụ thông báo
                initNotificationSystem()
            } else {
                // Nếu chưa đăng nhập, đá về màn hình đăng nhập
                startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                finish()
            }
        }

        // 3. Xử lý Logout
        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                database.clearAuth()
                val intent = Intent(this@MainActivity, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupBasicUI() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottom.setupWithNavController(navHostFragment.navController)
    }

    // Hàm này chỉ chạy khi đã có ID người dùng hợp lệ
    private fun initNotificationSystem() {
        // A. Khởi chạy SSE (Real-time)
        notificationViewModel.startSse(currentUserId)

        // B. Lắng nghe thông báo để hiện Dialog cảnh báo tưới cây
        notificationViewModel.totalUnReadCount.observe(this) { count ->
            if (count > 0 && !isDialogShowing) {
                showWateringAlert()
            }
        }

        // C. Bắt đầu vòng lặp check định kỳ
        handler.post(checkNotificationRunnable)

        // D. Xử lý nếu người dùng nhấn vào thông báo từ thanh trạng thái
        handleNotificationIntent(intent)
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
        isDialogShowing = true // Chặn không cho hiện thêm Dialog khi cái cũ chưa đóng
        MaterialAlertDialogBuilder(this)
            .setTitle("Cảnh báo nông nghiệp")
            .setIcon(R.drawable.ic_notifications)
            .setMessage("Cây trồng của bạn đang cần được chăm sóc ngay!")
            .setCancelable(false)
            .setPositiveButton("Xem lịch sử") { dialog, _ ->
                isDialogShowing = false
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                navHostFragment.navController.navigate(R.id.historyFragment)
                dialog.dismiss()
            }
            .setNegativeButton("Đóng") { dialog, _ ->
                notificationViewModel.markAllReadForUser(currentUserId)
                isDialogShowing = false
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(checkNotificationRunnable)
    }
}