package com.example.agritechda3k.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope

import com.example.agritechda3k.activity.AuthActivity
import com.example.agritechda3k.database.DatabaseSetup
import com.example.agritechda3k.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch



class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db= DatabaseSetup.getDatabase(requireContext())
        lifecycleScope.launch {
            val user = db.authDao().getAuth()
            user?.let {
                binding.profileFullName.text = it.fullName
                binding.profileUsername.text ="Tên Đăng Nhập: ${it.username}"
                binding.profileAddress.text = "Địa Chỉ: ${it.address}"
                binding.profilePhone.text = "Số Điện Thoại ${it.phone}"
                binding.profileRole.text = if(it.role == 1) "Vai Trò: Quản Trị Viên" else "Vai Trò: Người Dùng"
            }
        }
        binding.btnLogout.setOnClickListener {
            logout(db)
        }
    }

    private fun logout(db: DatabaseSetup) {
        lifecycleScope.launch {
            db.authDao().clearAuth()
            val  intent = Intent(requireContext(), AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }
    }


}