package com.example.grocerycrudsampleapp.ui.completedgrocerylist

import androidx.lifecycle.*
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList
import com.example.grocerycrudsampleapp.repository.GroceryRepository
import com.example.grocerycrudsampleapp.utils.Event
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CompletedGroceryListViewModel(private val repository: GroceryRepository) : ViewModel() {
    private var isUpdateOrDelete = false
    private lateinit var groceryToUpdateOrDelete: GroceryList
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun initUpdateAndDelete(groceryList: GroceryList) {
        isUpdateOrDelete = true
        groceryToUpdateOrDelete = groceryList
    }

    fun getCompletedGroceries() = liveData {
        repository.completedGroceries.collect {
            emit(it)
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            deleteGrocery(groceryToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    fun deleteGrocery(groceryList: GroceryList) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(groceryList)
        if (noOfRowsDeleted > 0) {
            isUpdateOrDelete = false
            statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    private fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAllCompletedGroceryList()
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Groceries Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }
}