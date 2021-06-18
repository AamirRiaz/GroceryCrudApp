package com.example.grocerycrudsampleapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerycrudsampleapp.R
import com.example.grocerycrudsampleapp.databinding.CompletedGroceryListItemBinding
import com.example.grocerycrudsampleapp.databinding.GroceryListItemBinding
import com.example.grocerycrudsampleapp.interfaces.OnCompletedGroceryUpdateListner
import com.example.grocerycrudsampleapp.interfaces.OnGroceryUpdateListner
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList

class CompletedGroceryAdapter (context: AppCompatActivity, private val clickListener: (GroceryList) -> Unit) :
    RecyclerView.Adapter<CompletedGroceryAdapter.MyViewHolder>() {
    private val groceriesList = ArrayList<GroceryList>()
    var mGroceryUpdateListener: OnCompletedGroceryUpdateListner? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: CompletedGroceryListItemBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.completed_grocery_list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return groceriesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(groceriesList[position], clickListener)

        holder.binding.llEdit.setOnClickListener{
            mGroceryUpdateListener?.onEdit(it, groceriesList[position])
            //Toast.makeText(it.context, "edit clicked", Toast.LENGTH_SHORT).show()
        }
        holder.binding.llDelete.setOnClickListener{
            mGroceryUpdateListener?.onDelete(it, groceriesList[position])
            //Toast.makeText(it.context, "delete clicked", Toast.LENGTH_SHORT).show()
        }

    }

    fun setGroceryList(groceries: List<GroceryList>) {
        groceriesList.clear()
        groceriesList.addAll(groceries)
    }

    fun setOnUpdateListener(groceryUpdateListner: OnCompletedGroceryUpdateListner?) {
        this.mGroceryUpdateListener = groceryUpdateListner
    }


    inner class MyViewHolder(val binding: CompletedGroceryListItemBinding) : RecyclerView.ViewHolder(binding.root), OnCompletedGroceryUpdateListner {

        fun bind(groceryList: GroceryList, clickListener: (GroceryList) -> Unit) {
            binding.tvName.text = groceryList.name
            //binding.tvAmount.text = "R" + grocery.amount
            binding.listItemLayout.setOnClickListener {
                clickListener(groceryList)
            }
        }

        override fun onEdit(view: View?, groceryList: GroceryList) {
            mGroceryUpdateListener?.onEdit(view, groceriesList[adapterPosition])
        }

        override fun onDelete(view: View?, groceryList: GroceryList) {
            mGroceryUpdateListener?.onDelete(view, groceriesList[adapterPosition])
        }

    }
}

