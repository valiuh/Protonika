package com.valiukh.protonika.compose.components.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valiukh.protonika.compose.theme.AppTheme

@Composable
fun MkButtonPlace(
    modifier: Modifier = Modifier,
    isPrimaryFunctionActive: Boolean = false,
    isSecondaryFunctionActive: Boolean = false,
    type: MkButtonType,
    primaryType: MkPrimaryFunctionType? = null,
    secondaryType: MkSecondaryFunctionType? = null,
    onClick: (
        type: MkButtonType,
        primaryType: MkPrimaryFunctionType?,
        secondaryType: MkSecondaryFunctionType?
    ) -> Unit = { _, _, _ -> },
) {
    val colors = AppTheme.colors
    Column(
        modifier = modifier.size(57.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (primaryType != null) {
                val primaryModifier = if(secondaryType != null) {
                    Modifier
                        .padding(start = 4.dp, bottom = 2.dp)
                        .align(Alignment.CenterStart)
                        .then(
                            if (isPrimaryFunctionActive) {
                                Modifier
                                    .border(width = 1.dp, color = colors.instrumentalInverted)
                                    .padding(2.dp)
                            } else {
                                Modifier
                            }
                        )
                } else {
                    Modifier.align(Alignment.Center)
                        .then(
                        if (isPrimaryFunctionActive) {
                            Modifier
                                .padding(bottom = 2.dp)
                                .border(width = 1.dp, color = colors.instrumentalInverted)
                                .padding(2.dp)
                        } else {
                            Modifier
                        }
                    )
                }
                Text(
                    modifier = primaryModifier,
                    text = primaryType.text,
                    color = colors.primaryFunctionalText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            if (secondaryType != null) {
                val secondaryModifier = Modifier
                    .padding(end = 4.dp, bottom = 2.dp)
                    .align(Alignment.CenterEnd)
                    .then(
                        if (isSecondaryFunctionActive) {
                            Modifier
                                .border(width = 1.dp, color = colors.instrumentalInverted)
                                .padding(2.dp)
                        } else {
                            Modifier
                        }
                    )

                Text(
                    modifier = secondaryModifier,
                    text = secondaryType.text,
                    color = colors.secondaryFunctionalText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        MkButton(
            type = type,
            onClick = {
                onClick(type, primaryType, secondaryType)
            },
        )
    }
}

@PreviewLightDark
@Composable
fun MkButtonPlacePreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(color = AppTheme.colors.instrumental)
                .padding(8.dp),
        ) {
            // Both function labels
            MkButtonPlace(
                modifier = Modifier.padding(bottom = 8.dp),
                type = MkButtonType.BUTTON_7,
                primaryType = MkPrimaryFunctionType.FUNCTION_SIN,
                secondaryType = MkSecondaryFunctionType.FUNCTION_INTEGER_PART,
            )
            MkButtonPlace(
                modifier = Modifier.padding(bottom = 8.dp),
                isPrimaryFunctionActive = true,
                type = MkButtonType.BUTTON_7,
                primaryType = MkPrimaryFunctionType.FUNCTION_SIN,
                secondaryType = MkSecondaryFunctionType.FUNCTION_INTEGER_PART,
            )
            MkButtonPlace(
                modifier = Modifier.padding(bottom = 8.dp),
                isSecondaryFunctionActive = true,
                type = MkButtonType.BUTTON_7,
                primaryType = MkPrimaryFunctionType.FUNCTION_SIN,
                secondaryType = MkSecondaryFunctionType.FUNCTION_INTEGER_PART,
            )
            // Primary function label only
            MkButtonPlace(
                modifier = Modifier.padding(bottom = 8.dp),
                type = MkButtonType.BUTTON_1,
                primaryType = MkPrimaryFunctionType.FUNCTION_E_POWER_X,
            )
            MkButtonPlace(
                modifier = Modifier.padding(bottom = 8.dp),
                isPrimaryFunctionActive = true,
                type = MkButtonType.BUTTON_2,
                primaryType = MkPrimaryFunctionType.FUNCTION_LG,
            )
            // No function labels
            MkButtonPlace(
                modifier = Modifier.padding(bottom = 8.dp),
                type = MkButtonType.BUTTON_F,
            )
        }
    }
}