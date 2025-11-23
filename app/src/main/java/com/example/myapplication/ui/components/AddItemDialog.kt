package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.myapplication.Priority
import com.example.myapplication.usecase.ParseTextUseCase
import com.example.myapplication.usecase.ParsedItem
import com.example.myapplication.ui.components.ConfirmItemsDialog
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

// Функция для извлечения количества из названия товара
fun extractQuantityFromName(name: String): Pair<String, Int> {
    val quantityRegex = Regex("(\\d+)\\s*(.+)|(.+)\\s*(\\d+)")
    val match = quantityRegex.find(name)
    
    if (match != null) {
        val (qtyStart, nameStart, nameEnd, qtyEnd) = match.destructured
        if (qtyStart.isNotEmpty() && nameStart.isNotEmpty()) {
            // Формат: "3 яблока"
            return Pair(nameStart.trim(), qtyStart.toIntOrNull() ?: 1)
        } else if (nameEnd.isNotEmpty() && qtyEnd.isNotEmpty()) {
            // Формат: "яблоки 5"
            return Pair(nameEnd.trim(), qtyEnd.toIntOrNull() ?: 1)
        }
    }
    
    // Если количество не найдено, возвращаем название как есть и количество 1
    return Pair(name, 1)
}

@Composable
fun AddItemDialog(
    onAddItem: (String, Priority, Int) -> Unit,
    onAddMultipleItems: (List<Pair<String, Priority>>) -> Unit,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var isMultiMode by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemNames by remember { mutableStateOf(TextFieldValue("")) }
    var selectedPriority by remember { mutableStateOf(Priority.NORMAL) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    val parseTextUseCase = remember { ParseTextUseCase() }
    
    // Parse items when in multi mode
    val parsedItems = if (isMultiMode) {
        parseTextUseCase.parse(itemNames.text)
    } else {
        emptyList()
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить покупку") },
        text = {
            Column {
                // Mode switcher
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text("Один товар")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = isMultiMode,
                        onCheckedChange = { isMultiMode = it }
                    )
                    Text("Несколько товаров")
                }
                
                if (isMultiMode) {
                    // Multi-item input
                    OutlinedTextField(
                        value = itemNames,
                        onValueChange = { itemNames = it },
                        label = { Text("Список покупок (через запятую)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        supportingText = {
                            Text("Пример: Яблоки 5, Молоко 2, Хлеб")
                        }
                    )
                } else {
                    // Single item input
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = { Text("Название") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                PrioritySelector(
                    selectedPriority = selectedPriority,
                    onPrioritySelected = { selectedPriority = it }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isMultiMode) {
                        if (parsedItems.isNotEmpty()) {
                            showConfirmDialog = true
                        }
                    } else {
                        if (itemName.isNotBlank()) {
                            // Попытка извлечь количество из названия
                            val (name, quantity) = extractQuantityFromName(itemName)
                            onAddItem(name, selectedPriority, quantity)
                            onDismiss()
                        }
                    }
                },
                enabled = if (isMultiMode) parsedItems.isNotEmpty() else itemName.isNotBlank()
            ) {
                Text(if (isMultiMode) "Далее" else "Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
    
    // Confirm dialog for multiple items
    if (showConfirmDialog && isMultiMode) {
        ConfirmItemsDialog(
            items = parsedItems,
            onConfirm = { itemsWithPriority ->
                onAddMultipleItems(itemsWithPriority)
                onDismiss()
            },
            onDismiss = { showConfirmDialog = false }
        )
    }
}
