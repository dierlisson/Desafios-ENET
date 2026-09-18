package com.example.financetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.financetracker.data.local.AppDatabase
import com.example.financetracker.data.remote.AwesomeExchangeApi
import com.example.financetracker.data.repository.ExpenseRepositoryImpl
import com.example.financetracker.ui.screens.DashboardScreen
import com.example.financetracker.ui.theme.FinanceTrackerTheme
import com.example.financetracker.ui.viewmodel.ExpenseViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ExpenseViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val api = AwesomeExchangeApi.create()
        val repository = ExpenseRepositoryImpl(database.expenseDao(), api)
        ExpenseViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinanceTrackerTheme {
                DashboardScreen(viewModel = viewModel)
            }
        }
    }
}
