package com.example.aplikasi_mobile_mp_kp2.data.remote

import com.example.aplikasi_mobile_mp_kp2.data.model.KaryawanX
import com.example.aplikasi_mobile_mp_kp2.data.model.UploadBuktiTugasResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.UploadFotoProfilResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface EmployeeInterface {
    @GET("user")
    suspend fun getDataUser() : Response<KaryawanX>

    @Multipart
    @POST("karyawan/upload-bukti-tugas/{id}")
    suspend fun uploadBuktiTugas(
        @Path("id") idTugas: Int,
        @Part file: MultipartBody.Part
    ): Response<UploadBuktiTugasResponse>

    @Multipart
    @POST("karyawan/upload-foto-profil")
    suspend fun uploadFotoProfil(
        @Part foto: MultipartBody.Part
    ): Response<UploadFotoProfilResponse>
}