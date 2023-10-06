package com.zak.rsisaobatku.ui.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zak.rsisaobatku.data.remote.response.ArticlesItem
import com.zak.rsisaobatku.data.remote.response.NewsResponse
import com.zak.rsisaobatku.data.remote.retrofit.ApiConfig
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {

    companion object{
        private const val TAG = "NewsViewModel"
    }

    private val _newsStatus = MutableLiveData<NewsResponse>()
    val newsStatus: LiveData<NewsResponse> = _newsStatus

    private val _newsItems = MutableLiveData<List<ArticlesItem>>()
    val newsItems: LiveData<List<ArticlesItem>> = _newsItems

    private val apiKey =  "cc4fd3cd17ad4fcd8bb29cbd4d31600e"
    private val language =  "id"

    val error = MutableLiveData("")

    fun getNews() {
        val client = ApiConfig.getApiService().getNews(apiKey, "pengobatan OR obat OR penyakit", language)
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    _newsStatus.postValue(response.body())
                    _newsItems.value = response.body()?.articles
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    response.errorBody()?.let {
                        val errorResponse = JSONObject(it.string())
                        val errorMessages = errorResponse.getString("message")
                        error.postValue(errorMessages)
                    }
                }
            }
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
            }
        })
    }
}