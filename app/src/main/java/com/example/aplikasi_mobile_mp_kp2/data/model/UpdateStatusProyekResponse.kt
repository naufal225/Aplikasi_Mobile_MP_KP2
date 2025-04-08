package com.example.aplikasi_mobile_mp_kp2.data.model


import com.google.gson.annotations.SerializedName

data class UpdateStatusProyekResponse(
    @SerializedName("data")
    val `data`: KaryawanX,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)