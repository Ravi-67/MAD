package com.example.budgettracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rate: CurrencyRate)

    @Query("SELECT * FROM currency_rates WHERE base=:base LIMIT 1")
    suspend fun getRate(base: String): CurrencyRate?
}
