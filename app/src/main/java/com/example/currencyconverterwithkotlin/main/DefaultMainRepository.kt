package com.example.currencyconverterwithkotlin.main

import com.example.currencyconverterwithkotlin.data.CurrencyApi
import com.example.currencyconverterwithkotlin.data.models.CurrencyResponse
import com.example.currencyconverterwithkotlin.util.Resource
import java.lang.Exception
import javax.inject.Inject

// model && Repository is called(data layer) , i use it in actual app directly without testing but i use testing so i provide it inside hilt ApiModule
class DefaultMainRepository @Inject constructor(
    private val api:CurrencyApi
):MainRepository {
    override suspend fun getRates(base: String): Resource<CurrencyResponse> {

        //validate data
        return try {
            val response = api.getRates(base)
            val result = response.body()
            if(response.isSuccessful && result !=null){
                Resource.Success(result)
            }
            else{
                Resource.Error(response.message())
            }
        }catch (e:Exception){
            Resource.Error(e.message ?:"An error occurred")
        }
    }
}