package com.example.financetracker.domain.model

import androidx.compose.ui.graphics.Color
import java.text.NumberFormat
import java.util.Locale

enum class Currency(val code: String, val symbol: String, val flag: String) {
    BRL("BRL", "R$", "🇧🇷"),
    USD("USD", "$", "🇺🇸"),
    EUR("EUR", "€", "🇪🇺"),
    GBP("GBP", "£", "🇬🇧");

    fun format(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(
            when (this) {
                BRL -> Locale("pt", "BR")
                USD -> Locale.US
                EUR -> Locale.GERMANY
                GBP -> Locale.UK
            }
        )
        return format.format(amount)
    }

    companion object {
        fun fromCode(code: String): Currency = entries.find { it.code == code } ?: BRL
    }
}

enum class ExpenseCategory(
    val displayName: String,
    val color: Color,
    val iconName: String
) {
    ALIMENTACAO("Alimentação", Color(0xFFFF6D00), "Restaurant"),
    TRANSPORTE("Transporte", Color(0xFF2979FF), "DirectionsBus"),
    LAZER("Lazer & Entretenimento", Color(0xFFAA00FF), "Movie"),
    SAUDE("Saúde & Bem-estar", Color(0xFF00E676), "MedicalServices"),
    MORADIA("Moradia & Contas", Color(0xFFFF1744), "Home"),
    OUTROS("Outros", Color(0xFF00E5FF), "Category");

    companion object {
        fun fromDisplayName(name: String): ExpenseCategory =
            entries.find { it.displayName.equals(name, ignoreCase = true) } ?: OUTROS
    }
}

data class Expense(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: ExpenseCategory,
    val dateMillis: Long,
    val currency: Currency = Currency.BRL
)

data class CurrencyRates(
    val usdBrl: Double = 5.50,
    val eurBrl: Double = 6.00,
    val gbpBrl: Double = 7.10,
    val lastUpdatedMillis: Long = System.currentTimeMillis()
) {
    fun convert(amount: Double, from: Currency, to: Currency): Double {
        if (from == to) return amount
        // Convert to BRL first
        val amountInBrl = when (from) {
            Currency.BRL -> amount
            Currency.USD -> amount * usdBrl
            Currency.EUR -> amount * eurBrl
            Currency.GBP -> amount * gbpBrl
        }
        // Convert from BRL to target
        return when (to) {
            Currency.BRL -> amountInBrl
            Currency.USD -> amountInBrl / usdBrl
            Currency.EUR -> amountInBrl / eurBrl
            Currency.GBP -> amountInBrl / gbpBrl
        }
    }
}

data class CategorySummary(
    val category: ExpenseCategory,
    val totalAmount: Double,
    val percentage: Float
)

data class MonthlySummary(
    val totalSpent: Double,
    val dailyAverage: Double,
    val highestExpense: Expense?,
    val topCategory: ExpenseCategory?,
    val categorySummaries: List<CategorySummary>
)
