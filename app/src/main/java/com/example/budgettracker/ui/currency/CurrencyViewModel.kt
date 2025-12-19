package com.example.budgettracker.ui.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.budgettracker.data.local.CurrencyRate
import com.example.budgettracker.data.remote.CurrencyRateResponse
import com.example.budgettracker.data.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class CurrencyViewModel(
    private val repo: CurrencyRepository
) : ViewModel() {

    private val _rate = MutableStateFlow<CurrencyRate?>(null)
    val rate: StateFlow<CurrencyRate?> = _rate

    // UI State
    var amount by mutableStateOf("1")
    var fromCurrency by mutableStateOf("USD")
    var toCurrency by mutableStateOf("EUR")
    var convertedAmount by mutableStateOf(0.0)

    // Parsed rates
    private var ratesMap: Map<String, Double> = emptyMap()
    
    // List of available currencies for dropdown
    var currencyCodes by mutableStateOf<List<String>>(emptyList())

    fun load(base: String) = viewModelScope.launch {
        // Try to load cached or fetch new
        _rate.value = repo.getCachedRates(base)
        parseRates(_rate.value)
        
        repo.refreshRates(base)
        _rate.value = repo.getCachedRates(base)
        parseRates(_rate.value)
        
        calculateConversion()
    }

    private fun parseRates(rate: CurrencyRate?) {
        if (rate == null) return
        try {
            val response = Json.decodeFromString<CurrencyRateResponse>(rate.ratesJson)
            ratesMap = response.conversion_rates
            currencyCodes = ratesMap.keys.sorted()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun calculateConversion() {
        val fromRate = ratesMap[fromCurrency] ?: 1.0
        val toRate = ratesMap[toCurrency] ?: 1.0
        val inputAmount = amount.toDoubleOrNull() ?: 0.0

        // Convert base -> target
        // Since base is always what we fetched (e.g. USD), the rates are 1 USD = x EUR
        // If we want to convert arbitrary From -> To, we might need to normalize through base if the API base is rigid,
        // but typically free APIs give rates relative to a base.
        // Assuming 'ratesMap' contains rates relative to 'base' (e.g. USD).
        // 1 Base = x From
        // 1 Base = y To
        // To get From -> To:  Amount * (To/From)
        
        if (ratesMap.isNotEmpty()) {
             convertedAmount = inputAmount * (toRate / fromRate)
        }
    }
    
    fun swapCurrencies() {
        val temp = fromCurrency
        fromCurrency = toCurrency
        toCurrency = temp
        calculateConversion()
    }
}
