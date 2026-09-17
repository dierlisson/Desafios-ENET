package com.example.financetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.financetracker.domain.model.Currency
import com.example.financetracker.domain.model.Expense
import com.example.financetracker.domain.model.ExpenseCategory
import com.example.financetracker.ui.components.getCategoryIcon
import com.example.financetracker.ui.theme.DarkBackground
import com.example.financetracker.ui.theme.DarkCardSurface
import com.example.financetracker.ui.theme.DarkSurface
import com.example.financetracker.ui.theme.GlassBorder
import com.example.financetracker.ui.theme.NeonMint
import com.example.financetracker.ui.theme.NeonPurple
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.TextSecondary

@Composable
fun AddEditExpenseDialog(
    expenseToEdit: Expense? = null,
    onDismiss: () -> Unit,
    onSave: (title: String, amount: Double, category: ExpenseCategory, currency: Currency) -> Unit
) {
    var title by remember { mutableStateOf(expenseToEdit?.title ?: "") }
    var amountText by remember { mutableStateOf(expenseToEdit?.amount?.let { if (it > 0) it.toString() else "" } ?: "") }
    var selectedCategory by remember { mutableStateOf(expenseToEdit?.category ?: ExpenseCategory.ALIMENTACAO) }
    var selectedCurrency by remember { mutableStateOf(expenseToEdit?.currency ?: Currency.BRL) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isEditing = expenseToEdit != null

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = if (isEditing) "✏️ Editar Gasto" else "➕ Novo Gasto",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Título
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Descrição (ex: Almoço, Uber)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = GlassBorder,
                        focusedLabelColor = NeonPurple,
                        unfocusedLabelColor = TextSecondary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo Valor
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Valor") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = GlassBorder,
                        focusedLabelColor = NeonPurple,
                        unfocusedLabelColor = TextSecondary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Seleção de Moeda
                Text(text = "Moeda do Gasto:", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Currency.entries.forEach { curr ->
                        val isSelected = curr == selectedCurrency
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NeonPurple else DarkCardSurface)
                                .border(1.dp, if (isSelected) NeonMint else GlassBorder, RoundedCornerShape(12.dp))
                                .clickable { selectedCurrency = curr }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${curr.flag} ${curr.code}",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Seleção de Categoria
                Text(text = "Categoria:", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ExpenseCategory.entries.toTypedArray()) { cat ->
                        val isSelected = cat == selectedCategory
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) cat.color.copy(alpha = 0.3f) else DarkCardSurface)
                                .border(1.dp, if (isSelected) cat.color else GlassBorder, RoundedCornerShape(16.dp))
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = getCategoryIcon(cat),
                                contentDescription = null,
                                tint = if (isSelected) cat.color else TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = cat.displayName,
                                color = if (isSelected) TextPrimary else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage!!,
                        color = com.example.financetracker.ui.theme.CrimsonRed,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botões Salvar e Cancelar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancelar", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val parsedAmount = amountText.replace(",", ".").toDoubleOrNull()
                            if (title.isBlank()) {
                                errorMessage = "Preencha a descrição do gasto."
                            } else if (parsedAmount == null || parsedAmount <= 0) {
                                errorMessage = "Digite um valor maior que zero."
                            } else {
                                onSave(title, parsedAmount, selectedCategory, selectedCurrency)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isEditing) "Salvar" else "Adicionar",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
