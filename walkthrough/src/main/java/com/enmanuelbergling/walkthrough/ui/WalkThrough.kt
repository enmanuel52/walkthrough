package com.enmanuelbergling.walkthrough.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.enmanuelbergling.walkthrough.common.DimenTokens
import com.enmanuelbergling.walkthrough.model.WalkIndicator
import com.enmanuelbergling.walkthrough.model.WalkStep
import com.enmanuelbergling.walkthrough.ui.components.DefaultDoneIcon
import com.enmanuelbergling.walkthrough.ui.components.DefaultForwardIcon
import com.enmanuelbergling.walkthrough.ui.components.DefaultPreviousIcon
import com.enmanuelbergling.walkthrough.ui.components.WalkThroughColors
import com.enmanuelbergling.walkthrough.ui.components.WalkThroughDefaults
import kotlinx.coroutines.launch

/**
 * @param steps for every single page
 * @param style for the walk track
 * @param colors for components
 * @param onEnd is launched once the user walk is finished
 * */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalkThrough(
    steps: List<WalkStep>,
    modifier: Modifier = Modifier,
    previousIcon: @Composable () -> Unit = { DefaultPreviousIcon() },
    forwardIcon: @Composable () -> Unit = { DefaultForwardIcon() },
    doneIcon: @Composable () -> Unit = { DefaultDoneIcon() },
    style: WalkIndicator = WalkIndicator.Dots,
    colors: WalkThroughColors = WalkThroughDefaults.colors(),
    onEnd: () -> Unit,
) {
    val pagerState = rememberPagerState { steps.count() }

    val scope = rememberCoroutineScope()

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(colors.containerColor)
    ) {
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
                }
        ) { index ->
            WalkStepUi(step = steps[index])
        }

        StepIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .constrainAs(indicator) {
                    start.linkTo(previous.end)
                    end.linkTo(next.start)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.percent(.15f)
                }
                .padding(DimenTokens.Medium),
            style = style,
            colors = colors.indicator()
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
                    onEnd()
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
            AnimatedContent(
                targetState = pagerState.canScrollForward,
                transitionSpec = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
                    ) togetherWith slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End
                    )
                },
                label = "forward animation icon",
            ) { canGoForward ->
                if (canGoForward) {
                    forwardIcon()
                } else {
                    doneIcon()
                }
            }
        }
    }
}

/**
 * A fully customizable implementation of the Walkthrough for the page content
 * @param pageCount number of pages
 * @param style for the walk track
 * @param colors for components
 * @param onEnd is launched once the user walk is finished
 * */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalkThrough(
    pageCount: Int,
    modifier: Modifier = Modifier,
    previousIcon: @Composable () -> Unit = { DefaultPreviousIcon() },
    forwardIcon: @Composable () -> Unit = { DefaultForwardIcon() },
    style: WalkIndicator = WalkIndicator.Dots,
    colors: WalkThroughColors = WalkThroughDefaults.colors(),
    onEnd: () -> Unit = {},
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
            pagerState = pagerState,
            modifier = Modifier
                .constrainAs(indicator) {
                    start.linkTo(previous.end)
                    end.linkTo(next.start)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.percent(.15f)
                }
                .padding(DimenTokens.Medium),
            style = style
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
                    onEnd()
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
}