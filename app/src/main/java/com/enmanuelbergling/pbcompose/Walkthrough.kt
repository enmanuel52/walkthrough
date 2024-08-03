package com.enmanuelbergling.pbcompose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pbcompose.data.WALK_STEPS
import com.enmanuelbergling.walkthrough.model.IndicatorStyle
import com.enmanuelbergling.walkthrough.model.WalkScrollStyle
import com.enmanuelbergling.walkthrough.ui.WalkThrough
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
fun WalkthroughSample() {
    val snackBarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var scrollStyle by remember { mutableStateOf<WalkScrollStyle>(WalkScrollStyle.Normal) }

    var boxReversed by remember {
        mutableStateOf(false)
    }

    var boxAngle by remember {
        mutableFloatStateOf(30f)
    }

    LaunchedEffect(key1 = boxAngle, key2 = boxReversed) {
        scrollStyle = WalkScrollStyle.Instagram(
            boxAngle = boxAngle.roundToInt(),
            reverse = boxReversed
        )
    }

    var isSheetVisible by remember {
        mutableStateOf(false)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHost) }
    ) { paddingValues ->
        val pagerState = rememberPagerState { WALK_STEPS.count() }

        Box(modifier = Modifier.padding(paddingValues)) {
            WalkThrough(
                steps = WALK_STEPS,
                pagerState = pagerState,
                bottomButton = {
                    Button(
                        onClick = {
                            scope.launch {
                                if (pagerState.canScrollForward) {
                                    pagerState.animateScrollToPage(
                                        pagerState.currentPage + 1,
                                        animationSpec = tween(500)
                                    )

                                } else {
                                    snackBarHost.showSnackbar(
                                        "The walk has ended",
                                        withDismissAction = true
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(.7f),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        AnimatedContent(
                            targetState = pagerState.canScrollForward,
                            label = "text button animation"
                        ) { forward ->
                            if (forward) {
                                Text(text = "Next")
                            } else {
                                Text(text = "Get started")
                            }
                        }
                    }
                },
                skipButton = {
                    SkipButton {
                        scope.launch {
                            snackBarHost.showSnackbar(
                                "The walk has been skipped",
                                withDismissAction = true
                            )
                        }
                    }
                },
                scrollStyle = scrollStyle,
                indicatorStyle = IndicatorStyle.Shift
            )

            ScrollStyleOptions(
                scrollStyle = scrollStyle,
                onClick = {
                    scrollStyle = when (scrollStyle) {
                        is WalkScrollStyle.Normal -> WalkScrollStyle.Instagram(
                            boxAngle = boxAngle.roundToInt(),
                            reverse = boxReversed
                        )

                        is WalkScrollStyle.Instagram -> WalkScrollStyle.Normal
                    }
                }, onSheet = { isSheetVisible = true })


        }
    }
    if (isSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isSheetVisible = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            InstagramOptions(
                boxReversed = boxReversed,
                onBoxReversed = { boxReversed = it },
                boxAngle = boxAngle,
                onBoxAngleChange = { boxAngle = it }
            )
        }
    }
}

@Composable
private fun ScrollStyleOptions(
    scrollStyle: WalkScrollStyle,
    onClick: () -> Unit,
    onSheet: () -> Unit,
) {
    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = onClick) {
            Text(text = "Change style")
        }

        if (scrollStyle is WalkScrollStyle.Instagram) {
            Spacer(modifier = Modifier.width(4.dp))

            IconButton(onClick = onSheet) {
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = "drop down icon"
                )
            }
        }


    }
}

@Composable
private fun SkipButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier) {
        Text(text = "Skip")
    }
}