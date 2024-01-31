package com.enmanuelbergling.walkthrough.ui.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class WalkThroughColors(
    val containerColor: Color,
    val buttonContainerColor: Color,
    val buttonContentColor: Color,
    val activeIndicatorColor: Color,
    val inactiveIndicatorColor: Color,
    val uncompletedIndicatorColor: Color,
) {
    internal fun indicator() = IndicatorColors(
        activeIndicatorColor = activeIndicatorColor,
        inactiveIndicatorColor = inactiveIndicatorColor,
        uncompletedIndicatorColor = uncompletedIndicatorColor,
    )

    @Composable
    internal fun filledButton() = ButtonDefaults.filledTonalButtonColors(
        containerColor = buttonContainerColor,
        contentColor = buttonContentColor
    )
}

object WalkThroughDefaults {

    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.background,
        buttonContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        buttonContentColor: Color = MaterialTheme.colorScheme.background,
        activeIndicatorColor: Color = MaterialTheme.colorScheme.primary,
        inactiveIndicatorColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        uncompletedIndicatorColor: Color = MaterialTheme.colorScheme.tertiary,
    ) = WalkThroughColors(
        containerColor = containerColor,
        buttonContainerColor = buttonContainerColor,
        buttonContentColor = buttonContentColor,
        activeIndicatorColor = activeIndicatorColor,
        inactiveIndicatorColor = inactiveIndicatorColor,
        uncompletedIndicatorColor = uncompletedIndicatorColor
    )
}

/**
 * @param uncompletedIndicatorColor when the percent isn't 100
 * */
@Stable
data class IndicatorColors(
    val activeIndicatorColor: Color,
    val inactiveIndicatorColor: Color,
    val uncompletedIndicatorColor: Color,
)

object IndicatorDefaults {

    @Composable
    fun colors(
        activeIndicatorColor: Color = MaterialTheme.colorScheme.primary,
        inactiveIndicatorColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        uncompletedIndicatorColor: Color = MaterialTheme.colorScheme.tertiary,
    ) = IndicatorColors(
        activeIndicatorColor = activeIndicatorColor,
        inactiveIndicatorColor = inactiveIndicatorColor,
        uncompletedIndicatorColor = uncompletedIndicatorColor
    )
}