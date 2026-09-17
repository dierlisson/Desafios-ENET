package com.example.financetracker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.financetracker.data.repository.ExpenseRepository
import com.example.financetracker.domain.model.CategorySummary
import com.example.financetracker.domain.model.Currency
import com.example.financetracker.domain.model.CurrencyRates
import com.example.financetracker.domain.model.Expense
import com.example.financetracker.domain.model.ExpenseCategory
import com.example.financetracker.domain.model.MonthlySummary
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _expenses = MutableLiveData<List<Expense>>(emptyList())
    val expenses: LiveData<List<Expense>> get() = _expenses

    private val _monthlySummary = MutableLiveData<MonthlySummary>()
    val monthlySummary: LiveData<MonthlySummary> get() = _monthlySummary

    private val _currencyRates = MutableLiveData<CurrencyRates>(CurrencyRates())
    val currencyRates: LiveData<CurrencyRates> get() = _currencyRates

    private val _selectedCurrency = MutableLiveData<Currency>(Currency.BRL)
    val selectedCurrency: LiveData<Currency> get() = _selectedCurrency

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> get() = _errorMessage

    init {
        observeExpenses()
        fetchExchangeRates()
    }

    private fun observeExpenses() {
        viewModelScope.launch {
            repository.getAllExpenses().collectLatest { expenseList ->
                _expenses.value = expenseList
                calculateMonthlySummary(expenseList, _selectedCurrency.value ?: Currency.BRL)
            }
        }
    }

    fun fetchExchangeRates() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.fetchCurrencyRates()
            _isLoading.value = false
            result.onSuccess { rates ->
                _currencyRates.value = rates
                // Recalcular resumo com novas taxas
                _expenses.value?.let { calculateMonthlySummary(it, _selectedCurrency.value ?: Currency.BRL) }
            }.onFailure { exception ->
                _errorMessage.value = "Falha ao obter taxas de câmbio: ${exception.localizedMessage}. Usando taxas offline."
            }
        }
    }

    fun setSelectedCurrency(currency: Currency) {
        _selectedCurrency.value = currency
        _expenses.value?.let { calculateMonthlySummary(it, currency) }
    }

    fun addExpense(
        title: String,
        amount: Double,
        category: ExpenseCategory,
        dateMillis: Long = System.currentTimeMillis(),
        currency: Currency = Currency.BRL
    ) {
        if (title.isBlank() || amount <= 0) {
            _errorMessage.value = "Título inválido ou valor deve ser maior que zero."
            return
        }

        val expense = Expense(
            title = title.trim(),
            amount = amount,
            category = category,
            dateMillis = dateMillis,
            currency = currency
        )

        viewModelScope.launch {
            try {
                repository.insertExpense(expense)
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao adicionar gasto: ${e.localizedMessage}"
            }
        }
    }

    fun updateExpense(
        id: Long,
        title: String,
        amount: Double,
        category: ExpenseCategory,
        dateMillis: Long,
        currency: Currency
    ) {
        if (title.isBlank() || amount <= 0) {
            _errorMessage.value = "Título inválido ou valor deve ser maior que zero."
            return
        }

        val expense = Expense(
            id = id,
            title = title.trim(),
            amount = amount,
            category = category,
            dateMillis = dateMillis,
            currency = currency
        )

        viewModelScope.launch {
            try {
                repository.updateExpense(expense)
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao atualizar gasto: ${e.localizedMessage}"
            }
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                repository.deleteExpense(expense)
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao excluir gasto: ${e.localizedMessage}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun calculateMonthlySummary(
        expenseList: List<Expense>,
        targetCurrency: Currency
    ) {
        val rates = _currencyRates.value ?: CurrencyRates()

        // Converter cada gasto para a moeda selecionada no Dashboard
        val convertedExpenses = expenseList.map { exp ->
            val convertedAmount = rates.convert(exp.amount, exp.currency, targetCurrency)
            exp.copy(amount = convertedAmount, currency = targetCurrency)
        }

        val totalSpent = convertedExpenses.sumOf { it.amount }

        // Calcular dias decorridos no mês atual para média diária
        val calendar = Calendar.getInstance()
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH).coerceAtLeast(1)
        val dailyAverage = totalSpent / dayOfMonth

        val highestExpense = convertedExpenses.maxByOrNull { it.amount }

        // Agrupar por categoria
        val categoryTotals = convertedExpenses.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }

        val topCategory = categoryTotals.maxByOrNull { it.value }?.key

        val categorySummaries = categoryTotals.map { (cat, total) ->
            val percentage = if (totalSpent > 0) ((total / totalSpent) * 100).toFloat() else 0f
            CategorySummary(
                category = cat,
                totalAmount = total,
                percentage = percentage
            )
        }.sortedByDescending { it.totalAmount }

        _monthlySummary.value = MonthlySummary(
            totalSpent = totalSpent,
            dailyAverage = dailyAverage,
            highestExpense = highestExpense,
            topCategory = topCategory,
            categorySummaries = categorySummaries
        )
    }

    class Factory(private val repository: ExpenseRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
                return ExpenseViewModel(repository) as T
            }
            throw IllegalArgumentException("Classe ViewModel desconhecida")
        }
    }
}
