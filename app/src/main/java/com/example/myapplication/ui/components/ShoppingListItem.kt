package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.myapplication.Priority
import com.example.myapplication.ShoppingItem
import com.example.myapplication.ui.theme.StatusPurchased
import com.example.myapplication.ui.theme.StatusActive
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Checkbox(
                checked = item.isPurchased,
                onCheckedChange = { onCheckedChange(it) }
            )
            
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = if (item.quantity > 1) "${item.name} (${item.quantity})" else item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (item.isPurchased) TextDecoration.LineThrough else null,
                    color = if (item.isPurchased) MaterialTheme.colorScheme.onSurfaceVariant
                           else MaterialTheme.colorScheme.onSurface
                )
                
                // Date information
                Text(
                    text = "Добавлено: ${SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(item.createdDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (item.completedDate != null) {
                    Text(
                        text = "Куплено: ${SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(item.completedDate)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Priority indicator
            PriorityIndicator(priority = item.priority)
            
            // Status indicator
            StatusIndicator(isPurchased = item.isPurchased)
        }
        
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Удалить",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun PriorityIndicator(priority: Priority) {
    val color = when (priority) {
        Priority.LOW -> MaterialTheme.colorScheme.primary
        Priority.NORMAL -> MaterialTheme.colorScheme.secondary
        Priority.HIGH -> MaterialTheme.colorScheme.error
    }
    
    androidx.compose.foundation.layout.Box(
       modifier = androidx.compose.ui.Modifier
           .padding(start = 8.dp)
           .background(color, androidx.compose.foundation.shape.CircleShape)
           .size(12.dp)
   )
}

@Composable
fun StatusIndicator(isPurchased: Boolean) {
    val color = if (isPurchased) {
        // Зеленый цвет для купленных товаров
        StatusPurchased
    } else {
        // Серый цвет для активных товаров
        StatusActive
    }
    
    androidx.compose.foundation.layout.Box(
        modifier = androidx.compose.ui.Modifier
            .padding(start = 8.dp)
            .background(color, androidx.compose.foundation.shape.CircleShape)
            .size(12.dp)
    )
}