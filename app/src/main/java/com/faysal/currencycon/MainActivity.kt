package com.faysal.currencycon

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.faysal.currencycon.data.models.CurrencyRate
import com.faysal.currencycon.data.ui.adapter.CurrencyRateAdapter
import com.faysal.currencycon.databinding.ActivityMainBinding
import com.faysal.currencycon.main.MainViewModel
import com.faysal.currencycon.util.Constants
import com.faysal.currencycon.util.SharedPref
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.lang.Exception


private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, TextWatcher {

   lateinit var loading: Dialog


    private val viewModel: MainViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    lateinit var rateAdapter: CurrencyRateAdapter

    private var currencyName = "USD"
    private var amount = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupViews()
        setupViewsActions()


    }

    private fun setupViewsActions() {
        lifecycleScope.launchWhenStarted {
            viewModel.currencies.collect { event ->

                when (event) {
                    is MainViewModel.CurrencyListEvent.Success -> {
                        event.currencyList?.let {
                            setUpCurrencyDropDown(it)
                        }

                        binding.listView.visibility = View.VISIBLE
                       dismissLoadingDialog()
                    }
                    is MainViewModel.CurrencyListEvent.Failure -> {
                        Toast.makeText(this@MainActivity, "" + event.errorMessage, Toast.LENGTH_SHORT).show()
                       dismissLoadingDialog()
                    }
                    is MainViewModel.CurrencyListEvent.Loading -> {
                        showLoadingDialog()
                    }
                    else -> Unit
                }

            }
        }
    }

    private fun setupViews() {
        initializeLoadingDialog()
        viewModel.getCurrenciesList()

        rateAdapter = CurrencyRateAdapter()
        binding.listView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity).also {
                it.orientation = LinearLayoutManager.VERTICAL
            }
            adapter = rateAdapter
        }



        binding.currenciesDropdown.onItemSelectedListener = this

        binding.etCurrency.addTextChangedListener(this)
    }

    private fun setUpCurrencyDropDown(listOfCurrencies: List<String>) {
        val dataAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.item_spinner, listOfCurrencies)
        dataAdapter.setDropDownViewResource(R.layout.item_spinner_checked)
        binding.currenciesDropdown.setAdapter(dataAdapter)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val currencyName = binding.currenciesDropdown.selectedItem.toString()
        this.currencyName = currencyName
        setupListViewAccordingCurrency(currencyName, amount)
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


    private fun setupListViewAccordingCurrency(currencyName: String, amount: Float = 1f) {
        val ratesJson = SharedPref.getString(this, Constants.SAVE_CURRENCY_RATE)

        try {
            val ratesMap = Gson().fromJson(ratesJson, Map::class.java)
            val ratesEntries = Gson().fromJson(ratesJson, Map::class.java).entries
            val usdRate = ratesEntries.find { it.key == "USD$currencyName" }!!.value

            val list = ratesMap.map {

                val x: Double = it.value as Double / 1 * 1 / usdRate as Double;
                val result = getRate(x, amount)

                CurrencyRate(
                    currencyName,
                    it.key.toString().removePrefix("USD"),
                    result
                )
            }

            rateAdapter.updateList(list)


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getRate(rates: Double, amount: Float): Double {
        return amount / 1 * rates / 1;
        //  return (Math.round(amount * rates * 100) / 100).toDouble()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val amount_et = binding.etCurrency.text.toString()
        amount = 1f;
        if (amount_et.length > 0) amount = amount_et.toFloat()
        setupListViewAccordingCurrency(currencyName, amount)
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun initializeLoadingDialog() {
        loading = Dialog(this)
        loading.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loading.setCancelable(false)
        loading.setContentView(R.layout.loading_layout)

    }

    private fun showLoadingDialog() {
        loading.show()
    }

    private fun dismissLoadingDialog() {
        loading.dismiss()
    }



}