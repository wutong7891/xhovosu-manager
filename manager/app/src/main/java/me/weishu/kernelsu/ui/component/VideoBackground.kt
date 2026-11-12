package me.weishu.kernelsu.ui.component

import android.net.Uri
import android.widget.VideoView
import androidx.annotation.RawRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

/**
 * 默认背景视频。
 *
 * 没设置自定义图片时用它铺底。用系统 VideoView，不引任何第三方播放器，
 * 静音循环播放——背景只负责氛围，不需要声音。
 *
 * VideoView 会按视频比例居中留黑边，所以整体放大 1.35 倍裁掉边，
 * 等效于 ContentScale.Crop。
 */
@Composable
fun VideoBackground(
    @RawRes resId: Int,
    modifier: Modifier = Modifier,
    alpha: Float = 0.5f,
) {
    val context = LocalContext.current
    val uri = remember(resId, context) {
        Uri.parse("android.resource://${context.packageName}/$resId")
    }

    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                // 放大裁边 + 整体透明度
                scaleX = 1.35f
                scaleY = 1.35f
                this.alpha = alpha
            },
        factory = {
            VideoView(context).apply {
                setVideoURI(uri)
                setOnPreparedListener { player ->
                    player.isLooping = true
                    // 静音
                    player.setVolume(0f, 0f)
                    start()
                }
                setOnErrorListener { _, _, _ ->
                    // 播放失败就当没背景，别让整个页面崩
                    true
                }
            }
        },
        update = { view ->
            if (!view.isPlaying) {
                runCatching { view.start() }
            }
        },
    )

    // 离开页面时停掉，避免后台还在解码耗电
    DisposableEffect(Unit) {
        onDispose {
            // AndroidView 的实例不在作用域里，靠 onDetachedFromWindow 自行回收
        }
    }
}
