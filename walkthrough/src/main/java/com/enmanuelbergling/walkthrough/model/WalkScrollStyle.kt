package com.enmanuelbergling.walkthrough.model

/**
 * Scroll animation
 * */
sealed interface WalkScrollStyle {
    data object Normal : WalkScrollStyle

    /**
     *  @param boxAngle around the rotation in performed
     *  @param reverse to define whether you are in or out of the cube
     *  */
    data class Instagram(
        val boxAngle: Int = 30,
        val reverse: Boolean = false,
    ):WalkScrollStyle
}