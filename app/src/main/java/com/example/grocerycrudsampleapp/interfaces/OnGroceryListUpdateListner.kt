package com.example.grocerycrudsampleapp.interfaces

import android.view.View
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList

interface OnGroceryListUpdateListner{
    fun onEdit(view: View?, groceryList: GroceryList)
    fun onDelete(view: View?, groceryList: GroceryList)
    fun onDone(view: View?, groceryList: GroceryList)
}