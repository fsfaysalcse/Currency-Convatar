package com.faysal.currencycon.data

import com.faysal.currencycon.data.models.CurrenciesListResponse
import com.faysal.currencycon.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("/list")
    suspend fun getCurrenciesList(
        @Query("access_key") access_key : String = Constants.API_KEY
    ) : Response<CurrenciesListResponse>

}