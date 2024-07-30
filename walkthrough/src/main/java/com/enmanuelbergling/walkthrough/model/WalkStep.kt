package com.enmanuelbergling.walkthrough.model

import androidx.annotation.DrawableRes

/**
 * @param titleResource when null just hide and make description bigger
 * */
data class WalkStep(
    @DrawableRes val imageResource: Int,
    val titleResource: String? = null,
    val descriptionResource: String,
)
