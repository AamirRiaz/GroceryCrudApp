package com.example.grocerycrudsampleapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.grocerycrudsampleapp.R
import com.example.grocerycrudsampleapp.databinding.ActivityMainBinding
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList
import com.example.grocerycrudsampleapp.ui.addgrocery.AddGroceryFragment
import com.example.grocerycrudsampleapp.ui.addgrocerylist.AddGroceryListFragment
import com.example.grocerycrudsampleapp.ui.completedgrocerylist.CompletedGroceryListFragment
import com.example.grocerycrudsampleapp.ui.home.HomeFragment

class MainActivity : AppCompatActivity(){

    private lateinit var mContext: AppCompatActivity
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mContext = this
        setUpBottomMenu()
        attachHomeFragment()

        binding.addFb.setOnClickListener { view ->
            attachAddGroceryListFragment()
        }

    }

    private fun setUpBottomMenu() {
        binding.bottomNavigation.apply {
            binding.bottomNavigation.itemIconTintList = null
            binding.bottomNavigation.setOnNavigationItemSelectedListener {
                binding.bottomNavigation.menu.setGroupCheckable(0, true, true)
                //currentFragment = supportFragmentManager.findFragmentById(R.id.rlContainer)
                when (it.itemId) {
                    R.id.home -> {
                        val homeFragment = HomeFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, homeFragment).commit()

                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.list -> {
                        val groceryListFragment = CompletedGroceryListFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, groceryListFragment).commit()

                        return@setOnNavigationItemSelectedListener true
                    }
                }
                false
            }
        }
    }

    private fun attachHomeFragment(){
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, homeFragment).commit()
    }

    private fun attachAddGroceryFragment(){
        val addGroceryFragment = AddGroceryFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, addGroceryFragment).commit()
    }

    private fun attachAddGroceryListFragment(){
        val addGroceryListFragment = AddGroceryListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, addGroceryListFragment).commit()
    }

    fun attachAddGroceryFragment(groceryList: GroceryList){
        val bundle = Bundle()
        bundle.putParcelable("grocery", groceryList)

        val addGroceryFragment = AddGroceryFragment()
        addGroceryFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, addGroceryFragment).commit()
    }

    fun attachAddGroceryListFragment(groceryList: GroceryList){
        val bundle = Bundle()
        bundle.putParcelable("grocery", groceryList)

        val addGroceryListFragment = AddGroceryListFragment()
        addGroceryListFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, addGroceryListFragment).commit()
    }

    fun attachAddGroceryListFragment(groceryList: GroceryList, duplicate: Boolean){
        val bundle = Bundle()
        bundle.putParcelable("grocery", groceryList)
        bundle.putBoolean("duplicate", duplicate)

        val addGroceryListFragment = AddGroceryListFragment()
        addGroceryListFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, addGroceryListFragment).commit()
    }

}