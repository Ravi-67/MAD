package com.example.budgettracker.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgettracker.ui.theme.BudgettrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    var showAbout by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            ListItem(
                headlineContent = { Text("Dark Mode") },
                trailingContent = {
                    Switch(checked = isDarkMode, onCheckedChange = onDarkModeChange)
                }
            )
            ListItem(
                headlineContent = { Text("About BudgetTracker") },
                supportingContent = {
                    if (showAbout) {
                        Text("BudgetTracker is your ultimate companion for tracking and managing your finances.")
                    }
                },
                modifier = Modifier.clickable { showAbout = !showAbout }
            )
            ListItem(
                headlineContent = { Text("Version") },
                supportingContent = { Text("1.0.0") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    BudgettrackerTheme {
        SettingsScreen(
            onBack = {},
            isDarkMode = false,
            onDarkModeChange = {}
        )
    }
}
