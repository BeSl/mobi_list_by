package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ShoppingItemEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ShoppingDatabase : RoomDatabase() {
    
    abstract fun shoppingDao(): ShoppingDao
    
    companion object {
        @Volatile
        private var INSTANCE: ShoppingDatabase? = null
        
        fun getDatabase(context: Context): ShoppingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}