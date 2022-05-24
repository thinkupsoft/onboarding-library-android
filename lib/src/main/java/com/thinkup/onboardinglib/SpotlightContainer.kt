package com.thinkup.onboardinglib

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipPath
import com.thinkup.onboardinglib.models.BaseSpotlight

@Composable
fun SpotlightContainer(spotlights: List<BaseSpotlight>, onFinished: () -> Unit) {
    var current by remember { mutableStateOf(0) }
    val spotlight = spotlights[current]
    val onClick = {
        if (spotlights.size == current + 1) {
            onFinished.invoke()
        } else {
            current++
        }
    }
    Spotlight(
        id = current,
        model = spotlight,
        onClick = { onClick() },
        hint = spotlight.hint,
    )
}

@Composable
fun Spotlight(
    id: Int,
    model: BaseSpotlight,
    onClick: () -> Unit,
    hint: @Composable () -> Unit = {}
) {
    var show by remember { mutableStateOf(false) }
    val animatedValue by animateFloatAsState(
        targetValue = if (show) model.finalAnimatedValue else model.initialAnimatedValue,
        animationSpec = model.animationSpec,
        finishedListener = { if (!show)
            onClick()
        }
    )
    LaunchedEffect(id) {
        show = true
    }
    Box(
        Modifier
            .fillMaxSize()
            .clickable {
                if (model.initialAnimatedValue == model.finalAnimatedValue) {
                    onClick()
                }
                show = false
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                val rect = model.getPathRect(size, animatedValue)
                if (model.isCircular) {
                    addOval(rect)
                } else {
                    addRect(rect)
                }
            }
            clipPath(path, clipOp = ClipOp.Difference) {
                drawRect(SolidColor(Color.Black.copy(alpha = model.getAlpha(animatedValue))))
            }
        }
        if (animatedValue == model.finalAnimatedValue) {
            hint()
        }
    }
}