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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.financetracker.domain.model.Currency
import com.example.financetracker.domain.model.Expense
import com.example.financetracker.ui.components.getCategoryIcon
import com.example.financetracker.ui.theme.CrimsonRed
import com.example.financetracker.ui.theme.DarkCardSurface
import com.example.financetracker.ui.theme.DarkSurface
import com.example.financetracker.ui.theme.GlassBorder
import com.example.financetracker.ui.theme.NeonCyan
import com.example.financetracker.ui.theme.NeonMint
import com.example.financetracker.ui.theme.NeonPurple
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExpenseDetailDrawerScreen(
    expense: Expense,
    displayCurrency: Currency,
    convertedAmount: Double,
    onDismiss: () -> Unit,
    onEdit: (Expense) -> Unit,
    onDelete: (Expense) -> Unit,
    onRepeat: (Expense) -> Unit
) {
    var copyMessage by remember { mutableStateOf<String?>(null) }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy • HH:mm", Locale("pt", "BR"))
    val formattedDate = dateFormat.format(Date(expense.dateMillis))

    val hash = "TX-${expense.id * 1000 + 9842}-88B-04"

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
                // Header Comprovante
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🧾 COMPROVANTE ANALÍTICO",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonMint.copy(alpha = 0.2f))
                            .border(1.dp, NeonMint, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Aprovado / Liquidado", color = NeonMint, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Destaque Valor
                Text(
                    text = displayCurrency.format(convertedAmount),
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = formattedDate,
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Terminal de Metadados
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkCardSurface)
                        .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        MetadataRow("Estabelecimento", expense.title)
                        MetadataRow("Categoria", expense.category.displayName, catColor = expense.category.color)
                        MetadataRow("Método", "Apple Pay • Cartão Infinite (Final 8841)")

                        // Geolocalização Simulada
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Geolocalização", color = TextSecondary, fontSize = 11.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("São Paulo, SP • (GPS Verification)", color = NeonCyan, fontSize = 11.sp)
                            }
                        }

                        // Hash da transação
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Hash da Transação", color = TextSecondary, fontSize = 10.sp)
                                Text(hash, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            IconButton(
                                onClick = { copyMessage = "Hash copiado!" },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copiar", tint = NeonMint, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                if (copyMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(copyMessage!!, color = NeonMint, fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Ações do Rodapé
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onRepeat(expense) },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Repeat, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Repetir", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onEdit(expense) },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkCardSurface),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = NeonMint, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar", color = NeonMint, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { onDelete(expense) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = CrimsonRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Excluir Registro", color = CrimsonRed, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun MetadataRow(label: String, value: String, catColor: androidx.compose.ui.graphics.Color? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextSecondary, fontSize = 11.sp)
        Text(value, color = catColor ?: TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
