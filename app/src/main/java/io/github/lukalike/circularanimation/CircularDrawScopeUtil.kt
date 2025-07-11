package io.github.lukalike.circularanimation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate

fun DrawScope.drawCircularIndicatorTrack(
    brush: Brush,
    stroke: Stroke
) = drawCircularIndicator(-100f, 360f, brush, stroke)

fun DrawScope.drawDeterminateCircularIndicator(
    rotation: Float,
    sweep: Float,
    brush: Brush,
    stroke: Stroke
) = drawCircularIndicator(rotation, sweep, brush, stroke)

fun DrawScope.drawCircularIndicator(
    rotation: Float,
    sweep: Float,
    brush: Brush,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset

    val arcSize = Size(arcDimen, arcDimen)
    val centerOffset = Offset(diameterOffset, diameterOffset)

    rotate(degrees = rotation) {
        drawArc(
            brush = brush,
            startAngle = CIRCLE_START_ANGLE + 100,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = centerOffset,
            size = arcSize,
            style = stroke
        )
    }
}
