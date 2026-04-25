package com.example.agritechda3k.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agritechda3k.R
import com.example.agritechda3k.adapter.NotificationAdapter
import com.example.agritechda3k.api.RetrofitClient
import com.example.agritechda3k.api.service.NotificationApi
import com.example.agritechda3k.database.DatabaseSetup
import com.example.agritechda3k.database.repository.NotificationRepository
import com.example.agritechda3k.databinding.FragmentHistoryBinding
import com.example.agritechda3k.viewmodel.NotificationViewModel
import com.example.agritechda3k.viewmodelfactory.NotificationViewModelFactory


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    // Khởi tạo ViewModel với Factory (Vì có Repository làm tham số)
    private val notificationViewModel: NotificationViewModel by viewModels {
        val api = RetrofitClient.createService(NotificationApi::class.java)
        val dao = DatabaseSetup.getDatabase(requireContext()).notificationDao()
        val repository = NotificationRepository(api, dao)
        NotificationViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHistoryBinding.bind(view)
        // 1. Hứng ID cây từ MyPlantDetailFragment truyền sang
        val plantUserId = arguments?.getLong("plantUserId") ?: -1L
        // 2. Setup Adapter (Bản gọn nhẹ không nhức đầu)
        val adapter = NotificationAdapter(emptyList()) { notification ->
            // Logic khi click vào từng item (ví dụ: đánh dấu đọc cái này)
            notificationViewModel.maskRead(notification.id)
        }
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        if (plantUserId != -1L) {
            // 3. Quan sát dữ liệu từ Local DB (Room)
            // Khi Room thay đổi (do refreshHistory), UI sẽ tự động cập nhật
            notificationViewModel.getLocalHistory(plantUserId).observe(viewLifecycleOwner) { list ->
                if (list.isNullOrEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                    adapter.setData(list)
                }
            }

            // 4. Đồng bộ dữ liệu từ Server về máy
            notificationViewModel.fetchAllHistory(plantUserId)

            // 5. Tùy chọn: Đánh dấu đã đọc toàn bộ của cây này (nếu ông muốn)
            // notificationViewModel.markAllReadForPlant(plantUserId)
        }

    }
}