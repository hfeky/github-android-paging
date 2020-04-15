package com.husseinelfeky.githubpaging.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {

    private lateinit var retrofit: Retrofit

    private const val BASE_URL = "https://api.github.com/"

    fun getClient(): Retrofit {
        if (!::retrofit.isInitialized) {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }
        return retrofit
    }
}
