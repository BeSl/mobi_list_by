package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myapplication.usecase.ItemFilter

@Composable
fun FilterTabs(
    selectedFilter: ItemFilter,
    onFilterSelected: (ItemFilter) -> Unit
) {
    TabRow(
        selectedTabIndex = when (selectedFilter) {
            ItemFilter.ALL -> 0
            ItemFilter.ACTIVE -> 1
            ItemFilter.COMPLETED -> 2
        }
    ) {
        FilterTab(
            text = "Все",
            selected = selectedFilter == ItemFilter.ALL,
            onClick = { onFilterSelected(ItemFilter.ALL) }
        )
        
        FilterTab(
            text = "Активные",
            selected = selectedFilter == ItemFilter.ACTIVE,
            onClick = { onFilterSelected(ItemFilter.ACTIVE) }
        )
        
        FilterTab(
            text = "Завершенные",
            selected = selectedFilter == ItemFilter.COMPLETED,
            onClick = { onFilterSelected(ItemFilter.COMPLETED) }
        )
    }
}

@Composable
fun FilterTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Tab(
        selected = selected,
        onClick = onClick,
        text = { Text(text) }
    )
}