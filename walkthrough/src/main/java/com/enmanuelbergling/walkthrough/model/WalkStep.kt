package com.enmanuelbergling.walkthrough.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.enmanuelbergling.walkthrough.R

data class WalkStep(
    @DrawableRes val imageResource: Int,
    @StringRes val titleResource: Int,
    @StringRes val descriptionResource: Int,
)

val WALK_STEPS = arrayListOf(
    WalkStep(
        R.drawable.ic_explore,
        R.string.explore,
        R.string.explore_description
    ),
    WalkStep(
        R.drawable.ic_booking,
        R.string.bookings,
        R.string.bookings_description
    ),
    WalkStep(
        R.drawable.ic_wizard_payment,
        R.string.pay,
        R.string.pay_description
    ),
    WalkStep(
        R.drawable.ic_wizard_waiting_room,
        R.string.waiting_room,
        R.string.waiting_room_description
    ),
    WalkStep(
        R.drawable.ic_notifications,
        R.string.notifications,
        R.string.notifications_description
    ),
)
