package com.example.agritechda3k.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.agritechda3k.R
import com.example.agritechda3k.databinding.FragmentDetailBinding
import com.example.agritechda3k.databinding.FragmentMyPlantDetailBinding
import com.example.agritechda3k.viewmodel.PlantViewModel


class DetailFragment : Fragment() {
    private var plantId : Long = -1
    private  var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlantViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        plantId = arguments?.getLong("key_cay_trong")?:-1
        // Lấy ID từ ViewModel thay vì arguments
        plantId = viewModel.currentPlantId
        Log.d("NAV_CHECK", "Đã nhận được ID: $plantId")
//        if (plantId != -1L) {
//            viewModel.getDetailPlant(plantId)
//        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(plantId != -1L) {
            viewModel.getDetailPlant(plantId)
        }
        viewModel.selectedPlant.observe(viewLifecycleOwner) {
            plant->
            plant?.let {
                binding.apply {
                    txtTenDetail.text = it.namePlant
                    txtIdealHumidity.text = "💧 Độ ẩm lý tưởng: ${it.idealHumidity}%"
                    txtWaterFrequency.text = "📅 Tần suất tưới: ${it.waterFrequency} ngày/lần"
                    txtFertilizer.text = "🧪 Phân bón: ${it.fertilizerInfo}"
                    txtDescriptionDetail.text = it.descriptionPlant
                    // Hiện ảnh to oạch bằng Glide
                    Glide.with(requireContext())
                        .load(it.thumbnailUrl)
                        .into(imgDetail)
                }
            }
        }
    }


}