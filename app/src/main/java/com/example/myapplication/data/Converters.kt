package com.example.myapplication.data

import androidx.room.TypeConverter
import com.example.myapplication.Priority

class Converters {
    
    @TypeConverter
    fun fromPriority(priority: Priority): Int {
        return when (priority) {
            Priority.LOW -> 0
            Priority.NORMAL -> 1
            Priority.HIGH -> 2
        }
    }
    
    @TypeConverter
    fun toPriority(priority: Int): Priority {
        return when (priority) {
            0 -> Priority.LOW
            1 -> Priority.NORMAL
            2 -> Priority.HIGH
            else -> Priority.NORMAL
        }
    }
}