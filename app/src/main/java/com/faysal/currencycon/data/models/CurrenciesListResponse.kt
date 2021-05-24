package com.faysal.currencycon.data.models

data class CurrenciesListResponse(
    val currencies: Currencies,
    val privacy: String,
    val success: Boolean,
    val terms: String
)