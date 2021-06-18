package com.example.grocerycrudsampleapp.repository

import com.example.grocerycrudsampleapp.database.GroceryDAO
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList
import kotlinx.coroutines.flow.Flow

class GroceryRepository (private val dao: GroceryDAO) {

    //val groceries = dao.getAllGroceries(groceryListId)
    val groceriesList = dao.getAllGroceriesList()
    val completedGroceries = dao.getAllCompletedGroceries()

    fun getGroceries(groceryListId: Int): Flow<List<Grocery>> {
        return dao.getAllGroceries(groceryListId)
    }

    suspend fun insert(groceryList: GroceryList): Long {
        return dao.insertGroceryList(groceryList)
    }

    suspend fun update(groceryList: GroceryList): Int {
        return dao.updateGroceryList(groceryList)
    }

    suspend fun delete(groceryList: GroceryList): Int {
        return dao.deleteGroceryList(groceryList)
    }

    suspend fun insert(grocery: Grocery): Long {
        return dao.insertGrocery(grocery)
    }

    suspend fun update(grocery: Grocery): Int {
        return dao.updateGrocery(grocery)
    }

    suspend fun delete(grocery: Grocery): Int {
        return dao.deleteGrocery(grocery)
    }

    suspend fun deleteAllGroceryItems(): Int {
        return dao.deleteAllGroceryItems()
    }

    suspend fun deleteAllGroceryList(): Int {
        return dao.deleteAllGroceryList()
    }

    suspend fun deleteAllCompletedGroceryList(): Int {
        return dao.deleteAllCompletedGroceryList()
    }
}