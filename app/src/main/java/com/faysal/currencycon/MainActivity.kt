package com.faysal.currencycon

import android.R
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.faysal.currencycon.databinding.ActivityMainBinding
import com.faysal.currencycon.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var loadingView: ProgressDialog


    private val viewModel: MainViewModel by viewModels()
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupViews()
        setupViewsActions()





        lifecycleScope.launchWhenStarted {
            viewModel.currencies.collect { event ->

                when (event) {
                    is MainViewModel.CurrencyEvent.Success -> {
                        event.currencyList?.let {
                            setUpCurrencyDropDown(it)
                        }
                        loadingView.dismiss()
                    }
                    is MainViewModel.CurrencyEvent.Failure -> {
                        Log.d(TAG, "Error :  " + event.errorMessage)
                        Toast.makeText(this@MainActivity, ""+event.errorMessage, Toast.LENGTH_SHORT).show()
                        loadingView.dismiss()
                    }
                    is MainViewModel.CurrencyEvent.Loading -> {
                        loadingView.show()
                    }
                    else -> Unit
                }

            }
        }


    }

    private fun setupViewsActions() {

    }

    private fun setupViews() {
        viewModel.getCurrenciesList()

        loadingView = ProgressDialog(this)
        loadingView.setMessage("Please wait a little bit")
        loadingView.show()

    }

    private fun setUpCurrencyDropDown(listOfCurrencies: List<String>) {
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.simple_spinner_item, listOfCurrencies)
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.currenciesDropdown.setAdapter(dataAdapter)
    }


}