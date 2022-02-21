package com.thinkup.onboardinglib.models

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

class StaticSpotlight(
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
    hint = hint,
) {

    override val initialAnimatedValue: Float
        get() = backgroundAlpha

    override val finalAnimatedValue: Float
        get() = backgroundAlpha

    override fun getPathRect(size: Size, animatedValue: Float): Rect {
        return if (isCircular) {
            Rect(center = rectangle.center, radius = rectangle.height / 2)
        } else {
            rectangle
        }
    }

    override fun getAlpha(animatedValue: Float) = backgroundAlpha
}