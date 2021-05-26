package com.faysal.currencycon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.faysal.currencycon.databinding.ActivityMainBinding
import com.faysal.currencycon.databinding.ActivitySplashBinding
import com.faysal.currencycon.main.MainViewModel
import com.faysal.currencycon.util.Constants
import com.faysal.currencycon.util.SharedPref
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ActivitySplash : AppCompatActivity() {

    //Creating an instance of view model
    private val viewModel: MainViewModel by viewModels()

    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val isFirstLaunch = SharedPref.getBoolean(this, Constants.IS_FIRST_LAUNCH)

        lifecycleScope.launchWhenResumed {
            delay(3000)

            if (isFirstLaunch == false) {
                viewModel.getCurrenciesRateMaps()
            } else {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.rates.collect { event ->

                when (event) {
                    is MainViewModel.CurrencyRatesEvent.Success -> {
                        event.currencyMap?.let {

                            //converting map to json format
                            val rateJson = Gson().toJson(it)

                            // Saving currency rate for offline usages
                            SharedPref.putString(applicationContext, Constants.SAVE_CURRENCY_RATE, rateJson)
                            SharedPref.putBoolean(applicationContext, Constants.IS_FIRST_LAUNCH, true)

                            //Go to Main Activity
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                        }

                        binding.loadingView.visibility = View.GONE
                    }
                    is MainViewModel.CurrencyRatesEvent.Failure -> {
                        Toast.makeText(this@ActivitySplash, "" + event.errorMessage, Toast.LENGTH_SHORT).show()
                        binding.loadingView.visibility = View.GONE
                    }
                    is MainViewModel.CurrencyRatesEvent.Loading -> {
                        binding.loadingView.visibility = View.VISIBLE
                    }
                    else -> Unit
                }

            }
        }

    }


}