package com.example.aplikasi_mobile_mp_kp2.data.model


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("jumlah")
    val jumlah: Int,
    @SerializedName("tugas")
    val tugas: List<TugasResponse>
)