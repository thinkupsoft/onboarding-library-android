package com.thinkup.onboardinglib.models

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.math.pow
import kotlin.math.sqrt

enum class HorizontalStart {
    LEFT,
    RIGHT
}

enum class VerticalStart {
    TOP,
    BOTTOM
}

class SlidingSpotlight(
    rectangle: Rect,
    isCircular: Boolean = false,
    backgroundAlpha: Float = 0.8f,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 500, easing = LinearEasing),
    private val horizontalStart: HorizontalStart? = null,
    private val verticalStart: VerticalStart? = VerticalStart.TOP,
    hint: @Composable () -> Unit
) : BaseSpotlight(
    rectangle = rectangle,
    isCircular = isCircular,
    backgroundAlpha = backgroundAlpha,
    animationSpec = animationSpec,
    hint = hint
) {

    override val initialAnimatedValue: Float
        get() = 1f

    override val finalAnimatedValue: Float
        get() = 0f

    override fun getPathRect(size: Size, animatedValue: Float): Rect {
        return Rect(
            offset = Offset(
                when (horizontalStart) {
                    HorizontalStart.LEFT -> rectangle.topLeft.x - (animatedValue * rectangle.bottomRight.x)
                    HorizontalStart.RIGHT -> rectangle.topLeft.x + (animatedValue * (size.width - rectangle.topLeft.x))
                    null -> rectangle.topLeft.x
                },
                when (verticalStart) {
                    VerticalStart.TOP -> rectangle.topLeft.y - (animatedValue * rectangle.bottomRight.y)
                    VerticalStart.BOTTOM -> rectangle.topLeft.y + (animatedValue * (size.height - rectangle.topLeft.y))
                    null -> rectangle.topLeft.y
                },
            ),
            size = rectangle.size
        )
    }

    override fun getAlpha(animatedValue: Float) = backgroundAlpha
}