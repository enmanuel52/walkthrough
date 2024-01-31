package com.enmanuelbergling.walkthrough.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.enmanuelbergling.walkthrough.common.DimenTokens
import com.enmanuelbergling.walkthrough.model.SkipLocation
import com.enmanuelbergling.walkthrough.model.StepStyle
import com.enmanuelbergling.walkthrough.model.WalkStep
import com.enmanuelbergling.walkthrough.ui.components.SkipButton
import com.enmanuelbergling.walkthrough.ui.components.WalkThroughColors
import com.enmanuelbergling.walkthrough.ui.components.WalkThroughDefaults
import com.enmanuelbergling.walkthrough.ui.components.springAnimation
import kotlinx.coroutines.launch

/**
 * @param steps for every single page
 * @param colors for components
 * @param helperButton to scroll
 * @param onGetStarted is launched once the user walk is finished
 * */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalkThrough(
    steps: List<WalkStep>,
    modifier: Modifier = Modifier,
    skipButton: @Composable () -> Unit = { SkipButton() },
    skipLocation: SkipLocation = SkipLocation.TopEnd,
    colors: WalkThroughColors = WalkThroughDefaults.colors(),
    helperButton: Boolean = false,
    stepStyle: StepStyle = StepStyle.ImageUp,
    onGetStarted: () -> Unit,
) {
    val pagerState = rememberPagerState { steps.count() }

    val scope = rememberCoroutineScope()

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(colors.containerColor)
    ) {
        val (
            page,
            indicator,
            skipButton,
            nextButton,
        ) = createRefs()

        val bottomContentTop = createGuidelineFromTop(.8f)

        HorizontalPager(
            state = pagerState, modifier = Modifier
                .constrainAs(page) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                },
            verticalAlignment = Alignment.Top
        ) { index ->
            WalkStepUi(
                step = steps[index],
                modifier = Modifier.fillMaxHeight(.8f),
                stepStyle = stepStyle
            )
        }

        StepIndicator(
            pageIndex = pagerState.currentPage,
            pageCount = pagerState.pageCount,
            modifier = Modifier
                .constrainAs(indicator) {
                    top.linkTo(bottomContentTop, margin = DimenTokens.Medium)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(nextButton.top)
                }
                .padding(DimenTokens.LessLarge),
            colors = colors.indicator()
        )

        Box(modifier = Modifier
            .constrainAs(
                skipButton
            ) {
                end.linkTo(parent.end)
                when (skipLocation) {
                    SkipLocation.TopEnd -> top.linkTo(parent.top)
                    SkipLocation.BottomEnd -> bottom.linkTo(parent.bottom)
                }
            }
            .padding(DimenTokens.Small)
        ) {
            skipButton()
        }

        AnimatedVisibility(
            visible = helperButton || !pagerState.canScrollForward,
            modifier = Modifier
                .constrainAs(nextButton) {
                    top.linkTo(indicator.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .padding(DimenTokens.LessLarge),
            enter = slideInVertically(springAnimation()) { it },
            exit = slideOutVertically { it } + fadeOut()
        ) {
            Button(onClick = {
                if (!pagerState.canScrollForward) {
                    onGetStarted()
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }) {
                Text(
                    text = if (!pagerState.canScrollForward) "Get started" else "Next",
                    modifier = Modifier.padding(vertical = DimenTokens.Small)
                )
            }
        }
    }
}

/**
 * A fully customizable implementation of the Walkthrough for the page content
 * @param pageCount number of pages
 * @param colors for components
 * @param onGetStarted is launched once the user walk is finished
 * */
/*
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalkThrough(
    pageCount: Int,
    modifier: Modifier = Modifier,
    skipButton: @Composable () -> Unit = { SkipButton {} },
    colors: WalkThroughColors = WalkThroughDefaults.colors(),
    onGetStarted: () -> Unit = {},
    pageContent: @Composable (PagerScope.(Int) -> Unit),
) {
    val pagerState = rememberPagerState { pageCount }

    val scope = rememberCoroutineScope()

    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (
            step,
            previous,
            indicator,
            next,
        ) = createRefs()

        val verticalGuideline15 = createGuidelineFromStart(.15f)
        val verticalGuideline85 = createGuidelineFromEnd(.15f)

        createHorizontalChain(previous, indicator, next, chainStyle = ChainStyle.Packed)

        HorizontalPager(
            state = pagerState, modifier = Modifier
                .constrainAs(step) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                },
            pageContent = pageContent
        )

        StepIndicator(
            pageIndex = pagerState.currentPage,
            pageCount = pagerState.pageCount,
            modifier = Modifier
                .constrainAs(indicator) {
                    start.linkTo(previous.end)
                    end.linkTo(next.start)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.percent(.15f)
                }
                .padding(DimenTokens.Medium),
        )

        AnimatedVisibility(
            visible = pagerState.canScrollBackward,
            modifier = Modifier
                .constrainAs(previous) {
                    start.linkTo(verticalGuideline15)
                    top.linkTo(indicator.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(indicator.start)
                }
        ) {
            FilledTonalButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                colors = colors.filledButton()
            ) {
                previousIcon()
            }
        }


        FilledTonalButton(
            onClick = {
                if (pagerState.canScrollForward) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    onGetStarted()
                }
            },
            modifier = Modifier
                .constrainAs(next) {
                    start.linkTo(indicator.end)
                    top.linkTo(indicator.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(verticalGuideline85)
                },
            colors = colors.filledButton()

        ) {
            forwardIcon()
        }
    }
}*/
