package com.example.aplikasi_mobile_mp_kp2.data.model

import com.google.gson.annotations.SerializedName

data class UploadFotoProfilResponse(
    val status: Boolean,
    val message: String,
    val data: FotoProfileData?
)

data class FotoProfileData(
    @SerializedName("url_foto_profile")
    val urlFotoProfile: String
)