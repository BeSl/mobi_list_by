package com.example.myapplication.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.Priority
import com.example.myapplication.ShoppingItem
import com.example.myapplication.ui.components.AddItemsDialog
import com.example.myapplication.ui.components.BottomFilterTabs
import com.example.myapplication.ui.components.ConfirmDeleteDialog
import com.example.myapplication.ui.components.ShoppingListItem
import com.example.myapplication.usecase.ItemFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    items: List<ShoppingItem>,
    filter: ItemFilter,
    onItemCheckedChange: (Long, Boolean) -> Unit,
    onItemDelete: (ShoppingItem) -> Unit,
    onAddItem: (String, Priority, Int) -> Unit,
    onAddMultipleItems: (List<Pair<String, Priority>>) -> Unit,
    onFilterChange: (ItemFilter) -> Unit,
    onItemPriorityChange: (Long, Priority) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<ShoppingItem?>(null) }
    val activeItemsCount = items.count { !it.isChecked }

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("Мой список") }
            )
        },
        bottomBar = {
            BottomFilterTabs(
                selectedFilter = filter,
                activeItemsCount = activeItemsCount,
                onFilterSelected = onFilterChange
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (items.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items, key = { it.id }) { item ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            ShoppingListItem(
                                item = item,
                                onCheckedChange = { onItemCheckedChange(item.id, it) },
                                onDelete = { itemToDelete = item },
                                onPriorityChange = { newPriority -> onItemPriorityChange(item.id, newPriority) }
                            )
                        }
                    }
                }

                itemToDelete?.let { item ->
                    ConfirmDeleteDialog(
                        itemName = item.name,
                        onConfirm = { 
                            onItemDelete(item)
                            itemToDelete = null
                         },
                        onDismiss = { itemToDelete = null }
                    )
                }
            }
        }

        if (showDialog) {
            AddItemsDialog(
                onAddItems = {
                    onAddMultipleItems(it)
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Список покупок пуст",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Нажмите + для добавления первой покупки",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
