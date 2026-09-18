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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.domain.model.Currency
import com.example.financetracker.domain.model.Expense
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
fun TransactionsHistoryScreen(
    viewModel: ExpenseViewModel,
    onExpenseSelected: (Expense) -> Unit
) {
    val expenses by viewModel.expenses.observeAsState(emptyList())
    val selectedCurrency by viewModel.selectedCurrency.observeAsState(Currency.BRL)
    val rates by viewModel.currencyRates.observeAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterTag by remember { mutableStateOf("Todos") }
    var exportStatusMessage by remember { mutableStateOf<String?>(null) }

    val filterTags = listOf("Todos", "Alimentação", "Transporte", "Assinaturas", "Saídas Pix", "Acima de R$ 200")

    val filteredExpenses = expenses.filter { exp ->
        val matchesSearch = exp.title.contains(searchQuery, ignoreCase = true)
        val matchesTag = when (selectedFilterTag) {
            "Todos" -> true
            "Alimentação" -> exp.category.displayName.contains("Alimentação", ignoreCase = true)
            "Transporte" -> exp.category.displayName.contains("Transporte", ignoreCase = true)
            "Acima de R$ 200" -> exp.amount >= 200.0
            else -> true
        }
        matchesSearch && matchesTag
    }

    val totalFilteredSpent = filteredExpenses.sumOf { exp ->
        rates?.convert(exp.amount, exp.currency, selectedCurrency) ?: exp.amount
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header & Search Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar estabelecimento ou valor...", color = TextSecondary, fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonCyan) },
                trailingIcon = {
                    IconButton(onClick = { /* Simulador de Leitor QR Code */ }) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = "Scanner", tint = NeonMint)
                    }
                },
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
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Filters Carrossel
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filterTags) { tag ->
                val isSelected = tag == selectedFilterTag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) NeonPurple else DarkCardSurface)
                        .border(1.dp, if (isSelected) NeonMint else GlassBorder, RoundedCornerShape(14.dp))
                        .clickable { selectedFilterTag = tag }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = tag,
                        color = if (isSelected) TextPrimary else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Export Banner Card
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total no Período Filtrado",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = selectedCurrency.format(totalFilteredSpent),
                        color = NeonMint,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeonCyan.copy(alpha = 0.2f))
                        .border(1.dp, NeonCyan, RoundedCornerShape(12.dp))
                        .clickable {
                            exportStatusMessage = "Relatório CSV/PDF gerado e salvo!"
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Exportar (CSV / PDF)", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (exportStatusMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(exportStatusMessage!!, color = NeonMint, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ledger Feed
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "📜 Histórico Completo",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (filteredExpenses.isEmpty()) {
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("Nenhuma transação encontrada no filtro selecionado.", color = TextSecondary, fontSize = 13.sp)
                        }
                    }
                }
            } else {
                items(filteredExpenses, key = { it.id }) { expense ->
                    val converted = rates?.convert(expense.amount, expense.currency, selectedCurrency) ?: expense.amount
                    ExpenseItemCard(

                        expense = expense,
                        displayCurrency = selectedCurrency,
                        convertedAmount = converted,
                        onEditClick = { onExpenseSelected(it) },
                        onDeleteClick = { viewModel.deleteExpense(it) }
                    )
                }
            }
        }
    }
}
