package com.example.agritechda3k.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.agritechda3k.R
import com.example.agritechda3k.databinding.FragmentMyPlantDetailBinding
import com.example.agritechda3k.databinding.FragmentMyplantBinding
import com.example.agritechda3k.viewmodel.PlantUserViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [MyPlantDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPlantDetailFragment : Fragment() {
    private  var _binding: FragmentMyPlantDetailBinding? = null
    private val binding get() = _binding!!
    // Dùng chung ViewModel với Activity để lấy được selectedMyPlantId
    private val viewModel: PlantUserViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPlantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectId = viewModel.selectedMyPlantId
        viewModel.getDetail(selectId).observe(viewLifecycleOwner) {
            plant->
            plant?.let {
                binding.apply {
                    tvCustomName.text = it.customName ?: it.plantName
                    tvDescriptionPlant.text = "Giống cây: ${it.plantName}"
                    tvLastWatered.text = it.lastWatered ?: "Chưa có dữ liệu"
                    tvFertilizerInfo.text = it.fertilizerInfo ?: "Chưa cập nhật"
                    // Các thông số fix cứng hoặc sau này lấy thêm từ DB
                    tvWaterFrequency.text = "2 lần/tuần"
                    tvIdealHumidity.text = "60% - 70%"

                    // Load ảnh "xịn"
                    Glide.with(requireContext())
                        .load(it.imageUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(ivPlantDetail)
                }
                }
            }

        binding.btnWaterNow.setOnClickListener {
            // Sau này ông sẽ gọi viewModel.updateWatering(selectedId) ở đây
            android.widget.Toast.makeText(requireContext(), "Hệ thống đã ghi nhận bạn vừa tưới cây!", android.widget.Toast.LENGTH_SHORT).show()
        }
        }
    }


