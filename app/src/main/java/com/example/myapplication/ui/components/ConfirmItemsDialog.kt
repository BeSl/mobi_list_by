package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.Priority
import com.example.myapplication.usecase.ParsedItem

@Composable
fun ConfirmItemsDialog(
    items: List<ParsedItem>,
    onConfirm: (List<Pair<String, Priority>>) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedItems = remember { mutableStateListOf(*items.toTypedArray()) }
    var selectedPriority by remember { mutableStateOf(Priority.NORMAL) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Подтвердите список покупок") },
        text = {
            Column {
                Text("Будут добавлены следующие покупки:")
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp)
                ) {
                    items(selectedItems) { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = true,
                                onCheckedChange = null // Read-only
                            )
                            Text(text = if (item.quantity != null) "${item.name} (${item.quantity} шт.)" else item.name)
                        }
                    }
                }
                
                PrioritySelector(
                    selectedPriority = selectedPriority,
                    onPrioritySelected = { priority -> selectedPriority = priority }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val itemsWithPriority = selectedItems.map { it.name to selectedPriority }
                    onConfirm(itemsWithPriority)
                    onDismiss()
                }
            ) {
                Text("Подтвердить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}