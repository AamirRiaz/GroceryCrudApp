package com.example.grocerycrudsampleapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerycrudsampleapp.repository.GroceryRepository
import java.lang.IllegalArgumentException

class HomeViewModelFactory(
        private val repository: GroceryRepository
        ):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
     if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
         return HomeViewModel(repository) as T
     }
        throw IllegalArgumentException("Unknown View Model class")
    }

}