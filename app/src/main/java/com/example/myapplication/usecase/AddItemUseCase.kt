package com.example.myapplication.usecase

import com.example.myapplication.Priority
import com.example.myapplication.data.ShoppingRepository
import com.example.myapplication.data.ShoppingItemEntity
import java.util.Date

class AddItemUseCase(private val repository: ShoppingRepository) {
    suspend operator fun invoke(name: String, priority: Priority = Priority.NORMAL, quantity: Int = 1): Long {
        val item = ShoppingItemEntity(
            name = name,
            quantity = quantity,
            priority = when (priority) {
                Priority.LOW -> 0
                Priority.NORMAL -> 1
                Priority.HIGH -> 2
            },
            createdDate = System.currentTimeMillis()
        )
        return repository.insertItem(item)
    }
}