package com.example.financetracker.repository

import com.example.financetracker.data.repository.ExpenseRepository
import com.example.financetracker.domain.model.CurrencyRates
import com.example.financetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeExpenseRepository : ExpenseRepository {

    private val _expensesFlow = MutableStateFlow<List<Expense>>(emptyList())
    val expensesList = mutableListOf<Expense>()

    var shouldReturnErrorOnRates: Boolean = false
    var customRates: CurrencyRates = CurrencyRates(usdBrl = 5.50, eurBrl = 6.00, gbpBrl = 7.00)

    override fun getAllExpenses(): Flow<List<Expense>> {
        return _expensesFlow.asStateFlow()
    }

    override suspend fun insertExpense(expense: Expense): Long {
        val newId = (expensesList.maxOfOrNull { it.id } ?: 0) + 1
        val newExpense = expense.copy(id = newId)
        expensesList.add(newExpense)
        _expensesFlow.value = expensesList.toList()
        return newId
    }

    override suspend fun updateExpense(expense: Expense) {
        val index = expensesList.indexOfFirst { it.id == expense.id }
        if (index != -1) {
            expensesList[index] = expense
            _expensesFlow.value = expensesList.toList()
        }
    }

    override suspend fun deleteExpense(expense: Expense) {
        expensesList.removeAll { it.id == expense.id }
        _expensesFlow.value = expensesList.toList()
    }

    override suspend fun fetchCurrencyRates(): Result<CurrencyRates> {
        return if (shouldReturnErrorOnRates) {
            Result.failure(RuntimeException("Erro simulado de conexão de rede"))
        } else {
            Result.success(customRates)
        }
    }
}
