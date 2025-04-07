package com.example.aplikasi_mobile_mp_kp2.data.model


import com.google.gson.annotations.SerializedName

data class ProyekProgressResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: String
)