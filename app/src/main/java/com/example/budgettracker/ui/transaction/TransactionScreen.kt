package com.example.budgettracker.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgettracker.data.local.Transaction
import com.example.budgettracker.ui.theme.BudgettrackerTheme

@Composable
fun TransactionScreen(vm: TransactionViewModel) {
    val transactions by vm.transactions.collectAsState()

    TransactionContent(
        transactions = transactions,
        isExpense = vm.isExpense,
        onTypeChange = { 
            vm.isExpense = it
            vm.updateDefaults()
        },
        amount = vm.amount,
        onAmountChange = { vm.amount = it },
        selectedCategory = vm.selectedCategory,
        selectedSubCategory = vm.selectedSubCategory,
        categories = if (vm.isExpense) vm.expenseCategories else vm.incomeCategories,
        onCategorySelect = { 
            vm.selectedCategory = it
            vm.selectedSubCategory = (if (vm.isExpense) vm.expenseCategories else vm.incomeCategories)[it]?.first() ?: ""
        },
        onSubCategorySelect = { vm.selectedSubCategory = it },
        customCategory = vm.customCategory,
        customSubCategory = vm.customSubCategory,
        isAddingCustom = vm.isAddingCustom,
        onCustomCategoryChange = { vm.customCategory = it },
        onCustomSubCategoryChange = { vm.customSubCategory = it },
        onToggleCustom = { vm.isAddingCustom = it },
        onAdd = { vm.add() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionContent(
    transactions: List<Transaction>,
    isExpense: Boolean,
    onTypeChange: (Boolean) -> Unit,
    amount: String,
    onAmountChange: (String) -> Unit,
    selectedCategory: String,
    selectedSubCategory: String,
    categories: Map<String, List<String>>,
    onCategorySelect: (String) -> Unit,
    onSubCategorySelect: (String) -> Unit,
    customCategory: String,
    customSubCategory: String,
    isAddingCustom: Boolean,
    onCustomCategoryChange: (String) -> Unit,
    onCustomSubCategoryChange: (String) -> Unit,
    onToggleCustom: (Boolean) -> Unit,
    onAdd: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Transaction") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val selectedIndex = if (isExpense) 1 else 0
            val contentColor = if (isExpense) MaterialTheme.colorScheme.error else Color(0xFF2E7D32)

            TabRow(
                selectedTabIndex = selectedIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = contentColor,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                        color = contentColor
                    )
                }
            ) {
                Tab(
                    selected = !isExpense,
                    onClick = { onTypeChange(false) },
                    text = { Text("Income") },
                    selectedContentColor = Color(0xFF2E7D32)
                )
                Tab(
                    selected = isExpense,
                    onClick = { onTypeChange(true) },
                    text = { Text("Expense") },
                    selectedContentColor = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Simplified Amount Input
            OutlinedTextField(
                value = amount,
                onValueChange = onAmountChange,
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                prefix = { Text("$ ") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!isAddingCustom) {
                // Category Dropdown
                CategoryDropdown(
                    label = "Category",
                    selected = selectedCategory,
                    options = categories.keys.toList(),
                    onSelect = onCategorySelect
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Sub-Category Dropdown
                CategoryDropdown(
                    label = "Sub-Category",
                    selected = selectedSubCategory,
                    options = categories[selectedCategory] ?: emptyList(),
                    onSelect = onSubCategorySelect
                )

                TextButton(
                    onClick = { onToggleCustom(true) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Add Custom Category")
                }
            } else {
                OutlinedTextField(
                    value = customCategory,
                    onValueChange = onCustomCategoryChange,
                    label = { Text("Custom Category Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = customSubCategory,
                    onValueChange = onCustomSubCategoryChange,
                    label = { Text("Custom Sub-Category Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                TextButton(
                    onClick = { onToggleCustom(false) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Use Presets")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onAdd,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isExpense) MaterialTheme.colorScheme.error else Color(0xFF4CAF50)
                )
            ) {
                Text("Save Transaction")
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(transactions.reversed()) { tx ->
                    TransactionItem(tx)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    label: String,
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun TransactionItem(tx: Transaction) {
    val isExpense = tx.amount < 0
    val amountColor = if (isExpense) MaterialTheme.colorScheme.error else Color(0xFF4CAF50)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(amountColor.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isExpense) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = amountColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tx.tag,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = java.util.Date(tx.timestamp).toString().take(10),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            
            Text(
                text = "${if (isExpense) "" else "+"}${tx.amount}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionScreenPreview() {
    BudgettrackerTheme {
        TransactionContent(
            transactions = listOf(
                Transaction(id = 1, title = "Food: Groceries", amount = -50.0, description = "", tag = "Food: Groceries", timestamp = System.currentTimeMillis()),
                Transaction(id = 2, title = "Salary: Full-time", amount = 2000.0, description = "", tag = "Salary: Full-time", timestamp = System.currentTimeMillis() - 86400000),
                Transaction(id = 3, title = "Custom Internet: Fiber", amount = -30.0, description = "", tag = "Custom Internet: Fiber", timestamp = System.currentTimeMillis() - 172800000)
            ),
            isExpense = true,
            onTypeChange = {},
            amount = "120.50",
            onAmountChange = {},
            selectedCategory = "Food",
            selectedSubCategory = "Dining Out",
            categories = mapOf(
                "Food" to listOf("Groceries", "Dining Out", "Snacks"),
                "Transportation" to listOf("Fuel", "Public Transport")
            ),
            onCategorySelect = {},
            onSubCategorySelect = {},
            customCategory = "Internet",
            customSubCategory = "Fiber",
            isAddingCustom = false,
            onCustomCategoryChange = {},
            onCustomSubCategoryChange = {},
            onToggleCustom = {},
            onAdd = {}
        )
    }
}
