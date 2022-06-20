package com.example.currencyconverterwithkotlin.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverterwithkotlin.data.models.Rates
import com.example.currencyconverterwithkotlin.util.DispatcherProvider
import com.example.currencyconverterwithkotlin.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

//business layer (for logic)
// we use MainRepository(interface) for ease testing repository inside viewModel but if i won't make any testing (actual app) i use DefaultMainRepository
// i will pass dispatcher for ease of testing inside viewModel but if i won't make any testing  (actual app)i don't use it and pass context dispatcher inside coroutine builder
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatcher: DispatcherProvider):ViewModel() {

    //handle more events
        sealed class CurrencyEvent{
            class Success (val resultText:String):CurrencyEvent()
            class Failure(val errorText:String):CurrencyEvent()
            object Loading :CurrencyEvent()
            object Empty :CurrencyEvent()
        }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion  :StateFlow<CurrencyEvent> get() = _conversion

    fun convert(amount :String,fromCurrency:String,toCurrency:String){
        val fromAmount = amount.toFloatOrNull()
        if (fromAmount ==null){
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }
        viewModelScope.launch(dispatcher.io) {
            _conversion.value = CurrencyEvent.Loading
            when(val rateResponse =repository.getRates(fromCurrency)){
                is Resource.Error-> _conversion.value =CurrencyEvent.Failure(rateResponse.message!!)
                is Resource.Success->{
                    val rates = rateResponse.data!!.rates
                    val rate = getRateForCurrency(toCurrency,rates)
                    if (rate == null ){
                        _conversion.value = CurrencyEvent.Failure("Unexpected error")
                    }else{
                        val convertedCurrency = round(fromAmount*rate *100)/100 // for taking only decimal places
                        _conversion.value = CurrencyEvent.Success("$fromAmount $fromCurrency = $convertedCurrency $toCurrency")
                    }
                }
            }
        }

    }
    private fun getRateForCurrency(toCurrency: String, rates: Rates) = when (toCurrency) {
        "CAD" -> rates.cAD
        "HKD" -> rates.hKD
        "ISK" -> rates.iSK
        "EUR" -> rates.eUR
        "PHP" -> rates.pHP
        "DKK" -> rates.dKK
        "HUF" -> rates.hUF
        "CZK" -> rates.cZK
        "AUD" -> rates.aUD
        "RON" -> rates.rON
        "SEK" -> rates.sEK
        "IDR" -> rates.iDR
        "INR" -> rates.iNR
        "BRL" -> rates.bRL
        "RUB" -> rates.rUB
        "HRK" -> rates.hRK
        "JPY" -> rates.jPY
        "THB" -> rates.tHB
        "CHF" -> rates.cHF
        "SGD" -> rates.sGD
        "PLN" -> rates.pLN
        "BGN" -> rates.bGN
        "CNY" -> rates.cNY
        "NOK" -> rates.nOK
        "NZD" -> rates.nZD
        "ZAR" -> rates.zAR
        "USD" -> rates.uSD
        "MXN" -> rates.mXN
        "ILS" -> rates.iLS
        "GBP" -> rates.gBP
        "KRW" -> rates.kRW
        "MYR" -> rates.mYR
        else -> null
    }
}