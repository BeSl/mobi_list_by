package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.Priority

@Composable
fun PrioritySelector(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Column {
        Text("Приоритет", style = MaterialTheme.typography.labelLarge)
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PriorityOption(
                priority = Priority.LOW,
                isSelected = selectedPriority == Priority.LOW,
                onClick = { onPrioritySelected(Priority.LOW) }
            )
            
            PriorityOption(
                priority = Priority.NORMAL,
                isSelected = selectedPriority == Priority.NORMAL,
                onClick = { onPrioritySelected(Priority.NORMAL) }
            )
            
            PriorityOption(
                priority = Priority.HIGH,
                isSelected = selectedPriority == Priority.HIGH,
                onClick = { onPrioritySelected(Priority.HIGH) }
            )
        }
    }
}

@Composable
fun PriorityOption(
    priority: Priority,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        when (priority) {
            Priority.LOW -> MaterialTheme.colorScheme.primary
            Priority.NORMAL -> MaterialTheme.colorScheme.secondary
            Priority.HIGH -> MaterialTheme.colorScheme.error
        }
    } else {
        MaterialTheme.colorScheme.outline
    }
    
    val backgroundColor = if (isSelected) {
        borderColor.copy(alpha = 0.1f)
    } else {
        Color.Transparent
    }
    
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .background(backgroundColor, RoundedCornerShape(8.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            PriorityIndicator(priority = priority)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = when (priority) {
                    Priority.LOW -> "Низкий"
                    Priority.NORMAL -> "Средний"
                    Priority.HIGH -> "Высокий"
                }
            )
        }
    }
}