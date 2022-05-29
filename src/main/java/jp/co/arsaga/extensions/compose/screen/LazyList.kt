package jp.co.arsaga.extensions.compose.screen

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@InternalCoroutinesApi
@Composable
fun InfiniteListHandler(
    listState: LazyListState,
    buffer: Int,
    onLoadMore: suspend () -> Unit
) {
    if (listState.layoutInfo.totalItemsCount == 0) return
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItemsNumber - buffer)
        }
    }

    LaunchedEffect(loadMore) {
        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                onLoadMore()
            }
    }
}

suspend fun autoScroll(
    lazyListState: LazyListState,
    totalScrollPixel: Float,
    totalDuration: Long,
    roughness: Long = 1L,
    isNeedScroll: () -> Boolean = { true }
) {
    val scrollSpeedUnit = totalScrollPixel / totalDuration * roughness
    autoScrollPrimitive(lazyListState, scrollSpeedUnit, roughness, isNeedScroll)
}

tailrec suspend fun autoScrollPrimitive(
    lazyListState: LazyListState,
    scrollPixelUnit: Float,
    delayTimeUnit: Long,
    isNeedScroll: () -> Boolean = { true }
) {
    if (isNeedScroll()) lazyListState.scroll(MutatePriority.UserInput) {
        scrollBy(scrollPixelUnit)
    }
    delay(delayTimeUnit)
    autoScrollPrimitive(lazyListState, scrollPixelUnit, delayTimeUnit)
}