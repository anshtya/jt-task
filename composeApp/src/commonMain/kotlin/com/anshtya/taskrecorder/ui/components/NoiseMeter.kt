package com.anshtya.taskrecorder.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anshtya.taskrecorder.ui.theme.MainTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NoiseMeter(
    currentValue: Float,
    modifier: Modifier = Modifier
) {
    val barWidth = 20.dp
    val startAngle = 150f
    val sweepAngle = 240f
    val step = 8
    val textMeasurer = rememberTextMeasurer()

    Box(modifier.padding(20.dp)) {
        Canvas(
            modifier = modifier.fillMaxSize(),
            onDraw = {
                drawColoredBars(
                    barWidth = barWidth,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    step = step
                )
                drawNumbers(
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    step = step,
                    textMeasurer = textMeasurer
                )
                drawNeedle(
                    startAngle = startAngle,
                    currentValue = currentValue,
                    sweepAngle = sweepAngle
                )
            }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(
                text = "${currentValue.toInt()}",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "dB",
                fontSize = 24.sp
            )
        }
    }
}

private fun DrawScope.drawColoredBars(
    barWidth: Dp,
    startAngle: Float,
    sweepAngle: Float,
    step: Int,
) {
    val anglePerSweep = sweepAngle / step

    val blueSweep = anglePerSweep * 5
    drawArc(
        color = Color.Blue,
        topLeft = Offset(0f, 0f),
        startAngle = startAngle,
        sweepAngle = blueSweep,
        useCenter = false,
        style = Stroke(width = barWidth.toPx())
    )

    val redSweep = anglePerSweep * 3
    drawArc(
        color = Color.Red,
        topLeft = Offset(0f, 0f),
        startAngle = startAngle + blueSweep,
        sweepAngle = redSweep,
        useCenter = false,
        style = Stroke(width = barWidth.toPx())
    )
}

private fun DrawScope.drawNumbers(
    startAngle: Float,
    sweepAngle: Float,
    step: Int,
    textMeasurer: TextMeasurer
) {
    val anglePerStep = sweepAngle / step

    (0..step).forEach { step ->
        val drawAngle = (startAngle + (anglePerStep * step))
        val radians = drawAngle * PI / 180
        val textRadius = center.x * 0.8f
        val x = center.x + textRadius * cos(radians).toFloat()
        val y = center.y + textRadius * sin(radians).toFloat()
        drawText(
            textLayoutResult = textMeasurer.measure(
                text = "${step * 10}",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            ),
            topLeft = Offset(x - 20, y - 30)
        )
    }
}

private fun DrawScope.drawNeedle(
    startAngle: Float,
    currentValue: Float,
    sweepAngle: Float,
) {
    val radius = center.x

    val totalRadians = sweepAngle * PI / 180
    val maxValue = 80f

    val valueInRadians = (totalRadians / maxValue) * currentValue.coerceAtMost(maxValue)
    val startAngleRadians = startAngle * PI / 180
    val radians = valueInRadians + startAngleRadians

    val needleLength = radius * 0.6f
    val x = center.x + needleLength * cos(radians).toFloat()
    val y = center.y + needleLength * sin(radians).toFloat()
    val needleOffset = Offset(x, y - 5)

    drawLine(
        color = Color.Gray,
        start = center,
        end = needleOffset,
        strokeWidth = 8.dp.toPx(),
        cap = StrokeCap.Round
    )
}

@Preview
@Composable
private fun NoiseMeterPreview() {
    MainTheme {
        NoiseMeter(
            currentValue = 27f,
            modifier = Modifier
                .size(300.dp)
                .background(Color.Yellow)
        )
    }
}