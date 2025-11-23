package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Priority
import com.example.myapplication.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemsDialog(
    onAddItems: (List<Pair<String, Priority>>) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var textInput by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current

    // Логика парсинга в реальном времени
    val parsedItems by remember(textInput) {
        derivedStateOf {
            if (textInput.isBlank()) emptyList()
            else {
                textInput.split(",", "\n") // Разделители: запятая или новая строка
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
                .navigationBarsPadding()
        ) {
            // Заголовок
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Массовое добавление",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                // Кнопка "Вставить из буфера" (опционально, для удобства)
                TextButton(
                    onClick = {
                        clipboardManager.getText()?.text?.let { textInput = it }
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.PlaylistAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Вставить")
                }
            }

            Text(
                text = "Введите список через запятую или с новой строки",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Поле ввода
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                label = { Text("Пример: Молоко 2, Хлеб, Яйца 10") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp), // Высокое поле для удобства
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryGreen,
                    focusedLabelColor = PrimaryGreen
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Предпросмотр (Preview)
            if (parsedItems.isNotEmpty()) {
                Text(
                    text = "Распознано товаров: ${parsedItems.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(parsedItems) { rawItem ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(rawItem) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка добавления
            Button(
                onClick = {
                    // Преобразуем строки в пары (Строка, Приоритет)
                    // При массовом добавлении ставим приоритет NORMAL по умолчанию
                    val resultList = parsedItems.map { it to Priority.NORMAL }
                    onAddItems(resultList)
                    onDismiss()
                },
                enabled = parsedItems.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen
                )
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Добавить список", fontSize = 16.sp)
            }
        }
    }
}
