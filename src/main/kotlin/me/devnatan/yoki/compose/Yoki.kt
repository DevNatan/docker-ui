package me.devnatan.yoki.compose

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import me.devnatan.yoki.Yoki

public val LocalYoki: ProvidableCompositionLocal<Yoki> =
    staticCompositionLocalOf {
        error("Yoki client instance not provided")
    }