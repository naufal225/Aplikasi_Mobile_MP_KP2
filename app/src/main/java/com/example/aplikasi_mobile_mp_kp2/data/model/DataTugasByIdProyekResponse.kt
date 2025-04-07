package com.example.aplikasi_mobile_mp_kp2.data.model


import com.google.gson.annotations.SerializedName

data class DataTugasByIdProyekResponse(
    @SerializedName("data")
    val `data`: DataX,
    @SerializedName("status")
    val status: String
)