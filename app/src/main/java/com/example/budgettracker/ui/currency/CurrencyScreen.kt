package com.example.budgettracker.ui.currency

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CurrencyScreen(vm: CurrencyViewModel) {

    val rate by vm.rate.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        Button(onClick = { vm.load("USD") }) {
            Text("Refresh Rates")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Latest: ${rate?.timestamp ?: "No data"}")
        Text("Raw JSON: ${rate?.ratesJson ?: "--"}")
    }
}
