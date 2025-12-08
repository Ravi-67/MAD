package com.example.budgettracker.data.repository

import com.example.budgettracker.data.local.CurrencyRate
import com.example.budgettracker.data.local.CurrencyRateDao
import com.example.budgettracker.data.remote.CurrencyApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CurrencyRepository(
    private val api: CurrencyApi,
    private val dao: CurrencyRateDao
) {

    suspend fun refreshRates(base: String) {
        val response = api.getRates(base)
        val entity = CurrencyRate(
            base = base,
            timestamp = System.currentTimeMillis(),
            ratesJson = Json.encodeToString(response)
        )
        dao.insert(entity)
    }

    suspend fun getCachedRates(base: String): CurrencyRate? {
        return dao.getRate(base)
    }
}
