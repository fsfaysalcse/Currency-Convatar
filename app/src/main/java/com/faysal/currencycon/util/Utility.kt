package com.faysal.currencycon.util

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.json.JSONObject

object Utility {

    internal fun getJsonObjectToArrayList(json: String): List<String> {
        val list: MutableList<String> = mutableListOf()
        return try {
            val jsonObj = JSONObject(json).getJSONObject("currencies")
            val element: JsonElement = JsonParser.parseString(jsonObj.toString())
            val obj = element.asJsonObject
            val entries = obj.entrySet()
            for ((key) in entries) {
                list.add(key.toString())
            }
            list
        } catch (e: Exception) {
            list
        }
    }

    internal fun getJsonObjectToMap(json: String): Map<String, Double> {
        val mapRates = mutableMapOf<String, Double>()
        return try {
            val objt = JSONObject(json).getJSONObject("quotes")
            val element = JsonParser.parseString(objt.toString())
            val obj = element.asJsonObject
            val entries = obj.entrySet()
            entries.associateTo(mapRates) {it.key to it.value.asDouble}
            mapRates
        } catch (e: Exception) {
            mapRates
        }
    }
}