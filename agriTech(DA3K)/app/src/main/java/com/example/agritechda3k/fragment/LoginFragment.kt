package com.example.agritechda3k.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.agritechda3k.R
import com.example.agritechda3k.activity.MainActivity
import com.example.agritechda3k.api.RetrofitClient
import com.example.agritechda3k.api.dto.LoginRequestDTO
import com.example.agritechda3k.api.service.AuthApi
import com.example.agritechda3k.database.DatabaseSetup
import com.example.agritechda3k.database.repository.AuthRepository
import com.example.agritechda3k.databinding.FragmentLoginBinding
import com.example.agritechda3k.viewmodel.AuthViewModel
import com.example.agritechda3k.viewmodelfactory.AuthViewModelFactory


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by activityViewModels {
        val api = RetrofitClient.createService(AuthApi::class.java)
        val dao = DatabaseSetup.getDatabase(requireContext()).authDao()
        val repository = AuthRepository(api,dao)
        AuthViewModelFactory(repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            if(username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(),"vui long nhap du truong", Toast.LENGTH_LONG).show()
            } else {
                val login = LoginRequestDTO(username,password)
                viewModel.login(login)
            }
        }
        observeViewModel()

    }

    private fun observeViewModel() {
        // Quan sát kết quả (Dùng 2 LiveData như ông muốn)
        viewModel.authResult.observe(viewLifecycleOwner){
            mess->
            Toast.makeText(context, mess, Toast.LENGTH_SHORT).show()
        }
        viewModel.isSuccess.observe(viewLifecycleOwner){
            mess->
            if(mess) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish() // Đóng AuthActivity
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            load->
            if(load) {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnLogin.isEnabled = false // Khóa nút lại tránh bấm liên tục
            } else {
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = false
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}