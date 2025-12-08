package com.example.budgettracker.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.budgettracker.data.local.AppDatabase
import com.example.budgettracker.data.repository.CurrencyRepository
import com.example.budgettracker.data.repository.TransactionRepository
import com.example.budgettracker.ui.auth.AuthScreen
import com.example.budgettracker.ui.auth.AuthViewModel
import com.example.budgettracker.ui.currency.CurrencyScreen
import com.example.budgettracker.ui.currency.CurrencyViewModel
import com.example.budgettracker.ui.home.HomeScreen
import com.example.budgettracker.ui.splash.SplashScreen
import com.example.budgettracker.ui.transaction.TransactionScreen
import com.example.budgettracker.ui.transaction.TransactionViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import androidx.room.Room
import com.example.budgettracker.data.remote.CurrencyApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

@Composable
fun AppRoot() {

    val context = LocalContext.current
    val authVM: AuthViewModel = viewModel()
    val auth = FirebaseAuth.getInstance()
    var loggedInUser by remember { mutableStateOf<com.example.budgettracker.data.local.User?>(null) }
    var showSplash by remember { mutableStateOf(true) }

    // Build Room DB only once
    val db = remember {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "budget_db"
        ).build()
    }

    // Retrofit for currency API
    val api = remember {
        Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(CurrencyApi::class.java)
    }

    // ViewModels for Transactions & Currency
    val txVM = remember {
        TransactionViewModel(TransactionRepository(db.transactionDao()))
    }

    val currencyVM = remember {
        CurrencyViewModel(CurrencyRepository(api, db.currencyDao()))
    }

    val nav = rememberNavController()

    // SPLASH + AUTH
    LaunchedEffect(Unit) {
        delay(300)
        showSplash = false

        auth.currentUser?.let { firebaseUser ->
            loggedInUser = authVM.getLocalUser(firebaseUser.uid)
        }
    }

    AnimatedVisibility(
        visible = showSplash,
        exit = fadeOut()
    ) {
        SplashScreen()
    }

    if (!showSplash) {
        if (loggedInUser == null) {
            AuthScreen(onAuthSuccess = { loggedInUser = it })
            return
        }
    }

    // NAVIGATION HOST AFTER LOGIN
    if (loggedInUser != null) {
        NavHost(
            navController = nav,
            startDestination = "home"
        ) {

            composable("home") {
                HomeScreen(
                    user = loggedInUser!!,
                    onLogout = {
                        authVM.logout()
                        loggedInUser = null
                    },
                    onGoToTransactions = { nav.navigate("transactions") },
                    onGoToCurrency = { nav.navigate("currency") }
                )
            }

            composable("transactions") {
                TransactionScreen(vm = txVM)
            }

            composable("currency") {
                CurrencyScreen(vm = currencyVM)
            }
        }
    }
}
