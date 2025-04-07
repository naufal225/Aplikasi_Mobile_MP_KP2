package com.example.aplikasi_mobile_mp_kp2.data.remote

import com.example.aplikasi_mobile_mp_kp2.data.model.AddProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.AddProjectResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.DataTugasByIdProyekResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.Proyek
import com.example.aplikasi_mobile_mp_kp2.data.model.ProyekByIdResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.ProyekProgressResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateProjectResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ManagerInterface {
    @GET("manajer/proyek-all")
    suspend fun getAllDataProyek() : Response<ProyekProgressResponse>

    @GET("manajer/proyek-done")
    suspend fun getDataProyekDone() : Response<ProyekProgressResponse>

    @GET("manajer/proyek-in-progress")
    suspend fun getDataProyekInProgress() : Response<ProyekProgressResponse>

    @GET("manajer/proyek-by-id/{id}")
    suspend fun getDataProyekById(@Path("id") id: Int) : Response<ProyekByIdResponse>

    @POST("manajer/proyek-add")
    suspend fun addDataProyek(@Body addProjectRequest: AddProjectRequest) : Response<AddProjectResponse>

    @GET("manajer/get-tugas-by-id-proyek/{id}")
    suspend fun getTugasByIdProyek(@Path("id") id: Int) : Response<DataTugasByIdProyekResponse>

    @PUT("manajer/update-proyek/{id}")
    suspend fun updateDataProyek(@Path("id") id: Int, @Body updateProjectRequest: UpdateProjectRequest) : Response<UpdateProjectResponse>
}