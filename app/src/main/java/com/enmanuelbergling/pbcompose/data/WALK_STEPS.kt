package com.enmanuelbergling.pbcompose.data

import com.enmanuelbergling.pbcompose.R
import com.enmanuelbergling.walkthrough.model.WalkStep

val IMAGES = listOf(
    R.drawable.ic_explore,
    R.drawable.ic_booking,
    R.drawable.ic_wizard_payment,
    R.drawable.ic_wizard_waiting_room,
    R.drawable.ic_notifications,
)

private const val LOREM_IPSUM =
    "Lorem ipsum odor amet, consectetuer adipiscing elit. Scelerisque dis metus parturient viverra enim. Quisque nostra dui metus eget viverra posuere nulla quisque. Auctor senectus blandit eros facilisi parturient risus volutpat curabitur."

val WALK_STEPS = IMAGES.map {
    WalkStep(
        it, description = LOREM_IPSUM
    )
}