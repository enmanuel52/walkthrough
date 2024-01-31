package com.enmanuelbergling.walkthrough.ui.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SkipButton(modifier: Modifier = Modifier, onClick: ()->Unit) {
    TextButton(onClick = onClick) {
        Text(text = "Skip")
    }
}