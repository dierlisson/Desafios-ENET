package com.example.financetracker.data.repository

import com.example.financetracker.data.local.ExpenseDao
import com.example.financetracker.data.local.ExpenseEntity
import com.example.financetracker.data.remote.AwesomeExchangeApi
import com.example.financetracker.domain.model.CurrencyRates
import com.example.financetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    suspend fun insertExpense(expense: Expense): Long
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun fetchCurrencyRates(): Result<CurrencyRates>
}

class ExpenseRepositoryImpl(
    private val expenseDao: ExpenseDao,
    private val exchangeApi: AwesomeExchangeApi
) : ExpenseRepository {

    override fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun insertExpense(expense: Expense): Long {
        return expenseDao.insertExpense(ExpenseEntity.fromDomain(expense))
    }

    override suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(ExpenseEntity.fromDomain(expense))
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(ExpenseEntity.fromDomain(expense))
    }

    override suspend fun fetchCurrencyRates(): Result<CurrencyRates> {
        return try {
            val response = exchangeApi.getExchangeRates()
            val usdBrl = response["USDBRL"]?.bid?.toDoubleOrNull() ?: 5.50
            val eurBrl = response["EURBRL"]?.bid?.toDoubleOrNull() ?: 6.00
            val gbpBrl = response["GBPBRL"]?.bid?.toDoubleOrNull() ?: 7.10

            val rates = CurrencyRates(
                usdBrl = usdBrl,
                eurBrl = eurBrl,
                gbpBrl = gbpBrl,
                lastUpdatedMillis = System.currentTimeMillis()
            )
            Result.success(rates)
        } catch (e: Exception) {
            // Em caso de falha de rede ou offline, retorna fallback padrão seguro
            Result.failure(e)
        }
    }
}
