package com.example.aplikasi_mobile_mp_kp2.data.model

import com.google.gson.annotations.SerializedName

data class NotificationRequest(
    @SerializedName("judul")
    val judul: String,

    @SerializedName("pesan")
    val pesan: String? = null,

    @SerializedName("target")
    val target: String, // "karyawan" atau "divisi"

    @SerializedName("id_target")
    val idTarget: Int
)

