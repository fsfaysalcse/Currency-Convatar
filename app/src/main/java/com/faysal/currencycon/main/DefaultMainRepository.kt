package com.faysal.currencycon.main

import com.faysal.currencycon.data.CurrencyApi
import com.faysal.currencycon.util.Resource
import okhttp3.ResponseBody
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: CurrencyApi
) : MainRepository {

    override suspend fun getCurrenciesList(): Resource<ResponseBody> {
        return try {
            val response = api.getCurrenciesList()
            val result = response.body()
            if (result !=null && response.isSuccessful){
                Resource.Success(result)
            }else{
                Resource.Error(response.message())
            }
        } catch (e : Exception){
            Resource.Error(e.message ?: "Something Wrong ! Please try again")
        }
    }

    override suspend fun getCurrencyRates(): Resource<ResponseBody> {
        return try {
            val response = api.getCurrencyRates()
            val result = response.body()
            if (result !=null && response.isSuccessful){
                Resource.Success(result)
            }else{
                Resource.Error(response.message())
            }
        } catch (e : Exception){
            Resource.Error(e.message ?: "Something Wrong ! Please try again")
        }
    }
}