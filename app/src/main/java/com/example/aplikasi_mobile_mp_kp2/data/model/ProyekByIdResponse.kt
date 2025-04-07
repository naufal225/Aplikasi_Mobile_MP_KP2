package com.example.aplikasi_mobile_mp_kp2.data.model


import com.google.gson.annotations.SerializedName

data class ProyekByIdResponse(
    @SerializedName("data")
    val `data`: DataById,
    @SerializedName("status")
    val status: String
)

data class DataById(
    @SerializedName("proyek")
    val proyek: Proyek,
    @SerializedName("jumlah")
    val jumlah: Int
)