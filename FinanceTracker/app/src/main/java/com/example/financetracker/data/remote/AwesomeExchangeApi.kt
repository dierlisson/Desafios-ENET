package com.example.financetracker.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class ExchangeRateDto(
    @SerializedName("code") val code: String,
    @SerializedName("codein") val codein: String,
    @SerializedName("name") val name: String,
    @SerializedName("bid") val bid: String,
    @SerializedName("ask") val ask: String
)

interface AwesomeExchangeApi {

    @GET("json/last/USD-BRL,EUR-BRL,GBP-BRL")
    suspend fun getExchangeRates(): Map<String, ExchangeRateDto>

    companion object {
        private const val BASE_URL = "https://economia.awesomeapi.com.br/"

        fun create(): AwesomeExchangeApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AwesomeExchangeApi::class.java)
        }
    }
}
