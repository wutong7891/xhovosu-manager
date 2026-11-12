package me.weishu.kernelsu.ui.screen.settings

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Article
import androidx.compose.material.icons.automirrored.rounded.Rule
import androidx.compose.material.icons.rounded.Opacity
import androidx.compose.material.icons.rounded.Brightness6
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Adb
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.DeveloperMode
import androidx.compose.material.icons.rounded.DisplaySettings
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LayersClear
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.rounded.HideImage
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import top.yukonga.miuix.kmp.basic.Slider
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.SystemUpdate
import androidx.compose.material.icons.rounded.SystemUpdateAlt
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.weishu.kernelsu.R
import me.weishu.kernelsu.ui.UiMode
import me.weishu.kernelsu.ui.component.KsuIsValid
import me.weishu.kernelsu.ui.component.dialog.rememberLoadingDialog
import me.weishu.kernelsu.ui.component.miuix.SendLogDialog
import me.weishu.kernelsu.ui.component.uninstalldialog.UninstallDialog
import me.weishu.kernelsu.ui.theme.LocalEnableBlur
import me.weishu.kernelsu.ui.util.BlurredBar
import me.weishu.kernelsu.ui.util.rememberBlurBackdrop
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.ui.unit.sp
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import top.yukonga.miuix.kmp.basic.CardDefaults

private const val SHOW_FOREGROUND_WATCHER = false

/**
 * @author weishu
 * @date 2023/1/1.
 */
