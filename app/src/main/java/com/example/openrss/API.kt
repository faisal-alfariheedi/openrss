package com.example.openrss

import retrofit2.Call
import retrofit2.http.GET

interface API {
    @get:GET("pics/.rss")
    val getfeed: Call<Feed?>?

}