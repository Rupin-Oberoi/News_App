package com.cse535.news_app

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getNewsByCategory(category: String, lang: String = "en", callback: NewsCallback) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val baseUrl = "https://newsapi.org/"
    val apiKey = ApiKeys.NEWS_API_KEY
    val urlString = "$baseUrl/v2/top-headlines?category=$category&apiKey=$apiKey&language=$lang"

    // Print the URL string
    Log.d("URL","Querying URL: $urlString")

    val apiService = retrofit.create(NewsRetroInterface::class.java)
    val call = apiService.getCategoryNews(category, ApiKeys.NEWS_API_KEY, lang)
    call.enqueue(object : Callback<NewsResponse> {
        override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
            if (response.isSuccessful) {
                val newsResponse = response.body()
                if (newsResponse != null) {
                    callback.onSuccess(newsResponse)
                } else {
                    callback.onFailure("Failed to fetch news")
                }
            }
            else {
                callback.onFailure("Failed to fetch news")
            }
        }

        override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
            println("Failed to fetch news")
        }
    })
}

fun getNewsByKeyword(keyword: String, callback: NewsCallback) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(NewsRetroInterface::class.java)
    val call = apiService.getKeywordNews(keyword, ApiKeys.NEWS_API_KEY)
    call.enqueue(object : Callback<NewsResponse> {
        override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
            if (response.isSuccessful) {
                val newsResponse = response.body()
                if (newsResponse != null) {
                    callback.onSuccess(newsResponse)
                } else {
                    callback.onFailure("Failed to fetch news")
                }
            }
            else {
                callback.onFailure("Failed to fetch news")
            }
        }

        override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
            println("Failed to fetch news")
        }
    })
}

fun getNewsByCountry(country: String, callback: NewsCallback) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(NewsRetroInterface::class.java)
    val call = apiService.getCountryNews(country, ApiKeys.NEWS_API_KEY)
    call.enqueue(object : Callback<NewsResponse> {
        override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
            if (response.isSuccessful) {
                val newsResponse = response.body()
                if (newsResponse != null) {
                    callback.onSuccess(newsResponse)
                } else {
                    callback.onFailure("Failed to fetch news")
                }
            }
            else {
                callback.onFailure("Failed to fetch news")
            }
        }

        override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
            println("Failed to fetch news")
        }
    })
}

fun getGlobalNews(callback: NewsCallback) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(NewsRetroInterface::class.java)
    val call = apiService.getGlobalNews(ApiKeys.NEWS_API_KEY, "us")
    call.enqueue(object : Callback<NewsResponse> {
        override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
            if (response.isSuccessful) {
                val newsResponse = response.body()
                if (newsResponse != null) {
                    callback.onSuccess(newsResponse)
                } else {
                    callback.onFailure("Failed to fetch news 1")
                }
            }
            else {
                callback.onFailure("Failed to fetch news 2")
                Log.d("News", response.toString())
            }
        }

        override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
            println("Failed to fetch news")
        }
    })
}



interface NewsCallback {
    fun onSuccess(newsResponse: NewsResponse)
    fun onFailure(message: String)
}