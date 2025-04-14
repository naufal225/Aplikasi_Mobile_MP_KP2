package com.example.aplikasi_mobile_mp_kp2.screens.manager.project

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplikasi_mobile_mp_kp2.data.model.KaryawanItem
import com.example.aplikasi_mobile_mp_kp2.data.model.ProjectUpdateTaskRequest
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel
import java.text.SimpleDateFormat
import java.util.*

// Define a dark gray color for consistent use throughout the UI
private val DarkGray = Color(0xFF333333)
private val LightGray = Color(0xFFF5F5F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerProjectUpdateTaskScreen(
    taskId: Int,
    managerViewModel: ManagerViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val response_tugas_by_id_tugas by managerViewModel.response_tugas_by_id_tugas.observeAsState()
    val response_update_tugas by managerViewModel.response_update_tugas.observeAsState()

    LaunchedEffect(response_update_tugas) {
        when(response_update_tugas) {
            is NetworkResponse.ERROR -> {
                Toast.makeText(context, (response_update_tugas as NetworkResponse.ERROR).message, Toast.LENGTH_SHORT).show()
                val res = (response_update_tugas as NetworkResponse.ERROR).message
                Log.d("ERROR_UPDATE_TASK", res)
            }
            NetworkResponse.LOADING -> {

            }
            is NetworkResponse.SUCCESS -> {
                val id_proyek = (response_update_tugas as NetworkResponse.SUCCESS).data.data!!.idProyek
                managerViewModel.response_update_tugas.postValue(null)
                managerViewModel.getTugasByIdProyek(id_proyek)
                Toast.makeText(context, "Berhasil memperbarui data tugas", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            null -> {}
        }
    }

    LaunchedEffect(Unit) {
        managerViewModel.getKaryawanInDivisi()
    }

    LaunchedEffect(taskId) {
        managerViewModel.getTugasByIdTugas(taskId)
    }

    var karyawanList by remember { mutableStateOf<List<KaryawanItem>>(emptyList()) }

    val responseKaryawanDivisiResponse by managerViewModel.response_karyawan_in_divisi.observeAsState()
    val responseAddTask by managerViewModel.response_add_tugas.observeAsState()

    LaunchedEffect(responseKaryawanDivisiResponse) {
        when (responseKaryawanDivisiResponse) {
            is NetworkResponse.SUCCESS -> {
                val data = (responseKaryawanDivisiResponse as NetworkResponse.SUCCESS).data.data.karyawan
                karyawanList = data.map { KaryawanItem(it.id, it.namaLengkap) }
            }
            is NetworkResponse.ERROR -> {
                Log.e("AddTask", "Error: ${(responseKaryawanDivisiResponse as NetworkResponse.ERROR).message}")
            }
            else -> {}
        }
    }

    // Form State
    var namaTugas by remember { mutableStateOf("") }
    var deskripsiTugas by remember { mutableStateOf("") }
    var tenggatWaktuMillis by remember { mutableStateOf<Long?>(null) }
    var selectedKaryawan by remember { mutableStateOf<KaryawanItem?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var karyawanExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val formattedDate = remember(tenggatWaktuMillis) {
        tenggatWaktuMillis?.let { dateFormatter.format(Date(it)) } ?: ""
    }

    LaunchedEffect(response_tugas_by_id_tugas) {
        when (response_tugas_by_id_tugas) {
            is NetworkResponse.SUCCESS -> {
                val data = (response_tugas_by_id_tugas as NetworkResponse.SUCCESS).data.data

                data?.let {
                    namaTugas = it.namaTugas
                    deskripsiTugas = it.deskripsiTugas

                    // Konversi string ke millis
                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    try {
                        val date = formatter.parse(it.tenggatWaktu)
                        tenggatWaktuMillis = date?.time
                    } catch (e: Exception) {
                        Log.e("ManagerProjectUpdateTask", "Date parsing error: ${e.message}")
                    }

                    // Set selected karyawan dari id_karyawan
                    selectedKaryawan = karyawanList.find { karyawan -> karyawan.id == it.idKaryawan }
                }
            }

            is NetworkResponse.ERROR -> {
                errorMessage = (response_tugas_by_id_tugas as NetworkResponse.ERROR).message
                Log.e("ManagerProjectUpdateTaskGetTugas", "Error: $errorMessage")
            }

            is NetworkResponse.LOADING -> {
                Log.d("ManagerProjectUpdateTaskgetTugas", "LOADING")
            }

            else -> {
                // do nothing
            }
        }
    }

    if (karyawanList.isEmpty()) {
        managerViewModel.getKaryawanInDivisi()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Edit Data Tugas",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = namaTugas,
            onValueChange = {
                namaTugas = it
                errorMessage = null
            },
            label = { Text("Nama Tugas", color = DarkGray) },
            modifier = Modifier.fillMaxWidth(),
            isError = namaTugas.isBlank(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = com.example.aplikasi_mobile_mp_kp2.screens.login.DarkGray,
                focusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        OutlinedTextField(
            value = deskripsiTugas,
            onValueChange = {
                deskripsiTugas = it
                errorMessage = null
            },
            label = { Text("Deskripsi Tugas", color = DarkGray) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            isError = deskripsiTugas.isBlank(),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = com.example.aplikasi_mobile_mp_kp2.screens.login.DarkGray,
                focusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Column {
            Text(
                "Tenggat Waktu",
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                color = LightGray
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formattedDate.ifEmpty { "Pilih tenggat waktu" },
                        color = if (formattedDate.isNotEmpty()) Color.Black
                        else DarkGray.copy(alpha = 0.6f)
                    )
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            Icons.Outlined.CalendarMonth,
                            contentDescription = "Pilih Tanggal",
                            tint = Color.Black
                        )
                    }
                }
            }
        }

        Column {
            Text(
                "Karyawan",
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            ExposedDropdownMenuBox(
                expanded = karyawanExpanded,
                onExpandedChange = { karyawanExpanded = !karyawanExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedKaryawan?.nama ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Pilih Karyawan", color = DarkGray) },
                    trailingIcon = {
                        Icon(
                            if (karyawanExpanded)
                                Icons.Outlined.KeyboardArrowUp
                            else
                                Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "Expand dropdown",
                            tint = Color.Black
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = TextFieldDefaults.colors(
                        unfocusedTextColor = com.example.aplikasi_mobile_mp_kp2.screens.login.DarkGray,
                        focusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                ExposedDropdownMenu(
                    expanded = karyawanExpanded,
                    onDismissRequest = { karyawanExpanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    karyawanList.forEach { karyawan ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    karyawan.nama,
                                    color = Color.Black
                                )
                            },
                            onClick = {
                                selectedKaryawan = karyawan
                                karyawanExpanded = false
                                errorMessage = null
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = Color.Black,
                                leadingIconColor = Color.Black,
                                trailingIconColor = Color.Black,
                                disabledTextColor = DarkGray,
                                disabledLeadingIconColor = DarkGray,
                                disabledTrailingIconColor = DarkGray
                            )
                        )
                    }
                }
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = {
                when {
                    namaTugas.isBlank() -> errorMessage = "Nama tugas wajib diisi"
                    deskripsiTugas.isBlank() -> errorMessage = "Deskripsi tugas wajib diisi"
                    tenggatWaktuMillis == null -> errorMessage = "Tenggat waktu wajib dipilih"
                    selectedKaryawan == null -> errorMessage = "Karyawan harus dipilih"
                    else -> {
                        errorMessage = null
                        val formatted = dateFormatter.format(Date(tenggatWaktuMillis!!))
                        val request = ProjectUpdateTaskRequest(
                            nama_tugas = namaTugas,
                            deskripsi_tugas = deskripsiTugas,
                            tenggat_waktu = formatted,
                            id_karyawan = selectedKaryawan!!.id
                        )
                        managerViewModel.updateTugas(taskId, request)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Edit Tugas")
        }
    }

    // Optional: Handler setelah tugas berhasil ditambahkan
    LaunchedEffect(responseAddTask) {
        if (responseAddTask is NetworkResponse.SUCCESS) {
            navController.popBackStack() // Kembali ke halaman sebelumnya
        } else if (responseAddTask is NetworkResponse.ERROR) {
            errorMessage = (responseAddTask as NetworkResponse.ERROR).message
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialDisplayMode = DisplayMode.Picker,
            initialSelectedDateMillis = tenggatWaktuMillis ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        tenggatWaktuMillis = datePickerState.selectedDateMillis
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = DarkGray
                    )
                ) {
                    Text("Batal")
                }
            },
            colors = DatePickerDefaults()
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults()
            )
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