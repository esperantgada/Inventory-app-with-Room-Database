package com.example.inventory.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inventory.room.ItemDao
import java.lang.IllegalArgumentException

class ItemViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ItemViewModel(itemDao) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}