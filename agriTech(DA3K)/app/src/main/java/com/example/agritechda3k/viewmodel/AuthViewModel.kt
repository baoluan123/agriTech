package com.example.agritechda3k.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agritechda3k.api.dto.LoginRequestDTO
import com.example.agritechda3k.database.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(val repository: AuthRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    // String này sẽ chứa thông báo thành công HOẶC lỗi
    private val _authResult = MutableLiveData<String>()
    val authResult: LiveData<String> get() = _authResult

    // Thêm một flag để Fragment biết là thành công hay thất bại mà chuyển màn
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    fun login(dto: LoginRequestDTO) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.login(dto.username,dto.password)
            _isLoading.value = false
            result.onSuccess{
                msg->_authResult.value = msg
                _isSuccess.value = true
            }.onFailure { exception->
                _authResult.value = exception.message
                _isSuccess.value = false
            }
        }
    }
}