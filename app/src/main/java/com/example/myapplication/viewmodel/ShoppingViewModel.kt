package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Priority
import com.example.myapplication.ShoppingItem
import com.example.myapplication.usecase.AddItemUseCase
import com.example.myapplication.usecase.DeleteItemUseCase
import com.example.myapplication.usecase.GetItemsUseCase
import com.example.myapplication.usecase.UpdateItemUseCase
import com.example.myapplication.usecase.ItemFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ShoppingViewModel(
    private val addItemUseCase: AddItemUseCase,
    private val updateItemUseCase: UpdateItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val getItemsUseCase: GetItemsUseCase
) : ViewModel() {

    private val _filter = MutableStateFlow(ItemFilter.ALL)
    private val _error = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<ShoppingListUiState> = combine(
        _filter.flatMapLatest { getItemsUseCase(it) },
        _filter,
        _isLoading,
        _error
    ) { items, filter, isLoading, error ->
        ShoppingListUiState(items, filter, isLoading, error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ShoppingListUiState()
    )

    fun addItem(name: String, priority: Priority, quantity: Int = 1) {
        viewModelScope.launch {
            try {
                addItemUseCase(name, priority, quantity)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun addMultipleItems(items: List<Pair<String, Priority>>) {
        viewModelScope.launch {
            try {
                items.forEach { (nameWithQuantity, priority) ->
                    val parts = nameWithQuantity.split(" ")
                    val name = if (parts.size >= 2 && parts.last().toIntOrNull() != null) parts.dropLast(1).joinToString(" ") else nameWithQuantity
                    val quantity = if (parts.size >= 2) parts.last().toIntOrNull() ?: 1 else 1
                    addItemUseCase(name, priority, quantity)
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateItemStatus(id: Long, isChecked: Boolean) {
        viewModelScope.launch {
            try {
                val item = uiState.value.items.find { it.id == id } ?: return@launch
                val updatedItem = item.copy(isChecked = isChecked, isPurchased = isChecked, completedDate = if (isChecked) java.util.Date() else null)
                updateItemUseCase(updatedItem.toEntity())
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateItemPriority(id: Long, newPriority: Priority) {
        viewModelScope.launch {
            try {
                val item = uiState.value.items.find { it.id == id } ?: return@launch
                val updatedItem = item.copy(priority = newPriority)
                updateItemUseCase(updatedItem.toEntity())
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                deleteItemUseCase(item.toEntity())
            } catch (e: Exception) {
                _error.value = "Ошибка при удалении: ${e.message}"
            }
        }
    }

    fun updateFilter(filter: ItemFilter) {
        _filter.value = filter
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

    class Factory(
        private val addItemUseCase: AddItemUseCase,
        private val updateItemUseCase: UpdateItemUseCase,
        private val deleteItemUseCase: DeleteItemUseCase,
        private val getItemsUseCase: GetItemsUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShoppingViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ShoppingViewModel(addItemUseCase, updateItemUseCase, deleteItemUseCase, getItemsUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

data class ShoppingListUiState(
    val items: List<ShoppingItem> = emptyList(),
    val filter: ItemFilter = ItemFilter.ALL,
    val isLoading: Boolean = false,
    val error: String? = null
)
