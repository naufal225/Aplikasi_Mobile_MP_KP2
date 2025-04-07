package com.example.aplikasi_mobile_mp_kp2.data.model

import com.google.gson.annotations.SerializedName

data class AddProjectResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Proyek,
    @SerializedName("message")
    val message: String?
)
