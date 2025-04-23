package com.example.aplikasi_mobile_mp_kp2.screens.manager.project

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.aplikasi_mobile_mp_kp2.R
import com.example.aplikasi_mobile_mp_kp2.data.model.NotificationRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.TugasWithBuktiResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateStatusTaskAndProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.RetrofitInstance
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel
import kotlinx.coroutines.delay

// Define a dark gray color for consistent use throughout the UI
private val DarkGray = Color(0xFF333333)
private val LightGray = Color(0xFFF5F5F5)

@Composable
fun ManagerProjectBuktiTaskScreen(
    taskId: String,
    navController: NavController,
    managerViewModel: ManagerViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val responseTugasWithBuktiResponse by managerViewModel.response_tugas_by_id_tugas_with_bukti.observeAsState()
    var tugas by remember { mutableStateOf<TugasWithBuktiResponse?>(null) }
    val responseUpdateStatus by managerViewModel.response_update_status_tugas.observeAsState()

    LaunchedEffect(Unit) {
        delay(5000)
    }

    var selectedImageUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(responseUpdateStatus) {
        when(responseUpdateStatus) {
            is NetworkResponse.ERROR -> {
                val res = (responseUpdateStatus as NetworkResponse.ERROR).message
                Log.e("ERROR_UPDATE_STATUS_DONE", res)
            }
            NetworkResponse.LOADING -> {
                Log.e("LOADING_UPDATE_STATUS_DONE", "LOADING")
            }
            is NetworkResponse.SUCCESS -> {
                managerViewModel.response_update_status_tugas.postValue(null)
                Toast.makeText(context, "Berhasil ubah status tugas", Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.ManagerProjectDetail.managerProjectDetailWithId(tugas!!.idProyek.toString()))
            }
            null -> {}
        }
    }

    // Ambil data dari API saat ID task berubah
    LaunchedEffect(taskId) {
        managerViewModel.getTugasByIdTugasWithBukti(taskId.toInt())
    }

    // Respon hasil pemanggilan API
    LaunchedEffect(responseTugasWithBuktiResponse) {
        when (responseTugasWithBuktiResponse) {
            is NetworkResponse.ERROR -> {
                val err = (responseTugasWithBuktiResponse as NetworkResponse.ERROR).message.toString()
                Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                Log.e("GET_TUGAS_WITH_BUKTI_ERR", err)
            }

            NetworkResponse.LOADING -> {
                Log.d("GET_TUGAS_WITH_BUKTI_LOADING", "Loading tugas dengan bukti...")
            }

            is NetworkResponse.SUCCESS -> {
                tugas = (responseTugasWithBuktiResponse as NetworkResponse.SUCCESS).data.data
                Log.d("GET_TUGAS_WITH_BUKTI_SUCCESS", "Tugas berhasil diambil")
            }

            null -> {}
        }
    }

    // UI
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        tugas?.let { task ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Task title and details
                Text(
                    text = "Bukti Pengerjaan Tugas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = task.namaTugas,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (task.file_bukti_tugas.isEmpty()) {
                    Text(
                        "Belum ada bukti pengerjaan.",
                        color = DarkGray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    task.file_bukti_tugas.forEach { bukti ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    selectedImageUrl = RetrofitInstance.BASE_URL_STORAGE + bukti.pathFile
                                }
                        ) {
                            val painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(RetrofitInstance.BASE_URL_STORAGE + bukti.pathFile) // Pastikan ini URL lengkap ya
                                    .crossfade(true)
                                    .diskCachePolicy(CachePolicy.ENABLED) // Ini penting buat persistent caching
                                    .build()
                            )

                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16 / 9f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(2.dp, LightGray, RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Tindakan:",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            managerViewModel.updateStatusTugas(taskId.toInt(), UpdateStatusTaskAndProjectRequest("done"))
                            managerViewModel.postNotification(
                                NotificationRequest(
                                    "Tugas diterima",
                                    "Tugas ${task.namaTugas} disetujui manajer",
                                    "karyawan",
                                    task.idKaryawan
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Terima dan Selesaikan Tugas", modifier = Modifier.padding(vertical = 4.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            managerViewModel.updateStatusTugas(taskId.toInt(), UpdateStatusTaskAndProjectRequest("in-progress"))
                            managerViewModel.postNotification(
                                NotificationRequest(
                                    "Tugas ditolak",
                                    "Tugas ${task.namaTugas} ditolak manajer",
                                    "karyawan",
                                    task.idKaryawan
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkGray,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Tolak Bukti dan Kembalikan ke In-Progress", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        } ?: run {
            // Tugas belum dimuat (loading state atau null)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Memuat data tugas...",
                    color = DarkGray,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

    }

    selectedImageUrl?.let { url ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .zIndex(9999f) // Pastikan di paling atas
        ) {
            GambarBuktiFullscreenZoomable(
                imageUrl = url,
                onDismiss = { selectedImageUrl = null }
            )
        }
    }
}

@Composable
fun GambarBuktiFullscreenZoomable(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    val scale = remember { mutableStateOf(1f) }
    val transformableState = rememberTransformableState { zoomChange, _, _ ->
        scale.value *= zoomChange
    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl) // Pastikan ini URL lengkap ya
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED) // Ini penting buat persistent caching
            .build()
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { onDismiss() } // Tap mana saja buat keluar
            .pointerInput(Unit) {} // Biar klik benar-benar nempel
    ) {
        Image(
            painter,
            contentDescription = "Fullscreen image",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    transformOrigin = TransformOrigin.Center
                )
                .transformable(state = transformableState),
            contentScale = ContentScale.Fit
        )
    }
}
