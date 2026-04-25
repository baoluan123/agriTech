package com.example.agritechda3k.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agritechda3k.R
import com.example.agritechda3k.adapter.PlantUserAdapter
import com.example.agritechda3k.api.RetrofitClient
import com.example.agritechda3k.api.service.PlantApi
import com.example.agritechda3k.api.service.PlantUserApi
import com.example.agritechda3k.database.DatabaseSetup
import com.example.agritechda3k.database.repository.PlantUserRepository
import com.example.agritechda3k.databinding.FragmentHomeBinding
import com.example.agritechda3k.databinding.FragmentMyplantBinding
import com.example.agritechda3k.viewmodel.PlantUserViewModel
import com.example.agritechda3k.viewmodelfactory.PlantUserViewModelFactory

class MyplantFragment : Fragment() {



    private var _binding: FragmentMyplantBinding? = null
    private val binding get() = _binding!!
    private lateinit var plantAdapter: PlantUserAdapter
    // 2. Cần khởi tạo repository để truyền vào Factory
    private val viewModel: PlantUserViewModel by activityViewModels {
        val database = DatabaseSetup.getDatabase(requireContext())
        val api = RetrofitClient.createService(PlantUserApi::class.java)
        val repository = PlantUserRepository(database.planUserDao(),api)
        PlantUserViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 3. Đổi lại đúng layout của MyPlant
        _binding = FragmentMyplantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        // Quan sát dữ liệu
        viewModel.myPlantList.observe(viewLifecycleOwner) { plants ->
            plantAdapter.setData(plants)
        }

    }



    //khoi tao giao dien adapter
    //chuyen qua details
    private fun setupRecyclerView() {
        plantAdapter = PlantUserAdapter(emptyList()) {
            plant->
            viewModel.selectedMyPlantId = plant.id
            findNavController().navigate(R.id.action_myplant_to_detail)
        }
        binding.rvMyPlants.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = plantAdapter
            // Giúp cuộn mượt hơn
            setHasFixedSize(true)
        }

        // Load data từ API (fix ID user = 1) *****
        viewModel.fetchMyPlants()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}