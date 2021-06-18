package com.example.grocerycrudsampleapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList

@Database(entities = [GroceryList::class,Grocery::class], version = 1)
abstract class GroceryDatabase : RoomDatabase() {
    abstract val groceryDAO : GroceryDAO

    companion object {
        @Volatile
        private var INSTANCE: GroceryDatabase? = null
        fun getInstance(context: Context): GroceryDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GroceryDatabase::class.java,
                        "grocery_database"
                    ).build()
                }
                return instance
            }
        }
    }
}

