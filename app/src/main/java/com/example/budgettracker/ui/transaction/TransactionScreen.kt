package com.example.budgettracker.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(vm: TransactionViewModel) {

    val transactions by vm.transactions.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Transactions") },
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

            // Type Segmented Button
            // Type Selection (TabRow)
            val selectedIndex = if (vm.isExpense) 1 else 0
            val containerColor = if (vm.isExpense) MaterialTheme.colorScheme.errorContainer else Color(0xFFE8F5E9)
            val contentColor = if (vm.isExpense) MaterialTheme.colorScheme.error else Color(0xFF2E7D32)

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
                    selected = !vm.isExpense,
                    onClick = { vm.isExpense = false },
                    text = { 
                        Text(
                            "Income", 
                            fontWeight = if (!vm.isExpense) FontWeight.Bold else FontWeight.Normal
                        ) 
                    },
                    selectedContentColor = Color(0xFF2E7D32),
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Tab(
                    selected = vm.isExpense,
                    onClick = { vm.isExpense = true },
                    text = { 
                        Text(
                            "Expense",
                            fontWeight = if (vm.isExpense) FontWeight.Bold else FontWeight.Normal
                        ) 
                    },
                    selectedContentColor = MaterialTheme.colorScheme.error,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Inputs
            OutlinedTextField(
                value = vm.amount,
                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) vm.amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = vm.description,
                onValueChange = { vm.description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { vm.add() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (vm.isExpense) MaterialTheme.colorScheme.error else Color(0xFF4CAF50)
                )
            ) {
                Text(if (vm.isExpense) "Add Expense" else "Add Income")
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(transactions.reversed()) { tx ->
                    TransactionItem(tx)
                }
            }
        }
    }
}

@Composable
fun TransactionItem(tx: com.example.budgettracker.data.local.Transaction) {
    val isExpense = tx.amount < 0
    val amountColor = if (isExpense) MaterialTheme.colorScheme.error else Color(0xFF4CAF50)
    val icon = if (isExpense) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = amountColor,
                modifier = Modifier
                    .size(40.dp)
                    .background(amountColor.copy(alpha = 0.1f), CircleShape)
                    .padding(8.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tx.description.ifBlank { "No Description" },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = java.util.Date(tx.timestamp).toString().take(10), // Simple date
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
