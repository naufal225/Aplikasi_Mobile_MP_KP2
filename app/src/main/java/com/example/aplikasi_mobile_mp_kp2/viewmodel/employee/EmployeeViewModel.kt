package com.example.aplikasi_mobile_mp_kp2.viewmodel.employee

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_mobile_mp_kp2.data.model.UploadBuktiTugasResponse
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

    private val _response_upload_bukti_tugas = MutableLiveData<NetworkResponse<UploadBuktiTugasResponse>>()
    val response_upload_bukti_tugas: LiveData<NetworkResponse<UploadBuktiTugasResponse>> = _response_upload_bukti_tugas

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
}
