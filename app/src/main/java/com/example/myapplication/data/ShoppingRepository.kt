package com.example.myapplication.data

import com.example.myapplication.Priority
import com.example.myapplication.ShoppingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class ShoppingRepository(private val shoppingDao: ShoppingDao) {
    
    val allItems: Flow<List<ShoppingItemEntity>> = shoppingDao.getAllItems()
    
    val activeItems: Flow<List<ShoppingItemEntity>> = shoppingDao.getActiveItems()
    
    val completedItems: Flow<List<ShoppingItemEntity>> = shoppingDao.getCompletedItems()
    
    suspend fun getItemById(id: Long): ShoppingItemEntity? {
        return shoppingDao.getItemById(id)
    }
    
    suspend fun insertItem(item: ShoppingItemEntity): Long {
        return shoppingDao.insertItem(item)
    }
    
    suspend fun updateItem(item: ShoppingItemEntity) {
        shoppingDao.updateItem(item)
    }
    
    suspend fun deleteItem(item: ShoppingItemEntity) {
        shoppingDao.deleteItem(item)
    }
    
    suspend fun deleteCompletedItems() {
        shoppingDao.deleteCompletedItems()
    }
    
    suspend fun updateItemStatus(id: Long, isChecked: Boolean, isPurchased: Boolean) {
        val completedDate = if (isChecked) System.currentTimeMillis() else null
        shoppingDao.updateItemStatus(id, isChecked, isPurchased, completedDate)
    }
    
    fun ShoppingItemEntity.toDomainModel(): ShoppingItem {
        return ShoppingItem(
            id = id,
            name = name,
            quantity = quantity,
            isChecked = isChecked,
            isPurchased = isPurchased,
            priority = toPriority(priority),
            createdDate = Date(createdDate),
            completedDate = completedDate?.let { Date(it) }
        )
    }
    
    fun ShoppingItem.toEntity(): ShoppingItemEntity {
        return ShoppingItemEntity(
            id = id,
            name = name,
            quantity = quantity,
            isChecked = isChecked,
            isPurchased = isPurchased,
            priority = fromPriority(priority),
            createdDate = createdDate.time,
            completedDate = completedDate?.time
        )
    }
    
    fun fromPriority(priority: Priority): Int {
        return when (priority) {
            Priority.LOW -> 0
            Priority.NORMAL -> 1
            Priority.HIGH -> 2
        }
    }
    
    fun toPriority(priority: Int): Priority {
        return when (priority) {
            0 -> Priority.LOW
            1 -> Priority.NORMAL
            2 -> Priority.HIGH
            else -> Priority.NORMAL
        }
    }
}