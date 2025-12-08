package com.example.budgettracker.ui.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.local.CurrencyRate
import com.example.budgettracker.data.repository.CurrencyRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CurrencyViewModel(
    private val repo: CurrencyRepository
) : ViewModel() {

    private val _rate = MutableStateFlow<CurrencyRate?>(null)
    val rate: StateFlow<CurrencyRate?> = _rate

    fun load(base: String) = viewModelScope.launch {
        _rate.value = repo.getCachedRates(base)
        repo.refreshRates(base)
        _rate.value = repo.getCachedRates(base)
    }
}
