package com.example.aplikasi_mobile_mp_kp2.screens.manager.project

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplikasi_mobile_mp_kp2.data.model.KaryawanItem
import com.example.aplikasi_mobile_mp_kp2.data.model.ProjectAddTaskRequest
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerProjectAddTaskScreen(
    projectId: Int,
    managerViewModel: ManagerViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        managerViewModel.getKaryawanInDivisi()
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

    LaunchedEffect(responseAddTask) {
        responseAddTask?.let {
            when(it) {
                is NetworkResponse.ERROR -> {
                    errorMessage = (responseAddTask as NetworkResponse.ERROR).message
                    Log.d("ERROR", "ERROR TUGAS")
                }
                NetworkResponse.LOADING -> {}
                is NetworkResponse.SUCCESS -> {
                    navController.navigate(Routes.ManagerProjectDetail.route)
                    managerViewModel.response_add_tugas.postValue(null)
                }
                else -> {}
            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Tambah Tugas Baru", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = namaTugas,
            onValueChange = {
                namaTugas = it
                errorMessage = null
            },
            label = { Text("Nama Tugas") },
            modifier = Modifier.fillMaxWidth(),
            isError = namaTugas.isBlank(),
            singleLine = true
        )

        OutlinedTextField(
            value = deskripsiTugas,
            onValueChange = {
                deskripsiTugas = it
                errorMessage = null
            },
            label = { Text("Deskripsi Tugas") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            isError = deskripsiTugas.isBlank()
        )

        Column {
            Text("Tenggat Waktu", fontWeight = FontWeight.Medium)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant
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
                        color = if (formattedDate.isNotEmpty()) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Outlined.CalendarMonth, contentDescription = "Pilih Tanggal")
                    }
                }
            }
        }

        Column {
            Text("Karyawan", fontWeight = FontWeight.Medium)
            ExposedDropdownMenuBox(
                expanded = karyawanExpanded,
                onExpandedChange = { karyawanExpanded = !karyawanExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedKaryawan?.nama ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Pilih Karyawan") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = karyawanExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = karyawanExpanded,
                    onDismissRequest = { karyawanExpanded = false }
                ) {
                    karyawanList.forEach { karyawan ->
                        DropdownMenuItem(
                            text = { Text(karyawan.nama) },
                            onClick = {
                                selectedKaryawan = karyawan
                                karyawanExpanded = false
                                errorMessage = null
                            }
                        )
                    }
                }
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
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
                        val request = ProjectAddTaskRequest(
                            nama_tugas = namaTugas,
                            deskripsi_tugas = deskripsiTugas,
                            tenggat_waktu = formatted,
                            id_karyawan = selectedKaryawan!!.id
                        )
                        managerViewModel.addTugasToProyek(projectId, request)
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Tugas")
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
                TextButton(onClick = {
                    tenggatWaktuMillis = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Batal") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
