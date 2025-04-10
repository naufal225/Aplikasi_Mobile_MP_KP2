package com.example.aplikasi_mobile_mp_kp2.screens.employee.profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.aplikasi_mobile_mp_kp2.data.model.KaryawanX
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.RetrofitInstance
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.viewmodel.AuthViewModel
import com.example.aplikasi_mobile_mp_kp2.viewmodel.employee.EmployeeViewModel

@Composable
fun EmployeeProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    employeeViewModel: EmployeeViewModel,
    authViewModel: AuthViewModel
) {
    val responseDataUser by employeeViewModel.response_data_user.observeAsState()
    val responseUploadPP by employeeViewModel.response_upload_foto_profil.observeAsState()
    val context = LocalContext.current
    val karyawan = remember { mutableStateOf<KaryawanX?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val logoutResponse by authViewModel.logout_result.observeAsState()

    // Gallery launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> selectedImageUri = uri }
    )

    // Ambil data awal
    LaunchedEffect(Unit) {
        employeeViewModel.getDataUser()
    }

    // Cek hasil pengambilan data user
    LaunchedEffect(responseDataUser) {
        responseDataUser?.let {
            when (it) {
                is NetworkResponse.ERROR -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                is NetworkResponse.SUCCESS -> karyawan.value = it.data
                else -> {}
            }
        }
    }

    // Cek hasil upload foto
    LaunchedEffect(responseUploadPP) {
        responseUploadPP?.let {
            when (it) {
                is NetworkResponse.ERROR -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                is NetworkResponse.SUCCESS -> {
                    employeeViewModel.response_upload_foto_profil.postValue(null)
                    Toast.makeText(context, "Upload berhasil!", Toast.LENGTH_SHORT).show()
                    employeeViewModel.getDataUser() // Refresh data user
                }
                else -> {}
            }
        }
    }

    // Upload jika user memilih gambar
    LaunchedEffect(selectedImageUri) {
        selectedImageUri?.let {
            employeeViewModel.uploadFotoProfile(it, context)
        }
    }

    LaunchedEffect(logoutResponse) {
        logoutResponse?.let {
            when(it) {
                is NetworkResponse.ERROR -> {
                    Log.e("ERROR_LOGOUT", it.message)
                }
                NetworkResponse.LOADING -> {

                }
                is NetworkResponse.SUCCESS -> {
                    navController.navigate(Routes.AuthGraph.route) {
                        popUpTo(0) { inclusive = true } // reset semua stack
                        launchSingleTop = true
                    }
                    authViewModel.logout_result.postValue(null)

                }
            }
        }
    }

    // Loader
    if (karyawan.value == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val data = karyawan.value!!

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = RetrofitInstance.BASE_URL_STORAGE + data.urlFotoProfile,
            contentDescription = "Foto Profil",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = data.namaLengkap, style = MaterialTheme.typography.titleMedium)
        Text(text = data.email, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Icon(Icons.Default.PhotoCamera, contentDescription = "Ganti Foto")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ganti Foto")
        }

        Spacer(modifier = Modifier.height(24.dp))

        ProfileField(label = "Username", value = data.username)
        ProfileField(label = "Jenis Kelamin", value = data.jenisKelamin)
        ProfileField(label = "Nomor Telepon", value = data.nomorTelepon)
        ProfileField(label = "Alamat", value = data.alamat)
        ProfileField(label = "Tanggal Lahir", value = data.tanggalLahir)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                authViewModel.logout()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Icon(Icons.Default.Logout, contentDescription = "Logout")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }
    }
}


@Composable
fun ProfileField(label: String, value: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(6.dp))
                .padding(8.dp)
        )
    }
}
