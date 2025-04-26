package com.example.mymoneynotes

import java.util.Date
import java.util.UUID

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val type: TransactionType,
    val amount: Double,
    var category: String,
    val date: Date = Date(),
    val note: String = ""
)

enum class TransactionType {
    INCOME,
    EXPENSE
}