package com.example.agritechda3k.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.agritechda3k.api.HT.SensorPoint
import com.example.agritechda3k.database.repository.PlantUserRepository
import com.example.agritechda3k.mapper.Notification.toSensorPoint
import com.example.agritechda3k.model.PlantUser
import kotlinx.coroutines.launch

class PlantUserViewModel(private val repository: PlantUserRepository) : ViewModel() {
    // Quan sát danh sách cây trong vườn từ Room
    val myPlantList: LiveData<List<PlantUser>> = repository.allMyPlants.asLiveData()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    // Biến để lưu ID khi người dùng chọn xem chi tiết
    var selectedMyPlantId: Long = -1L
    fun fetchMyPlants() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.loadMyPlants()
            _isLoading.value = false
        }
    }
    // 3. Lấy chi tiết 1 cây từ Room
    fun getDetail(id: Long): LiveData<PlantUser?> {
        return repository.getMyPlantById(id).asLiveData()
    }

    // Thêm LiveData để chứa kết quả đã gọt
    private val _sensorPoints = MutableLiveData<List<SensorPoint>>()
    val sensorPoints: LiveData<List<SensorPoint>> get() = _sensorPoints
    fun fetchSensorData(plantUserId:Long,limit :Int){
        viewModelScope.launch {
            try {
                // Gọi xuống Repository để lấy DTO từ API
                val response = repository.getSensorData(plantUserId, limit)


                if (response.isSuccessful){
                    val dtoList = response.body()

                    Log.d("AGRI_DEBUG", "API Success: Nhận được ${dtoList?.size ?: 0} bản ghi")
                    dtoList?.forEach {
                        Log.d("AGRI_DEBUG", "Data: ${it.soilMoisture}% lúc ${it.recordedAt}")
                    }
                    // Dùng Mapper để biến DTO thành Point
                    val points = dtoList?.map { it.toSensorPoint() } ?: emptyList()
                    _sensorPoints.postValue(points)
                } else {
                    Log.e("AGRI_DEBUG", "API Error: Code ${response.code()} - ${response.errorBody()?.string()}")
                }
            }catch (e: Exception) {
                // Log lỗi nếu cần
                Log.e("AGRI_DEBUG", "Connection Failed: ${e.message}")
            }
        }

    }

}