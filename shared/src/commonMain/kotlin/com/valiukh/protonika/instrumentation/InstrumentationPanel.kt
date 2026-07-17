package com.valiukh.protonika.instrumentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valiukh.protonika.compose.theme.AppTheme
import com.valiukh.protonika.instrumentation.compose.FeedbackPanel
import com.valiukh.protonika.instrumentation.compose.SessionScriptsPanel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InstrumentationPanel(
    modifier: Modifier = Modifier,
    viewModel: InstrumentationSheetViewModel = koinViewModel(),
    onScriptApplied: () -> Unit = {},
) {
    LaunchedEffect(viewModel) {
        viewModel.init()
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Panel(
        modifier = modifier,
        state = uiState,
        onScripSelected = { position ->
            viewModel.onScriptSelected(position)
        },
        onApplyScript = { position ->
            viewModel.onApplyScript(position)
            onScriptApplied()
        },
        onFeedbackSubmitted = { feedback ->
            viewModel.onFeedbackSubmitted(feedback)
        }
    )
}

@Composable
fun Panel(
    modifier: Modifier = Modifier,
    state: ViewState,
    initialTab: InstrumentationTab = InstrumentationTab.Scripts,
    onScripSelected: (Int) -> Unit,
    onApplyScript: (Int) -> Unit,
    onFeedbackSubmitted: (String) -> Unit,
) {
    var selectedTab by remember { mutableStateOf(initialTab) }

    val colors = AppTheme.colors

    Column(modifier = modifier.fillMaxSize()) {
        PrimaryTabRow(
            containerColor = colors.instrumental.copy(alpha = 0.8f),
            contentColor = colors.instrumentalInverted,
            selectedTabIndex = selectedTab.ordinal
        ) {
            InstrumentationTab.entries.forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    selectedContentColor = colors.instrumentalInverted,
                    unselectedContentColor = colors.instrumentalInverted.copy(alpha = 0.6f),
                    text = { Text(text = tab.title) },
                    icon = { Icon(imageVector = tab.icon, contentDescription = tab.title) },
                )
            }
        }

        when (selectedTab) {
            InstrumentationTab.Scripts -> SessionScriptsPanel(
                modifier = Modifier.fillMaxSize(),
                state = state.scriptsState,
                onScripSelected = onScripSelected,
                onApplyScript = onApplyScript
            )

            InstrumentationTab.Feedback -> FeedbackPanel(
                modifier = Modifier.fillMaxSize(),
                state = state.feedbackState,
                onFeedbackSubmitted = onFeedbackSubmitted,
            )
        }
    }
}

enum class InstrumentationTab(val title: String, val icon: ImageVector) {
    Scripts(title = "Scripts", icon = ScriptsTabIcon),
    Feedback(title = "Feedback", icon = FeedbackTabIcon),
}

private val ScriptsTabIcon: ImageVector = ImageVector.Builder(
    name = "ScriptsTab",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
).apply {
    path(fill = SolidColor(Color.Black)) {
        moveTo(6f, 2f)
        curveToRelative(-1.1f, 0f, -1.99f, 0.9f, -1.99f, 2f)
        lineTo(4f, 20f)
        curveToRelative(0f, 1.1f, 0.89f, 2f, 1.99f, 2f)
        horizontalLineTo(18f)
        curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
        verticalLineTo(8f)
        lineToRelative(-6f, -6f)
        horizontalLineTo(6f)
        close()
        moveToRelative(7f, 7f)
        verticalLineTo(3.5f)
        lineTo(18.5f, 9f)
        horizontalLineTo(13f)
        close()
    }
}.build()

private val FeedbackTabIcon: ImageVector = ImageVector.Builder(
    name = "FeedbackTab",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
).apply {
    path(fill = SolidColor(Color.Black)) {
        moveTo(20f, 2f)
        horizontalLineTo(4f)
        curveToRelative(-1.1f, 0f, -1.99f, 0.9f, -1.99f, 2f)
        lineTo(2f, 22f)
        lineToRelative(4f, -4f)
        horizontalLineToRelative(14f)
        curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
        verticalLineTo(4f)
        curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
        close()
        moveToRelative(-7f, 12f)
        horizontalLineToRelative(-2f)
        verticalLineToRelative(-2f)
        horizontalLineToRelative(2f)
        verticalLineToRelative(2f)
        close()
        moveToRelative(0f, -4f)
        horizontalLineToRelative(-2f)
        verticalLineTo(6f)
        horizontalLineToRelative(2f)
        verticalLineToRelative(4f)
        close()
    }
}.build()

private val sampleScriptsState = ScriptsState(
    availableScriptNames = listOf("factorial.mk61", "fibonacci.mk61", "sum.mk61"),
    selectedScriptInstructions = listOf("15", "X→П 8", "12", "X→П d", "С/П"),
)

private val sampleViewState = ViewState(
    scriptsState = sampleScriptsState,
    feedbackState = FeedbackState(),
)

@PreviewLightDark
@Composable
fun InstrumentationPanelScriptsPreview() {
    AppTheme {
        Panel(
            state = sampleViewState,
            onScripSelected = {  },
            onApplyScript = {  },
            onFeedbackSubmitted = {  },
        )
    }
}

@PreviewLightDark
@Composable
fun InstrumentationPanelFeedbackPreview() {
    AppTheme {
        Panel(
            state = sampleViewState,
            onScripSelected = {  },
            onApplyScript = {  },
            onFeedbackSubmitted = {  },
            initialTab = InstrumentationTab.Feedback,
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
fun InstrumentationPanelScriptsConfigurationsPreview() {
    AppTheme {
        Panel(
            state = sampleViewState,
            onScripSelected = {  },
            onApplyScript = {  },
            onFeedbackSubmitted = {  },
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
fun InstrumentationPanelFeedbackConfigurationsPreview() {
    AppTheme {
        Panel(
            state = sampleViewState,
            onScripSelected = {  },
            onApplyScript = {  },
            onFeedbackSubmitted = {  },
            initialTab = InstrumentationTab.Feedback,
        )
    }
}