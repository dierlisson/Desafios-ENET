package com.example.financetracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale("pt", "BR"))
    val updatedTime = timeFormat.format(Date(rates.lastUpdatedMillis))

    GlassCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "💱 Câmbio de Moedas (AwesomeAPI)",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Atualizado às $updatedTime",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
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
            }

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
