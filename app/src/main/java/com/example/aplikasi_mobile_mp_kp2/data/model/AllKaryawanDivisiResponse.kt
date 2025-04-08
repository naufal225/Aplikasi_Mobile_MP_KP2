package com.example.aplikasi_mobile_mp_kp2.data.model


import com.google.gson.annotations.SerializedName

data class AllKaryawanDivisiResponse(
    @SerializedName("data")
    val `data`: DataKaryawanDivisi,
    @SerializedName("status")
    val status: String
)