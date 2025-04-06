package com.example.aplikasi_mobile_mp_kp2.data.remote

import com.example.aplikasi_mobile_mp_kp2.data.model.LoginRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthInterface {
    @POST("login-mobile")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>
}