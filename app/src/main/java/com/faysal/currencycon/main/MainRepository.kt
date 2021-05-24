package com.faysal.currencycon.main

import com.faysal.currencycon.data.models.CurrenciesListResponse
import com.faysal.currencycon.util.Resource
import okhttp3.ResponseBody


interface MainRepository {
    suspend fun getCurrenciesList() : Resource<ResponseBody>
}