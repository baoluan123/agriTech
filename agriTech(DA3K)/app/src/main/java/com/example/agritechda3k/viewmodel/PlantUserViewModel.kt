package com.example.agritechda3k.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.agritechda3k.database.repository.PlantUserRepository
import com.example.agritechda3k.model.PlantUser
import kotlinx.coroutines.launch

class PlantUserViewModel(private val repository: PlantUserRepository) : ViewModel() {
    // Quan sát danh sách cây trong vườn từ Room
    val myPlantList: LiveData<List<PlantUser>> = repository.allMyPlants.asLiveData()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    // Biến để lưu ID khi người dùng chọn xem chi tiết
    var selectedMyPlantId: Long = -1L
    fun fetchMyPlants(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.loadMyPlants(userId)
            _isLoading.value = false
        }
    }
    // 3. Lấy chi tiết 1 cây từ Room
    fun getDetail(id: Long): LiveData<PlantUser?> {
        return repository.getMyPlantById(id).asLiveData()
    }
}