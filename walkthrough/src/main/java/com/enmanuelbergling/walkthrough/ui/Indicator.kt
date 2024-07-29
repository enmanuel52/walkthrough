package com.enmanuelbergling.walkthrough.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.enmanuelbergling.walkthrough.common.DimenTokens
import com.enmanuelbergling.walkthrough.ui.components.IndicatorColors
import com.enmanuelbergling.walkthrough.ui.components.IndicatorDefaults
import com.enmanuelbergling.walkthrough.ui.components.getPageProgress
import com.enmanuelbergling.walkthrough.ui.components.springAnimation

/**
 * @param pageIndex starts from 0
 * @param colors for dots
 * */
@Composable
fun StepIndicator(
    pageIndex: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
    spaceBetween: Dp = DimenTokens.VerySmall,
    stepSize: Dp = DimenTokens.Small,
    colors: IndicatorColors = IndicatorDefaults.colors(),
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        (0 until pageCount).forEach { currentPage ->
            val activeTransition =
                updateTransition(
                    targetState = pageIndex == currentPage,
                    label = "active transition animation"
                )

            val sizeAnimation by activeTransition.animateDp(
                label = "size animation",
                transitionSpec = { springAnimation() }
            ) {
                if (it) stepSize.times(1.5f)
                else stepSize
            }

            val colorAnimation by activeTransition.animateColor(
                label = "color animation",
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShiftIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    spaceBetween: Dp = DimenTokens.VerySmall,
    stepSize: Dp = DimenTokens.Small,
    shape: Shape = CircleShape,
    colors: IndicatorColors = IndicatorDefaults.colors(),
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        (0 until pagerState.pageCount).forEach { currentPage ->
            val widthAnimation by animateDpAsState(
                targetValue =
                stepSize * pagerState.getPageProgress(currentPage).plus(1f),
                label = "width animation"
            )

            Box(
                modifier = Modifier
                    .height(stepSize)
                    .width(widthAnimation)
                    .background(colors.activeIndicatorColor, shape)
            )
        }
    }
}