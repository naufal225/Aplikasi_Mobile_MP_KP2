package com.example.aplikasi_mobile_mp_kp2.data.model


import com.google.gson.annotations.SerializedName

data class TugasResponse(
    @SerializedName("created_at")
    val createdAt: Any,
    @SerializedName("deskripsi_tugas")
    val deskripsiTugas: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_karyawan")
    val idKaryawan: Int,
    @SerializedName("id_proyek")
    val idProyek: Int,
    @SerializedName("karyawan")
    val karyawan: Karyawan,
    @SerializedName("nama_tugas")
    val namaTugas: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("tenggat_waktu")
    val tenggatWaktu: String,
    @SerializedName("updated_at")
    val updatedAt: Any
)

data class TugasWithBuktiResponse(
    @SerializedName("created_at")
    val createdAt: Any,
    @SerializedName("deskripsi_tugas")
    val deskripsiTugas: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_karyawan")
    val idKaryawan: Int,
    @SerializedName("id_proyek")
    val idProyek: Int,
    @SerializedName("karyawan")
    val karyawan: Karyawan,
    @SerializedName("nama_tugas")
    val namaTugas: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("path_file_bukti_tugas")
    val path_file_bukti_tugas: String,
    @SerializedName("tenggat_waktu")
    val tenggatWaktu: String,
    @SerializedName("updated_at")
    val updatedAt: Any
)