package com.example.currencyconverterwithkotlin.util
//manage states for handle error or for validation
sealed class Resource<T>(
   val data:T? ,
   val message:String?
) {
    class Success<T>(data: T):Resource<T>(data,null)
    class Error<T>(message: String):Resource<T>(null,message)

}