package com.example.agritechda3k.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.agritechda3k.R
import com.example.agritechda3k.database.DatabaseSetup
import com.example.agritechda3k.databinding.ActivityAuthBinding
import kotlinx.coroutines.launch


class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val database by lazy { DatabaseSetup.getDatabase(this).authDao() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Khởi tạo ViewBinding
        binding = ActivityAuthBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            val auth =database.getAuth()
            if(auth != null) {
                val intent = Intent(this@AuthActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else{
                // Nếu chưa có user -> Không làm gì cả,
                // NavHostFragment sẽ tự nạp LoginFragment như bình thường.
            }
        }
    }
}