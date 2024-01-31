package com.enmanuelbergling.walkthrough.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
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