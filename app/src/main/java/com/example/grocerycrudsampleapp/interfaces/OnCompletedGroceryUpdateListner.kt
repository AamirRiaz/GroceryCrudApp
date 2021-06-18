package com.example.grocerycrudsampleapp.interfaces

import android.view.View
import com.example.grocerycrudsampleapp.models.GroceryList

interface OnCompletedGroceryUpdateListner {
    fun onEdit(view: View?, groceryList: GroceryList)
    fun onDelete(view: View?, groceryList: GroceryList)
}