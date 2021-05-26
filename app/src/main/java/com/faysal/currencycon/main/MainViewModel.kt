package com.faysal.currencycon.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faysal.currencycon.util.DispatcherProvider
import com.faysal.currencycon.util.Resource
import com.faysal.currencycon.util.Utility
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
) : ViewModel() {

    // Below this code used for handle list of currency response

    sealed class CurrencyListEvent {
        class Success(val currencyList: List<String>) : CurrencyListEvent()
        class Failure(val errorMessage: String) : CurrencyListEvent()
        object Loading : CurrencyListEvent()
        object Empty : CurrencyListEvent()
    }

    private val _currencies = MutableStateFlow<CurrencyListEvent>(CurrencyListEvent.Empty)
    val currencies: StateFlow<CurrencyListEvent> = _currencies

    fun getCurrenciesList() {

        viewModelScope.launch(dispatchers.io) {
            when (val response = repository.getCurrenciesList()) {
                is Resource.Error -> _currencies.value = CurrencyListEvent.Failure(response.message!!)
                is Resource.Success -> {
                    val data = response.data!!.string()
                    val listOfCurrency = Utility.getJsonObjectToArrayList(data)
                    _currencies.value = CurrencyListEvent.Success(listOfCurrency)
                }

            }
        }
    }





    // Below this code for handle currency rates response


    sealed class CurrencyRatesEvent {
        class Success(val currencyMap : Map<String,Double>) : CurrencyRatesEvent()
        class Failure(val errorMessage: String) : CurrencyRatesEvent()
        object Loading : CurrencyRatesEvent()
        object Empty : CurrencyRatesEvent()
    }

    private val _rates = MutableStateFlow<CurrencyRatesEvent>(CurrencyRatesEvent.Empty)
    val rates: StateFlow<CurrencyRatesEvent> = _rates

    fun getCurrenciesRateMaps() {

        viewModelScope.launch(dispatchers.io) {
            when (val response = repository.getCurrencyRates()) {
                is Resource.Error -> _rates.value = CurrencyRatesEvent.Failure(response.message!!)
                is Resource.Success -> {
                    val data = response.data!!.string()
                    val ratesMap = Utility.getJsonObjectToMap(data)
                    _rates.value = CurrencyRatesEvent.Success(ratesMap)
                }

            }
        }
    }




}