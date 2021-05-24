package com.faysal.currencycon.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faysal.currencycon.data.models.Currencies
import com.faysal.currencycon.util.DispatcherProvider
import com.faysal.currencycon.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val repository: MainRepository,
    val dispatcher: DispatcherProvider
) : ViewModel() {

    sealed class CurrencyEvent{
        class Success(val currencyList: Currencies) : CurrencyEvent()
        class Failure(val errorMessage : String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _currencies  = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val currencies : StateFlow<CurrencyEvent> = _currencies

    fun getCurrenciesList(){

        viewModelScope.launch(dispatcher.io) {
            when (val response = repository.getCurrenciesList()){
                is Resource.Error -> _currencies.value = CurrencyEvent.Failure(response.message!!)
                is Resource.Success -> {
                    val list = response.data!!.currencies
                   _currencies.value = CurrencyEvent.Success(list)
                }

            }
        }

    }

}