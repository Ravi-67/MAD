package com.example.budgettracker.ui

import androidx.compose.runtime.*
import com.example.budgettracker.data.local.User
import com.example.budgettracker.ui.auth.AuthScreen
import com.example.budgettracker.ui.home.HomeScreen

@Composable
fun AppRoot() {
    var loggedInUser by remember { mutableStateOf<User?>(null) }

    if (loggedInUser == null) {
        AuthScreen(onAuthSuccess = { user -> loggedInUser = user })
    } else {
        HomeScreen(user = loggedInUser!!, onLogout = { loggedInUser = null })
    }
}
