package com.example.aplikasi_mobile_mp_kp2.data.model


import com.google.gson.annotations.SerializedName

data class UpdateStatusTugasResponse(
    @SerializedName("data")
    val `data`: TugasResponse,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)