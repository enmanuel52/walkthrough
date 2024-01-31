package com.enmanuelbergling.walkthrough.ui

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.lerp
import com.enmanuelbergling.walkthrough.common.DimenTokens
import com.enmanuelbergling.walkthrough.common.TAG
import com.enmanuelbergling.walkthrough.model.WalkIndicator
import com.enmanuelbergling.walkthrough.ui.components.AnimatedCounter
import com.enmanuelbergling.walkthrough.ui.components.IndicatorColors
import com.enmanuelbergling.walkthrough.ui.components.IndicatorDefaults
import com.enmanuelbergling.walkthrough.ui.components.currentScrollProgress

typealias Between = Pair<Int, Int>

/**
 * @param style for the walk track
 * @param colors for components
 * */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StepIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    style: WalkIndicator,
    colors: IndicatorColors = IndicatorDefaults.colors(),
) {
    val between by remember {
        derivedStateOf {
            with(pagerState) {
                currentPage to targetPage
            }
        }
    }

    val progressToNext by remember {
        derivedStateOf {
            pagerState.currentScrollProgress
        }
    }

    val betweenTransition =
        updateTransition(
            targetState = between,
            label = "current page"
        )

    AnimatedContent(
        style,
        modifier = modifier,
        label = "indicator style animation",
        transitionSpec = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
            ) togetherWith slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Up
            ) + fadeOut()
        }
    ) { indicator ->
        when (indicator) {
            WalkIndicator.Dots -> {
                DotsIndicator(
                    pageCount = pagerState.pageCount,
                    betweenTransition = betweenTransition,
                    progressToNext = progressToNext,
                    colors = colors,
                )
            }

            WalkIndicator.Percent -> {
                PercentIndicator(
                    pageCount = pagerState.pageCount,
                    betweenTransition = betweenTransition,
                    progressToNext = progressToNext,
                    colors = colors,
                )
            }

            WalkIndicator.Pages -> {
                PageIndicator(
                    currentPage = pagerState.currentPage,
                    pageCount = pagerState.pageCount,
                )
            }
        }
    }
}

@Composable
fun PageIndicator(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
) {

    Box(modifier) {
        Row(Modifier.align(Alignment.Center)) {
            AnimatedCounter(
                number = currentPage.plus(1),
                textStyle = MaterialTheme.typography.titleMedium
            )
            Text(text = " / $pageCount", style = MaterialTheme.typography.titleMedium)
        }
    }
}

/**
 * @param betweenTransition pages of [HorizontalPager]
 * @param progressToNext to be scroll aware
 **/
@Composable
fun PercentIndicator(
    pageCount: Int,
    betweenTransition: Transition<Between>,
    progressToNext: Float,
    modifier: Modifier = Modifier,
    colors: IndicatorColors = IndicatorDefaults.colors(),
) {
    val stepPercent by remember {
        derivedStateOf {
            1f / pageCount
        }
    }

    val progress by betweenTransition.animateFloat(label = "percent animation") { (current, target) ->
        val scrollProgress: Float =
            if (target == current) 0f
            else if (target > current) progressToNext.times(stepPercent)
            else -progressToNext.times(stepPercent)

        current.plus(1) * stepPercent + scrollProgress
    }

    val progressColor by betweenTransition.animateColor(label = "percent color animation") { (current, target) ->
        val hasNotOffset = current == target
        if (current.plus(1) == pageCount && hasNotOffset) colors.activeIndicatorColor
        else colors.uncompletedIndicatorColor
    }

    Box(modifier) {
        LinearProgressIndicator(
            progress = progress,
            color = progressColor,
            modifier = Modifier
                .align(Alignment.Center)
                .height(DimenTokens.Small)
                .width(DimenTokens.IndicatorWidth),
            strokeCap = StrokeCap.Round
        )
    }
}

/**
 * @param betweenTransition pages of [HorizontalPager]
 * @param progressToNext to be scroll aware
 **/
@Composable
fun DotsIndicator(
    pageCount: Int,
    betweenTransition: Transition<Between>,
    progressToNext: Float,
    modifier: Modifier = Modifier,
    colors: IndicatorColors = IndicatorDefaults.colors(),
) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(DimenTokens.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->

            DotStep(
                betweenTransition = betweenTransition,
                index = index,
                progressToNext = progressToNext,
                colors = colors
            )
        }
    }
}

/**
 * @param betweenTransition pages of [HorizontalPager]
 * @param progressToNext to be scroll aware
 **/
@Composable
internal fun RowScope.DotStep(
    betweenTransition: Transition<Between>,
    index: Int,
    progressToNext: Float,
    colors: IndicatorColors = IndicatorDefaults.colors(),
) {
    val sizeAnimation by betweenTransition.animateDp(label = "size animation") { (prev, next) ->
        when {
            prev == next && prev == index -> DimenTokens.Medium
            index == prev -> {
                lerp(
                    start = DimenTokens.Small,
                    stop = DimenTokens.LessLarge,
                    fraction = 1 - progressToNext
                )
            }

            index == next -> {
                lerp(
                    start = DimenTokens.Small,
                    stop = DimenTokens.LessLarge,
                    fraction = progressToNext
                )
            }

            else -> DimenTokens.Small
        }
    }

    val backgroundAnimation by betweenTransition.animateColor(label = "background animation") { (prev, next) ->
        when {
            prev == next && prev == index -> {
                colors.activeIndicatorColor
            }

            index == prev -> runCatching {
                lerp(
                    start = colors.inactiveIndicatorColor,
                    stop = colors.activeIndicatorColor,
                    fraction = 1f - progressToNext
                )
            }.onFailure {
                Log.d(TAG, "StepIndicator: ${it.message}")
            }.getOrNull() ?: colors.inactiveIndicatorColor

            index == next -> runCatching {
                lerp(
                    start = colors.inactiveIndicatorColor,
                    stop = colors.activeIndicatorColor,
                    fraction = progressToNext
                )
            }.onFailure {
                Log.d(TAG, "StepIndicator: ${it.message}")
            }.getOrNull() ?: colors.inactiveIndicatorColor

            else -> colors.inactiveIndicatorColor
        }
    }

    Box(
        modifier = Modifier
            .size(sizeAnimation)
            .clip(CircleShape)
            .drawBehind {
                drawCircle(backgroundAnimation, size.height / 2, center)
            }
    )
}