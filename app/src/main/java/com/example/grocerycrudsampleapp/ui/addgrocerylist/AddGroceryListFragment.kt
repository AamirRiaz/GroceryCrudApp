package com.example.grocerycrudsampleapp.ui.addgrocerylist

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
import com.example.grocerycrudsampleapp.adapters.AddGroceryListAdapter
import com.example.grocerycrudsampleapp.adapters.CompletedGroceryAdapter
import com.example.grocerycrudsampleapp.database.GroceryDatabase
import com.example.grocerycrudsampleapp.databinding.AddGroceryFragmentBinding
import com.example.grocerycrudsampleapp.databinding.AddGroceryListFragmentBinding
import com.example.grocerycrudsampleapp.interfaces.OnCompletedGroceryUpdateListner
import com.example.grocerycrudsampleapp.interfaces.OnGroceryUpdateListner
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList
import com.example.grocerycrudsampleapp.repository.GroceryRepository

class AddGroceryListFragment : Fragment() {
    private lateinit var mContext: AppCompatActivity
    private lateinit var binding: AddGroceryListFragmentBinding
    private lateinit var addGroceryListViewModel: AddGroceryListViewModel
    private lateinit var adapter: AddGroceryListAdapter
    private var mGrocery: GroceryList? = null
    private var mDuplicate: Boolean? = false

    companion object {
        val TAG: String = AddGroceryListFragment::class.java.simpleName
        fun newInstance() = AddGroceryListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity as AppCompatActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.add_grocery_list_fragment, container, false
        )
        val view: View = binding.root

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = GroceryDatabase.getInstance(context?.applicationContext!!).groceryDAO
        val repository = GroceryRepository(dao)
        val factory = AddGroceryListViewModelFactory(repository)
        addGroceryListViewModel = ViewModelProvider(mContext, factory).get(AddGroceryListViewModel::class.java)
        binding.myViewModel = addGroceryListViewModel
        binding.lifecycleOwner = this

        addGroceryListViewModel.message.observe(mContext, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(mContext, it, Toast.LENGTH_LONG).show()
            }
        })
        getBundleData()
        updateUIDate()
        initRecyclerView()
        displayGroceriesList()
    }

    private fun getBundleData() {
        val bundle = this.arguments
        if (bundle != null) {
            mGrocery = bundle.getParcelable("grocery")!!
            mDuplicate = bundle.getBoolean("duplicate")
            //Log.e("bundle data-> ", "" + mGrocery.name!!)
        }
    }

    private fun updateUIDate(){
        if (mDuplicate == true){
            var groceries: List<Grocery> = ArrayList<Grocery>()
            addGroceryListViewModel.getSavedGroceries(mGrocery?.id!!).observe(mContext, Observer {
                groceries = it
                mGrocery?.let {
                    addGroceryListViewModel.initDuplicate(it, groceries)
                }
            })
        }else {
            mGrocery?.let {
                addGroceryListViewModel.initUpdateAndDelete(it)
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvGroceryList.layoutManager = LinearLayoutManager(mContext)
        binding.rvGroceryList.setPadding(0, 0, 0, 160)
        binding.rvGroceryList.clipToPadding = false
        binding.rvGroceryList.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
        adapter = AddGroceryListAdapter(mContext, { selectedItem: GroceryList -> listItemClicked(selectedItem) })
        binding.rvGroceryList.adapter = adapter
        adapter.setOnUpdateListener(object : OnCompletedGroceryUpdateListner {
            override fun onEdit(view: View?, groceryList: GroceryList) {
                addGroceryListViewModel.initUpdateAndDelete(groceryList)
            }

            override fun onDelete(view: View?, groceryList: GroceryList) {
                addGroceryListViewModel.deleteGrocery(groceryList)
            }

        })

    }

    private fun displayGroceriesList() {
        addGroceryListViewModel.getSavedGroceries().observe(mContext, Observer {
            adapter.setGroceryList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(groceryList: GroceryList) {
        addGroceryListViewModel.initUpdateAndDelete(groceryList)
    }

}
