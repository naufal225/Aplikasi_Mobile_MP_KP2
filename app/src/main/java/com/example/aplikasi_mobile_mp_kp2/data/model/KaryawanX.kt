package com.example.aplikasi_mobile_mp_kp2.data.model


import com.google.gson.annotations.SerializedName

data class KaryawanX(
    @SerializedName("alamat")
    val alamat: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_divisi")
    val idDivisi: Int,
    @SerializedName("jenis_kelamin")
    val jenisKelamin: String,
    @SerializedName("nama_lengkap")
    val namaLengkap: String,
    @SerializedName("nomor_telepon")
    val nomorTelepon: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("skor_kinerja")
    val skorKinerja: Int,
    @SerializedName("tanggal_lahir")
    val tanggalLahir: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("username")
    val username: String
)