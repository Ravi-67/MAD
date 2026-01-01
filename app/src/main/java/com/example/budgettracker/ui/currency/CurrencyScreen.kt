package com.example.budgettracker.ui.currency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgettracker.ui.theme.BudgettrackerTheme

@Composable
fun CurrencyScreen(vm: CurrencyViewModel) {
    val rate by vm.rate.collectAsState()
    
    LaunchedEffect(Unit) {
        if (vm.currencyCodes.isEmpty()) {
            vm.load("USD")
        }
    }

    CurrencyContent(
        amount = vm.amount,
        onAmountChange = { 
            vm.amount = it
            vm.calculateConversion()
        },
        fromCurrency = vm.fromCurrency,
        toCurrency = vm.toCurrency,
        currencyCodes = vm.currencyCodes,
        onFromCurrencyChange = {
            vm.fromCurrency = it
            vm.calculateConversion()
        },
        onToCurrencyChange = {
            vm.toCurrency = it
            vm.calculateConversion()
        },
        onSwap = { vm.swapCurrencies() },
        convertedAmount = vm.convertedAmount,
        lastUpdated = rate?.timestamp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyContent(
    amount: String,
    onAmountChange: (String) -> Unit,
    fromCurrency: String,
    toCurrency: String,
    currencyCodes: List<String>,
    onFromCurrencyChange: (String) -> Unit,
    onToCurrencyChange: (String) -> Unit,
    onSwap: () -> Unit,
    convertedAmount: Double,
    lastUpdated: Long?
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Currency Converter") },
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
            
            // Amount Input
            OutlinedTextField(
                value = amount,
                onValueChange = { 
                    if (it.all { char -> char.isDigit() || char == '.' }) {
                        onAmountChange(it)
                    }
                },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Currency Selectors Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // From Currency
                CurrencyDropdown(
                    selected = fromCurrency,
                    options = currencyCodes,
                    onSelect = onFromCurrencyChange,
                    modifier = Modifier.weight(1f)
                )

                // Swap Button
                IconButton(
                    onClick = onSwap,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Swap Currencies",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                // To Currency
                CurrencyDropdown(
                    selected = toCurrency,
                    options = currencyCodes,
                    onSelect = onToCurrencyChange,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Result Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Converted Amount",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = String.format("%.2f %s", convertedAmount, toCurrency),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = "Last updated: ${if (lastUpdated != null) java.util.Date(lastUpdated).toString() else "Never"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDropdown(
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(currency) },
                    onClick = {
                        onSelect(currency)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyScreenPreview() {
    BudgettrackerTheme {
        CurrencyContent(
            amount = "100",
            onAmountChange = {},
            fromCurrency = "USD",
            toCurrency = "EUR",
            currencyCodes = listOf("USD", "EUR", "GBP", "JPY", "INR"),
            onFromCurrencyChange = {},
            onToCurrencyChange = {},
            onSwap = {},
            convertedAmount = 85.50,
            lastUpdated = System.currentTimeMillis()
        )
    }
}
