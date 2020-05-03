package com.husseinelfeky.githubpaging.api

import com.husseinelfeky.githubpaging.BuildConfig
import com.husseinelfeky.githubpaging.api.adapter.GitHubRepoJsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {

    private lateinit var retrofit: Retrofit

    private const val BASE_URL = "https://api.github.com/"

    fun getClient(): Retrofit {
        if (!::retrofit.isInitialized) {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor())
                .addInterceptor { tokenInterceptor(it) }
                .build()

            val moshi = Moshi.Builder()
                .add(GitHubRepoJsonAdapter())
                .add(KotlinJsonAdapterFactory())
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return retrofit
    }

    private fun loggingInterceptor() = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private fun tokenInterceptor(chain: Interceptor.Chain) = chain.run {
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "token ${BuildConfig.GITHUB_ACCESS_TOKEN}")
                .build()
        )
    }
}
