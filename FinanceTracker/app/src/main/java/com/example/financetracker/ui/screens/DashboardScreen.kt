package com.example.financetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material.icons.filled.Tune

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.domain.model.Currency
import com.example.financetracker.domain.model.CurrencyRates
import com.example.financetracker.domain.model.Expense
import com.example.financetracker.domain.model.ExpenseCategory
import com.example.financetracker.domain.model.MonthlySummary
import com.example.financetracker.ui.components.DonutChart
import com.example.financetracker.ui.components.ExchangeRatesCard
import com.example.financetracker.ui.components.ExpenseItemCard
import com.example.financetracker.ui.components.GlassCard
import com.example.financetracker.ui.theme.DarkBackground
import com.example.financetracker.ui.theme.DarkCardSurface
import com.example.financetracker.ui.theme.DarkSurface
import com.example.financetracker.ui.theme.GlassBorder
import com.example.financetracker.ui.theme.NeonCyan
import com.example.financetracker.ui.theme.NeonMint
import com.example.financetracker.ui.theme.NeonPurple
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.TextSecondary
import com.example.financetracker.ui.viewmodel.ExpenseViewModel

@Composable
fun DashboardScreen(
    viewModel: ExpenseViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val expenses by viewModel.expenses.observeAsState(emptyList())
    val monthlySummary by viewModel.monthlySummary.observeAsState()
    val currencyRates by viewModel.currencyRates.observeAsState(CurrencyRates())
    val selectedCurrency by viewModel.selectedCurrency.observeAsState(Currency.BRL)
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    var showAddEditDialog by remember { mutableStateOf(false) }
    var expenseToEdit by remember { mutableStateOf<Expense?>(null) }
    var selectedExpenseDetail by remember { mutableStateOf<Expense?>(null) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf<ExpenseCategory?>(null) }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = DarkBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    expenseToEdit = null
                    showAddEditDialog = true
                },
                containerColor = NeonPurple,
                contentColor = TextPrimary,
                shape = CircleShape,
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Gasto",
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                contentColor = TextPrimary,
                tonalElevation = 8.dp
            ) {
                val navItems = listOf(
                    NavItem("Dash", Icons.Default.SpaceDashboard),
                    NavItem("Telemetry", Icons.Default.ShowChart),
                    NavItem("History", Icons.Default.History),
                    NavItem("Console", Icons.Default.Tune)
                )


                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonMint,
                            selectedTextColor = NeonMint,
                            indicatorColor = NeonMint.copy(alpha = 0.2f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> MainDashboardContent(
                    expenses = expenses,
                    monthlySummary = monthlySummary,
                    currencyRates = currencyRates,
                    selectedCurrency = selectedCurrency,
                    isLoading = isLoading,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    selectedCategoryFilter = selectedCategoryFilter,
                    onCategoryFilterChange = { selectedCategoryFilter = it },
                    onCurrencySelected = { viewModel.setSelectedCurrency(it) },
                    onRefreshRates = { viewModel.fetchExchangeRates() },
                    onExpenseClick = { selectedExpenseDetail = it },
                    onDeleteExpense = { viewModel.deleteExpense(it) }
                )
                1 -> AnalyticsTelemetriaScreen(viewModel = viewModel)
                2 -> TransactionsHistoryScreen(
                    viewModel = viewModel,
                    onExpenseSelected = { selectedExpenseDetail = it }
                )
                3 -> SettingsScreen()
            }
        }
    }

    // Modal de Criação / Edição
    if (showAddEditDialog) {
        AddEditExpenseDialog(
            expenseToEdit = expenseToEdit,
            onDismiss = { showAddEditDialog = false },
            onSave = { title, amount, category, currency ->
                if (expenseToEdit != null) {
                    viewModel.updateExpense(
                        id = expenseToEdit!!.id,
                        title = title,
                        amount = amount,
                        category = category,
                        dateMillis = expenseToEdit!!.dateMillis,
                        currency = currency
                    )
                } else {
                    viewModel.addExpense(
                        title = title,
                        amount = amount,
                        category = category,
                        currency = currency
                    )
                }
                showAddEditDialog = false
            }
        )
    }

    // Modal Comprovante Analítico (Detail Drawer)
    selectedExpenseDetail?.let { expense ->
        val converted = currencyRates.convert(expense.amount, expense.currency, selectedCurrency)
        ExpenseDetailDrawerScreen(
            expense = expense,
            displayCurrency = selectedCurrency,
            convertedAmount = converted,
            onDismiss = { selectedExpenseDetail = null },
            onEdit = {
                expenseToEdit = it
                selectedExpenseDetail = null
                showAddEditDialog = true
            },
            onDelete = {
                viewModel.deleteExpense(it)
                selectedExpenseDetail = null
            },
            onRepeat = {
                viewModel.addExpense(
                    title = it.title + " (Cópia)",
                    amount = it.amount,
                    category = it.category,
                    currency = it.currency
                )
                selectedExpenseDetail = null
            }
        )
    }
}

