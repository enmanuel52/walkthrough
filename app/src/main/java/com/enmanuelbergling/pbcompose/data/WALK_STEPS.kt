package com.enmanuelbergling.pbcompose.data

import com.enmanuelbergling.pbcompose.R
import com.enmanuelbergling.walkthrough.model.WalkStep

val WALK_STEPS = arrayListOf(
    WalkStep(
        R.drawable.ic_explore,
        null,//R.string.explore,
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