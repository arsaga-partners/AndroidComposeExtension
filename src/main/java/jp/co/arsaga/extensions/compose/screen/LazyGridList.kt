package jp.co.arsaga.extensions.compose.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import timber.log.Timber
import kotlin.math.ceil

@Deprecated("公式がexperimentalじゃなくなったら削除")
inline fun <T> LazyListScope.gridItems(
    items: List<T>,
    columnCount: Int,
    updateNotifier: Any = Any(),
    crossinline rowModifier: (Int) -> Modifier = { Modifier },
    crossinline itemLayout: @Composable RowScope.(T) -> Unit
) = gridItems(items, columnCount, updateNotifier, rowModifier) { item, _, _ ,_->
    itemLayout(item)
}
@Deprecated("公式がexperimentalじゃなくなったら削除")
inline fun <T> LazyListScope.gridItems(
    items: List<T>,
    columnCount: Int,
    updateNotifier: Any = Any(),
    crossinline rowModifier: (Int) -> Modifier = { Modifier },
    crossinline itemLayout: @Composable RowScope.(T, Any) -> Unit
) = gridItems(items, columnCount, updateNotifier, rowModifier) { item, _, _ ,notifier->
    itemLayout(item, notifier)
}
@Deprecated("公式がexperimentalじゃなくなったら削除")
inline fun <T> LazyListScope.gridItems(
    items: List<T>,
    columnCount: Int,
    updateNotifier: Any = Any(),
    crossinline rowModifier: (Int) -> Modifier = { Modifier },
    crossinline itemLayout: @Composable RowScope.(T, Int, Int, Any) -> Unit
) {
    val rowCount = ceil(items.size / columnCount.toDouble()).toInt()
    Timber.d("テスト通過１")
    return items(
        count = rowCount,
        key = { index: Int -> index }
    ) { rowPosition ->
        Timber.d("テスト通過２")
        val groupedFirstPosition = rowPosition * columnCount
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(rowModifier(rowPosition))
        ) {
            Timber.d("テスト通過３")
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
