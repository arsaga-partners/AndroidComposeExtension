package jp.co.arsaga.extensions.compose.screen

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
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

tailrec suspend fun autoScroll(
    lazyListState: LazyListState,
    scrollPixelUnit: Float,
    delayTimeUnit: Long,
    isNeedScroll: State<Boolean> = mutableStateOf(true)
) {
    if (isNeedScroll.value) lazyListState.scroll(MutatePriority.UserInput) {
        scrollBy(scrollPixelUnit)
    }
    delay(delayTimeUnit)
    autoScroll(lazyListState, scrollPixelUnit, delayTimeUnit)
}