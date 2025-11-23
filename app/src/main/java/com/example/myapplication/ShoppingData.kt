package com.example.myapplication

import java.util.Date

enum class Priority {
    LOW, NORMAL, HIGH
}

data class ShoppingItem(
    val id: Long = 0,
    val name: String,
    val quantity: Int = 1,
    val isChecked: Boolean = false,
    val isPurchased: Boolean = false,
    val priority: Priority = Priority.NORMAL,
    val createdDate: Date = Date(),
    val completedDate: Date? = null
)

data class ShoppingDay(val date: Date, val items: List<ShoppingItem>)

data class ShoppingList(val days: List<ShoppingDay>)
