package com.example.agritechda3k.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.agritechda3k.R
import com.example.agritechda3k.databinding.FragmentMyPlantDetailBinding
import com.example.agritechda3k.databinding.FragmentMyplantBinding
import com.example.agritechda3k.viewmodel.PlantUserViewModel


class MyPlantDetailFragment : Fragment() {
    private  var _binding: FragmentMyPlantDetailBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    private  val updateDataRunnable = object : Runnable {
        override fun run() {
            fetchSensorData() // Hàm gọi API lấy dữ liệu mới
            handler.postDelayed(this, 5 * 60 * 1000) // 5 phút (ms)
        }
    }
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

        setupChart() // 1. Khởi tạo biểu đồ
        handler.post(updateDataRunnable) // 2. Bắt đầu vòng lặp 5 phút

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
        // 2. PHẢI ĐƯA LỆNH CHUYỂN TRANG VÀO ĐÂY (Nút chuông/Nút lịch sử)
        binding.btnGoToHistory.setOnClickListener {
            val plantUserId = selectId
            val bundle = Bundle().apply {
                putLong("plantUserId", plantUserId)
            }
            findNavController().navigate(
                R.id.action_detail_to_history,
                bundle
            )
        }
    }
    private fun setupChart() {
        binding.steppedLineChart.apply {
            description.isEnabled = false
            setNoDataText("đang tải dữ liệu")
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)

            xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            axisRight.isEnabled = false // Tắt trục bên phải cho đỡ rối
        }
    }
    private fun fetchSensorData() {
        val selectId = viewModel.selectedMyPlantId
        if (selectId != -1L) {
            viewModel.fetchSensorData(selectId, 20)
        }

        viewModel.sensorPoints.observe(viewLifecycleOwner) { points ->
            if (!points.isNullOrEmpty()) {
                val entries = points.mapIndexed { index, point ->
                    com.github.mikephil.charting.data.Entry(index.toFloat(), point.value)
                }

                val dataSet =
                    com.github.mikephil.charting.data.LineDataSet(entries, "Độ ẩm (%)").apply {
                        mode = com.github.mikephil.charting.data.LineDataSet.Mode.STEPPED
                        color = android.graphics.Color.parseColor("#2E7D32")
                        setDrawFilled(true)
                        fillColor = android.graphics.Color.parseColor("#C8E6C9")
                        setDrawCircles(true)
                        lineWidth = 2f
                    }

                // Cấu hình nhãn trục X (HH:mm)
                binding.steppedLineChart.xAxis.valueFormatter =
                    object : com.github.mikephil.charting.formatter.ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val index = value.toInt()
                            return if (index >= 0 && index < points.size) points[index].timeLabel else ""
                        }
                    }

                binding.steppedLineChart.data = com.github.mikephil.charting.data.LineData(dataSet)
                binding.steppedLineChart.invalidate()
            }
        }
    }

}

    


