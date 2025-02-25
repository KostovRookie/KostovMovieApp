package com.example.kostovapp.mainactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.kostovapp.navigation.MoviesNavHost

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
    MoviesNavHost(navController = navController)
}