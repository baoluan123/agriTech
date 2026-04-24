package com.example.agritechda3k.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.agritechda3k.databinding.LayoutAddPlantBottomSheetBinding
import com.example.agritechda3k.model.Plant
import com.example.agritechda3k.viewmodel.PlantViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class AddPlantBottomSheet(
    private val plant: Plant,
    private val onAddSuccess: () -> Unit
): BottomSheetDialogFragment() {

    private lateinit var binding: LayoutAddPlantBottomSheetBinding
    // Dùng activityViewModels để share chung với HomeFragment/Activity
    private val viewModel: PlantViewModel by activityViewModels()
    private var selectedDateTime = Calendar.getInstance() // Mặc định là bây giờ
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutAddPlantBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvPlantName.text = "Cây: ${plant.namePlant}"
        // Giả lập danh sách Device (Sau này ông gọi API lấy list Device rảnh về đây)
        val devices = listOf("ESP32_GARDEN_01", "ESP32_GARDEN_02")
        val adapters = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, devices)
        binding.spinnerDevices.adapter = adapters

        // 1. Xử lý chọn Ngày và Giờ
        binding.btnPickDateTime.setOnClickListener {
            showDatePicker()
        }
        binding.btnConfirm.setOnClickListener {
            val selectedDevice = binding.spinnerDevices.selectedItem.toString()
            // Format thời gian theo chuẩn ISO_LOCAL_DATE_TIME (yyyy-MM-dd'T'HH:mm:ss)
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val formattedDate = sdf.format(selectedDateTime.time)
            // Chỗ này sau này ông gọi API Retrofit nì
            // Nếu API trả về Success thì mới chạy 3 dòng dưới:
            viewModel.registerPlantToDevice(plant.id,selectedDevice,formattedDate)
            Toast.makeText(context, "Đã thêm ${plant.namePlant} vào vườn!", Toast.LENGTH_SHORT).show()
            dismiss()
            onAddSuccess() // Gọi callback để chuyển Fragment
        }


    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Chọn ngày tưới")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            selectedDateTime.timeInMillis = selection
            showTimePicker() // Sau khi chọn ngày thì hiện chọn giờ luôn
        }
        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }

    private fun showTimePicker() {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Chọn giờ tưới")
            .build()

        timePicker.addOnPositiveButtonClickListener {
            selectedDateTime.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            selectedDateTime.set(Calendar.MINUTE, timePicker.minute)

            // Hiển thị lại lên Button để người dùng thấy
            val displayFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            binding.btnPickDateTime.text = displayFormat.format(selectedDateTime.time)
        }
        timePicker.show(parentFragmentManager, "TIME_PICKER")
    }
}

