package com.example.aplikasi_mobile_mp_kp2.data.model

import com.google.gson.annotations.SerializedName

data class TugasByIdTugasWithBuktiResponse(
    @SerializedName("data")
    val `data`: TugasWithBuktiResponse?,
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String
)