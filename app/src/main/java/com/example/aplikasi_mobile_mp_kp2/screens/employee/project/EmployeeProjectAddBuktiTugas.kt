package com.example.aplikasi_mobile_mp_kp2.screens.employee.project

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.aplikasi_mobile_mp_kp2.data.model.NotificationRequest
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.viewmodel.employee.EmployeeViewModel
import java.io.File

@Composable
fun EmployeeProjectAddBuktiTugas(
    modifier: Modifier = Modifier,
    employeeViewModel: EmployeeViewModel,
    taskId: Int,
    navController: NavController
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val response by employeeViewModel.response_upload_bukti_tugas.observeAsState()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    // 🔄 LaunchedEffect untuk response
    LaunchedEffect(response) {
        when (val result = response) {
            is NetworkResponse.SUCCESS -> {
                Toast.makeText(context, "Berhasil upload bukti tugas", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
                employeeViewModel.response_upload_bukti_tugas.postValue(null)
            }

            is NetworkResponse.ERROR -> {
                Toast.makeText(context, "Gagal: ${result.message}", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Upload Bukti Tugas", style = MaterialTheme.typography.headlineSmall)

        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text("Pilih Gambar")
        }

        selectedImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Preview",
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
        }

        Button(
            onClick = {
                selectedImageUri?.let { uri ->
                    employeeViewModel.uploadBuktiTugas(fileUri = uri, idTugas = taskId)

                } ?: Toast.makeText(context, "Pilih gambar dulu!", Toast.LENGTH_SHORT).show()
            },
            enabled = selectedImageUri != null,
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Upload")
        }
    }
}
