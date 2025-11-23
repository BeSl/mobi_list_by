package com.example.myapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myapplication.usecase.ItemFilter

@Composable
fun BottomFilterTabs(
    selectedFilter: ItemFilter,
    activeItemsCount: Int,
    onFilterSelected: (ItemFilter) -> Unit
) {
    TabRow(
        selectedTabIndex = when (selectedFilter) {
            ItemFilter.ACTIVE -> 0
            ItemFilter.COMPLETED -> 1
            ItemFilter.ALL -> 2
        }
    ) {
        FilterTab(
            text = "Активные ($activeItemsCount)",
            icon = Icons.Default.List,
            selected = selectedFilter == ItemFilter.ACTIVE,
            onClick = { onFilterSelected(ItemFilter.ACTIVE) }
        )

        FilterTab(
            text = "Завершенные",
            icon = Icons.Default.CheckCircle,
            selected = selectedFilter == ItemFilter.COMPLETED,
            onClick = { onFilterSelected(ItemFilter.COMPLETED) }
        )

        FilterTab(
            text = "Все",
            icon = Icons.Default.AllInclusive,
            selected = selectedFilter == ItemFilter.ALL,
            onClick = { onFilterSelected(ItemFilter.ALL) }
        )
    }
}

@Composable
fun FilterTab(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Tab(
        selected = selected,
        onClick = onClick,
        text = { Text(text) },
        icon = { Icon(icon, contentDescription = text) }
    )
}