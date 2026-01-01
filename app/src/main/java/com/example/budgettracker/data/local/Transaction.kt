package com.example.budgettracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val description: String,
    val tag: String = "Others",
    val timestamp: Long = System.currentTimeMillis()
)
