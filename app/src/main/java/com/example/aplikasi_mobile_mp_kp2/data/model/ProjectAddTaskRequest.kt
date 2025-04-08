package com.example.aplikasi_mobile_mp_kp2.data.model

data class ProjectAddTaskRequest(
    val nama_tugas: String,
    val deskripsi_tugas: String,
    val tenggat_waktu: String, // Format: "yyyy-MM-dd" (pastikan sesuai Laravel)
    val id_karyawan: Int
)