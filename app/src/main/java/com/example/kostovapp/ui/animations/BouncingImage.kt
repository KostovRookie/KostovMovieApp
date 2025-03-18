package com.example.kostovapp.ui.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.example.kostovapp.R

@Composable
fun BouncingImage() {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        startAnimation = true
    }

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(600, easing = EaseOutBounce),
        label = "Bouncing Scale"
    )

    Image(
        painter = painterResource(id = R.drawable.profile_picture),
        contentDescription = "Welcome Image",
        modifier = Modifier
            .size(120.dp)
            .scale(scale)
    )
}