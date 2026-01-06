package com.example.placy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.ktor.websocket.Frame
import kotlinx.datetime.Month
import org.jetbrains.compose.ui.tooling.preview.Preview
import placy.composeapp.generated.resources.Res
import placy.composeapp.generated.resources.pin
import ru.sulgik.mapkit.compose.Placemark
import ru.sulgik.mapkit.compose.YandexMap
import ru.sulgik.mapkit.compose.imageProvider
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.compose.rememberPlacemarkState
import ru.sulgik.mapkit.geometry.Point
import ru.sulgik.mapkit.map.CameraPosition

@Composable
@Preview
fun TempApp() {
    MaterialTheme {
        TempMapScreen()
    }
}

@Composable
fun TempMapScreen() {
    val navController = rememberNavController()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(
            Point(55.751225, 37.62954),
            17.0f,
            150.0f,
            30.0f
        )
    }
    val imageProvider = imageProvider(Res.drawable.pin)

    // Навигационный граф
    NavHost(
        navController = navController,
        startDestination = "map"
    ) {
        composable("map") {
            // Основной экран с картой и кнопкой
            Box(modifier = Modifier.fillMaxSize()) {
                // Карта
                YandexMap(
                    cameraPositionState = cameraPositionState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Placemark(
                        state = rememberPlacemarkState(Point(55.751225, 37.62954)),
                        icon = imageProvider
                    )
                }

                // Кнопка камеры
                @OptIn(ExperimentalMaterial3Api::class)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            bottom = WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateBottomPadding() + 16.dp
                        ),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        onClick = {
                            // Навигация на экран камеры
                            navController.navigate("camera")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "Камера",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        }

        composable("camera") {
            // Экран камеры
            CameraView(navController = navController)
        }
    }
}