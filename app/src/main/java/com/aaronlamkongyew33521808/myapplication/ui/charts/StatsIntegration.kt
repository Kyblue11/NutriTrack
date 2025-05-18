package com.aaronlamkongyew33521808.myapplication.ui.charts

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Disclaimer:
// GenAi model ChatGPT 4.o was used to format and generate
// the mathematical calculations needed to form the animatedHeights of each bar in the barchart,
// to add the grid lines and the axis labels to the scatterplot,
// to add the axis labels to the barchart,
// and to fix overlapping label bugs.

@Composable
fun ChartBar(
    data: List<Pair<String, Double>>,
    modifier: Modifier = Modifier,
    chartTitle: String = "Total HEIFA Scores by User",
    xAxisLabel: String = "User ID",
    yAxisLabel: String = "Score (0–100)",
    maxValue: Double = 100.0
) {
    val colors = MaterialTheme.colorScheme
    val density = LocalDensity.current

    // Precompute your animated heights
    val animatedHeights = data.map { (_, value) ->
        val target = with(density) {
            // scale to 240.dp max height
            (value / maxValue).coerceIn(0.0, 1.0) * 240.dp.toPx()
        }
        animateFloatAsState(
            targetValue = target.toFloat(),
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        ).value
    }

    Column(modifier.padding(16.dp)) {
        // 1) Chart title
        Text(
            chartTitle,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 2) Chart + axes
        Box(Modifier.fillMaxWidth()) {
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                val fullWidth = size.width
                val fullHeight = size.height
                val marginLeft = 48.dp.toPx()      // leave space for Y-axis ticks/label
                val marginBottom = 32.dp.toPx()    // leave space for X-axis labels

                val chartWidth = fullWidth - marginLeft
                val chartHeight = fullHeight - marginBottom

                // a) draw horizontal grid lines + Y-axis tick labels
                val steps = 4
                for (i in 0..steps) {
                    val y = chartHeight - (chartHeight / steps) * i
                    // grid
                    drawLine(
                        color = colors.onSurface.copy(alpha = 0.2f),
                        start = Offset(marginLeft, y),
                        end   = Offset(fullWidth, y),
                        strokeWidth = 1f
                    )
                    // tick label
                    drawContext.canvas.nativeCanvas.drawText(
                        "${(maxValue / steps * i).toInt()}",
                        marginLeft - 8.dp.toPx(),
                        y + 4.dp.toPx(),
                        android.graphics.Paint().apply {
                            textSize = 12.sp.toPx()
                            textAlign = android.graphics.Paint.Align.RIGHT
                            color = android.graphics.Color.DKGRAY
                        }
                    )
                }

                // b) draw bars + X-axis ticks/labels
                val barCount = data.size
                val barWidth = chartWidth / (barCount * 1.5f)

                data.forEachIndexed { idx, (label, value) ->
                    val left = marginLeft + idx * 1.5f * barWidth + barWidth * 0.25f
                    val barH = animatedHeights[idx]

                    // background track
                    drawRoundRect(
                        color = colors.onSurface.copy(alpha = 0.05f),
                        topLeft = Offset(left, 0f),
                        size    = Size(barWidth, chartHeight),
                        cornerRadius = CornerRadius(6.dp.toPx())
                    )
                    // active bar
                    drawRoundRect(
                        color = colors.primary,
                        topLeft = Offset(left, chartHeight - barH),
                        size    = Size(barWidth, barH),
                        cornerRadius = CornerRadius(6.dp.toPx())
                    )
                    // value label above bar
                    drawContext.canvas.nativeCanvas.drawText(
                        "${"%.1f".format(value)}",
                        left + barWidth / 2f,
                        chartHeight - barH - 6.dp.toPx(),
                        android.graphics.Paint().apply {
                            textSize = 12.sp.toPx()
                            textAlign = android.graphics.Paint.Align.CENTER
                            color = android.graphics.Color.BLACK
                        }
                    )
                    // X-axis tick label
                    drawContext.canvas.nativeCanvas.drawText(
                        label,
                        left + barWidth / 2f,
                        chartHeight + 16.dp.toPx(),
                        android.graphics.Paint().apply {
                            textSize = 12.sp.toPx()
                            textAlign = android.graphics.Paint.Align.CENTER
                            color = android.graphics.Color.DKGRAY
                        }
                    )
                }

                // c) Y‑axis header (rotated) in that left margin
                drawContext.canvas.nativeCanvas.apply {
                    save()
                    rotate(-90f, 8.dp.toPx(), chartHeight / 2f)
                    drawText(
                        yAxisLabel,
                        -chartHeight / 2f,
                        8.dp.toPx(),
                        android.graphics.Paint().apply {
                            textSize = 14.sp.toPx()
                            textAlign = android.graphics.Paint.Align.CENTER
                            color = android.graphics.Color.DKGRAY
                        }
                    )
                    restore()
                }
            }
        }

        // 3) X‑axis main label
        Text(
            xAxisLabel,
            style = MaterialTheme.typography.bodySmall.copy(color = colors.onSurfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}




@Composable
fun ChartScatter(
    points: List<Pair<Double, Double>>,
    modifier: Modifier = Modifier,
    chartTitle: String = "Fruit vs. Vegetable HEIFA Sub‑scores",
    xAxisLabel: String = "Fruit Score (0–10)",
    yAxisLabel: String = "Vegetable Score (0–10)",
    maxAxis: Double = 10.0
) {
    val colors = MaterialTheme.colorScheme

    Column(modifier.padding(16.dp)) {
        Text(
            chartTitle,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Box(Modifier.fillMaxWidth()) {
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    // 1) add padding so nothing touches the edges
                    .padding(start = 48.dp, end = 24.dp, bottom = 32.dp, top = 16.dp)
            ) {
                val w = size.width
                val h = size.height
                val originX = 0f
                val originY = h

                // draw axes
                drawLine(colors.onSurfaceVariant, Offset(originX, 0f), Offset(originX, originY), strokeWidth = 2f)
                drawLine(colors.onSurfaceVariant, Offset(originX, originY), Offset(w, originY), strokeWidth = 2f)

                // grid & ticks
                val steps = 5
                for (i in 0..steps) {
                    val y = originY - (h / steps) * i
                    val x = originX + (w / steps) * i

                    // horizontal grid
                    drawLine(colors.onSurface.copy(alpha = 0.1f), Offset(originX, y), Offset(w, y), strokeWidth = 1f)
                    // vertical grid
                    drawLine(colors.onSurface.copy(alpha = 0.1f), Offset(x, 0f), Offset(x, originY), strokeWidth = 1f)

                    // Y‑axis tick labels
                    drawContext.canvas.nativeCanvas.drawText(
                        "${"%.1f".format(maxAxis / steps * i)}",
                        originX - 8.dp.toPx(),
                        y + 4.dp.toPx(),
                        android.graphics.Paint().apply {
                            textSize = 12.sp.toPx()
                            textAlign = android.graphics.Paint.Align.RIGHT
                            color = android.graphics.Color.DKGRAY
                        }
                    )

                    // 2) skip drawing the very first/last X‑tick if it collides with the axis label below
                    if (i != 0 && i != steps) {
                        drawContext.canvas.nativeCanvas.drawText(
                            "${"%.1f".format(maxAxis / steps * i)}",
                            x,
                            originY + 16.dp.toPx(),
                            android.graphics.Paint().apply {
                                textSize = 12.sp.toPx()
                                textAlign = android.graphics.Paint.Align.CENTER
                                color = android.graphics.Color.DKGRAY
                            }
                        )
                    }
                }

                // plot points
                points.forEach { (xVal, yVal) ->
                    val px = originX + ((xVal / maxAxis).coerceIn(0.0, 1.0) * w).toFloat()
                    val py = (originY - (yVal / maxAxis).coerceIn(0.0, 1.0) * h).toFloat()
                    drawCircle(
                        color = colors.secondary,
                        radius = 8.dp.toPx(),
                        center = Offset(px, py),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }
        }

        // 3) axis‑label Row—still left/right aligned, but thanks to the padding above, it won’t overlap
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 48.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(yAxisLabel, style = MaterialTheme.typography.bodySmall.copy(color = colors.onSurfaceVariant))
            Text(xAxisLabel, style = MaterialTheme.typography.bodySmall.copy(color = colors.onSurfaceVariant))
        }
    }
}