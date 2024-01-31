package com.enmanuelbergling.walkthrough.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.enmanuelbergling.walkthrough.common.DimenTokens
import com.enmanuelbergling.walkthrough.ui.components.IndicatorColors
import com.enmanuelbergling.walkthrough.ui.components.IndicatorDefaults

/**
 * @param pageIndex starts from 0
 * @param colors for dots
 * */
@Composable
fun StepIndicator(
    pageIndex: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
    colors: IndicatorColors = IndicatorDefaults.colors(),
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DimenTokens.VerySmall)
    ) {
        (0 until pageCount).forEach { currentPage ->
            val activeTransition =
                updateTransition(
                    targetState = pageIndex == currentPage,
                    label = "active transition animation"
                )

            val sizeAnimation by activeTransition.animateDp(
                label = "size animation",
                transitionSpec = {
                    spring(
                        Spring.DampingRatioLowBouncy,
                        Spring.StiffnessLow
                    )
                }
            ) {
                if (it) DimenTokens.MediumSmall
                else DimenTokens.Small
            }

            val colorAnimation by activeTransition.animateColor(
                label = "color animation",
                transitionSpec = { tween() }
            ) { active ->
                if (active) colors.activeIndicatorColor
                else colors.inactiveIndicatorColor
            }

            Canvas(modifier = Modifier.size(sizeAnimation)) {
                drawCircle(colorAnimation)
            }
        }
    }
}