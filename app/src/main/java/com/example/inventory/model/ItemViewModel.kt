package com.example.inventory.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.room.ItemDao
import kotlinx.coroutines.launch

class ItemViewModel(private val itemDao: ItemDao) : ViewModel(){

    val allItems : LiveData<List<Item>> = itemDao.getAllItems().asLiveData()

    /**
     * Takes an Item object or instance and insert it into the database off the main thread
     */
    private fun insertItem(item: Item){
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    /**
     * Takes three strings in and return an instance of Item
     */
    private fun getNewItemEntryInstance(
        itemName: String, itemPrice: String, itemQuantity: String): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            itemQuantityInStock = itemQuantity.toInt()
        )
    }

    /**
     * Takes three strings in, call [getNewItemEntryInstance] method, pass them in,
     * assign it to a variable and insert it in the database
     */
    fun addNewItem(itemName : String, itemPrice: String, itemQuantity: String){
        val newItem = getNewItemEntryInstance(itemName, itemPrice, itemQuantity)
        insertItem(newItem)
    }

    /**
     * Checks if the entry is valid and return a boolean
     */
    fun isEntryValid(itemName: String, itemPrice: String, itemQuantity: String) : Boolean{
        return !(itemName.isBlank() || itemPrice.isBlank() || itemQuantity.isBlank())
    }

    /**
     * Retrieves a specific item identified by its id from the database
     */
    fun retrieveItem(id : Int) : LiveData<Item>{
        return itemDao.getItemById(id).asLiveData()
    }

    /**
     * Launches coroutine to update item
     */
    private fun updateItem(item : Item){
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    /**
     * Checks if the item quantity is greater than 0 and decrease it by one.
     * Updates the item quantity in the database by calling [updateItem] method
     */
    fun sellItem(item : Item){
        if (item.itemQuantityInStock > 0){
            val newItemQuantity = item.copy(itemQuantityInStock = item.itemQuantityInStock - 1)
            updateItem(newItemQuantity)
        }
    }

    /**
     * Checks if there is available item defined by its quantity
     */
    fun isSotckAvailable(item: Item) : Boolean{
        return item.itemQuantityInStock > 0
    }

    /**
     * Launches a coroutine to delete an item from the database
     */
     fun deleteItem(item : Item){
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    /**
     * Updates an item in the database by returning an instance of item
     */
    private fun getUpdatedItemEntry(
        itemId : Int,
        itemName: String,
        itemPrice: String,
        itemQuantity: String
    ) : Item{
        return Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            itemQuantityInStock = itemQuantity.toInt()
        )
    }

    /**
     * Updates an item in the database by calling [getUpdatedItemEntry] method
     */
    fun updateItemInDatabase(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemQuantity: String
    ){
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemQuantity)
        updateItem(updatedItem)
    }
}