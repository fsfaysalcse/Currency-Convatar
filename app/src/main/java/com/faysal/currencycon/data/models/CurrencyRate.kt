package com.faysal.currencycon.data.models

data class CurrencyRate(
    val fromCurrencyName : String,
    val toCurrencyName : String,
    val rates : Double
)
