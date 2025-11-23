package com.example.myapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.Priority

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "is_checked")
    val isChecked: Boolean = false,
    
    @ColumnInfo(name = "is_purchased")
    val isPurchased: Boolean = false,
    
    @ColumnInfo(name = "priority")
    val priority: Int = 1, // 0 - LOW, 1 - NORMAL, 2 - HIGH
    
    @ColumnInfo(name = "created_date")
    val createdDate: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "completed_date")
    val completedDate: Long? = null,
    
    @ColumnInfo(name = "category")
    val category: String? = null,
    
    @ColumnInfo(name = "quantity")
    val quantity: Int = 1
)