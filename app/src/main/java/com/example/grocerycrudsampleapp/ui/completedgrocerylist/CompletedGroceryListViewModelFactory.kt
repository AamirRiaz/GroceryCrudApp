package com.example.grocerycrudsampleapp.ui.completedgrocerylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerycrudsampleapp.repository.GroceryRepository
import java.lang.IllegalArgumentException

class CompletedGroceryListViewModelFactory(
    private val repository: GroceryRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CompletedGroceryListViewModel::class.java)){
            return CompletedGroceryListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}