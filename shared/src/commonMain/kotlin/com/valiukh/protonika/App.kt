package com.valiukh.protonika

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.valiukh.protonika.compose.theme.AppTheme
import com.valiukh.protonika.di.appModules
import com.valiukh.protonika.operational.OperationalConsoleScreen
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration

@PreviewLightDark
@Composable
fun App() {
    KoinApplication(
        configuration = koinConfiguration { modules(appModules) },
    ) {
        AppTheme {
            OperationalConsoleScreen()
        }
    }
}