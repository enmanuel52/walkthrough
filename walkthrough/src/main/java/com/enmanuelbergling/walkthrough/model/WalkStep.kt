package com.enmanuelbergling.walkthrough.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.enmanuelbergling.walkthrough.R

data class WalkStep(
    @DrawableRes val imageResource: Int,
    @StringRes val titleResource: Int,
    @StringRes val descriptionResource: Int,
)
