package me.weishu.kernelsu.ui.component

import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.view.Surface
import android.view.TextureView
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
 * 用 TextureView + MediaPlayer，不能用 VideoView：
 * VideoView 内部是 SurfaceView，属于独立窗口层，会盖在所有 Compose 内容之上，
 * 结果就是「打开只看得见视频，界面全没了」。
 * TextureView 走普通 View 渲染管线，能正常叠在 Compose 图层里。
 *
 * 静音循环。视频层要放在内容之前（底层），不要叠到内容上面。
 */
@Composable
fun VideoBackground(
    @RawRes resId: Int,
    modifier: Modifier = Modifier,
    alpha: Float = 0.5f,
) {
    val context = LocalContext.current
    val textureView = remember {
        TextureView(context).apply { isOpaque = false }
    }

    DisposableEffect(resId, textureView) {
        val player = MediaPlayer()
        var started = false

        fun startPlayback() {
            if (started) return
            val texture = textureView.surfaceTexture ?: return
            started = true
            runCatching {
                context.resources.openRawResourceFd(resId)?.use { afd ->
                    player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                }
                player.isLooping = true
                // 静音
                player.setVolume(0f, 0f)
                player.setSurface(Surface(texture))
                player.prepare()
                player.start()
            }.onFailure {
                started = false
            }
        }

        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                startPlayback()
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) = Unit

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean = true

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) = Unit
        }

        // 复用时纹理可能已经就绪，直接开
        if (textureView.isAvailable) startPlayback()

        onDispose {
            textureView.surfaceTextureListener = null
            runCatching { player.release() }
        }
    }

    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                // 放大裁掉黑边，等效 ContentScale.Crop
                scaleX = 1.35f
                scaleY = 1.35f
                this.alpha = alpha
            },
        factory = { textureView },
    )
}
