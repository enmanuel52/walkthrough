package com.enmanuelbergling.pbcompose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.walkthrough.ui.components.InstagramPager
import kotlin.math.roundToInt

val DOG_IMAGES = listOf(
    R.drawable.beach_dog,
    R.drawable.black_dog,
    R.drawable.dog_friends,
    R.drawable.puppy,
    R.drawable.sand_beach_dog,
    R.drawable.sleepy_dog,
    R.drawable.lay_dog,
    R.drawable.dog_fall,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun InstagramSample(modifier: Modifier = Modifier) {

    var boxReversed by remember {
        mutableStateOf(false)
    }

    var boxAngle by remember {
        mutableFloatStateOf(30f)
    }

    var isSheetVisible by remember {
        mutableStateOf(false)
    }

    val pagerState = rememberPagerState {
        DOG_IMAGES.count()
    }

    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            InstagramPager(
                state = pagerState,
                boxAngle = boxAngle.roundToInt(),
                reverse = boxReversed,
                pageSpacing = 4.dp
            ) { index, pageModifier ->
                Image(
                    painter = painterResource(id = DOG_IMAGES[index]),
                    contentDescription = "dog image",
                    modifier = pageModifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            IconButton(onClick = { isSheetVisible = true }) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "settings icon"
                )
            }
        }
    }

    if (isSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isSheetVisible = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            InstagramOptions(
                boxReversed = boxReversed,
                onBoxReversed = { boxReversed = it },
                boxAngle = boxAngle,
                onBoxAngleChange = { boxAngle = it }
            )
        }
    }
}

@Composable
fun InstagramOptions(
    boxReversed: Boolean,
    onBoxReversed: (Boolean) -> Unit,
    boxAngle: Float,
    onBoxAngleChange: (Float) -> Unit,
) {
    Column(
        Modifier
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Reversed")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = boxReversed,
                onCheckedChange = onBoxReversed
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "Box angle")
            Slider(
                value = boxAngle,
                onValueChange = onBoxAngleChange,
                valueRange = 10f..90f,
                modifier = Modifier.weight(1f)
            )
            Text(text = boxAngle.roundToInt().toString())
        }
    }


}