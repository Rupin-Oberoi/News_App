package com.cse535.news_app
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsRetroInterface {
    @GET("v2/top-headlines")
    fun getKeywordNews(
        @Query("q") keyword: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>

    @GET("v2/top-headlines")
    fun getCategoryNews(
        @Query("category") category: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>

    @GET("v2/top-headlines")
    fun getCountryNews(
        @Query("country") country: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>

    fun getGlobalNews(
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>

}