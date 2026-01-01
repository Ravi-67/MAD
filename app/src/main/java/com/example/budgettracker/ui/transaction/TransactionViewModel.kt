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

    // Categories and Subcategories
    val incomeCategories = mapOf(
        "Salary" to listOf("Full-time", "Freelance", "Bonus"),
        "Investments" to listOf("Dividends", "Stock Sale", "Crypto"),
        "Other Income" to listOf("Gift", "Rental", "Refund")
    )

    val expenseCategories = mapOf(
        "Food" to listOf("Groceries", "Dining Out", "Snacks"),
        "Transportation" to listOf("Fuel", "Public Transport", "Uber/Taxi"),
        "Housing" to listOf("Rent", "Utilities", "Maintenance"),
        "Entertainment" to listOf("Movies", "Games", "Subscription"),
        "Health" to listOf("Pharmacy", "Doctor", "Gym")
    )

    // UI State
    var amount by mutableStateOf("")
    var selectedCategory by mutableStateOf("")
    var selectedSubCategory by mutableStateOf("")
    var isExpense by mutableStateOf(true)

    // Custom Category Management
    var customCategory by mutableStateOf("")
    var customSubCategory by mutableStateOf("")
    var isAddingCustom by mutableStateOf(false)

    init {
        // Initialize default selections
        updateDefaults()
    }

    fun updateDefaults() {
        val categories = if (isExpense) expenseCategories else incomeCategories
        selectedCategory = categories.keys.first()
        selectedSubCategory = categories[selectedCategory]?.first() ?: ""
    }

    val spendingByTag = transactions.map { list ->
        list.filter { it.amount < 0 }
            .groupBy { it.tag.substringBefore(":") } // Group by main category
            .mapValues { (_, txs) ->
                kotlin.math.abs(txs.sumOf { it.amount })
            }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyMap()
    )

    fun add() {
        val amt = amount.toDoubleOrNull() ?: return
        if (amt <= 0) return
        
        val finalAmount = if (isExpense) -amt else amt
        val tag = if (isAddingCustom && customCategory.isNotBlank()) {
            if (customSubCategory.isNotBlank()) {
                "Custom $customCategory: $customSubCategory"
            } else {
                "Custom: $customCategory"
            }
        } else {
            "$selectedCategory: $selectedSubCategory"
        }
        
        viewModelScope.launch {
            repo.addTransaction(Transaction(
                title = tag,
                amount = finalAmount, 
                description = "", 
                tag = tag
            ))
            // Reset input
            amount = ""
            customCategory = ""
            customSubCategory = ""
            isAddingCustom = false
            updateDefaults()
        }
    }
}
