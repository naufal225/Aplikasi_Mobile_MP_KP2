package com.example.aplikasi_mobile_mp_kp2.data.model

data class UploadBuktiTugasResponse(
    val status: String,
    val message: String?,
    val file_path: String? = null, // nullable karena bisa error
    val error: String? = null
)
