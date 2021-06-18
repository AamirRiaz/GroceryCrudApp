package com.example.grocerycrudsampleapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerycrudsampleapp.R
import com.example.grocerycrudsampleapp.databinding.AddGroceryListItemBinding
import com.example.grocerycrudsampleapp.databinding.CompletedGroceryListItemBinding
import com.example.grocerycrudsampleapp.databinding.GroceryListItemBinding
import com.example.grocerycrudsampleapp.interfaces.OnCompletedGroceryUpdateListner
import com.example.grocerycrudsampleapp.interfaces.OnGroceryUpdateListner
import com.example.grocerycrudsampleapp.models.Grocery
import com.example.grocerycrudsampleapp.models.GroceryList

class AddGroceryAdapter(context: AppCompatActivity, private val clickListener: (Grocery) -> Unit) :
    RecyclerView.Adapter<AddGroceryAdapter.MyViewHolder>() {
    private val groceriesList = ArrayList<Grocery>()
    var mGroceryUpdateListener: OnGroceryUpdateListner? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: AddGroceryListItemBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.add_grocery_list_item, parent, false)
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
        holder.binding.llDone.setOnClickListener{
            mGroceryUpdateListener?.onDone(it, groceriesList[position])
            //Toast.makeText(it.context, "delete clicked", Toast.LENGTH_SHORT).show()
        }
    }

    fun setGroceryList(groceries: List<Grocery>) {
        groceriesList.clear()
        groceriesList.addAll(groceries)
    }

    fun setOnUpdateListener(groceryUpdateListner: OnGroceryUpdateListner?) {
        this.mGroceryUpdateListener = groceryUpdateListner
    }


    inner class MyViewHolder(val binding: AddGroceryListItemBinding) : RecyclerView.ViewHolder(binding.root), OnGroceryUpdateListner {

        fun bind(grocery: Grocery, clickListener: (Grocery) -> Unit) {
            binding.tvName.text = grocery.name
            binding.tvAmount.text = "R" + grocery.amount
            binding.listItemLayout.setOnClickListener {
                clickListener(grocery)
            }
            if (grocery.status == "completed"){
                binding.llDone.visibility = View.GONE
            }else {
                binding.llDone.visibility = View.VISIBLE
            }
        }

        override fun onEdit(view: View?, grocery: Grocery) {
            mGroceryUpdateListener?.onEdit(view, groceriesList[adapterPosition])
        }

        override fun onDelete(view: View?, grocery: Grocery) {
            mGroceryUpdateListener?.onDelete(view, groceriesList[adapterPosition])
        }

        override fun onDone(view: View?, grocery: Grocery) {
            mGroceryUpdateListener?.onDone(view, groceriesList[adapterPosition])
        }

    }
}

