package com.example.grocerycrudsampleapp.ui.addgrocery

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
import com.example.grocerycrudsampleapp.adapters.AddGroceryAdapter
import com.example.grocerycrudsampleapp.adapters.CompletedGroceryAdapter
import com.example.grocerycrudsampleapp.database.GroceryDatabase
import com.example.grocerycrudsampleapp.databinding.AddGroceryFragmentBinding
import com.example.grocerycrudsampleapp.interfaces.OnGroceryUpdateListner
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList
import com.example.grocerycrudsampleapp.repository.GroceryRepository

class AddGroceryFragment : Fragment() {
    private lateinit var mContext: AppCompatActivity
    private lateinit var binding: AddGroceryFragmentBinding
    private lateinit var addGroceryViewModel: AddGroceryViewModel
    private lateinit var adapter: AddGroceryAdapter
    private var mGrocery: GroceryList? = null

    companion object {
        val TAG: String = AddGroceryFragment::class.java.simpleName
        fun newInstance() = AddGroceryFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity as AppCompatActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.add_grocery_fragment, container, false
        )
        val view: View = binding.root

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = GroceryDatabase.getInstance(context?.applicationContext!!).groceryDAO
        val repository = GroceryRepository(dao)
        val factory = AddGroceryViewModelFactory(repository)
        addGroceryViewModel = ViewModelProvider(mContext, factory).get(AddGroceryViewModel::class.java)
        binding.myViewModel = addGroceryViewModel
        binding.lifecycleOwner = this

        addGroceryViewModel.message.observe(mContext, Observer {
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
            //Log.e("bundle data-> ", "" + mGrocery.name!!)
        }
    }

    private fun updateUIDate(){
        mGrocery?.let {
            addGroceryViewModel.initGroceryListId(it.id)
        }
    }

    private fun initRecyclerView() {
        binding.rvGrocery.layoutManager = LinearLayoutManager(mContext)
        binding.rvGrocery.setPadding(0, 0, 0, 160)
        binding.rvGrocery.clipToPadding = false
        binding.rvGrocery.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
        adapter = AddGroceryAdapter(mContext, {selectedItem: Grocery -> listItemClicked(selectedItem) })
        binding.rvGrocery.adapter = adapter
        adapter.setOnUpdateListener(object : OnGroceryUpdateListner {
            override fun onEdit(view: View?, grocery: Grocery) {
                addGroceryViewModel.initUpdateAndDelete(grocery)
            }

            override fun onDelete(view: View?, grocery: Grocery) {
                addGroceryViewModel.deleteGrocery(grocery)
            }

            override fun onDone(view: View?, grocery: Grocery) {
                addGroceryViewModel.initUpdate(grocery)
            }
        })

    }

    private fun displayGroceriesList() {
        addGroceryViewModel.getSavedGroceries(mGrocery?.id!!).observe(mContext, Observer {
            val size: Int = it.size
            var count: Int = 0
            for (grocery in it) {
                if (grocery.status == "completed"){
                    count++
                }
            }
            if (count != 0 && size != 0 && count == size){
                addGroceryViewModel.completeTheGroceryList(mGrocery!!)
            }

            adapter.setGroceryList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(grocery: Grocery) {
        addGroceryViewModel.initUpdateAndDelete(grocery)
    }

}
