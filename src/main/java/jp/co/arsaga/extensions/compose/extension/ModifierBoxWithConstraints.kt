package jp.co.arsaga.extensions.compose.extension

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier


fun Modifier.widthRatio(
    boxWithConstraintsScope: BoxWithConstraintsScope,
    ratio: Float
): Modifier = width(boxWithConstraintsScope.maxWidth * ratio)

fun Modifier.widthCarousel(
    boxWithConstraintsScope: BoxWithConstraintsScope,
    visibleCount: Int,
    allWidth: Float = 0.8F
): Modifier = width(boxWithConstraintsScope.maxWidth * (allWidth / visibleCount))
