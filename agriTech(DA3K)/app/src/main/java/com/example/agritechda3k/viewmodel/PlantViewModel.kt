package com.example.agritechda3k.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.agritechda3k.database.repository.PlantRepository
import com.example.agritechda3k.model.Plant
import kotlinx.coroutines.launch

class PlantViewModel(private val repository: PlantRepository) :  ViewModel() {
    var currentPlantId: Long = -1L // Biến trung gian
    // 1. Biến "nội bộ" để chỉnh sửa dữ liệu (Mutable)
//    private val _plantList = MutableLiveData<List<Plant>>()
    // 2. Biến "công khai" cho Fragment quan sát (Chỉ đọc - LiveData)
//    val plantList: LiveData<List<Plant>> get() = _plantList
//***repository.allPlant.asLiveData()
    val plantList: LiveData<List<Plant>> = repository.allPlant.asLiveData()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
//***viewModelScope.launch
//    fun addPlant(id: Int, name: String) = viewModelScope.launch {
//    repository.addPlant(Plant(id, name))
//}
//Fragment không cần phải gọi viewModel.loadData()
//    init {
//        // Tự động gọi đồng bộ dữ liệu khi ViewModel được khởi tạo
//        loadData()
//    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                repository.loadData()
            } catch (e: Exception) {
                Log.e("PLANT_VM", "Lỗi load data: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private val _selectedPlant = MutableLiveData<Plant?>()
    val selectedPlant: LiveData<Plant?> = _selectedPlant

    fun getDetailPlant(id:Long) {
        viewModelScope.launch {
            val detailPlant = repository.detailData(id)
            detailPlant.onSuccess {
                plant-> _selectedPlant.value = plant
            }.onFailure {
                // Log lỗi hoặc hiện Toast báo không tìm thấy cây
                Log.e("VM", "Lỗi lấy chi tiết: ${it.message}")
            }
        }
    }
}