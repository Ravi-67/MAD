package com.example.budgettracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Transaction::class, CurrencyRate::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun currencyDao(): CurrencyRateDao
}
