package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Priority
import com.example.myapplication.ShoppingItem
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onPriorityChange: (Priority) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PriorityIndicator(
            priority = item.priority,
            onClick = { newPriority ->
                onPriorityChange(newPriority)
            }
        )

        Spacer(modifier = Modifier.width(12.dp))

        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChange
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else null,
                color = if (item.isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
            )

            val formattedDate = remember(item.createdDate) {
                SimpleDateFormat("dd.MM HH:mm", Locale.getDefault()).format(item.createdDate)
            }
            Text(
                text = "Добавлено: $formattedDate",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (item.quantity > 1) {
            QuantityBadge(quantity = item.quantity)
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
fun PriorityIndicator(
    priority: Priority,
    onClick: ((Priority) -> Unit)? = null
) {
    val color = when (priority) {
        Priority.LOW -> Color(0xFF4CAF50).copy(alpha = 0.7f)
        Priority.NORMAL -> Color(0xFFFFC107).copy(alpha = 0.7f)
        Priority.HIGH -> Color(0xFFF44336).copy(alpha = 0.7f)
    }

    var modifier = Modifier
        .size(24.dp)
        .clip(CircleShape)
        .background(color)

    if (onClick != null) {
        val nextPriority = when (priority) {
            Priority.LOW -> Priority.NORMAL
            Priority.NORMAL -> Priority.HIGH
            Priority.HIGH -> Priority.LOW
        }
        modifier = modifier.clickable { onClick(nextPriority) }
    }

    Box(modifier = modifier)
}

@Composable
fun QuantityBadge(quantity: Int) {
    Box(
        modifier = Modifier
            .padding(end = 12.dp)
            .size(28.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "x$quantity",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}
