package com.example.inventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.data.Item
import com.example.inventory.data.getFormattedPrice
import com.example.inventory.databinding.ItemListItemBinding

/**
 * ListAdapter for Item
 */
class ItemListAdapter(private val onItemClicked: (Item) -> Unit) :
    ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback){


    class ItemViewHolder(private val binding: ItemListItemBinding) :RecyclerView.ViewHolder(binding.root){

        fun bind(item: Item){
            binding.apply {
                itemName.text = item.itemName
                itemPrice.text = item.getFormattedPrice()
                itemQuantity.text = item.itemQuantityInStock.toString()
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemListAdapter.ItemViewHolder {
        val layout = ItemListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ItemListAdapter.ItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.itemView.setOnClickListener{
            onItemClicked(currentItem)
        }
        holder.bind(currentItem)
    }

    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>(){
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }
}