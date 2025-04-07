package com.example.aplikasi_mobile_mp_kp2.data.model


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("tipe")
    val tipe: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("divisi")
    val divisi: Divisi?
)