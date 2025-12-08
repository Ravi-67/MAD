package com.example.budgettracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_rates")
data class CurrencyRate(
    @PrimaryKey val base: String,
    val timestamp: Long,
    val ratesJson: String
)
