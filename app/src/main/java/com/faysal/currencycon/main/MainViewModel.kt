package com.faysal.currencycon.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faysal.currencycon.util.DispatcherProvider
import com.faysal.currencycon.util.Resource
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
): ViewModel() {

    sealed class CurrencyEvent{
        class Success(val currencyList: List<String>) : CurrencyEvent()
        class Failure(val errorMessage : String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _currencies  = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val currencies : StateFlow<CurrencyEvent> = _currencies

    fun getCurrenciesList(){

        viewModelScope.launch(dispatchers.io) {
            when (val response = repository.getCurrenciesList()){
                is Resource.Error -> _currencies.value = CurrencyEvent.Failure(response.message!!)
                is Resource.Success -> {
                    val data = response.data!!.string()
                    val listOfCurrency = getJsonObjectToArrayList(data)
                   _currencies.value = CurrencyEvent.Success(listOfCurrency)
                }

            }
        }
    }

    fun getJsonObjectToArrayList(json : String) : List<String>{
        val list : MutableList<String> = mutableListOf()
        try {
            val jsonObj = JSONObject(json).getJSONObject("currencies")
            val element: JsonElement = JsonParser.parseString(jsonObj.toString())
            val obj = element.asJsonObject //since you know it's a JsonObject
            val entries = obj.entrySet() //will return members of your object
            for ((key) in entries) {
                list.add(key.toString())
            }
            return list
        }catch (e : Exception){
           return list
        }
    }

}