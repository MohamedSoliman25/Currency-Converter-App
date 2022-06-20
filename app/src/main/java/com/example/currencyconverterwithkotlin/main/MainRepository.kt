package com.example.currencyconverterwithkotlin.main

import com.example.currencyconverterwithkotlin.data.models.CurrencyResponse
import com.example.currencyconverterwithkotlin.util.Resource

//for easy testing in view model
interface MainRepository {
    suspend fun getRates(base:String):Resource<CurrencyResponse>
}