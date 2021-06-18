package com.example.grocerycrudsampleapp.ui.completedgrocerylist

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
import com.example.grocerycrudsampleapp.adapters.CompletedGroceryAdapter
import com.example.grocerycrudsampleapp.database.GroceryDatabase
import com.example.grocerycrudsampleapp.databinding.GroceryListFragmentBinding
import com.example.grocerycrudsampleapp.interfaces.OnCompletedGroceryUpdateListner
import com.example.grocerycrudsampleapp.interfaces.OnGroceryUpdateListner
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList
import com.example.grocerycrudsampleapp.repository.GroceryRepository
import com.example.grocerycrudsampleapp.ui.main.MainActivity

class CompletedGroceryListFragment : Fragment() {
    private lateinit var mContext: AppCompatActivity
    private lateinit var binding: GroceryListFragmentBinding
    private lateinit var groceryViewModel: CompletedGroceryListViewModel
    private lateinit var adapter: CompletedGroceryAdapter


    companion object {
        val TAG: String = CompletedGroceryListFragment::class.java.simpleName
        fun newInstance() = CompletedGroceryListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity as AppCompatActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.grocery_list_fragment, container, false
        )
        val view: View = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = GroceryDatabase.getInstance(context?.applicationContext!!).groceryDAO
        val repository = GroceryRepository(dao)
        val factory = CompletedGroceryListViewModelFactory(repository)
        groceryViewModel = ViewModelProvider(this, factory).get(CompletedGroceryListViewModel::class.java)
        binding.myViewModel = groceryViewModel
        binding.lifecycleOwner = this

        groceryViewModel.message.observe(mContext, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(mContext, it, Toast.LENGTH_LONG).show()
            }
        })

        initRecyclerView()
        displayCompletedGroceriesList()
    }

    private fun initRecyclerView() {
        binding.rvList.layoutManager = LinearLayoutManager(mContext)
        adapter = CompletedGroceryAdapter(mContext, { selectedItem: GroceryList -> listItemClicked(selectedItem) })
        binding.rvList.adapter = adapter
        adapter.setOnUpdateListener(object : OnCompletedGroceryUpdateListner {
            override fun onEdit(view: View?, groceryList: GroceryList) {
                (activity as MainActivity).attachAddGroceryListFragment(groceryList, true)
            }

            override fun onDelete(view: View?, groceryList: GroceryList) {
                groceryViewModel.deleteGrocery(groceryList)
            }
        })
    }

    private fun displayCompletedGroceriesList() {
        groceryViewModel.getCompletedGroceries().observe(mContext, Observer {
            adapter.setGroceryList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(groceryList: GroceryList) {
        //groceryViewModel.initUpdateAndDelete(groceryList)
        (activity as MainActivity).attachAddGroceryFragment(groceryList)
    }

}

