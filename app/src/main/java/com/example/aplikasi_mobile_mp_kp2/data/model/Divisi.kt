package com.example.aplikasi_mobile_mp_kp2.data.model


import com.google.gson.annotations.SerializedName

data class Divisi(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("deskripsi")
    val deskripsi: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_manajer")
    val idManajer: Int,
    @SerializedName("nama_divisi")
    val namaDivisi: String,
    @SerializedName("updated_at")
    val updatedAt: String
)