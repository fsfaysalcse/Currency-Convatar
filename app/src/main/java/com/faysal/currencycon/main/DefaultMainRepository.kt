package com.faysal.currencycon.main

import com.faysal.currencycon.data.CurrencyApi
import com.faysal.currencycon.data.models.CurrenciesListResponse
import com.faysal.currencycon.util.Resource
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api : CurrencyApi
) : MainRepository{

    override suspend fun getCurrenciesList(): Resource<CurrenciesListResponse> {
        return try {
            val response = api.getCurrenciesList()
            val result = response.body()
            if (result !=null && result.success){
                Resource.Success(result)
            }else{
                Resource.Error(response.message())
            }
        } catch (e : Exception){
            Resource.Error(e.message ?: "Something Wrong ! Please try again")
        }
    }
}