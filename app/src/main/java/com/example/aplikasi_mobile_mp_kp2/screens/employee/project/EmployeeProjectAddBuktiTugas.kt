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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import coil.compose.rememberAsyncImagePainter
import java.io.File

@Composable
fun EmployeeProjectAddBuktiTugas(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
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
                if (selectedImageUri != null) {
                    // TODO: Ganti logika ini untuk upload ke server pakai ViewModel atau Repository
                    Toast.makeText(context, "Upload bukti berhasil (simulasi)", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Pilih gambar dulu!", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = selectedImageUri != null,
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Upload")
        }
    }
}
