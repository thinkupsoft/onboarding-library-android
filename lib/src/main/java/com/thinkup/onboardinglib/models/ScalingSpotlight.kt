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

class ScalingSpotlight(
    rectangle: Rect,
    isCircular: Boolean = false,
    backgroundAlpha: Float = 0.8f,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 500, easing = LinearEasing),
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
        return if (isCircular) {
            val maxRadius = sqrt((size.height).pow(2) + (size.width).pow(2))
            val targetRadius = rectangle.height / 2
            Rect(center = rectangle.center, radius = (targetRadius + animatedValue * (maxRadius - targetRadius)))
        } else {
            Rect(
                topLeft = Offset(
                    rectangle.topLeft.x - (animatedValue * rectangle.topLeft.x),
                    rectangle.topLeft.y - (animatedValue * rectangle.topLeft.y)
                ), bottomRight = Offset(
                    rectangle.bottomRight.x + (animatedValue * (size.width - rectangle.bottomRight.x)),
                    rectangle.bottomRight.y + (animatedValue * (size.height - rectangle.bottomRight.y))
                )
            )
        }
    }

    override fun getAlpha(animatedValue: Float) = backgroundAlpha
}