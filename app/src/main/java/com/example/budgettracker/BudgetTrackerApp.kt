package com.example.budgettracker

import android.app.Application
import com.google.firebase.FirebaseApp
import android.util.Log

class BudgetTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Log.d("BudgetTrackerApp", "Firebase initialized correctly")
    }
}
