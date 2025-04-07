package com.example.aplikasi_mobile_mp_kp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.aplikasi_mobile_mp_kp2.navigation.NavGraph
import com.example.aplikasi_mobile_mp_kp2.ui.theme.Aplikasi_Mobile_MP_KP2Theme
import com.example.aplikasi_mobile_mp_kp2.viewmodel.AuthViewModel
import com.example.aplikasi_mobile_mp_kp2.viewmodel.AuthViewModelFactory
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val factory = AuthViewModelFactory(application)
        val managerViewModelFactory = ManagerViewModelFactory(application)
        val authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class]
        val managerViewModel = ViewModelProvider(this, managerViewModelFactory)[ManagerViewModel::class]
        setContent {
            val navController = rememberNavController()
            Aplikasi_Mobile_MP_KP2Theme {
                NavGraph(navController, authViewModel = authViewModel, managerViewModel = managerViewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Aplikasi_Mobile_MP_KP2Theme {
        Greeting("Android")
    }
}