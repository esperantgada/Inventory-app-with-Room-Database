package com.example.inventory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat


@Entity(tableName = "item_table")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,

    @ColumnInfo(name = "item_name")
    val itemName : String,

    @ColumnInfo(name = "item_price")
    val itemPrice : Double,

    @ColumnInfo(name = "item_quantity_in_stock")
    val itemQuantityInStock : Int
)

//An extension function that formats the price
fun Item.getFormattedPrice() : String = NumberFormat.getCurrencyInstance().format(itemPrice)
