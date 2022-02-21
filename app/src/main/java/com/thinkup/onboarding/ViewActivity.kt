package com.thinkup.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.isGone
import com.thinkup.onboarding.databinding.ActivityViewBinding
import com.thinkup.onboardinglib.SpotlightContainer
import com.thinkup.onboardinglib.models.FadingSpotlight
import com.thinkup.onboardinglib.models.ScalingSpotlight
import com.thinkup.onboardinglib.models.SlidingSpotlight

class ViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewBinding

    private var onboardingShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!onboardingShown) {
            onboardingShown = true
            binding.onboarding.apply {
                setContent {
                    MaterialTheme {
                        SpotlightContainer(
                            spotlights = listOf(
                                firstSpotlight(),
                                secondSpotlight(),
                                thirdSpotlight(),
                                )
                        ) {
                            binding.onboarding.isGone = true
                        }
                    }
                }
            }
        }
    }

    private fun firstSpotlight() = FadingSpotlight(
        rectangle = Rect(
            binding.text.left.toFloat(),
            binding.text.top.toFloat(),
            binding.text.right.toFloat(),
            binding.text.bottom.toFloat()
        ),
        isCircular = false,
    ) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val text = createRef()
            val guideline = createGuidelineFromTop(with(LocalDensity.current) {
                binding.text.bottom.toDp()
            })
            Text("This is a TextView", color = Color.White, modifier = Modifier.constrainAs(text) {
                top.linkTo(guideline, 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
        }
    }

    private fun secondSpotlight() = SlidingSpotlight(
        rectangle = Rect(
            binding.button.left.toFloat(),
            binding.button.top.toFloat(),
            binding.button.right.toFloat(),
            binding.button.bottom.toFloat()
        ),
        isCircular = false,
    ) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val text = createRef()
            val guideline = createGuidelineFromTop(with(LocalDensity.current) {
                binding.button.bottom.toDp()
            })
            Text("This is a Button", color = Color.White, modifier = Modifier.constrainAs(text) {
                top.linkTo(guideline, 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
        }
    }

    private fun thirdSpotlight() = ScalingSpotlight(
        rectangle = Rect(
            binding.fab.left.toFloat(),
            binding.fab.top.toFloat(),
            binding.fab.right.toFloat(),
            binding.fab.bottom.toFloat()
        ),
        isCircular = true,
    ) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val text = createRef()
            val topGuideline = createGuidelineFromTop(with(LocalDensity.current) {
                binding.fab.top.toDp()
            })
            val bottomGuideline = createGuidelineFromTop(with(LocalDensity.current) {
                binding.fab.bottom.toDp()
            })
            val startGuideline = createGuidelineFromStart(with(LocalDensity.current) {
                binding.fab.left.toDp()
            })
            Text("This is a FAB", color = Color.White, modifier = Modifier.constrainAs(text) {
                top.linkTo(topGuideline)
                bottom.linkTo(bottomGuideline)
                end.linkTo(startGuideline, 8.dp)
            })
        }
    }
}