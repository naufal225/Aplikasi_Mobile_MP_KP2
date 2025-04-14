package com.example.aplikasi_mobile_mp_kp2.screens.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aplikasi_mobile_mp_kp2.R
import com.example.aplikasi_mobile_mp_kp2.data.model.LoginRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.LoginResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.viewmodel.AuthViewModel

// Define a dark gray color for consistent use throughout the UI
val DarkGray = Color(0xFF333333)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("mayasari.gaduh") }
    var password by remember { mutableStateOf("password") }
    var passwordVisible by remember { mutableStateOf(false) }

    val loginResult by loginViewModel.login_result.observeAsState()
    val context = LocalContext.current

    // Observe result dan navigasi
    LaunchedEffect(loginResult) {
        when (loginResult) {
            is NetworkResponse.SUCCESS -> {
                val tipe = (loginResult as NetworkResponse.SUCCESS<LoginResponse>).data.tipe
                when (tipe?.lowercase()) {
                    "manager" -> {
                        Log.d("NAVIGATION", "Navigating to manager graph")
                        navController.navigate(Routes.ManagerGraph.route) {
                            popUpTo(Routes.AuthGraph.route) { inclusive = true }
                        }
                    }
                    "employee" -> {
                        navController.navigate(Routes.EmployeeGraph.route) {
                            popUpTo(Routes.AuthGraph.route) { inclusive = true }
                        }
                    }
                    else -> {
                        Toast.makeText(context, "Role tidak dikenal: $tipe", Toast.LENGTH_SHORT).show()
                    }
                }
                loginViewModel.login_result.postValue(null)
            }
            is NetworkResponse.ERROR -> {
                Toast.makeText(context, (loginResult as NetworkResponse.ERROR).message ?: "Login gagal", Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    val isLoading = loginResult is NetworkResponse.LOADING

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo image from drawable resources
            Image(
                painter = painterResource(id = R.drawable.logo_grafit),
                contentDescription = "App Logo",
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Selamat Datang!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Login untuk melanjutkan",
                style = MaterialTheme.typography.bodyLarge,
                color = DarkGray
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email atau Username", color = DarkGray) },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = DarkGray)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = DarkGray,
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(22.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = DarkGray) },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = DarkGray)
                },

                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = DarkGray
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = DarkGray,
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    loginViewModel.login(LoginRequest(email, password))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    disabledContainerColor = DarkGray.copy(alpha = 0.5f),
                    disabledContentColor = Color.White.copy(alpha = 0.7f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Login")
                }
            }
        }
    }
}