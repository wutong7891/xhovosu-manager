package me.weishu.kernelsu.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/**
 * 背景氛围雪花。
 *
 * 纯绘制层，不占布局、不接手势，叠在背景图之上、内容之下。
 * 每片雪花只有纵向匀速 + 横向正弦漂流，参数固定种子生成，
 * 保证每次进入同一位置，不会每次重建时雪花突然乱跳。
 */
private data class SnowFlake(
    /** 横向基准位置，0..1 相对宽度 */
    val x: Float,
    /** 纵向起始偏移，0..1 */
    val y: Float,
    /** 半径，dp */
    val radius: Float,
    /** 下落速度倍率 */
    val speed: Float,
    /** 横向漂移幅度，相对宽度 */
    val drift: Float,
    /** 漂移相位 */
    val phase: Float,
    /** 不透明度 */
    val alpha: Float,
)

@Composable
fun SnowfallOverlay(
    modifier: Modifier = Modifier,
    count: Int = 72,
) {
    val flakes = remember(count) {
        val random = Random(20261112)
        List(count) {
            SnowFlake(
                x = random.nextFloat(),
                y = random.nextFloat(),
                radius = 1.1f + random.nextFloat() * 2.4f,
                speed = 0.5f + random.nextFloat() * 0.9f,
                drift = 0.010f + random.nextFloat() * 0.032f,
                phase = random.nextFloat() * 2f * PI.toFloat(),
                alpha = 0.22f + random.nextFloat() * 0.46f,
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "snowfall")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "snowProgress",
    )

    Canvas(modifier) {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@Canvas

        flakes.forEach { flake ->
            val fall = (flake.y + progress * flake.speed) % 1f
            val cy = fall * (h + 140f) - 70f

            val sway = sin(progress * 2f * PI.toFloat() * flake.speed + flake.phase)
            val cx = (flake.x + sway * flake.drift) * w

            val r = flake.radius * density

            // 外圈柔光 + 内核亮点，凑出一点体积感，避免看起来像死板的圆点
            drawCircle(
                color = Color.White.copy(alpha = flake.alpha * 0.30f),
                radius = r * 2.1f,
                center = Offset(cx, cy),
            )
            drawCircle(
                color = Color.White.copy(alpha = flake.alpha),
                radius = r,
                center = Offset(cx, cy),
            )
        }
    }
}
