/*
* RetrofitClient.kt
* © 2024 Fleetio
* */

package com.example.levysample.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.levysample.common.Tag
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Full implementation of the API infrastructure, including HTTP requests and
 * the formatting of requests and responses.
 */
object RetrofitClient {
    private const val BASE_URL = "https://secure.fleetio.com"

    // Status code.
    private val statusCodeLiveData = MutableLiveData<Int?>()
    private val statusCodeInterceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        Log.d(Tag.NETW, "HTTP Status Code: ${response.code}")
        statusCodeLiveData.postValue(response.code)
        response
    }

    // Ensure responses go to logcat; helpful for seeing mistakes.
    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(statusCodeInterceptor)
        .build()

    // Conversion between Kotlin data classes and JSON.
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // The API service used by all HTTP requests.
    val levySampleApiService: LevySampleApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()
        .create(LevySampleApiService::class.java)

    // Status code live data.
    fun getStatusCodeLiveData(): LiveData<Int?> = statusCodeLiveData
}
