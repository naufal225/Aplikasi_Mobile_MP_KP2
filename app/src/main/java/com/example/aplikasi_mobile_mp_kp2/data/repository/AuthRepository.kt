package com.example.aplikasi_mobile_mp_kp2.data.repository

import com.example.aplikasi_mobile_mp_kp2.data.model.LoginRequest
import com.example.aplikasi_mobile_mp_kp2.data.remote.AuthInterface

class AuthRepository(private val authInterface: AuthInterface) {
    suspend fun login(loginRequest: LoginRequest) = authInterface.login(loginRequest)

    suspend fun logout() = authInterface.logout()
}