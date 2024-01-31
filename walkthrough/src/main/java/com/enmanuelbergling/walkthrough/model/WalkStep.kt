package com.enmanuelbergling.walkthrough.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * @param titleResource when null just hide and make description bigger
 * */
data class WalkStep(
    @DrawableRes val imageResource: Int,
    @StringRes val titleResource: Int? = null,
    @StringRes val descriptionResource: Int,
)
