package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.data.ShoppingDatabase
import com.example.myapplication.data.ShoppingRepository
import com.example.myapplication.ui.ShoppingListScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.usecase.AddItemUseCase
import com.example.myapplication.usecase.DeleteItemUseCase
import com.example.myapplication.usecase.GetItemsUseCase
import com.example.myapplication.usecase.UpdateItemUseCase
import com.example.myapplication.viewmodel.ShoppingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val database = ShoppingDatabase.getDatabase(this)
                    val repository = ShoppingRepository(database.shoppingDao())
                    val addItemUseCase = AddItemUseCase(repository)
                    val updateItemUseCase = UpdateItemUseCase(repository)
                    val deleteItemUseCase = DeleteItemUseCase(repository)
                    val getItemsUseCase = GetItemsUseCase(repository)
                    
                    val viewModel = ShoppingViewModel(
                        addItemUseCase = addItemUseCase,
                        updateItemUseCase = updateItemUseCase,
                        deleteItemUseCase = deleteItemUseCase,
                        getItemsUseCase = getItemsUseCase
                    )
                    
                    ShoppingApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun ShoppingApp(viewModel: ShoppingViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    ShoppingListScreen(
        items = uiState.items,
        filter = uiState.filter,
        onItemCheckedChange = viewModel::updateItemStatus,
        onItemDelete = viewModel::deleteItem,
        onAddItem = { name, priority, quantity -> viewModel.addItem(name, priority, quantity) },
        onAddMultipleItems = viewModel::addMultipleItems,
        onFilterChange = viewModel::updateFilter
    )
}
