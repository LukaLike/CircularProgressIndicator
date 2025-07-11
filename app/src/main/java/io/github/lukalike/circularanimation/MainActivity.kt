package io.github.lukalike.circularanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lukalike.circularanimation.ui.theme.BRIGHT_BLUE
import io.github.lukalike.circularanimation.ui.theme.CircularAnimationTheme
import io.github.lukalike.circularanimation.ui.theme.DEEP_BLUE
import io.github.lukalike.circularanimation.ui.theme.GREY
import io.github.lukalike.circularanimation.ui.theme.LILAC
import io.github.lukalike.circularanimation.ui.theme.RED
import io.github.lukalike.circularanimation.ui.theme.ROSE_PINK

const val CIRCLE_START_ANGLE = 270f // Start at 12 o'clock

val CIRCULAR_TOTAL_VALUE_RANGE = 0f..1f

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CircularAnimationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CircularIndicator(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CircularIndicator(modifier: Modifier = Modifier) {
    var sliderValue by remember { mutableFloatStateOf(0f) }

    val animatedNumber by animateFloatAsState(
        targetValue = sliderValue * 200,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
    )
    val displayText = animatedNumber.toInt().let { if (it > 0) "+$it%" else "$it%" }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressBar(progress = { sliderValue }, modifier = Modifier.size(154.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_electricity),
                    contentDescription = stringResource(R.string.electricity_icon_description),
                    tint = Color.Unspecified
                )

                Text(
                    text = displayText,
                    modifier = Modifier.padding(top = 5.5.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        letterSpacing = 0.15.sp,
                        color = DEEP_BLUE,
                    )
                )
            }
        }

        MySlider(modifier = Modifier.padding(50.dp), sliderValue = sliderValue) { sliderValue = it }
    }
}

@Composable
fun CircularProgressBar(
    progress: () -> Float,
    modifier: Modifier = Modifier
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
    }

    val animatedSweep by animateFloatAsState(
        targetValue = progress().coerceIn(0f, 1f) * 720f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
    )

    Canvas(modifier) {
        drawCircularIndicatorTrack(Brush.sweepGradient(List(2) { GREY }), stroke)

        drawDeterminateCircularIndicator(
            -100f,
            animatedSweep,
            Brush.sweepGradient(0f to BRIGHT_BLUE, .25f to LILAC, .5f to RED, 1f to RED),
            stroke
        )

        if (animatedSweep >= 345f) {
            drawDeterminateCircularIndicator(
                -115f,
                minOf(animatedSweep - 345, 345f),
                Brush.sweepGradient(0f to RED, .25f to RED, .5f to ROSE_PINK, 1f to ROSE_PINK),
                stroke
            )
        }

        if (animatedSweep >= 690f) {
            drawDeterminateCircularIndicator(
                -130f,
                animatedSweep - 690,
                Brush.sweepGradient(listOf(ROSE_PINK, ROSE_PINK)),
                stroke
            )
        }
    }
}

@Composable
fun MySlider(modifier: Modifier = Modifier, sliderValue: Float, onValueChange: (Float) -> Unit) {
    Slider(
        value = sliderValue,
        onValueChange = onValueChange,
        modifier = modifier,
        valueRange = CIRCULAR_TOTAL_VALUE_RANGE,
        colors = SliderDefaults.colors().copy(
            thumbColor = DEEP_BLUE,
            activeTrackColor = BRIGHT_BLUE,
            inactiveTrackColor = LILAC
        )
    )
}

@Preview
@Composable
fun CircularIndicatorPreview() {
    CircularIndicator()
}

@Preview
@Composable
fun CircularProgressBarPreview() {
    val commonModifier = Modifier
        .size(100.dp)
        .padding(10.dp)

    Row {
        Column {
            for (i in generateSequenceUpTo(to = 50.0)) {
                CircularProgressBar(progress = { i.toFloat() / 100 }, modifier = commonModifier)
            }
        }

        Column {
            for (i in generateSequenceUpTo(from = 62.5, to = 100.0)) {
                CircularProgressBar(progress = { i.toFloat() / 100 }, modifier = commonModifier)
            }
        }
    }
}

private fun generateSequenceUpTo(from: Double = 0.0, to: Double) =
    generateSequence(from) { it + 12.5 }.takeWhile { it <= to }
