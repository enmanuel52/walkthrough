package com.enmanuelbergling.pbcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.enmanuelbergling.pbcompose.data.WALK_STEPS
import com.enmanuelbergling.pbcompose.ui.theme.PbcomposeTheme
import com.enmanuelbergling.walkthrough.model.WalkScrollStyle
import com.enmanuelbergling.walkthrough.ui.WalkThrough
import com.enmanuelbergling.walkthrough.ui.components.SkipButton
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {

            val snackBarHost = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            PbcomposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(snackbarHost = { SnackbarHost(snackBarHost) }) { paddingValues ->
                        val pagerState = rememberPagerState { WALK_STEPS.count() }

                        WalkThrough(
                            steps = WALK_STEPS.map { it.toModel(context) },
                            pagerState = pagerState,
                            modifier = Modifier.padding(paddingValues),
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
                                                snackBarHost.showSnackbar("The walk has ended")
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
                                        snackBarHost.showSnackbar("The walk has been skipped")
                                    }
                                }
                            },
                            scrollStyle = WalkScrollStyle.Instagram
                        )
                    }
                }
            }
        }
    }
}