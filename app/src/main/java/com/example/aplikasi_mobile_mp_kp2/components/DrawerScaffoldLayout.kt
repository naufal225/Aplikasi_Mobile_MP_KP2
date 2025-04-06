package com.example.aplikasi_mobile_mp_kp2.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerScaffoldLayout(
    navController: NavHostController, // navController utama
    drawerRoutes: List<Routes>,
    startDestination: String,
    graphRoute: String,
    buildNavGraph: NavGraphBuilder.() -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val drawerNavController = rememberNavController()
    val currentRoute = drawerNavController.currentBackStackEntryAsState().value?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                drawerRoutes.forEach { route ->
                    NavigationDrawerItem(
                        label = { Text(route.route) },
                        selected = currentRoute == route.route,
                        onClick = {
                            drawerNavController.navigate(route.route) {
                                popUpTo(graphRoute) { inclusive = false }
                                launchSingleTop = true
                            }
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentRoute ?: "Beranda") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = drawerNavController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding),
                route = graphRoute,
                builder = buildNavGraph
            )
        }
    }
}
