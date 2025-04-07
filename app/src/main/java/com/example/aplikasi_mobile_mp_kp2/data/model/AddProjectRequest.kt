package com.example.aplikasi_mobile_mp_kp2.data.model

data class AddProjectRequest(
    val nama_proyek: String,
    val deskripsi_proyek: String,
    val tanggal_mulai: String, // format: "yyyy-MM-dd"
    val tenggat_waktu: String  // format: "yyyy-MM-dd"
)
