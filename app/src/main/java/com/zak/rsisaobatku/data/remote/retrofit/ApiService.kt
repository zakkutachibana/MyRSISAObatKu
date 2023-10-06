package com.zak.rsisaobatku.data.remote.retrofit

import com.zak.rsisaobatku.data.remote.response.NewsResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("everything")
    fun getNews(
        @Query("apiKey") apiKey: String,
        @Query("q") q: String,
        @Query("language") language: String
    ): Call<NewsResponse>

}