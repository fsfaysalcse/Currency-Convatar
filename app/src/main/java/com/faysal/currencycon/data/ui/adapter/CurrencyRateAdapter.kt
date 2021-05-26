package com.faysal.currencycon.data.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.faysal.currencycon.R
import com.faysal.currencycon.data.models.CurrencyRate

class CurrencyRateAdapter : RecyclerView.Adapter<CurrencyRateAdapter.CurrencyRateHolder>() {

    private val rateList : MutableList<CurrencyRate> = mutableListOf()

    inner class CurrencyRateHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val fromCurrency: TextView  = itemView.findViewById(R.id.from_currency)
        val toCurrency: TextView  = itemView.findViewById(R.id.to_currency)
        val actualRate: TextView = itemView.findViewById(R.id.actual_rate)

        fun bind(currency: CurrencyRate) {
            currency.fromCurrencyName?.let { fromCurrency.text = it }
            currency.toCurrencyName?.let { toCurrency.text = it }
            currency.rates?.let { actualRate.text = it.toFloat().toString() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRateHolder {
       return CurrencyRateHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_currency_rate,parent,false))
    }



    override fun onBindViewHolder(holder: CurrencyRateHolder, position: Int) {
       val currency = rateList.get(position)
        holder.bind(currency)
    }

    public fun updateList(nlist : List<CurrencyRate>){
        rateList.clear()
        rateList.addAll(nlist)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = rateList.size
}