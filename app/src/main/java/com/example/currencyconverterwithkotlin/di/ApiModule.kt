package com.example.currencyconverterwithkotlin.di

import com.example.currencyconverterwithkotlin.data.CurrencyApi
import com.example.currencyconverterwithkotlin.data.models.CurrencyResponse
import com.example.currencyconverterwithkotlin.main.DefaultMainRepository
import com.example.currencyconverterwithkotlin.main.MainRepository
import com.example.currencyconverterwithkotlin.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private  val  BASE_URL= "https://api.exchangeratesapi.io/"
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideCurrencyApi():CurrencyApi =
        Retrofit.
        Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyApi):MainRepository{
        return DefaultMainRepository(api)
    }

    @Singleton
    @Provides
    fun provideDispatcher():DispatcherProvider = object :DispatcherProvider{
        override val main: CoroutineDispatcher
            get()  = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }
}