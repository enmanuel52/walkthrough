package com.enmanuelbergling.pbcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.enmanuelbergling.pbcompose.data.WALK_STEPS
import com.enmanuelbergling.pbcompose.ui.theme.PbcomposeTheme
import com.enmanuelbergling.walkthrough.common.DimenTokens
import com.enmanuelbergling.walkthrough.ui.WalkThrough
import com.enmanuelbergling.walkthrough.ui.components.SkipButton
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {

            val snackBarHost = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            PbcomposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        snackbarHost = { SnackbarHost(snackBarHost) }
                    ) { paddingValues ->
                        WalkThrough(
                            steps = WALK_STEPS,
                            modifier = Modifier.padding(paddingValues),
                            nextButtonText = { ended ->
                                Text(
                                    text = if (ended) "Get started" else "Next",
                                    modifier = Modifier.padding(vertical = DimenTokens.Small)
                                )
                            },
                            skipButton = {
                                SkipButton {
                                    scope.launch {
                                        snackBarHost.showSnackbar("The walk has been skipped")
                                    }
                                }
                            },
                            nextButtonVisible = true
                        ) {
                            scope.launch {
                                snackBarHost.showSnackbar("The walk has ended")
                            }
                        }
                    }
                }
            }
        }
    }
}