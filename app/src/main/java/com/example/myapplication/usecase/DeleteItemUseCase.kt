package com.example.myapplication.usecase

import com.example.myapplication.data.ShoppingRepository
import com.example.myapplication.data.ShoppingItemEntity

class DeleteItemUseCase(private val repository: ShoppingRepository) {
    suspend operator fun invoke(item: ShoppingItemEntity) {
        repository.deleteItem(item)
    }
}