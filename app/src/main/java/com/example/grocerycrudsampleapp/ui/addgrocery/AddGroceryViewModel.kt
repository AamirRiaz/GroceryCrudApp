package com.example.grocerycrudsampleapp.ui.addgrocery

import androidx.lifecycle.*
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList
import com.example.grocerycrudsampleapp.repository.GroceryRepository
import com.example.grocerycrudsampleapp.utils.Event
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddGroceryViewModel(private val repository: GroceryRepository) : ViewModel() {
    private var isUpdateOrDelete = false
    private lateinit var groceryToUpdateOrDelete: Grocery
    private lateinit var groceryListToUpdate: GroceryList
    val inputName = MutableLiveData<String>()
    val inputAmount = MutableLiveData<String>()
    val inputStatus = MutableLiveData<String>()
    val inputGroceryId = MutableLiveData<Int>()
    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun initUpdateAndDelete(grocery: Grocery) {
        inputName.value = grocery.name
        inputAmount.value = grocery.amount
        inputGroceryId.value = grocery.groceryListId
        isUpdateOrDelete = true
        groceryToUpdateOrDelete = grocery
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    fun initGroceryListId(groceryListId: Int) {
        inputGroceryId.value = groceryListId
        /*inputName.value = grocery.name
        inputAmount.value = grocery.amount
        inputGroceryId.value = grocery.groceryListId
        isUpdateOrDelete = true
        groceryToUpdateOrDelete = grocery
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"*/
    }


    fun initUpdate(grocery: Grocery) {
        inputName.value = grocery.name
        inputAmount.value = grocery.amount
        inputStatus.value = grocery.status
        isUpdateOrDelete = true
        groceryToUpdateOrDelete = grocery
        completeTheGrocery()
    }

    fun completeTheGrocery() {
        if (isUpdateOrDelete) {
            groceryToUpdateOrDelete.name = inputName.value!!
            groceryToUpdateOrDelete.amount = inputAmount.value!!
            groceryToUpdateOrDelete.status = "completed"
            updateGroceryStatus(groceryToUpdateOrDelete)
        }
    }

    fun completeTheGroceryList(groceryList: GroceryList) {
        groceryListToUpdate = groceryList
        groceryListToUpdate.name = groceryList.name
        groceryListToUpdate.status = "completed"
        updateCompletedGrocery(groceryListToUpdate)
    }

    private fun updateCompletedGrocery(groceryList: GroceryList) = viewModelScope.launch {
        val noOfRows = repository.update(groceryList)
    }


    private fun updateGroceryStatus(grocery: Grocery) = viewModelScope.launch {
        val noOfRows = repository.update(grocery)
        if (noOfRows > 0) {
            inputName.value = ""
            inputAmount.value = ""
            inputStatus.value = ""
            isUpdateOrDelete = false
            statusMessage.value = Event("$noOfRows Row Updated Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun saveOrUpdate() {
        if (inputName.value == null) {
            statusMessage.value = Event("Please enter grocery name")
        } else if (inputAmount.value == null) {
            statusMessage.value = Event("Please enter grocery amount")
        } else {
            if (isUpdateOrDelete) {
                groceryToUpdateOrDelete.name = inputName.value!!
                groceryToUpdateOrDelete.amount = inputAmount.value!!
                groceryToUpdateOrDelete.groceryListId = inputGroceryId.value!!
                updateGrocery(groceryToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val amount = inputAmount.value!!
                val status = "pending"
                val groceryId = inputGroceryId.value!!
                insertGrocery(Grocery(0, groceryId, name, amount, status))
                inputName.value = ""
                inputAmount.value = ""
            }
        }
    }

    private fun insertGrocery(grocery: Grocery) = viewModelScope.launch {
        val newRowId = repository.insert(grocery)
        if (newRowId > -1) {
            statusMessage.value = Event("Grocery Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }


    private fun updateGrocery(grocery: Grocery) = viewModelScope.launch {
        val noOfRows = repository.update(grocery)
        if (noOfRows > 0) {
            inputName.value = ""
            inputAmount.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRows Row Updated Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun getSavedGroceries(groceryListId: Int) = liveData {
        repository.getGroceries(groceryListId).collect {
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

    fun deleteGrocery(grocery: Grocery) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(grocery)
        if (noOfRowsDeleted > 0) {
            inputName.value = ""
            inputAmount.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    private fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAllGroceryItems()
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Groceries Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }
}