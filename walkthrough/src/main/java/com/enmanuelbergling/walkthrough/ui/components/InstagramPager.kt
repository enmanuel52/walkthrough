package com.enmanuelbergling.walkthrough.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

/**
 * Working onto [HorizontalPager] under the hood
 * @param state of pager
 * @param reverse to define whether you are in or out of the cube
 * @param pageContent the content of the page, you must use this modifier in its page to ensure
 * the instagram effect
 * */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InstagramPager(
    state: PagerState,
    modifier: Modifier = Modifier,
    reverse: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pageSize: PageSize = PageSize.Fill,
    pageSpacing: Dp = 0.dp,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    userScrollEnabled: Boolean = true,
    pageNestedScrollConnection: NestedScrollConnection = remember(state) {
        PagerDefaults.pageNestedScrollConnection(state, Orientation.Horizontal)
    },
    pageContent: @Composable (index: Int, pageModifier: Modifier) -> Unit,
) {

    HorizontalPager(
        state = state,
        modifier,
        verticalAlignment = verticalAlignment,
        contentPadding = contentPadding,
        pageSize = pageSize,
        pageSpacing = pageSpacing,
        userScrollEnabled = userScrollEnabled,
        pageNestedScrollConnection = pageNestedScrollConnection
    ) { pageIndex ->

        val degreesIn by remember(state.currentPageOffsetFraction) {
            derivedStateOf {
                if (pageIndex == state.currentPage) {
                    state.currentPageOffsetFraction * 90
                } else if (pageIndex > state.currentPage) {
                    -90 + abs(state.currentPageOffsetFraction * 90)
                } else {
                    90 - abs(state.currentPageOffsetFraction * 90)
                }
            }
        }

        val degreesOut by remember(state.currentPageOffsetFraction) {
            derivedStateOf {
                if (pageIndex == state.currentPage) {
                    -state.currentPageOffsetFraction * 90
                } else if (pageIndex < state.currentPage) {
                    -90 + abs(state.currentPageOffsetFraction * 90)
                } else {
                    90 - abs(state.currentPageOffsetFraction * 90)
                }
            }
        }

        val transformOriginX by remember(key1 = state.currentPage) {
            derivedStateOf {
                if (pageIndex == state.currentPage) {
                    if (state.currentPageOffsetFraction > 0f) {
                        1f
                    } else {
                        0f
                    }
                } else if (pageIndex < state.currentPage) {
                    1f
                } else {
                    0f
                }
            }
        }

        pageContent(
            pageIndex,
            Modifier.graphicsLayer {
                rotationY = if (reverse) degreesIn else degreesOut
                transformOrigin = TransformOrigin(
                    transformOriginX,
                    .5f
                )
            }
        )
    }
}