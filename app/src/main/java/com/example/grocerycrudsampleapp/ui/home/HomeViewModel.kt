package com.example.grocerycrudsampleapp.ui.home

import android.util.Patterns
import androidx.lifecycle.*
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList
import com.example.grocerycrudsampleapp.repository.GroceryRepository
import com.example.grocerycrudsampleapp.utils.Event
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: GroceryRepository) : ViewModel() {
    private var isUpdateOrDelete = false
    private lateinit var groceryToUpdateOrDelete: GroceryList
    val clearAllOrDeleteButtonText = MutableLiveData<String>()
    val inputName = MutableLiveData<String>()
    val inputAmount = MutableLiveData<String>()
    val inputStatus = MutableLiveData<String>()
    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun initUpdate(groceryList: GroceryList) {
        inputName.value = groceryList.name
        //inputAmount.value = groceryList.amount
        inputStatus.value = groceryList.status
        isUpdateOrDelete = true
        groceryToUpdateOrDelete = groceryList
        completeTheGrocery()
    }

    fun completeTheGrocery() {
            if (isUpdateOrDelete) {
                groceryToUpdateOrDelete.name = inputName.value!!
                //groceryToUpdateOrDelete.amount = inputAmount.value!!
                groceryToUpdateOrDelete.status = "completed"
                updateGrocery(groceryToUpdateOrDelete)
            }
    }

    private fun updateGrocery(groceryList: GroceryList) = viewModelScope.launch {
        val noOfRows = repository.update(groceryList)
        if (noOfRows > 0) {
            inputName.value = ""
            //inputAmount.value = ""
            inputStatus.value = ""
            isUpdateOrDelete = false
            statusMessage.value = Event("$noOfRows Row Updated Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun getSavedGroceries() = liveData {
        repository.groceriesList.collect {
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
        val noOfRowsDeleted = repository.deleteAllGroceryList()
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Groceries Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }
}