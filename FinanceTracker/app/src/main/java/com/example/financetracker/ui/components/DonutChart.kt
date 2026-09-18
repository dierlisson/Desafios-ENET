package com.example.financetracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.domain.model.CategorySummary
import com.example.financetracker.domain.model.Currency
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.TextSecondary

@Composable
fun DonutChart(
    categorySummaries: List<CategorySummary>,
    totalSpent: Double,
    selectedCurrency: Currency,
    modifier: Modifier = Modifier,
    chartSize: Dp = 160.dp,
    strokeWidth: Dp = 20.dp
) {
    Box(
        modifier = modifier.size(chartSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            var startAngle = -90f
            val total = categorySummaries.sumOf { it.totalAmount }

            if (total == 0.0 || categorySummaries.isEmpty()) {
                // Desenhar anel cinza vazio
                drawArc(
                    color = TextSecondary.copy(alpha = 0.2f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            } else {
                categorySummaries.forEach { summary ->
                    val sweepAngle = (summary.totalAmount / total * 360).toFloat()
                    if (sweepAngle > 0) {
                        drawArc(
                            color = summary.category.color,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle - 2f, // Pequeno gap visual
                            useCenter = false,
                            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                        )
                        startAngle += sweepAngle
                    }
                }
            }
        }

        Text(
            text = selectedCurrency.format(totalSpent),
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
