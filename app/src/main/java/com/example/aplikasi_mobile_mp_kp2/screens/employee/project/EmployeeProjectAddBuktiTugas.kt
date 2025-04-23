package com.example.aplikasi_mobile_mp_kp2.screens.employee.project

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.viewmodel.employee.EmployeeViewModel

// Define a dark gray color for consistent use throughout the UI
private val DarkGray = Color(0xFF333333)
private val LightGray = Color(0xFFF5F5F5)

@Composable
fun EmployeeProjectAddBuktiTugas(
    modifier: Modifier = Modifier,
    employeeViewModel: EmployeeViewModel,
    taskId: Int,
    navController: NavController
) {
    val context = LocalContext.current
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val response by employeeViewModel.response_upload_bukti_tugas.observeAsState()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        selectedImageUris = uris
    }

    // 🔄 LaunchedEffect untuk response
    LaunchedEffect(response) {
        when (val result = response) {
            is NetworkResponse.SUCCESS -> {
                isLoading = false
                Toast.makeText(context, "Berhasil upload bukti tugas", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
                employeeViewModel.response_upload_bukti_tugas.postValue(null)
            }

            is NetworkResponse.ERROR -> {
                isLoading = false
                Toast.makeText(context, "Gagal: ${result.message}", Toast.LENGTH_SHORT).show()
            }

            is NetworkResponse.LOADING -> {
                isLoading = true
            }

            else -> {
                isLoading = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black
            )
        ) {
            Text(
                "Upload Bukti Tugas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Image selection area
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(LightGray)
                .border(
                    width = 1.dp,
                    color = DarkGray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                ),
        ) {
            items(selectedImageUris) { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

        }

        // Select image button
        Button(
            onClick = {
                launcher.launch("image/*")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = "Select Image",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pilih Gambar")
        }

        // Upload button
        Button(
            onClick = {
                if (selectedImageUris.isNotEmpty()) {
                    selectedImageUris.let { uri ->
                        employeeViewModel.uploadBuktiTugas(fileUris = uri, idTugas = taskId)
                    }
                } else {
                    Toast.makeText(context, "Pilih gambar dulu!", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !isLoading && selectedImageUris.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray.copy(alpha = 0.5f),
                disabledContentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Uploading...")
            } else {
                Icon(
                    imageVector = Icons.Outlined.CloudUpload,
                    contentDescription = "Upload",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Upload Bukti")
            }
        }

        // Back button
        Button(
            onClick = {
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kembali")
        }
    }
}