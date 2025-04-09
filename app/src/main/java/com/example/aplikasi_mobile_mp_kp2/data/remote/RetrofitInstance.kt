package com.example.aplikasi_mobile_mp_kp2.data.remote

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val BASE_URL = "http://10.227.0.157:8000/api-mobile/"

    public fun getRetrofit(context: Context) : Retrofit {
        val sharedPrefsManager = SharedPrefsManager(context)
        val authInterceptor = AuthInterceptor(sharedPrefsManager)

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun getInstanceAuthInterface(context: Context) : AuthInterface {
        val retroit = getRetrofit(context)
        return retroit.create(AuthInterface::class.java)
    }

    fun getInstanceManagerInterface(context: Context) : ManagerInterface {
        val retrofit = getRetrofit(context)
        return retrofit.create(ManagerInterface::class.java)
    }
}