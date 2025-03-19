package com.example.kostovapp.mainactivity

import com.example.kostovapp.ui.screens.WelcomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.kostovapp.navigationcomponents.BottomNavigationBar
import com.example.kostovapp.navigation.MoviesNavHost
import com.example.kostovapp.viewmodel.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MoviesAppNavigation()
                }
            }
        }
    }
}

@Composable
fun MoviesAppNavigation() {
    val navController = rememberNavController()
    val viewModel: MoviesViewModel = koinViewModel()
    val showDialog = remember { mutableStateOf(true) }
    var selectedTabIndex = remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, selectedTabIndex.intValue) { newIndex ->
                selectedTabIndex.intValue = newIndex
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            MoviesNavHost(
                navController = navController, viewModel = viewModel,
                selectedTabIndex = selectedTabIndex.intValue, onTabSelected = { newIndex ->
                    selectedTabIndex.intValue = newIndex
                })
        }
    }

    if (showDialog.value) {
        WelcomeScreen(onDismiss = { showDialog.value = false })
    }
}