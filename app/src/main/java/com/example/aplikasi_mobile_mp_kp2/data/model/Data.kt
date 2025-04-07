package com.example.aplikasi_mobile_mp_kp2.data.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("jumlah")
    val jumlah: Int,
    @SerializedName("proyek")
    val proyek: List<Proyek>
)