package com.valiukh.protonika.operational

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valiukh.protonika.compose.components.input.MkButtonSurface
import com.valiukh.protonika.compose.components.input.MkButtonType
import com.valiukh.protonika.compose.components.MkOutputConsole
import com.valiukh.protonika.compose.components.editor.MkProgramEditor
import com.valiukh.protonika.compose.components.input.MkPrimaryFunctionType
import com.valiukh.protonika.compose.components.input.MkSecondaryFunctionType
import com.valiukh.protonika.compose.theme.AppTheme
import com.valiukh.protonika.instrumentation.InstrumentationPanel
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import org.koin.compose.viewmodel.koinViewModel

private const val MK61_EXTENSION = "mk61"
private const val MB61_EXTENSION = "mb61"
private const val DEFAULT_SCRIPT_NAME = "program"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationalConsoleScreen(
    modifier: Modifier = Modifier,
    viewModel: OperationalConsoleViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val openScriptLauncher = rememberFilePickerLauncher(
        type = FileKitType.File(extensions = listOf(MK61_EXTENSION, MB61_EXTENSION)),
        mode = FileKitMode.Single,
        dialogSettings = FileKitDialogSettings.createDefault(),
    ) { file ->
        if (file != null) viewModel.onScriptOpen(file)
        viewModel.onOpenFilePickerHandled()
    }

    val saveScriptLauncher = rememberFileSaverLauncher(
        dialogSettings = FileKitDialogSettings.createDefault(),
    ) { file ->
        if (file != null) viewModel.onScriptSave(file)
        viewModel.onSaveFilePickerHandled()
    }

    OperationalConsoleScreen(
        modifier = modifier,
        state = uiState,
        onKeyPressed = viewModel::onKeyPressed,
    )

    LaunchedEffect(uiState.needOpenScriptFile) {
        if (uiState.needOpenScriptFile) {
            openScriptLauncher.launch()
        }
    }

    LaunchedEffect(uiState.needSaveScriptToFile) {
        if (uiState.needSaveScriptToFile) {
            saveScriptLauncher.launch(
                suggestedName = DEFAULT_SCRIPT_NAME,
                defaultExtension = MK61_EXTENSION,
            )
        }
    }

    if (uiState.needShowInstrumentationPanel) {
        ModalBottomSheet(
            containerColor = AppTheme.colors.instrumentalInverted,
            contentColor = AppTheme.colors.instrumental,
            onDismissRequest = {  },
        ) {
            InstrumentationPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f),
                onScriptApplied = viewModel::onInstrumentationPanelDismissed,
            )
        }
    }
}

@Composable
private fun OperationalConsoleScreen(
    modifier: Modifier = Modifier,
    state: ViewState,
    onKeyPressed: (
        type: MkButtonType,
        primaryType: MkPrimaryFunctionType?,
        secondaryType: MkSecondaryFunctionType?
    ) -> Unit = { _, _, _ -> },
) {
    BoxWithConstraints(
        modifier = modifier
            .background(AppTheme.colors.instrumental)
            .statusBarsPadding()
            .padding(8.dp),
    ) {
        val isLandscape = maxWidth > maxHeight

        if (isLandscape) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MkOutputConsole(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    state = state.outputConsoleState,
                )
                MkProgramEditor(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    instructions = state.instructions,
                )
                MkButtonSurface(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp),
                    isPrimaryFunctionActive = state.isPrimaryFunctionActive,
                    isSecondaryFunctionActive = state.isSecondaryFunctionActive,
                    onClick = onKeyPressed,
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    MkOutputConsole(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .weight(1f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        state = state.outputConsoleState,
                    )
                    MkProgramEditor(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .weight(1f)
                            .fillMaxHeight(),
                        instructions = state.instructions,
                    )
                }
                MkButtonSurface(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp),
                    isPrimaryFunctionActive = state.isPrimaryFunctionActive,
                    isSecondaryFunctionActive = state.isSecondaryFunctionActive,
                    onClick = onKeyPressed,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun OperationalConsoleScreenPreview() {
    AppTheme {
        OperationalConsoleScreen(
            state = ViewState(),
            onKeyPressed = { _, _, _ -> },
        )
    }
}

@PreviewLightDark
@Composable
fun OperationalConsoleScreenWithInstrumentationPreview() {
    AppTheme {
        OperationalConsoleScreen(
            state = ViewState(
                needShowInstrumentationPanel = true
            ),
            onKeyPressed = { _, _, _ -> },
        )
    }
}

@Preview(device = "id:pixel")
@Preview(device = "id:pixel_9a")
@Preview(device = "spec:parent=pixel_fold,orientation=portrait")
@Preview(device = "spec:parent=pixel,orientation=landscape")
@Preview(device = "spec:parent=pixel_9a,orientation=landscape")
@Preview(device = "spec:parent=pixel_fold,orientation=landscape")
@Composable
fun OperationalConsoleConfigurationsPreview() {
    AppTheme {
        OperationalConsoleScreen(
            state = ViewState(),
            onKeyPressed = { _, _, _ -> },
        )
    }
}
