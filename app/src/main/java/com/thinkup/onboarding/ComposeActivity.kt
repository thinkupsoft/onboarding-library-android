package com.thinkup.onboarding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Dimension
import com.thinkup.onboarding.ui.theme.OnboardingTheme
import com.thinkup.onboardinglib.*
import com.thinkup.onboardinglib.models.FadingSpotlight
import com.thinkup.onboardinglib.models.ScalingSpotlight
import com.thinkup.onboardinglib.models.SlidingSpotlight

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnboardingTheme {
                Content()
            }
        }
    }
}

@Composable
fun Content() {
    var hintList by remember { mutableStateOf<List<Rect>?>(null) }
    var showOnBoarding by remember { mutableStateOf(true) }
    Box(
        Modifier
            .fillMaxSize()
    ) {
        Home { hintList = it }
        hintList?.let {
            if (showOnBoarding) {
                SpotlightContainer(spotlights = it.mapIndexed { index, rect ->
                    when (index) {
                        0 -> firstSpotlight(rect)
                        1 -> secondSpotlight(rect)
                        2 -> thirdSpotlight(rect)
                        else -> fourthSpotlight(rect)
                    }
                }) {
                    showOnBoarding = false
                }
            }
        }
    }
}

private fun firstSpotlight(rect: Rect) = ScalingSpotlight(
    rectangle = rect,
    isCircular = false,
) {
    ConstraintLayout(Modifier.fillMaxSize()) {
        val text = createRef()
        val topGuideline = createGuidelineFromTop(with(LocalDensity.current) {
            rect.top.toInt().toDp()
        })
        val startGuideline = createGuidelineFromStart(with(LocalDensity.current) {
            rect.left.toInt().toDp()
        })
        Text("Here you can see the weather", color = Color.White,
            modifier = Modifier.constrainAs(text) {
                bottom.linkTo(topGuideline)
                start.linkTo(startGuideline)
            }
        )
    }
}


private fun secondSpotlight(rect: Rect) = ScalingSpotlight(
    rectangle = rect,
    isCircular = true,
) {
    ConstraintLayout(Modifier.fillMaxSize()) {
        val text = createRef()
        val topGuideline = createGuidelineFromTop(with(LocalDensity.current) {
            rect.top.toInt().toDp()
        })
        val bottomGuideline = createGuidelineFromTop(with(LocalDensity.current) {
            rect.bottom.toInt().toDp()
        })
        val endGuideline = createGuidelineFromStart(with(LocalDensity.current) {
            rect.left.toInt().toDp()
        })
        Text("This button updates the temperature", color = Color.White, textAlign = TextAlign.End,
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(text) {
                    top.linkTo(topGuideline)
                    bottom.linkTo(bottomGuideline)
                    end.linkTo(endGuideline)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                }
        )
    }
}

private fun thirdSpotlight(rect: Rect) = FadingSpotlight(
    rectangle = rect,
    isCircular = true,
) {
    ConstraintLayout(Modifier.fillMaxSize()) {
        val text = createRef()
        val topGuideline = createGuidelineFromTop(with(LocalDensity.current) {
            rect.top.toInt().toDp()
        })
        val startGuideline = createGuidelineFromStart(with(LocalDensity.current) {
            rect.left.toInt().toDp()
        })
        Text("This is a FAB", color = Color.White,
            modifier = Modifier
                .constrainAs(text) {
                    bottom.linkTo(topGuideline)
                    end.linkTo(startGuideline)
                }
        )
    }
}

private fun fourthSpotlight(rect: Rect) = SlidingSpotlight(
    rectangle = rect,
    isCircular = true,
) {
    ConstraintLayout(Modifier.fillMaxSize()) {
        val column = createRef()
        Column(Modifier.constrainAs(column) {
            bottom.linkTo(parent.bottom, 128.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            Text(
                "The library uses the Slots API", color = Color.White,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                "This could be any composable", color = Color.White,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                tint = Color.Green,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(Color.White, shape = MaterialTheme.shapes.small)
            )
        }
    }
}