private data class NavItem(val label: String, val icon: ImageVector)

@Composable
private fun MainDashboardContent(
    expenses: List<Expense>,
    monthlySummary: MonthlySummary?,
    currencyRates: CurrencyRates,
    selectedCurrency: Currency,
    isLoading: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategoryFilter: ExpenseCategory?,
    onCategoryFilterChange: (ExpenseCategory?) -> Unit,
    onCurrencySelected: (Currency) -> Unit,
    onRefreshRates: () -> Unit,
    onExpenseClick: (Expense) -> Unit,
    onDeleteExpense: (Expense) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header
        HeaderSection(
            selectedCurrency = selectedCurrency,
            onCurrencySelected = onCurrencySelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Câmbio ao Vivo Card
        ExchangeRatesCard(
            rates = currencyRates,
            isLoading = isLoading,
            onRefreshClick = onRefreshRates
        )

        Spacer(modifier = Modifier.height(16.dp))

        val filteredExpenses = expenses.filter { exp ->
            val matchesSearch = exp.title.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategoryFilter == null || exp.category == selectedCategoryFilter
            matchesSearch && matchesCategory
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                monthlySummary?.let { summary ->
                    SummarySection(
                        summary = summary,
                        selectedCurrency = selectedCurrency
                    )
                }
            }

            item {
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Buscar gastos...", color = TextSecondary) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonCyan) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = DarkSurface,
                            unfocusedContainerColor = DarkSurface,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            CategoryFilterChip(
                                label = "Todos",
                                isSelected = selectedCategoryFilter == null,
                                onClick = { onCategoryFilterChange(null) }
                            )
                        }
                        items(ExpenseCategory.entries.toTypedArray()) { category ->
                            CategoryFilterChip(
                                label = category.displayName,
                                isSelected = selectedCategoryFilter == category,
                                onClick = {
                                    onCategoryFilterChange(if (selectedCategoryFilter == category) null else category)
                                }
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "📋 Transações (${filteredExpenses.size})",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (filteredExpenses.isEmpty()) {
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)) {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("Nenhum gasto registrado ainda. Toque no + para adicionar!", color = TextSecondary, fontSize = 14.sp)
                        }
                    }
                }
            } else {
                items(filteredExpenses, key = { it.id }) { expense ->
                    val convertedAmount = currencyRates.convert(expense.amount, expense.currency, selectedCurrency)
                    ExpenseItemCard(
                        expense = expense,
                        displayCurrency = selectedCurrency,
                        convertedAmount = convertedAmount,
                        onEditClick = { onExpenseClick(it) },
                        onDeleteClick = { onDeleteExpense(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    selectedCurrency: Currency,
    onCurrencySelected: (Currency) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("FinanceTracker", color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text("Monitor de Gastos Pessoais", color = TextSecondary, fontSize = 12.sp)
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(DarkCardSurface)
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
                .padding(4.dp)
        ) {
            Currency.entries.forEach { curr ->
                val isSelected = curr == selectedCurrency
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) NeonPurple else androidx.compose.ui.graphics.Color.Transparent)
                        .clickable { onCurrencySelected(curr) }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = curr.code,
                        color = if (isSelected) TextPrimary else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun SummarySection(
    summary: MonthlySummary,
    selectedCurrency: Currency
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text("📊 Resumo Financeiro do Mês", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DonutChart(
                    categorySummaries = summary.categorySummaries,
                    totalSpent = summary.totalSpent,
                    selectedCurrency = selectedCurrency,
                    chartSize = 130.dp
                )

                Column(
                    modifier = Modifier.weight(1f).padding(start = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatRow("Total Gasto", selectedCurrency.format(summary.totalSpent), NeonMint)
                    StatRow("Média Diária", selectedCurrency.format(summary.dailyAverage), NeonCyan)
                    summary.topCategory?.let {
                        StatRow("Maior Categoria", it.displayName, it.color)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column {
        Text(label, color = TextSecondary, fontSize = 11.sp)
        Text(value, color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CategoryFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) NeonPurple else DarkCardSurface)
            .border(1.dp, if (isSelected) NeonMint else GlassBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) TextPrimary else TextSecondary,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
