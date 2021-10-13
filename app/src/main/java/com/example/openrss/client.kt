package com.example.openrss

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class client {
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit? {
        retrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com/r/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
        return retrofit
    }

}