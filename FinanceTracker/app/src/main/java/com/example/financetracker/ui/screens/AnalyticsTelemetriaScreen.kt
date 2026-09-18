package com.example.financetracker.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.domain.model.Currency
import com.example.financetracker.domain.model.ExpenseCategory
import com.example.financetracker.ui.components.GlassCard
import com.example.financetracker.ui.components.getCategoryIcon
import com.example.financetracker.ui.theme.CrimsonRed
import com.example.financetracker.ui.theme.DarkBackground
import com.example.financetracker.ui.theme.DarkCardSurface
import com.example.financetracker.ui.theme.GlassBorder
import com.example.financetracker.ui.theme.NeonCyan
import com.example.financetracker.ui.theme.NeonMint
import com.example.financetracker.ui.theme.NeonPurple
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.TextSecondary
import com.example.financetracker.ui.viewmodel.ExpenseViewModel

@Composable
fun AnalyticsTelemetriaScreen(
    viewModel: ExpenseViewModel
) {
    val selectedCurrency by viewModel.selectedCurrency.observeAsState(Currency.BRL)
    val monthlySummary by viewModel.monthlySummary.observeAsState()

    var selectedTimeRange by remember { mutableStateOf("30D") }
    val timeRanges = listOf("7D", "30D", "3M", "1A", "ANO FISCAL")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "TELEMETRIA & PROJEÇÕES",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "● SYSTEM ONLINE • LIVE 120Hz",
                    color = NeonMint,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Seletor Temporal (Time Range Chips)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(timeRanges) { range ->
                val isSelected = range == selectedTimeRange
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) NeonMint.copy(alpha = 0.2f) else DarkCardSurface)
                        .border(1.dp, if (isSelected) NeonMint else GlassBorder, RoundedCornerShape(12.dp))
                        .clickable { selectedTimeRange = range }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = range,
                        color = if (isSelected) NeonMint else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card Principal: Fluxo Preditivo Neon (Spline Chart)
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📈 Fluxo Preditivo Neon",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonCyan.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Previsão: R$ 4.280 / Meta: R$ 5.000",
                                    color = NeonCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Spline Line Chart Canvas
                        SplineChartCanvas(modifier = Modifier.fillMaxWidth().height(120.dp))
                    }
                }
            }

            // Grid 2x2: Métricas Chave
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 1. Burn Rate Diário
                        GlassCard(modifier = Modifier.weight(1f)) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Burn Rate Diário", color = TextSecondary, fontSize = 11.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = selectedCurrency.format(monthlySummary?.dailyAverage ?: 142.50),
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = NeonMint, modifier = Modifier.size(12.dp))
                                    Text("-12% vs semana passada", color = NeonMint, fontSize = 10.sp)
                                }
                            }
                        }

                        // 2. Índice de Poupança
                        GlassCard(modifier = Modifier.weight(1f)) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Índice de Poupança", color = TextSecondary, fontSize = 11.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("34.8%", color = NeonCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { 0.348f },
                                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                                    color = NeonCyan,
                                    trackColor = DarkCardSurface
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 3. Maior Ofensor
                        GlassCard(modifier = Modifier.weight(1f)) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Maior Ofensor", color = TextSecondary, fontSize = 11.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = monthlySummary?.topCategory?.displayName ?: "Alimentação",
                                    color = CrimsonRed,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("42% do teto mensal", color = TextSecondary, fontSize = 10.sp)
                            }
                        }

                        // 4. Eficiência Financeira
                        GlassCard(modifier = Modifier.weight(1f)) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Eficiência Financeira", color = TextSecondary, fontSize = 11.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Score A+", color = NeonMint, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(NeonMint)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Excelente controle", color = TextSecondary, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            // Orçamentos por Categoria (Budget Envelopes)
            item {
                Text(
                    text = "🎯 Orçamentos por Categoria (Envelopes)",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(ExpenseCategory.entries.toTypedArray()) { cat ->
                val summary = monthlySummary?.categorySummaries?.find { it.category == cat }
                val spent = summary?.totalAmount ?: 150.0
                val budgetLimit = 1000.0
                val ratio = (spent / budgetLimit).toFloat().coerceIn(0f, 1f)

                val barColor = when {
                    ratio >= 0.85f -> CrimsonRed
                    ratio >= 0.60f -> Color(0xFFFFB300)
                    else -> NeonMint
                }

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(cat.color.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getCategoryIcon(cat),
                                contentDescription = null,
                                tint = cat.color,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(cat.displayName, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    "${selectedCurrency.format(spent)} / ${selectedCurrency.format(budgetLimit)}",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { ratio },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = barColor,
                                trackColor = DarkCardSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SplineChartCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            moveTo(0f, height * 0.7f)
            cubicTo(
                width * 0.25f, height * 0.9f,
                width * 0.5f, height * 0.3f,
                width * 0.75f, height * 0.5f
            )
            cubicTo(
                width * 0.85f, height * 0.6f,
                width * 0.95f, height * 0.2f,
                width, height * 0.25f
            )
        }

        // Draw Line
        drawPath(
            path = path,
            color = NeonMint,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw Fill Gradient
        val fillPath = Path().apply {
            addPath(path)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(NeonMint.copy(alpha = 0.3f), Color.Transparent)
            )
        )

        // Draw Meta Line (Dotted Threshold)
        drawLine(
            color = NeonCyan.copy(alpha = 0.5f),
            start = androidx.compose.ui.geometry.Offset(0f, height * 0.35f),
            end = androidx.compose.ui.geometry.Offset(width, height * 0.35f),
            strokeWidth = 1.dp.toPx()
        )
    }
}
