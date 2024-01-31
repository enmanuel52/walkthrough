package com.enmanuelbergling.walkthrough.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
internal val PagerState.currentScrollProgress: Float
    get() = if (targetPage > currentPage) {
        if (currentPageOffsetFraction > 0) {
            currentPageOffsetFraction
        } else {
            1f - currentPageOffsetFraction
        }
    } else {
        if (currentPageOffsetFraction > 0) {
            currentPageOffsetFraction + 1f //currentPageOffsetFraction grows from -.5 to 0
        } else {
            abs(currentPageOffsetFraction)
        }
    }

@Composable
fun <T> springAnimation(): SpringSpec<T> =
    spring(
        Spring.DampingRatioLowBouncy,
        Spring.StiffnessLow
    )