package com.example.grocerycrudsampleapp.ui.addgrocery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerycrudsampleapp.repository.GroceryRepository
import java.lang.IllegalArgumentException

class AddGroceryViewModelFactory(
    private val repository: GroceryRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddGroceryViewModel::class.java)){
            return AddGroceryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}