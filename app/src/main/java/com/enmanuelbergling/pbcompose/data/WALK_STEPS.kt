package com.enmanuelbergling.pbcompose.data

import android.content.Context
import androidx.annotation.DrawableRes
import com.enmanuelbergling.pbcompose.R
import com.enmanuelbergling.walkthrough.model.WalkStep

val WALK_STEPS = arrayListOf(
    WalkStepRes(
        R.drawable.ic_explore,
        null,//R.string.explore,
        R.string.explore_description
    ),
    WalkStepRes(
        R.drawable.ic_booking,
        R.string.bookings,
        R.string.bookings_description
    ),
    WalkStepRes(
        imageResource = R.drawable.ic_wizard_payment,
        titleResource = R.string.pay,
        descriptionResource = R.string.pay_description
    ),
    WalkStepRes(
        R.drawable.ic_wizard_waiting_room,
        R.string.waiting_room,
        R.string.waiting_room_description
    ),
    WalkStepRes(
        R.drawable.ic_notifications,
        R.string.notifications,
        R.string.notifications_description
    ),
)

data class WalkStepRes(
    @DrawableRes val imageResource: Int,
    val titleResource: Int? = null,
    val descriptionResource: Int,
) {
    fun toModel(context: Context) = WalkStep(
        imageResource = imageResource,
        titleResource = titleResource?.let { context.getString(it) },
        descriptionResource = context.getString(descriptionResource),
    )
}