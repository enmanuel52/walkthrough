package com.enmanuelbergling.pbcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.enmanuelbergling.pbcompose.ui.theme.PbcomposeTheme
import com.enmanuelbergling.walkthrough.common.DimenTokens
import com.enmanuelbergling.walkthrough.model.WALK_STEPS
import com.enmanuelbergling.walkthrough.model.WalkIndicator
import com.enmanuelbergling.walkthrough.ui.WalkThrough
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {

            var style by remember {
                mutableStateOf(WalkIndicator.Dots)
            }

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
                        Box(Modifier.padding(paddingValues)) {
                            WalkThrough(steps = WALK_STEPS, style = style) {
                                scope.launch {
                                    snackBarHost.showSnackbar(
                                        message = "The walk has finished",
                                        duration = SnackbarDuration.Indefinite,
                                        withDismissAction = true
                                    )
                                }
                            }


                            Button(
                                onClick = {
                                    style = when (style) {
                                        WalkIndicator.Dots -> WalkIndicator.Percent
                                        WalkIndicator.Percent -> WalkIndicator.Pages
                                        WalkIndicator.Pages -> WalkIndicator.Dots
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(bottom = DimenTokens.Huge, end = DimenTokens.Medium)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Settings,
                                    contentDescription = "style toggle"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}