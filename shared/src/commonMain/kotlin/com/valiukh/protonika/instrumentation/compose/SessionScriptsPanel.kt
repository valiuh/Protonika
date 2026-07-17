package com.valiukh.protonika.instrumentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.valiukh.protonika.compose.components.editor.MkProgramEditor
import com.valiukh.protonika.compose.theme.AppTheme
import com.valiukh.protonika.instrumentation.ScriptsState

@Composable
fun SessionScriptsPanel(
    modifier: Modifier = Modifier,
    state: ScriptsState,
    onScripSelected: (Int) -> Unit,
    onApplyScript: (Int) -> Unit,
) {
    var selectedIndex by remember { mutableStateOf(0) }

    val colors = AppTheme.colors

    Column {

        Button(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colors.instrumental)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colors.instrumentalInverted),
            onClick = { onApplyScript(selectedIndex) },
        ) {
            Text(
                color = colors.instrumental,
                text = "Apply Script"
            )
        }

        Row(
            modifier = modifier
                .fillMaxSize()
                .background(colors.instrumental),
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .border(width = 1.dp, color = colors.instrumentalInverted),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                itemsIndexed(state.availableScriptNames) { index, name ->
                    Text(
                        text = name,
                        color = if (index == selectedIndex) colors.instrumental else colors.instrumentalInverted,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.extraSmall)
                            .background(
                                if (index == selectedIndex) {
                                    colors.instrumentalInverted
                                } else {
                                    Color.Transparent
                                },
                            )
                            .clickable {
                                selectedIndex = index
                                onScripSelected(index)
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                    )
                }
            }

            MkProgramEditor(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                instructions = state.selectedScriptInstructions,
            )
        }
    }



}

private val sampleScriptsState = ScriptsState(
    availableScriptNames = listOf("factorial.mk61", "fibonacci.mk61", "sum.mk61"),
    selectedScriptInstructions = listOf("15", "X→П 8", "12", "X→П d", "С/П"),
)

@PreviewLightDark
@Composable
fun OperationalConsoleScreenPreview() {
    AppTheme {
        SessionScriptsPanel(
            state = sampleScriptsState,
            onScripSelected = {  },
            onApplyScript = {  }
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
        SessionScriptsPanel(
            state = sampleScriptsState,
            onScripSelected = {  },
            onApplyScript = {  }
        )
    }
}