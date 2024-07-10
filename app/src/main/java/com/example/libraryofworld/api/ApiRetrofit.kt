package com.example.libraryofworld.api

import com.google.gson.GsonBuilder
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiRetrofit {
    private val retrofit :Retrofit = Retrofit.Builder()
            .baseUrl("https://openlibrary.org/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

    fun getBookApi():BookApi {
        return retrofit.create(BookApi::class.java)
    }


}
