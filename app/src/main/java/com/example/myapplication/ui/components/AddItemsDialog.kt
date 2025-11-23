package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myapplication.Priority
import com.example.myapplication.ui.theme.PrimaryGreen

data class ShoppingItemDraft(
    val name: String = "",
    val quantity: Int = 1,
    val priority: Priority = Priority.NORMAL
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemsDialog(
    onAddItems: (List<Pair<String, Priority>>) -> Unit,
    onDismiss: () -> Unit
) {
    var itemDrafts = remember { 
        mutableStateListOf(
            ShoppingItemDraft(),
            ShoppingItemDraft()
        ) 
    }
    
    var showConfirmDialog by remember { mutableStateOf(false) }
    
    fun addNewItem() {
        itemDrafts.add(ShoppingItemDraft())
    }
    
    fun removeItem(index: Int) {
        if (itemDrafts.size > 1) {
            itemDrafts.removeAt(index)
        }
    }
    
    fun updateItemName(index: Int, name: String) {
        itemDrafts[index] = itemDrafts[index].copy(name = name)
    }
    
    fun updateItemQuantity(index: Int, quantity: Int) {
        itemDrafts[index] = itemDrafts[index].copy(quantity = quantity)
    }
    
    fun updateItemPriority(index: Int, priority: Priority) {
        itemDrafts[index] = itemDrafts[index].copy(priority = priority)
    }
    
    fun getValidItems(): List<Pair<String, Priority>> {
        return itemDrafts
            .filter { it.name.isNotBlank() }
            .map { "${it.name} ${it.quantity}" to it.priority }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                text = "Добавить покупки",
                color = PrimaryGreen
            ) 
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Введите список покупок",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(itemDrafts.size) { index ->
                        ShoppingItemInput(
                            item = itemDrafts[index],
                            onNameChange = { updateItemName(index, it) },
                            onQuantityChange = { updateItemQuantity(index, it) },
                            onPriorityChange = { updateItemPriority(index, it) },
                            onRemove = { removeItem(index) },
                            showRemoveButton = itemDrafts.size > 1
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { addNewItem() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen.copy(alpha = 0.2f),
                        contentColor = PrimaryGreen
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Добавить еще")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val validItems = getValidItems()
                    if (validItems.isNotEmpty()) {
                        onAddItems(validItems)
                        onDismiss()
                    }
                },
                enabled = getValidItems().isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Добавить все")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingItemInput(
    item: ShoppingItemDraft,
    onNameChange: (String) -> Unit,
    onQuantityChange: (Int) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onRemove: () -> Unit,
    showRemoveButton: Boolean
) {
    val focusManager = LocalFocusManager.current
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryGreen.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = item.name,
                    onValueChange = onNameChange,
                    label = { Text("Название") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )
                
                if (showRemoveButton) {
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Удалить",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = item.quantity.toString(),
                    onValueChange = { newValue ->
                        val quantity = newValue.toIntOrNull() ?: 1
                        onQuantityChange(quantity)
                    },
                    label = { Text("Количество") },
                    modifier = Modifier.width(120.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (showRemoveButton) ImeAction.Next else ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )
                
                com.example.myapplication.ui.components.PrioritySelector(
                    selectedPriority = item.priority,
                    onPrioritySelected = onPriorityChange
                )
            }
        }
    }
}