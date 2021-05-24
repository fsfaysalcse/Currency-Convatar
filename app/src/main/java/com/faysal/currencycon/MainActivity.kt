package com.faysal.currencycon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.faysal.currencycon.databinding.ActivityMainBinding
import com.faysal.currencycon.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val viewModel: MainViewModel by viewModels()

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.getCurrenciesList()


        lifecycleScope.launchWhenStarted {
            viewModel.currencies.collect { event ->

                when (event) {
                    is MainViewModel.CurrencyEvent.Success -> {
                        Log.d(TAG, "Success :  "+event.currencyList)
                    }
                    is MainViewModel.CurrencyEvent.Failure -> {
                        Log.d(TAG, "Error :  "+event.errorMessage)
                    }
                    is MainViewModel.CurrencyEvent.Loading -> {
                        Log.d(TAG, "Loading...")
                    }
                    else -> Unit
                }

            }
        }


    }


}