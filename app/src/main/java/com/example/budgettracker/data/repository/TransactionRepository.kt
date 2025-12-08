package com.example.budgettracker.data.repository

import com.example.budgettracker.data.local.Transaction
import com.example.budgettracker.data.local.TransactionDao
import kotlinx.coroutines.flow.Flow

class TransactionRepository(
    private val dao: TransactionDao
) {
    fun getTransactions(): Flow<List<Transaction>> = dao.getAll()

    suspend fun addTransaction(tx: Transaction) {
        dao.insert(tx)
    }
}
