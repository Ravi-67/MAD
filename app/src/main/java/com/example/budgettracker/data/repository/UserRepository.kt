package com.example.budgettracker.data.repository

import com.example.budgettracker.data.local.User
import com.example.budgettracker.data.local.UserDao

class UserRepository(private val userDao: UserDao) {

    suspend fun saveFirebaseUser(uid: String, email: String) {
        userDao.insertUser(User(firebaseUid = uid, email = email))
    }

    suspend fun getLocalUser(uid: String): User? {
        return userDao.getUserByUid(uid)
    }
}

