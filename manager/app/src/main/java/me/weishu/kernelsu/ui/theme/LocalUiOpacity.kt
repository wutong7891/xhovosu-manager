package me.weishu.kernelsu.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * 界面容器的不透明度。
 *
 * 用户可以在设置里调，值从 MainActivity 通过 CompositionLocal 往下传，
 * 各页面的 Card 从这里读，这样拖动滑块整界面会跟着变透明。
 * 以前是各页面写死固定值，调了等于没调。
 */
val LocalUiOpacity = staticCompositionLocalOf { 0.05f }
