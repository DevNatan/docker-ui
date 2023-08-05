package me.devnatan.yoki.compose

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import me.devnatan.yoki.Yoki
import me.devnatan.yoki.YokiConfigBuilder
import me.devnatan.yoki.compose.screens.HomeScreen

fun main() {
    val yoki = Yoki.create(YokiConfigBuilder().forCurrentPlatform().build())
    application {
        Window(onCloseRequest = ::exitApplication) {
            CompositionLocalProvider(LocalYoki provides yoki) {
                Navigator(HomeScreen())
            }
        }
    }
}