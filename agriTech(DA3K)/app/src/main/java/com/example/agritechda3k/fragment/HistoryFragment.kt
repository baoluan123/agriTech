package com.example.agritechda3k.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agritechda3k.R
import com.example.agritechda3k.adapter.NotificationAdapter
import com.example.agritechda3k.api.RetrofitClient
import com.example.agritechda3k.api.SSEClient
import com.example.agritechda3k.api.service.NotificationApi
import com.example.agritechda3k.database.DatabaseSetup
import com.example.agritechda3k.database.repository.NotificationRepository
import com.example.agritechda3k.databinding.FragmentHistoryBinding
import com.example.agritechda3k.viewmodel.NotificationViewModel
import com.example.agritechda3k.viewmodelfactory.NotificationViewModelFactory
import kotlinx.coroutines.launch


class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationAdapter: NotificationAdapter
    // Khởi tạo ViewModel với Factory (Vì có Repository làm tham số)
    private val notificationViewModel: NotificationViewModel by viewModels {
        val api = RetrofitClient.createService(NotificationApi::class.java)
        val dao = DatabaseSetup.getDatabase(requireContext()).notificationDao()
        val sseClient = SSEClient(requireContext(), dao)
        val repository = NotificationRepository(api, dao, sseClient)
        NotificationViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 1. Lấy ID cây truyền sang
        val plantUserId = arguments?.getLong("plantUserId") ?: -1L
        // 2. Setup RecyclerView
        setupRecyclerView()

        // 3. Bắt đầu luồng xử lý dữ liệu
        if (plantUserId != -1L) {
            observeData(plantUserId)
            // Đồng bộ dữ liệu mới nhất từ Server về máy
            notificationViewModel.loadHistory(plantUserId)
        }

    }

    private fun setupRecyclerView() {
        notificationAdapter = NotificationAdapter(emptyList()) { notification ->
            // Khi nhấn vào một tin: Đánh dấu là đã đọc
            notificationViewModel.markAsRead(notification.id)
        }
        binding.rvHistory.apply {
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(requireContext())
            // Thêm đường kẻ giữa các item cho đẹp
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }
    private fun observeData(plantUserId: Long){
        // Quan sát StateFlow (hoặc LiveData) từ ViewModel
        // Vì ViewModel của ông dùng StateFlow nên ta dùng lifecycleScope
        viewLifecycleOwner.lifecycleScope.launch{
            notificationViewModel.notifications.collect{
                    list ->
                if (list.isNullOrEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rvHistory.visibility = View.GONE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvHistory.visibility = View.VISIBLE
                    notificationAdapter.setData(list)
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}