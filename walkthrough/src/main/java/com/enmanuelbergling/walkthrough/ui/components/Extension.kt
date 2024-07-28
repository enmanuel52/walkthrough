package com.enmanuelbergling.walkthrough.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import kotlin.math.abs

/**
 * @return a value between 0f and 1f
 * */
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.getPageProgress(index: Int): Float = if (currentPage == index) {
    1 - abs(currentPageOffsetFraction)
} else if (currentPageOffsetFraction > 0f && index == currentPage + 1) {
    currentPageOffsetFraction
} else if (currentPageOffsetFraction < 0f && index == currentPage - 1) {
    abs(currentPageOffsetFraction)
} else {
    0f
}

@Composable
fun <T> springAnimation(): SpringSpec<T> =
    spring(
        Spring.DampingRatioLowBouncy,
        Spring.StiffnessLow
    )