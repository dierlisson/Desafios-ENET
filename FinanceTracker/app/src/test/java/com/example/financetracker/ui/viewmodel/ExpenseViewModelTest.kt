package com.example.financetracker.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.financetracker.domain.model.Currency
import com.example.financetracker.domain.model.CurrencyRates
import com.example.financetracker.domain.model.Expense
import com.example.financetracker.domain.model.ExpenseCategory
import com.example.financetracker.repository.FakeExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: FakeExpenseRepository
    private lateinit var viewModel: ExpenseViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeExpenseRepository()
        viewModel = ExpenseViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addExpense insere novo gasto e atualiza lista de gastos no LiveData`() = runTest {
        viewModel.addExpense(
            title = "Almoço Restaurante",
            amount = 45.0,
            category = ExpenseCategory.ALIMENTACAO,
            currency = Currency.BRL
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val list = viewModel.expenses.value
        assertNotNull(list)
        assertEquals(1, list?.size)
        assertEquals("Almoço Restaurante", list?.first()?.title)
        assertEquals(45.0, list?.first()?.amount ?: 0.0, 0.01)
        assertEquals(ExpenseCategory.ALIMENTACAO, list?.first()?.category)
    }

    @Test
    fun `addExpense com titulo vazio exibe mensagem de erro e nao insere gasto`() = runTest {
        viewModel.addExpense(
            title = "   ",
            amount = 50.0,
            category = ExpenseCategory.TRANSPORTE,
            currency = Currency.BRL
        )

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Título inválido ou valor deve ser maior que zero.", viewModel.errorMessage.value)
        assertTrue(viewModel.expenses.value.isNullOrEmpty())
    }

    @Test
    fun `updateExpense edita gasto existente com sucesso`() = runTest {
        viewModel.addExpense(
            title = "Uber",
            amount = 25.0,
            category = ExpenseCategory.TRANSPORTE,
            currency = Currency.BRL
        )
        testDispatcher.scheduler.advanceUntilIdle()

        val insertedExpense = viewModel.expenses.value?.first()!!

        viewModel.updateExpense(
            id = insertedExpense.id,
            title = "Uber Black",
            amount = 40.0,
            category = ExpenseCategory.TRANSPORTE,
            dateMillis = insertedExpense.dateMillis,
            currency = Currency.BRL
        )
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedList = viewModel.expenses.value
        assertEquals(1, updatedList?.size)
        assertEquals("Uber Black", updatedList?.first()?.title)
        assertEquals(40.0, updatedList?.first()?.amount ?: 0.0, 0.01)
    }

    @Test
    fun `deleteExpense remove gasto do repositorio e atualiza LiveData`() = runTest {
        viewModel.addExpense(
            title = "Cinema",
            amount = 30.0,
            category = ExpenseCategory.LAZER,
            currency = Currency.BRL
        )
        testDispatcher.scheduler.advanceUntilIdle()

        val expense = viewModel.expenses.value?.first()!!

        viewModel.deleteExpense(expense)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.expenses.value.isNullOrEmpty())
    }

    @Test
    fun `calculateMonthlySummary calcula total e porcentagens por categoria corretamente`() = runTest {
        viewModel.addExpense("Supermercado", 200.0, ExpenseCategory.ALIMENTACAO, currency = Currency.BRL)
        viewModel.addExpense("Farmácia", 100.0, ExpenseCategory.SAUDE, currency = Currency.BRL)
        testDispatcher.scheduler.advanceUntilIdle()

        val summary = viewModel.monthlySummary.value
        assertNotNull(summary)
        assertEquals(300.0, summary?.totalSpent ?: 0.0, 0.01)
        assertEquals(ExpenseCategory.ALIMENTACAO, summary?.topCategory)
        assertEquals("Supermercado", summary?.highestExpense?.title)

        // Verificar porcentagens
        val alimentacaoSummary = summary?.categorySummaries?.find { it.category == ExpenseCategory.ALIMENTACAO }
        val saudeSummary = summary?.categorySummaries?.find { it.category == ExpenseCategory.SAUDE }

        assertEquals(66.66f, alimentacaoSummary?.percentage ?: 0f, 1f)
        assertEquals(33.33f, saudeSummary?.percentage ?: 0f, 1f)
    }

    @Test
    fun `setSelectedCurrency converte valores para moeda escolhida`() = runTest {
        repository.customRates = CurrencyRates(usdBrl = 5.0, eurBrl = 6.0, gbpBrl = 7.0)
        viewModel.fetchExchangeRates()

        viewModel.addExpense("Compra em USD", 100.0, ExpenseCategory.OUTROS, currency = Currency.BRL)
        testDispatcher.scheduler.advanceUntilIdle()

        // Mudar para USD (onde 100 BRL com taxa USD=5.0 equivale a 20 USD)
        viewModel.setSelectedCurrency(Currency.USD)
        testDispatcher.scheduler.advanceUntilIdle()

        val summaryInUsd = viewModel.monthlySummary.value
        assertEquals(20.0, summaryInUsd?.totalSpent ?: 0.0, 0.01)
        assertEquals(Currency.USD, viewModel.selectedCurrency.value)
    }

    @Test
    fun `fetchExchangeRates em caso de falha de rede exibe mensagem de erro e mantem taxas padrao`() = runTest {
        repository.shouldReturnErrorOnRates = true

        viewModel.fetchExchangeRates()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.errorMessage.value)
        assertTrue(viewModel.errorMessage.value!!.contains("Falha ao obter taxas de câmbio"))
    }
}
