package com.example.budgettracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.budgettracker.data.local.User
import com.example.budgettracker.ui.theme.BudgettrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: User,
    balance: Double,
    spendingByTag: Map<String, Double>,
    onLogout: () -> Unit,
    onGoToTransactions: () -> Unit,
    onGoToCurrency: () -> Unit,
    onGoToSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Welcome back,",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = user.email.substringBefore("@"),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onGoToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Text(
                            text = if (balance < 0) "Total Debt" else "Total Balance",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$%.2f".format(kotlin.math.abs(balance)),
                            style = MaterialTheme.typography.displayMedium,
                            color = if (balance < 0) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
                    )
                }
            }

            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ActionCard(
                    title = "Transactions",
                    icon = Icons.AutoMirrored.Filled.List,
                    onClick = onGoToTransactions,
                    modifier = Modifier.weight(1f)
                )
                ActionCard(
                    title = "Currency",
                    icon = Icons.Default.AttachMoney,
                    onClick = onGoToCurrency,
                    modifier = Modifier.weight(1f)
                )
            }

            // Spending Visualization
            if (spendingByTag.isNotEmpty()) {
                Text(
                    text = "Spending Breakdown",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )

                SpendingPieChart(spendingByTag)
            }
        }
    }
}

@Composable
fun SpendingPieChart(data: Map<String, Double>) {
    val total = data.values.sum()
    val colors = listOf(
        Color(0xFFEF5350), Color(0xFFAB47BC), Color(0xFF42A5F5),
        Color(0xFF26A69A), Color(0xFFD4E157), Color(0xFFFFA726),
        Color(0xFF8D6E63), Color(0xFF78909C)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Canvas(modifier = Modifier.size(180.dp)) {
                    var startAngle = -90f
                    data.keys.forEachIndexed { index, key ->
                        val amount = data[key] ?: 0.0
                        val sweepAngle = (amount.toFloat() / total.toFloat()) * 360f
                        val color = colors[index % colors.size]

                        drawArc(
                            color = color,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = true
                        )
                        startAngle += sweepAngle
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Legend
            data.keys.forEachIndexed { index, key ->
                val amount = data[key] ?: 0.0
                val percent = (amount / total) * 100
                val color = colors[index % colors.size]

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(color, androidx.compose.foundation.shape.CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = key, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                    Text(
                        text = "$%.2f (%.1f%%)".format(amount, percent),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BudgettrackerTheme {
        HomeScreen(
            user = User(firebaseUid = "1", email = "test@example.com"),
            balance = 1250.50,
            spendingByTag = mapOf("Food" to 200.0, "Rent" to 800.0, "Entertainment" to 150.0),
            onLogout = {},
            onGoToTransactions = {},
            onGoToCurrency = {},
            onGoToSettings = {}
        )
    }
}
