package jp.co.arsaga.extensions.compose.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlin.math.ceil

@Deprecated("公式がexperimentalじゃなくなったら削除")
inline fun <T> LazyListScope.gridItems(
    items: List<T>,
    columnCount: Int,
    crossinline rowModifier: (Int) -> Modifier = { Modifier },
    crossinline itemLayout: @Composable RowScope.(T) -> Unit
) = gridItems(items, columnCount, rowModifier) { item, _, _ ->
    itemLayout(item)
}
inline fun <T> LazyListScope.gridItems(
    items: List<T>,
    columnCount: Int,
    crossinline rowModifier: (Int) -> Modifier = { Modifier },
    crossinline itemLayout: @Composable RowScope.(T, Int, Int) -> Unit
) {
    val rowCount = ceil(items.size / columnCount.toDouble()).toInt()
    return items(
        count = rowCount,
        key = { index: Int -> index }
    ) { rowPosition ->
        val groupedFirstPosition = rowPosition * columnCount
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(rowModifier(rowPosition))
        ) {
            repeat(columnCount) { columnPosition ->
                val position = groupedFirstPosition + columnPosition
                items.getOrNull(position)?.let {
                    itemLayout(it, rowPosition, columnPosition)
                } ?: run {
                    Spacer(modifier = Modifier.weight(1F))
                }
            }
        }
    }
}
