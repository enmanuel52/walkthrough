package com.enmanuelbergling.walkthrough.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
internal fun DefaultDoneIcon() {
    Icon(
        imageVector = Icons.Rounded.Done,
        contentDescription = "done"
    )
}

@Composable
internal fun DefaultPreviousIcon() {
    Icon(
        imageVector = Icons.Rounded.ArrowBack,
        contentDescription = "step back"
    )
}

@Composable
internal fun DefaultForwardIcon() {
    Icon(
        imageVector = Icons.Rounded.ArrowForward,
        contentDescription = "step forward"
    )
}