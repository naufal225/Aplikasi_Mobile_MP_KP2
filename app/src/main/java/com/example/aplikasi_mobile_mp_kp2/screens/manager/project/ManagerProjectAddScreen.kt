package com.example.aplikasi_mobile_mp_kp2.screens.manager.project

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.aplikasi_mobile_mp_kp2.data.model.AddProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerProjectAddScreen(
    managerViewModel: ManagerViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var namaProyek by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }

    // Date states
    var tanggalMulaiMillis by remember { mutableStateOf<Long?>(null) }
    var tenggatWaktuMillis by remember { mutableStateOf<Long?>(null) }

    // Date picker dialog states
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Date formatter
    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    // Format dates for display
    val formattedStartDate = remember(tanggalMulaiMillis) {
        tanggalMulaiMillis?.let { dateFormatter.format(Date(it)) } ?: ""
    }

    val formattedEndDate = remember(tenggatWaktuMillis) {
        tenggatWaktuMillis?.let { dateFormatter.format(Date(it)) } ?: ""
    }
    val context = LocalContext.current
    val responseAddProject = managerViewModel.response_add_project.observeAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {

            Text(
                "Tambah Proyek Baru",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(16.dp)
            )


        }

        Button(
            onClick = {
                navController.navigate(Routes.ManagerProjects.route)
            }
        ) {
            Text("<- Kembali")
        }

        // Project name field
        OutlinedTextField(
            value = namaProyek,
            onValueChange = { namaProyek = it },
            label = { Text("Nama Proyek") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Project description field
        OutlinedTextField(
            value = deskripsi,
            onValueChange = { deskripsi = it },
            label = { Text("Deskripsi Proyek") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        // Start date picker
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Tanggal Mulai",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (formattedStartDate.isNotEmpty()) formattedStartDate else "Pilih tanggal mulai",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (formattedStartDate.isNotEmpty())
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )

                    IconButton(onClick = { showStartDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = "Pilih tanggal mulai",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // End date picker
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Tenggat Waktu",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (formattedEndDate.isNotEmpty()) formattedEndDate else "Pilih tenggat waktu",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (formattedEndDate.isNotEmpty())
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )

                    IconButton(onClick = { showEndDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = "Pilih tenggat waktu",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Error message
        if (errorMessage != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    errorMessage ?: "",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Save button
        Button(
            onClick = {
                if (namaProyek.isBlank() || deskripsi.isBlank() || tanggalMulaiMillis == null || tenggatWaktuMillis == null) {
                    errorMessage = "Semua kolom harus diisi."
                } else if (tenggatWaktuMillis!! < tanggalMulaiMillis!!) {
                    errorMessage = "Tenggat waktu tidak boleh sebelum tanggal mulai."
                } else {
                    // Format dates for API
                    val tanggalMulai = dateFormatter.format(Date(tanggalMulaiMillis!!))
                    val tenggatWaktu = dateFormatter.format(Date(tenggatWaktuMillis!!))

                    val request = AddProjectRequest(
                        nama_proyek = namaProyek,
                        deskripsi_proyek = deskripsi,
                        tanggal_mulai = tanggalMulai,
                        tenggat_waktu = tenggatWaktu
                    )

                    managerViewModel.addDataProject(request)
                }
            },

            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Proyek")

            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current
            val responseAddProject = managerViewModel.response_add_project.observeAsState()

// Tambahkan indikator loading atau feedback
            when (val result = responseAddProject.value) {
                is NetworkResponse.LOADING -> {
                    Column {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                }
                is NetworkResponse.SUCCESS -> {
                    // Navigasi balik atau tampilkan snackbar
                    LaunchedEffect(Unit) {
                        Toast.makeText(context, "Proyek berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
                is NetworkResponse.ERROR -> {
                    LaunchedEffect(result.message) {
                        errorMessage = result.message
                    }
                }

                null -> {}
            }

        }
    }

    // Start Date Picker Dialog
    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialDisplayMode = DisplayMode.Picker,
            initialSelectedDateMillis = tanggalMulaiMillis ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        tanggalMulaiMillis = datePickerState.selectedDateMillis
                        showStartDatePicker = false
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showStartDatePicker = false },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Batal")
                }
            },
            modifier = Modifier.padding(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    "Pilih Tanggal Mulai",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    "Pilih tanggal mulai proyek",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                DatePicker(
                    state = datePickerState,
                    showModeToggle = true,
                    title = null,
                    headline = null,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }

    // End Date Picker Dialog
    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialDisplayMode = DisplayMode.Picker,
            initialSelectedDateMillis = tenggatWaktuMillis ?: System.currentTimeMillis()
        )

        // Set minimum date to start date if available
        val validatorForEndDate = remember(tanggalMulaiMillis) {
            if (tanggalMulaiMillis != null) {
                { timestamp: Long ->
                    timestamp >= tanggalMulaiMillis!!
                }
            } else {
                { _: Long -> true }
            }
        }

        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        tenggatWaktuMillis = datePickerState.selectedDateMillis
                        showEndDatePicker = false
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEndDatePicker = false },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Batal")
                }
            },
            modifier = Modifier.padding(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    "Pilih Tenggat Waktu",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    "Pilih tenggat waktu proyek",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                DatePicker(
                    state = datePickerState,
                    showModeToggle = true,
                    title = null,
                    headline = null,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

