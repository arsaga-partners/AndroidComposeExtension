package jp.co.arsaga.extensions.compose.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimateVerticalToggleLayout(
    isVisible: Boolean,
    direction: Alignment.Vertical,
    dampingRatio: Float = Spring.DampingRatioNoBouncy,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val animationSpec = spring(
        dampingRatio = dampingRatio,
        visibilityThreshold = IntSize.VisibilityThreshold
    )
    AnimatedVisibility(
        visible = isVisible,
        modifier = modifier,
        enter = expandVertically(expandFrom = direction, animationSpec = animationSpec),
        exit = shrinkVertically(shrinkTowards = direction, animationSpec = animationSpec)
    ) {
        content()
    }
}