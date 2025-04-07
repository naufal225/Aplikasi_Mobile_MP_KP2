package com.example.aplikasi_mobile_mp_kp2.data.model


import com.google.gson.annotations.SerializedName

data class Proyek(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("deskripsi_proyek")
    val deskripsiProyek: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_divisi")
    val idDivisi: Int,
    @SerializedName("nama_proyek")
    val namaProyek: String,
    @SerializedName("progress")
    val progress: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("tanggal_mulai")
    val tanggalMulai: String,
    @SerializedName("tanggal_selesai")
    val tanggalSelesai: String,
    @SerializedName("tenggat_waktu")
    val tenggatWaktu: String,
    @SerializedName("updated_at")
    val updatedAt: String
)