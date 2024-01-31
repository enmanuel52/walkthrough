package com.enmanuelbergling.walkthrough.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun AnimatedCounter(
    modifier: Modifier = Modifier,
    number: Int,
    textStyle: TextStyle = LocalTextStyle.current,
    previous: Boolean = false,
) {
    Row(modifier = modifier) {
        for (char in number.toString()) {
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically { if (previous) -it else it } togetherWith
                            slideOutVertically { if (previous) it else -it }
                },
                label = "number animation"
            ) {
                Text(
                    text = it.toString(),
                    style = textStyle,
                )
            }
        }
    }
}