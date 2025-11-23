package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
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

    private val viewModel: ShoppingViewModel by viewModels {
        val database = ShoppingDatabase.getDatabase(this)
        val repository = ShoppingRepository(database.shoppingDao())
        ShoppingViewModel.Factory(
            AddItemUseCase(repository),
            UpdateItemUseCase(repository),
            DeleteItemUseCase(repository),
            GetItemsUseCase(repository)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel()

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShoppingApp(viewModel) { activeCount ->
                        updateBadge(activeCount)
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Badge Channel"
            val descriptionText = "Channel for app icon badges"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("badge_channel", name, importance).apply {
                description = descriptionText
                setShowBadge(true)
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateBadge(count: Int) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (count == 0) {
            notificationManager.cancel(1) // Remove badge if count is zero
            return
        }

        val notification = NotificationCompat.Builder(this, "badge_channel")
            .setContentTitle("Активные покупки")
            .setContentText("У вас $count активных покупок")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Use a valid drawable resource
            .setNumber(count)
            .build()

        notificationManager.notify(1, notification)
    }
}

@Composable
fun ShoppingApp(viewModel: ShoppingViewModel, onActiveItemsCountChange: (Int) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val activeItemsCount = uiState.items.count { !it.isChecked }

    LaunchedEffect(activeItemsCount) {
        onActiveItemsCountChange(activeItemsCount)
    }

    ShoppingListScreen(
        items = uiState.items,
        filter = uiState.filter,
        onItemCheckedChange = viewModel::updateItemStatus,
        onItemDelete = viewModel::deleteItem,
        onAddItem = viewModel::addItem,
        onAddMultipleItems = viewModel::addMultipleItems,
        onFilterChange = viewModel::updateFilter,
        onItemPriorityChange = viewModel::updateItemPriority // Corrected this line
    )
}
