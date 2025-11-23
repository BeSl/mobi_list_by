package com.example.myapplication.usecase

import com.example.myapplication.data.ShoppingRepository
import com.example.myapplication.data.ShoppingItemEntity

class UpdateItemUseCase(private val repository: ShoppingRepository) {
    suspend operator fun invoke(item: ShoppingItemEntity) {
        repository.updateItem(item)
    }
}