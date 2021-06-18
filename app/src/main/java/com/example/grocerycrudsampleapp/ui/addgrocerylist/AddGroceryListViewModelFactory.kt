package com.example.grocerycrudsampleapp.ui.addgrocerylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerycrudsampleapp.repository.GroceryRepository
import java.lang.IllegalArgumentException

class AddGroceryListViewModelFactory(
    private val repository: GroceryRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddGroceryListViewModel::class.java)){
            return AddGroceryListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}