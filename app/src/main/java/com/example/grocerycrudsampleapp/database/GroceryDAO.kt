package com.example.grocerycrudsampleapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList
import kotlinx.coroutines.flow.Flow
@Dao
interface GroceryDAO {

    @Insert
    suspend fun insertGroceryList(groceryList: GroceryList) : Long

    @Update
    suspend fun updateGroceryList(groceryList: GroceryList) : Int

    @Delete
    suspend fun deleteGroceryList(groceryList: GroceryList) : Int

    @Insert
    suspend fun insertGrocery(grocery: Grocery) : Long

    @Update
    suspend fun updateGrocery(grocery: Grocery) : Int

    @Delete
    suspend fun deleteGrocery(grocery: Grocery) : Int

    @Query("DELETE FROM grocery_list_table WHERE grocery_list_status='pending'")
    suspend fun deleteAllGroceryList() : Int

    @Query("DELETE FROM grocery_table")
    suspend fun deleteAllGroceryItems() : Int

    @Query("DELETE FROM grocery_list_table WHERE grocery_list_status='completed'")
    suspend fun deleteAllCompletedGroceryList() : Int

    @Query("SELECT * FROM grocery_list_table WHERE grocery_list_status='pending'")
    fun getAllGroceriesList():Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_table WHERE grocery_list_id=:groceryListId")
    fun getAllGroceries(groceryListId: Int):Flow<List<Grocery>>

    @Query("SELECT * FROM grocery_list_table WHERE grocery_list_status='completed'")
    fun getAllCompletedGroceries():Flow<List<GroceryList>>
}