package com.example.inventory.room

import androidx.room.*
import com.example.inventory.data.Item
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item : Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM item_table")
    fun getAllItems() : Flow<List<Item>>

    @Query("SELECT * FROM item_table WHERE id = :id")
    fun getItemById(id : Int) : Flow<Item>
}