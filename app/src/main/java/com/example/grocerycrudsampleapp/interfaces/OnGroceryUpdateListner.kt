package com.example.grocerycrudsampleapp.interfaces

import android.view.View
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList

interface OnGroceryUpdateListner {
    fun onEdit(view: View?, grocery: Grocery)
    fun onDelete(view: View?, grocery: Grocery)
    fun onDone(view: View?, grocery: Grocery)
}