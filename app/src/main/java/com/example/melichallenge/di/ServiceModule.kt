package com.example.melichallenge.di

import com.example.melichallenge.search.model.service.SearchService
import com.facebook.stetho.okhttp3.StethoInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

val client  = OkHttpClient.Builder().addNetworkInterceptor(StethoInterceptor()).build()

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.mercadolibre.com/sites/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val serviceModule = module {

    factory<SearchService> {
        retrofit.create(SearchService::class.java)
    }
}