package com.example.aplikasi_mobile_mp_kp2.viewmodel.employee

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_mobile_mp_kp2.data.model.KaryawanX
import com.example.aplikasi_mobile_mp_kp2.data.model.NotificationListResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.NotificationRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.NotificationResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.UploadBuktiTugasResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.UploadFotoProfilResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.RetrofitInstance
import com.example.aplikasi_mobile_mp_kp2.data.repository.EmployeeRepository
import com.example.aplikasi_mobile_mp_kp2.utils.FileUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody

class EmployeeViewModel(application: Application) : AndroidViewModel(application) {

    val employeeInterface = RetrofitInstance.getInstanceEmployeeInterface(application.applicationContext)
    val employeeRepository = EmployeeRepository(employeeInterface)

    private val _response_create_notif = MutableLiveData<NetworkResponse<NotificationResponse>>()

    private val _response_upload_bukti_tugas = MutableLiveData<NetworkResponse<UploadBuktiTugasResponse>>()
    private val _response_upload_foto_profile = MutableLiveData<NetworkResponse<UploadFotoProfilResponse>>()

    private val _response_data_user = MutableLiveData<NetworkResponse<KaryawanX>>()

    private val _response_list_notif = MutableLiveData<NetworkResponse<NotificationListResponse>>()

    val response_list_notif = _response_list_notif

    val response_create_notif = _response_create_notif
    val response_upload_bukti_tugas: LiveData<NetworkResponse<UploadBuktiTugasResponse>> = _response_upload_bukti_tugas
    val response_upload_foto_profil = _response_upload_foto_profile

    val response_data_user = _response_data_user


    fun uploadBuktiTugas(idTugas: Int, fileUri: Uri) {
        _response_upload_bukti_tugas.postValue(NetworkResponse.LOADING)

        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext

                val file = FileUtil.from(context, fileUri) // gunakan helper
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val multipart = MultipartBody.Part.createFormData("file", file.name, requestFile)

                val response = employeeRepository.uploadBuktiTugas(idTugas, multipart)

                if (response.isSuccessful && response.body() != null) {
                    _response_upload_bukti_tugas.postValue(NetworkResponse.SUCCESS(response.body()!!))
                } else {
                    _response_upload_bukti_tugas.postValue(NetworkResponse.ERROR("Gagal mengunggah bukti."))
                    Log.e("ERROR_FILE_UPLOAD", "Code: ${response.code()}, Message: ${response.message()}, ErrorBody: ${response.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                _response_upload_bukti_tugas.postValue(NetworkResponse.ERROR("Error: ${e.message}"))
                Log.e("ERROR_UPLOAD_FILE", "Exception saat upload", e)
            }
        }
    }

    fun getDataUser() {
        _response_data_user.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = employeeRepository.getDataUser()
                if(response.isSuccessful && response.body() != null) {
                    _response_data_user.postValue(NetworkResponse.SUCCESS(response.body()!!))
                } else {
                    if(response.body() == null || !response.isSuccessful) {
                        _response_data_user.postValue(NetworkResponse.ERROR("Kesalahan autentikasi (kayaknya)"))
                    }
                }
            } catch (e : Exception) {
                _response_data_user.postValue(NetworkResponse.ERROR("kalau ga salah server salah kodenya (kyknya)"))
            }
        }
    }

    fun uploadFotoProfile(uri: Uri, context: Context) {
        _response_upload_foto_profile.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val file = FileUtil.from(context, uri) // kamu bisa pakai helper FileUtil untuk convert Uri jadi File
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val fotoPart = MultipartBody.Part.createFormData("foto", file.name, requestFile)

                val response = employeeRepository.uploadFotoProfil(fotoPart)
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        _response_upload_foto_profile.postValue(NetworkResponse.SUCCESS(it))
                    }
                } else {
                    _response_upload_foto_profile.postValue(NetworkResponse.ERROR("Gagal upload, kesalahan autentikasi"))
                }
            } catch (e: Exception) {
                _response_upload_foto_profile.postValue(NetworkResponse.ERROR(e.message ?: "Error"))
            }
        }
    }

    fun postNotification(notificationRequest: NotificationRequest) {
        _response_create_notif.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = employeeRepository.createNotification(notificationRequest)
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        _response_create_notif.postValue(NetworkResponse.SUCCESS(it))
                    }
                } else {
                    _response_create_notif.postValue(NetworkResponse.ERROR("Gagal buat notifikasi"))
                }
            } catch (e: Exception) {
                _response_create_notif.postValue(NetworkResponse.ERROR(e.message ?: "Error"))
            }
        }
    }

    fun getNotification() {
        _response_list_notif.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = employeeRepository.getAllDataNotification()
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        _response_list_notif.postValue(NetworkResponse.SUCCESS(it))
                    }
                } else {
                    _response_list_notif.postValue(NetworkResponse.ERROR("Gagal ambil notifikasi"))
                }
            } catch (e: Exception) {
                _response_list_notif.postValue(NetworkResponse.ERROR(e.message ?: "Error"))
            }
        }
    }

}
