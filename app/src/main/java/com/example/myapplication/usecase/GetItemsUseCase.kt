package com.example.myapplication.usecase

import com.example.myapplication.data.ShoppingRepository
import com.example.myapplication.data.ShoppingItemEntity
import com.example.myapplication.Priority
import com.example.myapplication.ShoppingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

enum class ItemFilter {
    ALL, ACTIVE, COMPLETED
}

class GetItemsUseCase(private val repository: ShoppingRepository) {
    operator fun invoke(filter: ItemFilter = ItemFilter.ALL): Flow<List<ShoppingItem>> {
        return when (filter) {
            ItemFilter.ALL -> repository.allItems
            ItemFilter.ACTIVE -> repository.activeItems
            ItemFilter.COMPLETED -> repository.completedItems
        }.map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    private fun ShoppingItemEntity.toDomainModel(): ShoppingItem {
        return ShoppingItem(
            id = id,
            name = name,
            isChecked = isChecked,
            isPurchased = isPurchased,
            priority = toPriority(priority),
            createdDate = Date(createdDate),
            completedDate = completedDate?.let { Date(it) }
        )
    }
    
    private fun toPriority(priority: Int): Priority {
        return when (priority) {
            0 -> Priority.LOW
            1 -> Priority.NORMAL
            2 -> Priority.HIGH
            else -> Priority.NORMAL
        }
    }
}