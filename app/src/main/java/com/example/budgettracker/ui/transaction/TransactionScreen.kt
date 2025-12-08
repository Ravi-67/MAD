package com.example.budgettracker.ui.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TransactionScreen(vm: TransactionViewModel) {

    var amount by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            label = { Text("Description") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val amt = amount.toDoubleOrNull() ?: 0.0
            vm.add(amt, desc)
            amount = ""
            desc = ""
        }) {
            Text("Add Transaction")
        }

        Spacer(modifier = Modifier.height(16.dp))

        val transactions by vm.transactions.collectAsState()

        transactions.forEach {
            Text("${it.amount} - ${it.description}")
        }
    }
}
