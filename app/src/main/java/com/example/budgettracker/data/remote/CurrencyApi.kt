package com.example.budgettracker.data.remote

import com.example.budgettracker.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
interface CurrencyApi {

    @GET("v6/${BuildConfig.EXCHANGE_RATE_API_KEY}/latest/{base}")
    suspend fun getRates(
        @Path("base") base: String
    ): CurrencyRateResponse
}
