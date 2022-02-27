package jp.co.arsaga.extensions.compose.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ceil

@Deprecated("公式がexperimentalじゃなくなったら削除")
inline fun <T> LazyListScope.gridItems(
    items: List<T>,
    columnCount: Int,
    updateNotifier: Any = Any(),
    spaceDp: Dp = 0.dp ,
    crossinline keyGenerator: (Int) -> Any = { it },
    crossinline rowModifier: (Int) -> Modifier = { Modifier },
    crossinline itemLayout: @Composable RowScope.(T) -> Unit
) = gridItems(items, columnCount, updateNotifier, spaceDp, keyGenerator, rowModifier) { item, _, _ ,_->
    itemLayout(item)
}
@Deprecated("公式がexperimentalじゃなくなったら削除")
inline fun <T> LazyListScope.gridItems(
    items: List<T>,
    columnCount: Int,
    updateNotifier: Any = Any(),
    spaceDp: Dp = 0.dp ,
    crossinline keyGenerator: (Int) -> Any = { it },
    crossinline rowModifier: (Int) -> Modifier = { Modifier },
    crossinline itemLayout: @Composable RowScope.(T, Any) -> Unit
) = gridItems(items, columnCount, updateNotifier, spaceDp, keyGenerator, rowModifier) { item, _, _ ,notifier->
    itemLayout(item, notifier)
}
@Deprecated("公式がexperimentalじゃなくなったら削除")
inline fun <T> LazyListScope.gridItems(
    items: List<T>,
    columnCount: Int,
    updateNotifier: Any = Any(),
    spaceDp: Dp = 0.dp ,
    crossinline keyGenerator: (Int) -> Any = { it },
    crossinline rowModifier: (Int) -> Modifier = { Modifier },
    crossinline itemLayout: @Composable RowScope.(T, Int, Int, Any) -> Unit
) {
    val rowCount = ceil(items.size / columnCount.toDouble()).toInt()
    return items(
        count = rowCount,
        key = { index: Int -> keyGenerator(index) }
    ) { rowPosition ->
        val groupedFirstPosition = rowPosition * columnCount
        Row(
            horizontalArrangement = Arrangement.spacedBy(spaceDp),
            modifier = Modifier
                .fillMaxWidth()
                .then(rowModifier(rowPosition))
        ) {
            repeat(columnCount) { columnPosition ->
                val position = groupedFirstPosition + columnPosition
                items.getOrNull(position)?.let {
                    itemLayout(it, rowPosition, columnPosition, updateNotifier)
                } ?: run {
                    Spacer(modifier = Modifier.weight(1F))
                }
            }
        }
    }
}
