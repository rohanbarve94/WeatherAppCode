package com.example.weatherappcode.data.api


import com.example.weatherapp.data.api.NetworkService
import com.example.weatherappcode.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient() {

    val retrofitClient: Retrofit.Builder by lazy {

        val levelType: Level = Level.BODY

        val logging = HttpLoggingInterceptor()
        logging.setLevel(levelType)

        val okhttpClient = OkHttpClient.Builder()
        okhttpClient.addInterceptor(logging)

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())

    }

    val apiInterface: NetworkService by lazy {
        retrofitClient
            .build()
            .create(NetworkService::class.java)
    }
}