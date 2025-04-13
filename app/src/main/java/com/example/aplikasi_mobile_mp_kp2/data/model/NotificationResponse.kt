package com.example.aplikasi_mobile_mp_kp2.data.model

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: NotificationData
)

data class NotificationData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("judul")
    val judul: String,

    @SerializedName("pesan")
    val pesan: String?,

    @SerializedName("target")
    val target: String,

    @SerializedName("id_target")
    val idTarget: Int,

    @SerializedName("dibaca")
    val dibaca: Boolean,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)

