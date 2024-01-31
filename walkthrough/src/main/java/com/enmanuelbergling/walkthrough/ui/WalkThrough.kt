package com.enmanuelbergling.walkthrough.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
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
import com.enmanuelbergling.walkthrough.ui.components.IndicatorColors
import com.enmanuelbergling.walkthrough.ui.components.IndicatorDefaults
import com.enmanuelbergling.walkthrough.ui.components.WalkThroughColors
import com.enmanuelbergling.walkthrough.ui.components.WalkThroughDefaults
import com.enmanuelbergling.walkthrough.ui.components.springAnimation
import kotlinx.coroutines.launch

/**
 * @param steps for every single page
 * @param colors for components
 * @param nextButtonVisible to scroll
 * @param onGetStarted is launched once the user walk is finished
 * */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalkThrough(
    steps: List<WalkStep>,
    modifier: Modifier = Modifier,
    skipButton: @Composable () -> Unit = { },
    skipLocation: SkipLocation = SkipLocation.TopEnd,
    colors: WalkThroughColors = WalkThroughDefaults.colors(),
    nextButtonVisible: Boolean = false,
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
            skipButtonRef,
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
                modifier = Modifier.fillMaxHeight(.8f)
                    .fillMaxWidth(),
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
                skipButtonRef
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
            visible = nextButtonVisible || !pagerState.canScrollForward,
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
 * @param indicatorColors
 * @param nextButton will be placed at the bottom of the screen
 * */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalkThrough(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    nextButton: @Composable () -> Unit = { },
    skipButton: @Composable () -> Unit = { },
    skipLocation: SkipLocation = SkipLocation.TopEnd,
    indicatorColors: IndicatorColors = IndicatorDefaults.colors(),
    pageContent: @Composable (PagerScope.(Int) -> Unit),
) {

    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (
            page,
            indicator,
            skipButtonRef,
            nextButtonRef,
        ) = createRefs()

        val bottomContentTop = createGuidelineFromTop(.8f)

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .constrainAs(page) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                },
        ) {
            Column(modifier = Modifier.fillMaxHeight(.8f)) {
                pageContent(it)
            }
        }

        StepIndicator(
            pageIndex = pagerState.currentPage,
            pageCount = pagerState.pageCount,
            modifier = Modifier
                .constrainAs(indicator) {
                    top.linkTo(bottomContentTop, margin = DimenTokens.Medium)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(nextButtonRef.top)
                }
                .padding(DimenTokens.Medium),
            colors = indicatorColors
        )

        Box(modifier = Modifier
            .constrainAs(
                skipButtonRef
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
            visible = !pagerState.canScrollForward,
            modifier = Modifier
                .constrainAs(nextButtonRef) {
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
            nextButton()
        }
    }
}
