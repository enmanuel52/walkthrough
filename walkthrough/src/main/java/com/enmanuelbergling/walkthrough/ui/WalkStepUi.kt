package com.enmanuelbergling.walkthrough.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.enmanuelbergling.walkthrough.common.DimenTokens
import com.enmanuelbergling.walkthrough.model.StepStyle
import com.enmanuelbergling.walkthrough.model.WalkStep

@Composable
fun WalkStepUi(
    step: WalkStep,
    modifier: Modifier = Modifier,
    stepStyle: StepStyle = StepStyle.ImageUp,
) {
    Column(
        modifier
            .padding(DimenTokens.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        when (stepStyle) {
            StepStyle.ImageUp -> {
                Image(step.imageResource, step.titleResource)

                Information(step.titleResource, step.descriptionResource)
            }

            StepStyle.ImageDown -> {
                Information(step.titleResource, step.descriptionResource)

                Image(step.imageResource, step.titleResource)
            }
        }
    }
}

@Composable
private fun Image(resource: Int, title: Int?) {
    Image(
        painter = painterResource(id = resource),
        contentDescription = title?.let { stringResource(title) },
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    )
}

@Composable
private fun Information(title: Int?, description: Int) {
    title?.let {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
    }

    Text(
        text = stringResource(id = description),
        style = if (title != null)
            MaterialTheme.typography.bodyMedium
        else LocalTextStyle.current,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis,
    )
}