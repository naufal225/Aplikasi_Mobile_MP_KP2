package com.example.aplikasi_mobile_mp_kp2.data.model

import com.google.gson.annotations.SerializedName

data class NotificationListResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: List<NotificationData>
)

