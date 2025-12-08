package com.example.budgettracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class User(
    @PrimaryKey val firebaseUid: String,
    val email: String
)