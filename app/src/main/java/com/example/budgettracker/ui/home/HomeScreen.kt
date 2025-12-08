package com.example.budgettracker.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.budgettracker.data.local.User

@Composable
fun HomeScreen(
    user: User,
    onLogout: () -> Unit,
    onGoToTransactions: () -> Unit,
    onGoToCurrency: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Welcome, ${user.email}",
                style = MaterialTheme.typography.titleLarge
            )

            // Transactions Button
            Button(
                onClick = onGoToTransactions,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Go to Transactions")
            }

            // Currency Button
            Button(
                onClick = onGoToCurrency,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("View Currency Rates")
            }

            // Logout Button
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Logout")
            }
        }
    }
}
