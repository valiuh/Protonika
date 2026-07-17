package com.valiukh.protonika.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// COLOR SCHEME LIGHT
val LightColors = AppColors(
    instrumental = White,
    instrumentalInverted = SmokyUmber,
    primaryBackground = WarmGreige,
    borderOperational = Black,
    primaryOperational = SoftConcrete,
    secondaryOperational = CharcoalMist,
    primaryFunctional = SignalYellow,
    secondaryFunctional = SignalBlue,
    instrumentalFunctional = SignalRed,
    primaryFunctionalText = SignalYellow,
    secondaryFunctionalText = SignalBlue
)

// COLOR SCHEME DARK
val DarkColors = AppColors(
    instrumental = SmokyUmber,
    instrumentalInverted = White,
    primaryBackground = SmokyUmber,
    borderOperational = White,
    primaryOperational = SoftConcrete,
    secondaryOperational = DeepSlate,
    primaryFunctional = SignalYellowDark,
    secondaryFunctional = SignalBlueDark,
    instrumentalFunctional = SignalRedDark,
    primaryFunctionalText = SignalYellowDark,
    secondaryFunctionalText = SignalBlueDark
)


@Immutable
data class AppColors(
    val instrumental: Color,
    val instrumentalInverted: Color,
    val primaryBackground: Color,
    val borderOperational: Color,
    val primaryOperational: Color,
    val secondaryOperational: Color,
    val primaryFunctional: Color,
    val secondaryFunctional: Color,
    val instrumentalFunctional: Color,
    val primaryFunctionalText: Color,
    val secondaryFunctionalText: Color,
)