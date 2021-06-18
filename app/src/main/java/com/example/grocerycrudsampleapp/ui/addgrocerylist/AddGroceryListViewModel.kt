package com.example.grocerycrudsampleapp.ui.addgrocerylist

import androidx.lifecycle.*
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList
import com.example.grocerycrudsampleapp.repository.GroceryRepository
import com.example.grocerycrudsampleapp.utils.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch

class AddGroceryListViewModel(private val repository: GroceryRepository) : ViewModel() {
    private var isUpdateOrDelete = false
    private var isDuplicate = false
    private lateinit var groceryToUpdateOrDelete: GroceryList
    private lateinit var groceryToDuplicate: List<Grocery>
    val inputName = MutableLiveData<String>()
    val inputAmount = MutableLiveData<String>()
    val inputStatus = MutableLiveData<String>()
    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun initUpdateAndDelete(groceryList: GroceryList) {
        inputName.value = groceryList.name
        //inputAmount.value = groceryList.amount
        inputStatus.value = groceryList.status
        isUpdateOrDelete = true
        groceryToUpdateOrDelete = groceryList
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    fun initDuplicate(groceryList: GroceryList, groceries: List<Grocery>) {
        inputName.value = groceryList.name
        //inputAmount.value = groceryList.amount
        inputStatus.value = groceryList.status
        isDuplicate = true
        groceryToUpdateOrDelete = groceryList
        groceryToDuplicate = groceries
        saveOrUpdateButtonText.value = "Duplicate"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    fun saveOrUpdate() {
        if (inputName.value == null) {
            statusMessage.value = Event("Please enter grocery name")
        } else {
            if (isUpdateOrDelete) {
                groceryToUpdateOrDelete.name = inputName.value!!
                //groceryToUpdateOrDelete.amount = inputAmount.value!!
                groceryToUpdateOrDelete.status = inputStatus.value!!
                updateGrocery(groceryToUpdateOrDelete)
            }else if (isDuplicate) {
                val name = inputName.value!!
                //val amount = inputAmount.value!!
                val status = "pending"
                duplicateGrocery(GroceryList(0, name, status))
                inputName.value = ""
                inputAmount.value = ""
                inputStatus.value = ""
            } else {
                val name = inputName.value!!
                //val amount = inputAmount.value!!
                val status = "pending"
                insertGrocery(GroceryList(0, name, status))
                inputName.value = ""
                inputAmount.value = ""
                inputStatus.value = ""
            }
        }
    }

    private fun insertGrocery(groceryList: GroceryList) = viewModelScope.launch {
        val newRowId = repository.insert(groceryList)
        if (newRowId > -1) {
            statusMessage.value = Event("Grocery Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    private fun duplicateGrocery(groceryList: GroceryList) = viewModelScope.launch {
        val newRowId = repository.insert(groceryList)
        for (grocery in groceryToDuplicate){
            insertGrocery(Grocery(0, newRowId.toInt(), grocery.name, grocery.amount, grocery.status))
        }
        if (newRowId > -1) {
            statusMessage.value = Event("Grocery Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
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

    private fun updateGrocery(groceryList: GroceryList) = viewModelScope.launch {
        val noOfRows = repository.update(groceryList)
        if (noOfRows > 0) {
            inputName.value = ""
            //inputAmount.value = ""
            inputStatus.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
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

    fun deleteGrocery(groceryList: GroceryList) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(groceryList)
        if (noOfRowsDeleted > 0) {
            inputName.value = ""
            //inputAmount.value = ""
            inputStatus.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
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