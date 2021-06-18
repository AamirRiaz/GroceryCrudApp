package com.example.grocerycrudsampleapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerycrudsampleapp.R
import com.example.grocerycrudsampleapp.adapters.GroceryListAdapter
import com.example.grocerycrudsampleapp.database.GroceryDatabase
import com.example.grocerycrudsampleapp.databinding.HomeFragmentBinding
import com.example.grocerycrudsampleapp.interfaces.OnGroceryListUpdateListner
import com.example.grocerycrudsampleapp.interfaces.OnGroceryUpdateListner
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList
import com.example.grocerycrudsampleapp.repository.GroceryRepository
import com.example.grocerycrudsampleapp.ui.main.MainActivity

class HomeFragment : Fragment() {
    private lateinit var mContext: AppCompatActivity
    private lateinit var binding: HomeFragmentBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: GroceryListAdapter


    companion object {
        val TAG: String = HomeFragment::class.java.simpleName
        fun newInstance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity as AppCompatActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.home_fragment, container, false
        )
        val view: View = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = GroceryDatabase.getInstance(context?.applicationContext!!).groceryDAO
        val repository = GroceryRepository(dao)
        val factory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        binding.myViewModel = homeViewModel
        binding.lifecycleOwner = this

        homeViewModel.message.observe(mContext, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(mContext, it, Toast.LENGTH_LONG).show()
            }
        })

        initRecyclerView()
        displayGroceriesList()
    }

    private fun initRecyclerView() {
        binding.rvHome.layoutManager = LinearLayoutManager(mContext)
        adapter = GroceryListAdapter(mContext, { selectedItem: GroceryList -> listItemClicked(selectedItem) })
        binding.rvHome.adapter = adapter
        adapter.setOnUpdateListener(object : OnGroceryListUpdateListner {
            override fun onEdit(view: View?, groceryList: GroceryList) {
                (activity as MainActivity).attachAddGroceryListFragment(groceryList)
            }

            override fun onDelete(view: View?, groceryList: GroceryList) {
                homeViewModel.deleteGrocery(groceryList)
            }

            override fun onDone(view: View?, groceryList: GroceryList) {
                homeViewModel.initUpdate(groceryList)
            }
        })
    }

    private fun displayGroceriesList() {
        homeViewModel.getSavedGroceries().observe(mContext, Observer {

            adapter.setGroceryList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(groceryList: GroceryList) {
        (activity as MainActivity).attachAddGroceryFragment(groceryList)
    }

}
