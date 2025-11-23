package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Priority
import com.example.myapplication.ShoppingItem
import com.example.myapplication.data.ShoppingRepository
import com.example.myapplication.usecase.AddItemUseCase
import com.example.myapplication.usecase.DeleteItemUseCase
import com.example.myapplication.usecase.GetItemsUseCase
import com.example.myapplication.usecase.UpdateItemUseCase
import com.example.myapplication.usecase.ItemFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ShoppingListUiState(
    val items: List<ShoppingItem> = emptyList(),
    val filter: ItemFilter = ItemFilter.ALL,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ShoppingViewModel(
    private val addItemUseCase: AddItemUseCase,
    private val updateItemUseCase: UpdateItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val getItemsUseCase: GetItemsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState
    
    init {
        loadItems()
    }
    
    fun addItem(name: String, priority: Priority, quantity: Int = 1) {
        viewModelScope.launch {
            try {
                addItemUseCase(name, priority, quantity)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    fun addMultipleItems(items: List<Pair<String, Priority>>) {
        viewModelScope.launch {
            try {
                items.forEach { (nameWithQuantity, priority) ->
                    // Извлекаем название и количество из строки
                    val parts = nameWithQuantity.split(" ")
                    if (parts.size >= 2) {
                        val name = parts.dropLast(1).joinToString(" ")
                        val quantity = parts.last().toIntOrNull() ?: 1
                        addItemUseCase(name, priority, quantity)
                    } else {
                        // Если количество не указано, используем 1
                        addItemUseCase(nameWithQuantity, priority, 1)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    fun updateItemStatus(id: Long, isChecked: Boolean) {
        viewModelScope.launch {
            try {
                val item = _uiState.value.items.find { it.id == id }
                item?.let {
                    val updatedItem = it.copy(isChecked = isChecked, isPurchased = isChecked, completedDate = if (isChecked) java.util.Date() else null)
                    // Convert to entity and update
                    updateItemUseCase(updatedItem.toEntity())
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                deleteItemUseCase(item.toEntity())
                // Обновляем список после удаления
                loadItems()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Ошибка при удалении: ${e.message}")
            }
        }
    }
    
    fun updateFilter(filter: ItemFilter) {
        _uiState.value = _uiState.value.copy(filter = filter)
        loadItems()
    }
    
    private fun loadItems() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                getItemsUseCase(_uiState.value.filter).collectLatest { items ->
                    _uiState.value = _uiState.value.copy(
                        items = items,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    private fun ShoppingItem.toEntity() = com.example.myapplication.data.ShoppingItemEntity(
        id = id,
        name = name,
        isChecked = isChecked,
        isPurchased = isPurchased,
        priority = when (priority) {
            Priority.LOW -> 0
            Priority.NORMAL -> 1
            Priority.HIGH -> 2
        },
        createdDate = createdDate.time,
        completedDate = completedDate?.time,
        category = null
    )
}