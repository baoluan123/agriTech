package com.example.agritechda3k.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agritechda3k.R
import com.example.agritechda3k.adapter.PlantAdapter
import com.example.agritechda3k.api.RetrofitClient
import com.example.agritechda3k.api.service.PlantApi
import com.example.agritechda3k.database.DatabaseSetup
import com.example.agritechda3k.database.repository.PlantRepository
import com.example.agritechda3k.databinding.FragmentHomeBinding
import com.example.agritechda3k.viewmodel.PlantViewModel
import com.example.agritechda3k.viewmodelfactory.PlantViewModelFactory
import kotlin.getValue

class HomeFragment : Fragment() {
    // 1. Khai báo ViewBinding cho Fragment
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var plantAdapter: PlantAdapter
    //khởi tạo database
    private val database by lazy { DatabaseSetup.getDatabase(requireContext()) }
    // khoi tao api
    private val plantApi: PlantApi by lazy { RetrofitClient.createService(PlantApi::class.java) }
    // 1. Khởi tạo Repository trước
    private val repository by lazy { PlantRepository(database.plantDao(), plantApi) }
    // Luân cần thêm PlantUserViewModel để thực hiện lưu vào SQLite/MySQL
    // (Vì PlantViewModel thường chỉ để lấy danh sách cây chung)
//    private val plantUserViewModel: com.example.DA3.viewmodel.PlantUserViewModel by activityViewModels()
    // 1. Khai báo ViewModel (Sử dụng viewModels delegate)
    private val viewModel: PlantViewModel by activityViewModels{
        PlantViewModelFactory(repository)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        // 4. Giả lập dữ liệu (Sau này chỗ này sẽ là lúc gọi ViewModel)
        // 2. QUAN SÁT (OBSERVE) dữ liệu từ ViewModel
        // --- QUAN TRỌNG: Đăng ký Context Menu cho RecyclerView ---
        registerForContextMenu(binding.rvHome)
        viewModel.plantList.observe(viewLifecycleOwner) {
            // Khi dữ liệu trong ViewModel thay đổi, hàm này tự chạy
                plants -> plantAdapter.setData(plants)
        }
//        binding.btnAdd.setOnClickListener {
//            val randomId = (1..1000).random()
////            viewModel.addPlant(randomId, "Cây ảo số $randomId")
//        }
        // Đổ dữ liệu vào Adapter
        // 3. RA LỆNH cho ViewModel lấy dữ liệu
//        viewModel.plantList

    }


    private fun setupRecyclerView() {
        // Khởi tạo Adapter với danh sách rỗng ban đầu
        plantAdapter = PlantAdapter(mutableListOf()) {
                plant->
            // Đừng tạo Bundle nữa, truyền thẳng vào hàm navigate theo cặp Key-Value
//            val idToSend = plant.id
            // Gán thẳng ID vào biến trong ViewModel
            viewModel.currentPlantId = plant.id
            Log.d("NAV_CHECK", "Đang gửi ID: ${plant.id}")
//            val bundle = Bundle().apply {
//                putLong("argPlantId",plant.id)
//            }


            Toast.makeText(context, "Bạn chọn: ${plant.namePlant}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_nav_home_to_detailFragment)
        }

        // RA LỆNH: "Này ViewModel, đi lấy dữ liệu từ Spring Boot về đi!"
        viewModel.loadData()

        binding.rvHome.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = plantAdapter
        }
    }
    override fun onContextItemSelected(item: android.view.MenuItem): Boolean {
        val plant = plantAdapter.getSelectedPlant()
        return when (item.itemId) {
            R.id.action_add_to_garden -> { // Giả sử ID trong file menu của ông là menu_save
                plant?.let {
                    Toast.makeText(context, "Đã lưu: ${it.namePlant}", Toast.LENGTH_SHORT).show()
                    // Xử lý logic lưu vào danh sách cá nhân ở đây
                }
                true
            }
            R.id.action_view_detail -> {
                // Xử lý xóa nếu cần
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}