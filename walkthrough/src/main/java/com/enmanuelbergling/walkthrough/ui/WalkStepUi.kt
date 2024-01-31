package com.enmanuelbergling.walkthrough.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.enmanuelbergling.walkthrough.model.WalkStep

@Composable
fun WalkStepUi(step: WalkStep, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(DimenTokens.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(id = step.imageResource),
            contentDescription = stringResource(step.titleResource),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        Text(
            text = stringResource(id = step.titleResource),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = stringResource(id = step.descriptionResource),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Light,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
        )
    }
}