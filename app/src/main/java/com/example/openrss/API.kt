package com.example.openrss

import retrofit2.Call
import retrofit2.http.GET

interface API {
    var link: String

    @get:GET(".rss")
    val getfeed: Call<Feed?>?


}