package jp.co.arsaga.extensions.compose.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
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
    AnimateVerticalToggleLayoutImpl(dampingRatio) { animationSpec ->
        AnimatedVisibility(
            visible = isVisible,
            modifier = modifier,
            enter = expandVertically(expandFrom = direction, animationSpec = animationSpec),
            exit = shrinkVertically(shrinkTowards = direction, animationSpec = animationSpec)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimateVerticalToggleLayout(
    isVisibleState: MutableTransitionState<Boolean>,
    direction: Alignment.Vertical,
    dampingRatio: Float = Spring.DampingRatioNoBouncy,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimateVerticalToggleLayoutImpl(dampingRatio) { animationSpec ->
        AnimatedVisibility(
            visibleState = isVisibleState,
            modifier = modifier,
            enter = expandVertically(expandFrom = direction, animationSpec = animationSpec),
            exit = shrinkVertically(shrinkTowards = direction, animationSpec = animationSpec)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimateVerticalToggleLayoutImpl(
    dampingRatio: Float = Spring.DampingRatioNoBouncy,
    content: @Composable (SpringSpec<IntSize>) -> Unit
) {
    val animationSpec = spring(
        dampingRatio = dampingRatio,
        visibilityThreshold = IntSize.VisibilityThreshold
    )
    content(animationSpec)
}