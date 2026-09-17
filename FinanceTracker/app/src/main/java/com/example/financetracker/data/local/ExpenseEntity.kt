package com.example.financetracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.financetracker.domain.model.Currency
import com.example.financetracker.domain.model.Expense
import com.example.financetracker.domain.model.ExpenseCategory

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val dateMillis: Long,
    val currency: String = "BRL"
) {
    fun toDomain(): Expense {
        return Expense(
            id = id,
            title = title,
            amount = amount,
            category = ExpenseCategory.fromDisplayName(category),
            dateMillis = dateMillis,
            currency = Currency.fromCode(currency)
        )
    }

    companion object {
        fun fromDomain(expense: Expense): ExpenseEntity {
            return ExpenseEntity(
                id = expense.id,
                title = expense.title,
                amount = expense.amount,
                category = expense.category.displayName,
                dateMillis = expense.dateMillis,
                currency = expense.currency.code
            )
        }
    }
}
