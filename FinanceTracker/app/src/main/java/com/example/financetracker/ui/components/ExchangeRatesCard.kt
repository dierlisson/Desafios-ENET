package com.example.financetracker.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.domain.model.CurrencyRates
import com.example.financetracker.ui.theme.NeonCyan
import com.example.financetracker.ui.theme.NeonMint
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExchangeRatesCard(
    rates: CurrencyRates,
    isLoading: Boolean,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
    isCollapsed: Boolean = false,
    onToggleCollapse: (() -> Unit)? = null
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val updatedTime = timeFormat.format(Date(rates.lastUpdatedMillis))

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .then(
                if (onToggleCollapse != null) {
                    Modifier.clickable { onToggleCollapse() }
                } else Modifier
            )
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "💱 Câmbio de Moedas",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (!isCollapsed) {
                        Text(
                            text = "($updatedTime)",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = NeonCyan,
                            strokeWidth = 2.dp
                        )
                    } else {
                        IconButton(
                            onClick = onRefreshClick,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Atualizar Câmbio",
                                tint = NeonCyan
                            )
                        }
                    }

                    if (onToggleCollapse != null) {
                        Icon(
                            imageVector = if (isCollapsed) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            contentDescription = if (isCollapsed) "Expandir Câmbio" else "Colapsar Câmbio",
                            tint = TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = !isCollapsed,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        RateChip(flag = "🇺🇸", pair = "USD / BRL", rate = String.format(Locale.US, "R$ %.2f", rates.usdBrl))
                        RateChip(flag = "🇪🇺", pair = "EUR / BRL", rate = String.format(Locale.US, "R$ %.2f", rates.eurBrl))
                        RateChip(flag = "🇬🇧", pair = "GBP / BRL", rate = String.format(Locale.US, "R$ %.2f", rates.gbpBrl))
                    }
                }
            }

            AnimatedVisibility(
                visible = isCollapsed,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🇺🇸 USD R$ ${String.format(Locale.US, "%.2f", rates.usdBrl)}",
                        color = NeonMint,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("•", color = TextSecondary, fontSize = 10.sp)
                    Text(
                        text = "🇪🇺 EUR R$ ${String.format(Locale.US, "%.2f", rates.eurBrl)}",
                        color = NeonMint,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("•", color = TextSecondary, fontSize = 10.sp)
                    Text(
                        text = "🇬🇧 GBP R$ ${String.format(Locale.US, "%.2f", rates.gbpBrl)}",
                        color = NeonMint,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun RateChip(flag: String, pair: String, rate: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "$flag $pair", color = TextSecondary, fontSize = 11.sp)
        Text(
            text = rate,
            color = NeonMint,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

