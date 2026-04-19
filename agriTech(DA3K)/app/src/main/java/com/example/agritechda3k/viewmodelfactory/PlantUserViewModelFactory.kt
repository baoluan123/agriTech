package com.example.agritechda3k.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.agritechda3k.database.repository.PlantUserRepository
import com.example.agritechda3k.viewmodel.PlantUserViewModel


class PlantUserViewModelFactory(private val repository: PlantUserRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlantUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlantUserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}