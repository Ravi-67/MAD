package com.example.budgettracker.ui.transaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.local.Transaction
import com.example.budgettracker.data.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val repo: TransactionRepository
) : ViewModel() {

    val transactions = repo.getTransactions()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val balance = transactions.map { list ->
        list.sumOf { it.amount }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0.0
    )

    // UI State
    var amount by mutableStateOf("")
    var description by mutableStateOf("")
    var isExpense by mutableStateOf(true) // Default to Expense

    fun add() {
        val amt = amount.toDoubleOrNull() ?: return
        if (amt <= 0) return
        
        // Expense = absolute negative, Income = absolute positive
        val finalAmount = if (isExpense) -amt else amt
        
        viewModelScope.launch {
            repo.addTransaction(Transaction(amount = finalAmount, description = description))
            // Reset input
            amount = ""
            description = ""
        }
    }
}
