package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {
    
    @Query("SELECT * FROM shopping_items ORDER BY priority DESC, created_date DESC")
    fun getAllItems(): Flow<List<ShoppingItemEntity>>
    
    @Query("SELECT * FROM shopping_items WHERE is_purchased = 0 ORDER BY priority DESC, created_date DESC")
    fun getActiveItems(): Flow<List<ShoppingItemEntity>>
    
    @Query("SELECT * FROM shopping_items WHERE is_purchased = 1 ORDER BY completed_date DESC")
    fun getCompletedItems(): Flow<List<ShoppingItemEntity>>
    
    @Query("SELECT * FROM shopping_items WHERE id = :id")
    suspend fun getItemById(id: Long): ShoppingItemEntity?
    
    @Insert
    suspend fun insertItem(item: ShoppingItemEntity): Long
    
    @Update
    suspend fun updateItem(item: ShoppingItemEntity)
    
    @Delete
    suspend fun deleteItem(item: ShoppingItemEntity)
    
    @Query("DELETE FROM shopping_items WHERE is_purchased = 1")
    suspend fun deleteCompletedItems()
    
    @Query("UPDATE shopping_items SET is_checked = :isChecked, is_purchased = :isPurchased, completed_date = :completedDate WHERE id = :id")
    suspend fun updateItemStatus(id: Long, isChecked: Boolean, isPurchased: Boolean, completedDate: Long? = null)
}