@Composable
fun SettingPagerMiuix(
    uiState: SettingsUiState,
    actions: SettingsScreenActions,
    bottomInnerPadding: Dp,
) {
    val scrollBehavior = MiuixScrollBehavior()
    val enableBlur = LocalEnableBlur.current
    val backdrop = rememberBlurBackdrop(enableBlur)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else colorScheme.surface.copy(alpha = me.weishu.kernelsu.ui.theme.LocalUiOpacity.current)
    val loadingDialog = rememberLoadingDialog()
    val showUninstallDialog = rememberSaveable { mutableStateOf(false) }
    val showSendLogDialog = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(

        containerColor = top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme.surface.copy(alpha = 0.72f),
        topBar = {
            BlurredBar(backdrop) {
                TopAppBar(
                    color = barColor,
                    title = stringResource(R.string.settings),
                    scrollBehavior = scrollBehavior
                )
            }
        },
        popupHost = { },
        contentWindowInsets = WindowInsets.systemBars.add(WindowInsets.displayCutout).only(WindowInsetsSides.Horizontal),
    ) { innerPadding ->
        Box(modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .scrollEndHaptic()
                    .overScrollVertical()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .padding(horizontal = 12.dp),
                contentPadding = innerPadding,
                overscrollEffect = null,
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
            colors = CardDefaults.defaultColors(color = colorScheme.surface.copy(alpha = me.weishu.kernelsu.ui.theme.LocalUiOpacity.current)),
        ) {
                        SwitchPreference(
                            title = stringResource(id = R.string.settings_check_update),
                            summary = stringResource(id = R.string.settings_check_update_summary),
                            startAction = {
                                Icon(
                                    Icons.Rounded.SystemUpdate,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = stringResource(id = R.string.settings_check_update),
                                    tint = colorScheme.onBackground
                                )
                            },
                            checked = uiState.checkUpdate,
                            onCheckedChange = actions.onSetCheckUpdate
                        )
                        // ---------- 特色功能 ----------
                    KsuIsValid {
                        Card(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(),
            colors = CardDefaults.defaultColors(color = colorScheme.surface.copy(alpha = me.weishu.kernelsu.ui.theme.LocalUiOpacity.current)),
        ) {
                            val stealthTitle = stringResource(id = R.string.customize_title)
                            ArrowPreference(
                                title = stealthTitle,
                                summary = stringResource(id = R.string.customize_switch_summary),
                                startAction = {
                                    Icon(
                                        Icons.Rounded.Visibility,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = stealthTitle,
                                        tint = colorScheme.onBackground,
                                    )
                                },
                                onClick = actions.onOpenStealth,
                            )
                        }
                    }

                    KsuIsValid {
                            SwitchPreference(
                                title = stringResource(id = R.string.settings_module_check_update),
                                summary = stringResource(id = R.string.settings_check_update_summary),
                                startAction = {
                                    Icon(
                                        Icons.Rounded.SystemUpdateAlt,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = stringResource(id = R.string.settings_check_update),
                                        tint = colorScheme.onBackground
                                    )
                                },
                                checked = uiState.checkModuleUpdate,
                                onCheckedChange = actions.onSetCheckModuleUpdate
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
            colors = CardDefaults.defaultColors(color = colorScheme.surface.copy(alpha = me.weishu.kernelsu.ui.theme.LocalUiOpacity.current)),
        ) {
                        OverlayDropdownPreference(
                            title = stringResource(id = R.string.settings_ui_mode),
                            summary = stringResource(id = R.string.settings_ui_mode_summary),
                            items = UiMode.entries.map { it.name },
                            startAction = {
                                Icon(
                                    Icons.Rounded.DisplaySettings,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = stringResource(id = R.string.settings_ui_mode),
                                    tint = colorScheme.onBackground
                                )
                            },
                            selectedIndex = if (uiState.uiMode == UiMode.Material.value) 1 else 0,
                            onSelectedIndexChange = actions.onSetUiModeIndex
                        )
                        ArrowPreference(
                            title = stringResource(id = R.string.settings_theme),
                            summary = stringResource(id = R.string.settings_theme_summary),
                            startAction = {
                                Icon(
                                    Icons.Rounded.Palette,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = stringResource(id = R.string.settings_theme),
                                    tint = colorScheme.onBackground
                                )
                            },
                            onClick = actions.onOpenTheme
                        )
                    }

                    // 自定义背景图：选一张图铺在整个应用后面，配合雪花做氛围
                    val showBgIntensity = remember { mutableStateOf(false) }
                    val showUiOpacity = remember { mutableStateOf(false) }
                    val showSnowSpeed = remember { mutableStateOf(false) }
                    // 本地拖动值：直接绑 uiState 会导致每次移动都写配置、触发全局刷新，
                    // 手势被反复打断，表现就是「拖不动」。松手才落盘。
                    var bgIntensityState by remember(uiState.backgroundIntensity) {
                        mutableFloatStateOf(uiState.backgroundIntensity)
                    }
                    var uiOpacityState by remember(uiState.uiOpacity) {
                        mutableFloatStateOf(uiState.uiOpacity)
                    }
                    var snowSpeedState by remember(uiState.snowSpeed) {
                        mutableFloatStateOf(uiState.snowSpeed)
                    }
                    Card(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
            colors = CardDefaults.defaultColors(color = colorScheme.surface.copy(alpha = me.weishu.kernelsu.ui.theme.LocalUiOpacity.current)),
        ) {
                        val backgroundTitle = stringResource(id = R.string.settings_background)
                        val hasBackground = uiState.nightBackgroundUri.isNotBlank()
                        ArrowPreference(
                            title = backgroundTitle,
                            summary = if (hasBackground) {
                                // 显示选中的文件名，一眼能看出到底选没选上
                                android.net.Uri.decode(uiState.nightBackgroundUri)
                                    .substringAfterLast('/')
                                    .ifBlank { stringResource(id = R.string.settings_background_set) }
                            } else {
                                stringResource(id = R.string.settings_background_summary)
                            },
                            startAction = {
                                Icon(
                                    Icons.Rounded.Image,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = backgroundTitle,
                                    tint = colorScheme.onBackground
                                )
                            },
                            onClick = actions.onPickBackground
                        )
                        // 三个可调项。用 ArrowPreference + bottomAction 挂滑块，
                        // 和主题页那个缩放滑块是同一种写法——裸 Column 里的 Slider
                        // 在竖向滚动容器中量不出宽度，手指划不动。
                        ArrowPreference(
                            title = stringResource(id = R.string.settings_bg_intensity),
                            summary = stringResource(id = R.string.settings_bg_intensity_summary),
                            startAction = {
                                Icon(
                                    Icons.Rounded.Brightness6,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = null,
                                    tint = colorScheme.onBackground
                                )
                            },
                            endActions = {
                                // 横向拖动在部分 ROM 上收不到手势，两端配步进按钮兜底
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    StepperButton("－") {
                                        val v = (bgIntensityState - 0.05f).coerceIn(0f, 1f)
                                        bgIntensityState = v
                                        actions.onSetBackgroundIntensity(v)
                                    }
                                    Text(
                                        text = "${(bgIntensityState * 100).toInt()}%",
                                        color = colorScheme.onSurfaceVariantActions,
                                    )
                                    StepperButton("＋") {
                                        val v = (bgIntensityState + 0.05f).coerceIn(0f, 1f)
                                        bgIntensityState = v
                                        actions.onSetBackgroundIntensity(v)
                                    }
                                }
                            },
                            onClick = { showBgIntensity.value = !showBgIntensity.value },
                            holdDownState = showBgIntensity.value,
                            bottomAction = {
                                Slider(
                                    value = bgIntensityState,
                                    onValueChange = { bgIntensityState = it },
                                    onValueChangeFinished = {
                                        actions.onSetBackgroundIntensity(bgIntensityState)
                                    },
                                    valueRange = 0f..1f,
                                )
                            },
                        )
                        ArrowPreference(
                            title = stringResource(id = R.string.settings_ui_opacity),
                            summary = stringResource(id = R.string.settings_ui_opacity_summary),
                            startAction = {
                                Icon(
                                    Icons.Rounded.Opacity,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = null,
                                    tint = colorScheme.onBackground
                                )
                            },
                            endActions = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    StepperButton("－") {
                                        val v = (uiOpacityState - 0.05f).coerceIn(0.05f, 1f)
                                        uiOpacityState = v
                                        actions.onSetUiOpacity(v)
                                    }
                                    Text(
                                        text = "${(uiOpacityState * 100).toInt()}%",
                                        color = colorScheme.onSurfaceVariantActions,
                                    )
                                    StepperButton("＋") {
                                        val v = (uiOpacityState + 0.05f).coerceIn(0.05f, 1f)
                                        uiOpacityState = v
                                        actions.onSetUiOpacity(v)
                                    }
                                }
                            },
                            onClick = { showUiOpacity.value = !showUiOpacity.value },
                            holdDownState = showUiOpacity.value,
                            bottomAction = {
                                Slider(
                                    value = uiOpacityState,
                                    onValueChange = { uiOpacityState = it },
                                    onValueChangeFinished = {
                                        actions.onSetUiOpacity(uiOpacityState)
                                    },
                                    valueRange = 0.05f..1f,
                                )
                            },
                        )
                        ArrowPreference(
                            title = stringResource(id = R.string.settings_snow_speed),
                            summary = stringResource(id = R.string.settings_snow_speed_summary),
                            startAction = {
                                Icon(
                                    Icons.Rounded.AcUnit,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = null,
                                    tint = colorScheme.onBackground
                                )
                            },
                            endActions = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    StepperButton("－") {
                                        val v = (snowSpeedState - 0.1f).coerceIn(0.2f, 3f)
                                        snowSpeedState = v
                                        actions.onSetSnowSpeed(v)
                                    }
                                    Text(
                                        text = String.format("%.1fx", snowSpeedState),
                                        color = colorScheme.onSurfaceVariantActions,
                                    )
                                    StepperButton("＋") {
                                        val v = (snowSpeedState + 0.1f).coerceIn(0.2f, 3f)
                                        snowSpeedState = v
                                        actions.onSetSnowSpeed(v)
                                    }
                                }
                            },
                            onClick = { showSnowSpeed.value = !showSnowSpeed.value },
                            holdDownState = showSnowSpeed.value,
                            bottomAction = {
                                Slider(
                                    value = snowSpeedState,
                                    onValueChange = { snowSpeedState = it },
                                    onValueChangeFinished = {
                                        actions.onSetSnowSpeed(snowSpeedState)
                                    },
                                    valueRange = 0.2f..3f,
                                )
                            },
                        )
                        if (hasBackground) {
                            ArrowPreference(
                                title = stringResource(id = R.string.settings_background_clear),
                                startAction = {
                                    Icon(
                                        Icons.Rounded.HideImage,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = null,
                                        tint = colorScheme.onBackground
                                    )
                                },
                                onClick = actions.onClearBackground
                            )
                        }
                    }

                    KsuIsValid {
                        Card(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(),
            colors = CardDefaults.defaultColors(color = colorScheme.surface.copy(alpha = me.weishu.kernelsu.ui.theme.LocalUiOpacity.current)),
        ) {
                            val profileTemplate = stringResource(id = R.string.settings_profile_template)
                            ArrowPreference(
                                title = profileTemplate,
                                summary = stringResource(id = R.string.settings_profile_template_summary),
                                startAction = {
                                    Icon(
                                        Icons.Rounded.Description,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = profileTemplate,
                                        tint = colorScheme.onBackground
                                    )
                                },
                                onClick = actions.onOpenProfileTemplate
                            )
                        }
                    }

                    KsuIsValid {
                        Card(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(),
            colors = CardDefaults.defaultColors(color = colorScheme.surface.copy(alpha = me.weishu.kernelsu.ui.theme.LocalUiOpacity.current)),
        ) {
                            // 前台监听功能暂时下线
                            val watcherTitle = stringResource(id = R.string.foreground_watcher_title)
                            if (SHOW_FOREGROUND_WATCHER) ArrowPreference(
                                title = watcherTitle,
                                summary = stringResource(id = R.string.foreground_watcher_settings_summary),
                                startAction = {
                                    Icon(
                                        Icons.Rounded.Visibility,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = watcherTitle,
                                        tint = colorScheme.onBackground,
                                    )
                                },
                                onClick = actions.onOpenForegroundWatcher,
                            )
                        }
                    }

                    KsuIsValid {
                        Card(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(),
            colors = CardDefaults.defaultColors(color = colorScheme.surface.copy(alpha = me.weishu.kernelsu.ui.theme.LocalUiOpacity.current)),
        ) {
                            val suCompatModeItems = listOf(
                                stringResource(id = R.string.settings_mode_enable_by_default),
                                stringResource(id = R.string.settings_mode_disable_until_reboot),
                                stringResource(id = R.string.settings_mode_disable_always),
                            )

                            val suSummary = when (uiState.suCompatStatus) {
                                "unsupported" -> stringResource(id = R.string.feature_status_unsupported_summary)
                                "managed" -> stringResource(id = R.string.feature_status_managed_summary)
                                "forced" -> stringResource(id = R.string.feature_status_forced_summary)
                                else -> stringResource(id = R.string.settings_sucompat_summary)
                            }
                            OverlayDropdownPreference(
                                title = stringResource(id = R.string.settings_sucompat),
                                summary = suSummary,
                                items = suCompatModeItems,
                                startAction = {
                                    Icon(
                                        Icons.Rounded.AdminPanelSettings,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = stringResource(id = R.string.settings_sucompat),
                                        tint = colorScheme.onBackground
                                    )
                                },
                                enabled = uiState.suCompatStatus == "supported",
                                selectedIndex = uiState.suCompatMode,
                                onSelectedIndexChange = actions.onSetSuCompatMode
                            )

                            val umountSummary = when (uiState.kernelUmountStatus) {
                                "unsupported" -> stringResource(id = R.string.feature_status_unsupported_summary)
                                "managed" -> stringResource(id = R.string.feature_status_managed_summary)
                                "forced" -> stringResource(id = R.string.feature_status_forced_summary)
                                else -> stringResource(id = R.string.settings_kernel_umount_summary)
                            }
                            SwitchPreference(
                                title = stringResource(id = R.string.settings_kernel_umount),
                                summary = umountSummary,
                                startAction = {
                                    Icon(
                                        Icons.Rounded.LayersClear,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = stringResource(id = R.string.settings_kernel_umount),
                                        tint = colorScheme.onBackground
                                    )
                                },
                                enabled = uiState.kernelUmountStatus == "supported",
                                checked = uiState.isKernelUmountEnabled,
                                onCheckedChange = actions.onSetKernelUmountEnabled
                            )

                            val selinuxHideSummary = when (uiState.selinuxHideStatus) {
                                "unsupported" -> stringResource(id = R.string.feature_status_unsupported_summary)
                                "managed" -> stringResource(id = R.string.feature_status_managed_summary)
                                "forced" -> stringResource(id = R.string.feature_status_forced_summary)
                                else -> stringResource(id = R.string.settings_selinux_hide_summary)
                            }
                            SwitchPreference(
                                title = stringResource(id = R.string.settings_selinux_hide),
                                summary = selinuxHideSummary,
                                startAction = {
                                    Icon(
                                        Icons.Rounded.Security,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = stringResource(id = R.string.settings_selinux_hide),
                                        tint = colorScheme.onBackground
                                    )
                                },
                                enabled = uiState.selinuxHideStatus == "supported",
                                checked = uiState.isSelinuxHideEnabled,
                                onCheckedChange = actions.onSetSelinuxHideEnabled
                            )

                            val sulogSummary = when (uiState.sulogStatus) {
                                "unsupported" -> stringResource(id = R.string.feature_status_unsupported_summary)
                                "managed" -> stringResource(id = R.string.feature_status_managed_summary)
                                "forced" -> stringResource(id = R.string.feature_status_forced_summary)
                                else -> stringResource(id = R.string.settings_sulog_summary)
                            }
                            SwitchPreference(
                                title = stringResource(id = R.string.settings_sulog),
                                summary = sulogSummary,
                                startAction = {
                                    Icon(
                                        Icons.AutoMirrored.Rounded.Article,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = stringResource(id = R.string.settings_sulog),
                                        tint = if (uiState.sulogStatus == "supported") colorScheme.onBackground else colorScheme.disabledOnSecondaryVariant
                                    )
                                },
                                enabled = uiState.sulogStatus == "supported",
                                checked = uiState.isSulogEnabled,
                                onCheckedChange = actions.onSetSulogEnabled
                            )

                            val adbRootSummary = when (uiState.adbRootStatus) {
                                "unsupported" -> stringResource(id = R.string.feature_status_unsupported_summary)
                                "managed" -> stringResource(id = R.string.feature_status_managed_summary)
                                "forced" -> stringResource(id = R.string.feature_status_forced_summary)
                                else -> stringResource(id = R.string.settings_adb_root_summary)
                            }
                            SwitchPreference(
                                title = stringResource(id = R.string.settings_adb_root),
                                summary = adbRootSummary,
                                startAction = {
                                    Icon(
                                        Icons.Rounded.Adb,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = stringResource(id = R.string.settings_adb_root),
                                        tint = colorScheme.onBackground
                                    )
                                },
                                enabled = uiState.adbRootStatus == "supported",
                                checked = uiState.isAdbRootEnabled,
                                onCheckedChange = actions.onSetAdbRootEnabled
                            )

                            val shellSuAdbOnlySummary = when (uiState.shellSuAdbOnlyStatus) {
                                "unsupported" -> stringResource(id = R.string.feature_status_unsupported_summary)
                                "managed" -> stringResource(id = R.string.feature_status_managed_summary)
                                "forced" -> stringResource(id = R.string.feature_status_forced_summary)
                                else -> stringResource(id = R.string.settings_shell_su_adb_only_summary)
                            }
                            SwitchPreference(
                                title = stringResource(id = R.string.settings_shell_su_adb_only),
                                summary = shellSuAdbOnlySummary,
                                startAction = {
                                    Icon(
                                        Icons.Rounded.VerifiedUser,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = stringResource(id = R.string.settings_shell_su_adb_only),
                                        tint = if (uiState.shellSuAdbOnlyStatus == "supported") colorScheme.onBackground else colorScheme.disabledOnSecondaryVariant
                                    )
                                },
                                enabled = uiState.shellSuAdbOnlyStatus == "supported",
                                checked = uiState.isShellSuAdbOnlyEnabled,
                                onCheckedChange = actions.onSetShellSuAdbOnlyEnabled
                            )
                            SwitchPreference(
                                title = stringResource(id = R.string.settings_soft_reboot),
                                summary = stringResource(id = R.string.settings_soft_reboot_summary),
                                startAction = {
                                    Icon(
                                        Icons.Rounded.RestartAlt,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = stringResource(id = R.string.settings_soft_reboot),
                                        tint = if (uiState.isLateLoadMode) colorScheme.disabledOnSecondaryVariant else colorScheme.onBackground
                                    )
                                },
                                enabled = !uiState.isLateLoadMode,
                                checked = uiState.isLateLoadMode || uiState.useSoftReboot,
                                onCheckedChange = actions.onSetUseSoftReboot
                            )
                        }

                        Card(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(),
            colors = CardDefaults.defaultColors(color = colorScheme.surface.copy(alpha = me.weishu.kernelsu.ui.theme.LocalUiOpacity.current)),
        ) {
                            SwitchPreference(
                                title = stringResource(id = R.string.settings_umount_modules_default),
                                summary = stringResource(id = R.string.settings_umount_modules_default_summary),
                                startAction = {
                                    Icon(
                                        Icons.AutoMirrored.Rounded.Rule,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = stringResource(id = R.string.settings_umount_modules_default),
                                        tint = colorScheme.onBackground
                                    )
                                },
                                checked = uiState.isDefaultUmountModules,
                                onCheckedChange = actions.onSetDefaultUmountModules
                            )

                            SwitchPreference(
                                title = stringResource(id = R.string.enable_web_debugging),
                                summary = stringResource(id = R.string.enable_web_debugging_summary),
                                startAction = {
                                    Icon(
                                        Icons.Rounded.DeveloperMode,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = stringResource(id = R.string.enable_web_debugging),
                                        tint = colorScheme.onBackground
                                    )
                                },
                                checked = uiState.enableWebDebugging,
                                onCheckedChange = actions.onSetEnableWebDebugging
                            )
                            SwitchPreference(
                                title = stringResource(id = R.string.settings_auto_jailbreak),
                                summary = stringResource(id = R.string.settings_auto_jailbreak_summary),
                                startAction = {
                                    Icon(
                                        Icons.Rounded.FlashOn,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = stringResource(id = R.string.settings_auto_jailbreak),
                                        tint = if (uiState.isLateLoadMode) colorScheme.onBackground else colorScheme.disabledOnSecondaryVariant
                                    )
                                },
                                enabled = uiState.isLateLoadMode,
                                checked = uiState.autoJailbreak,
                                onCheckedChange = actions.onSetAutoJailbreak
                            )
                        }
                    }

                    if (uiState.isLkmMode) {
                        Card(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(),
            colors = CardDefaults.defaultColors(color = colorScheme.surface.copy(alpha = me.weishu.kernelsu.ui.theme.LocalUiOpacity.current)),
        ) {
                            val uninstall = stringResource(id = R.string.settings_uninstall)
                            ArrowPreference(
                                title = uninstall,
                                enabled = !uiState.isLateLoadMode,
                                startAction = {
                                    Icon(
                                        Icons.Rounded.Delete,
                                        modifier = Modifier.padding(end = 6.dp),
                                        contentDescription = uninstall,
                                        tint = colorScheme.onBackground,
                                    )
                                },
                                onClick = { showUninstallDialog.value = true },
                            )
                            UninstallDialog(
                                show = showUninstallDialog.value,
                                onDismissRequest = { showUninstallDialog.value = false }
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .fillMaxWidth(),
            colors = CardDefaults.defaultColors(color = colorScheme.surface.copy(alpha = me.weishu.kernelsu.ui.theme.LocalUiOpacity.current)),
        ) {
                        ArrowPreference(
                            title = stringResource(id = R.string.send_log),
                            startAction = {
                                Icon(
                                    Icons.Rounded.BugReport,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = stringResource(id = R.string.send_log),
                                    tint = colorScheme.onBackground
                                )
                            },
                            onClick = { showSendLogDialog.value = true },
                        )
                        SendLogDialog(
                            show = showSendLogDialog.value,
                            onDismissRequest = { showSendLogDialog.value = false },
                            loadingDialog = loadingDialog
                        )
                        val about = stringResource(id = R.string.about)
                        ArrowPreference(
                            title = about,
                            startAction = {
                                Icon(
                                    Icons.Rounded.Info,
                                    modifier = Modifier.padding(end = 6.dp),
                                    contentDescription = about,
                                    tint = colorScheme.onBackground
                                )
                            },
                            onClick = actions.onOpenAbout,
                        )
                    }
                    Spacer(Modifier.height(bottomInnerPadding))
                }
            }
        }
    }
}

/** 加减步进按钮。滑动手势在部分 ROM 上传不到滑块，用它兜底。 */
@Composable
private fun StepperButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, fontSize = 18.sp, color = colorScheme.primary)
    }
}
