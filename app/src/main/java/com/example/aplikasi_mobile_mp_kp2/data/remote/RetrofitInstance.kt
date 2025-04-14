package com.example.aplikasi_mobile_mp_kp2.data.remote

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val BASE = "http://192.168.138.170:8000/"
    val BASE_URL = BASE + "api-mobile/"
    val BASE_URL_STORAGE = BASE + "storage/"

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

    fun getInstanceEmployeeInterface(context: Context) : EmployeeInterface {
        val retrofit = getRetrofit(context)
        return retrofit.create(EmployeeInterface::class.java)
    }
}