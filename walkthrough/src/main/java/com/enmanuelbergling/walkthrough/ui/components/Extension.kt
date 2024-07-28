package com.enmanuelbergling.walkthrough.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import kotlin.math.abs


@Composable
fun <T> springAnimation(): SpringSpec<T> =
    spring(
        Spring.DampingRatioLowBouncy,
        Spring.StiffnessLow
    )