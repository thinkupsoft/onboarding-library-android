package com.thinkup.onboardinglib.models

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

abstract class BaseSpotlight(
    val rectangle: Rect,
    val isCircular: Boolean = false,
    val backgroundAlpha: Float = 0.8f,
    val animationSpec: AnimationSpec<Float> = tween(durationMillis = 500, easing = LinearEasing),
    val hint: @Composable () -> Unit
) {

    abstract val initialAnimatedValue: Float
    abstract val finalAnimatedValue: Float

    abstract fun getPathRect(size: Size, animatedValue: Float): Rect
    abstract fun getAlpha(animatedValue: Float): Float
}

