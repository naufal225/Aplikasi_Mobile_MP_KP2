package com.example.aplikasi_mobile_mp_kp2.data.model

data class ProjectAddTaskResponse(
    val status: String,
    val message: Map<String, List<String>>? = null, // Untuk error validation
    val data: TugasResponse? = null
)
