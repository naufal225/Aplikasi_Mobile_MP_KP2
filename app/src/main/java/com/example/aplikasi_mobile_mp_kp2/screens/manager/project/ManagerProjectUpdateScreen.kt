package com.example.aplikasi_mobile_mp_kp2.screens.manager.project

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val DarkGray = Color(0xFF333333)
private val LightGray = Color(0xFFF5F5F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerProjectUpdateScreen(
    managerViewModel: ManagerViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    id: String
) {
    // States for form fields
    var namaProyek by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }

    // Date states
    var tanggalMulaiMillis by remember { mutableStateOf<Long?>(null) }
    var tenggatWaktuMillis by remember { mutableStateOf<Long?>(null) }

    // Status options
    val statusOptions = listOf("pending", "in-progress")

    // Dropdown expanded state
    var statusExpanded by remember { mutableStateOf(false) }

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

    // Observe responses
    val responseUpdate by managerViewModel.response_update_project.observeAsState()
    val responseById = managerViewModel.response_by_id_proyek.observeAsState()

    // Fetch project data when screen loads
    LaunchedEffect(id) {
        try {
            val projectId = id.toInt()
            managerViewModel.getDataProyekById(projectId)
        } catch (e: NumberFormatException) {
            errorMessage = "ID proyek tidak valid"
        }
    }

    // Process project data when received
    LaunchedEffect(responseById.value) {
        when (val result = responseById.value) {
            is NetworkResponse.SUCCESS -> {
                val proyek = result.data.data.proyek
                namaProyek = proyek.namaProyek
                deskripsi = proyek.deskripsiProyek
                status = proyek.status

                // Parse dates
                try {
                    val startDate = dateFormatter.parse(proyek.tanggalMulai)
                    val endDate = dateFormatter.parse(proyek.tenggatWaktu)

                    tanggalMulaiMillis = startDate?.time
                    tenggatWaktuMillis = endDate?.time
                } catch (e: Exception) {
                    Log.e("DateParse", "Error parsing dates: ${e.message}")
                }
            }
            is NetworkResponse.ERROR -> {
                errorMessage = "Gagal memuat data proyek: ${result.message}"
            }
            else -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black
            )
        ) {
            Text(
                "Perbarui Data Proyek",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Back button
        Button(
            onClick = {
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGray,
                contentColor = Color.White
            )
        ) {
            Text("<- Kembali")
        }

        OutlinedTextField(
            value = namaProyek,
            onValueChange = {
                namaProyek = it
                errorMessage = null
            },
            label = { Text("Nama Tugas", color = DarkGray) },
            modifier = Modifier.fillMaxWidth(),
            isError = namaProyek.isBlank(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = com.example.aplikasi_mobile_mp_kp2.screens.login.DarkGray,
                focusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        OutlinedTextField(
            value = deskripsi,
            onValueChange = {
                deskripsi = it
                errorMessage = null
            },
            label = { Text("Deskripsi Tugas", color = DarkGray) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            isError = deskripsi.isBlank(),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = com.example.aplikasi_mobile_mp_kp2.screens.login.DarkGray,
                focusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        // Status dropdown
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Status Proyek",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = status,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status", color = DarkGray) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded)
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedTextColor = com.example.aplikasi_mobile_mp_kp2.screens.login.DarkGray,
                        focusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    statusOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, color = Color.Black) },
                            onClick = {
                                status = option
                                statusExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // Start date picker
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Tanggal Mulai",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                color = LightGray
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
                            Color.Black
                        else
                            DarkGray.copy(alpha = 0.7f)
                    )

                    IconButton(onClick = { showStartDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = "Pilih tanggal mulai",
                            tint = Color.Black
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
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                color = LightGray
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
                            Color.Black
                        else
                            DarkGray.copy(alpha = 0.7f)
                    )

                    IconButton(onClick = { showEndDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = "Pilih tenggat waktu",
                            tint = Color.Black
                        )
                    }
                }
            }
        }

        // Error message
        if (errorMessage != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Red.copy(alpha = 0.1f)
                )
            ) {
                Text(
                    errorMessage ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Save button
        Button(
            onClick = {
                if (namaProyek.isBlank() || deskripsi.isBlank() || status.isBlank() ||
                    tanggalMulaiMillis == null || tenggatWaktuMillis == null) {
                    errorMessage = "Semua kolom harus diisi."
                } else if (tenggatWaktuMillis!! < tanggalMulaiMillis!!) {
                    errorMessage = "Tenggat waktu tidak boleh sebelum tanggal mulai."
                } else {
                    // Format dates for API
                    val tanggalMulai = dateFormatter.format(Date(tanggalMulaiMillis!!))
                    val tenggatWaktu = dateFormatter.format(Date(tenggatWaktuMillis!!))

                    val request = UpdateProjectRequest(
                        nama_proyek = namaProyek,
                        status = status,
                        deskripsi_proyek = deskripsi,
                        tanggal_mulai = tanggalMulai,
                        tenggat_waktu = tenggatWaktu
                    )

                    // Convert id to Int and call updateProject
                    try {
                        val projectId = id.toInt()
                        managerViewModel.updateProject(projectId, request)
                    } catch (e: NumberFormatException) {
                        errorMessage = "ID proyek tidak valid"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Simpan Perubahan")
        }
    }

    // Loading indicator for initial data fetch
    when (val result = responseById.value) {
        is NetworkResponse.LOADING -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.8f)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Memuat data proyek...", color = Color.Black)
            }
        }
        else -> {}
    }

    // Handle update response
    when (val result = responseUpdate) {
        is NetworkResponse.LOADING -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.8f)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Menyimpan perubahan...", color = Color.Black)
            }
        }
        is NetworkResponse.SUCCESS -> {
            LaunchedEffect(result) {
                Toast.makeText(context, "Proyek berhasil diperbarui", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            managerViewModel.response_update_project.postValue(null)
        }
        is NetworkResponse.ERROR -> {
            LaunchedEffect(result.message) {
                errorMessage = result.message
            }
        }
        null -> {}
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
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showStartDatePicker = false },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = DarkGray
                    )
                ) {
                    Text("Batal")
                }
            },
            modifier = Modifier.padding(24.dp),
            colors = DatePickerDefaults()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    "Pilih Tanggal Mulai",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.Black
                )

                Text(
                    "Pilih tanggal mulai proyek",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkGray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                DatePicker(
                    state = datePickerState,
                    showModeToggle = true,
                    title = null,
                    headline = null,
                    modifier = Modifier.padding(vertical = 8.dp),
                    colors = DatePickerDefaults()
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
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEndDatePicker = false },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = DarkGray
                    )
                ) {
                    Text("Batal")
                }
            },
            modifier = Modifier.padding(24.dp),
            colors = DatePickerDefaults()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    "Pilih Tenggat Waktu",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.Black
                )

                Text(
                    "Pilih tenggat waktu proyek",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkGray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                DatePicker(
                    state = datePickerState,
                    showModeToggle = true,
                    title = null,
                    headline = null,
                    modifier = Modifier.padding(vertical = 8.dp),
                    colors = DatePickerDefaults()
                )
            }
        }
    }
}

// Custom DatePickerDefaults function to create consistent date picker colors
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDefaults() = DatePickerDefaults.colors(
    containerColor = Color.White,
    titleContentColor = Color.Black,
    headlineContentColor = Color.Black,
    weekdayContentColor = DarkGray,
    subheadContentColor = Color.Black,
    yearContentColor = Color.Black,
    currentYearContentColor = Color.White,
    selectedYearContentColor = Color.White,
    selectedYearContainerColor = Color.Black,
    dayContentColor = Color.Black,
    selectedDayContentColor = Color.White,
    selectedDayContainerColor = Color.Black,
    todayContentColor = Color.Black,
    todayDateBorderColor = Color.Black,
    dayInSelectionRangeContentColor = Color.Black,
    dayInSelectionRangeContainerColor = LightGray
)