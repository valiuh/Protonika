package com.valiukh.protonika.instrumentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valiukh.protonika.instrumentation.FeedbackState

@Composable
fun FeedbackPanel(
    modifier: Modifier = Modifier,
    state: FeedbackState,
    onFeedbackSubmitted: (String) -> Unit,
) {
    var feedback by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = "Share your feedback")

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = feedback,
            onValueChange = { feedback = it },
            enabled = !state.isLoading,
            label = { Text(text = "Your feedback") },
            minLines = 3,
        )

        state.errorMessage?.let { message ->
            Text(text = message, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                onFeedbackSubmitted(feedback)
                feedback = ""
            },
            enabled = feedback.isNotBlank() && !state.isLoading,
        ) {
            Text(text = if (state.isLoading) "Submitting…" else "Submit")
        }
    }
}