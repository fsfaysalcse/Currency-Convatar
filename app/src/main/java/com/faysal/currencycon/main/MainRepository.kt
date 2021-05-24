package com.faysal.currencycon.main

import com.faysal.currencycon.data.models.CurrenciesListResponse
import com.faysal.currencycon.util.Resource


interface MainRepository {
    suspend fun getCurrenciesList() : Resource<CurrenciesListResponse>
}