package jp.co.arsaga.extensions.compose.screen

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalPagerApi::class,
)
data class TabPagerState<T> constructor(
    val tabItemList: List<T>,
    val coroutineScope: CoroutineScope,
    val pagerState: PagerState
) {
    companion object {
        @OptIn(ExperimentalPagerApi::class)
        @Composable
        fun <T> factory(tabItemList: List<T>) = TabPagerState(
            tabItemList,
            rememberCoroutineScope(),
            rememberPagerState()
        )
    }
}

@OptIn(
    ExperimentalPagerApi::class
)
data class TabConfig<T> internal constructor(
    val state: TabPagerState<T>,
    val isTabPositionBottom: Boolean = true,
    val onClick: (Int) -> Unit = {
        state.coroutineScope.launch {
            state.pagerState.scrollToPage(it)
        }
    },
    val tabFactory: @Composable () -> Unit = {},
    val tabRowLayout: @Composable ColumnScope.(currentPage: Int, tabFactory: @Composable () -> Unit) -> Unit,
    val tabItemLayout: @Composable ColumnScope.(isSelected: Boolean, tabEntity: T) -> Unit
) {
    companion object {
        @OptIn(ExperimentalPagerApi::class)
        fun <T> factory(
            tabPagerState: TabPagerState<T>,
            tabFactory: ((TabConfig<T>) -> @Composable () -> Unit)? = null,
            tabRowLayout: @Composable ColumnScope.(currentPage: Int, tabFactory: @Composable () -> Unit) -> Unit,
            tabItemLayout: @Composable ColumnScope.(isSelected: Boolean, tabEntity: T) -> Unit,
            process: (TabConfig<T>) -> TabConfig<T> = { it }
        ) = process(
            TabConfig(
                tabPagerState,
                tabRowLayout = tabRowLayout,
                tabItemLayout = tabItemLayout
            )
        ).run { copy(tabFactory = tabFactory?.invoke(this) ?: defaultTabFactory(this)) }

        @OptIn(ExperimentalPagerApi::class)
        private fun <T> defaultTabFactory(tabConfig: TabConfig<T>): @Composable () -> Unit = {
            tabConfig.state.tabItemList.forEachIndexed { index, tabPagerType ->
                val isSelected = tabConfig.state.pagerState.currentPage == index
                Tab(
                    selected = isSelected,
                    onClick = { tabConfig.onClick(index) },
                ) {
                    tabConfig.tabItemLayout(this, isSelected, tabPagerType)
                }
            }
        }
    }
}

data class PagerConfig<T> @OptIn(ExperimentalPagerApi::class) internal constructor(
    val tabConfig: TabConfig<T>,
    val verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    val dragEnabled: Boolean,
    val pager: @Composable ColumnScope.() -> Unit = {},
    val contents: @Composable (Int) -> @Composable () -> Unit
) {
    companion object {
        @OptIn(ExperimentalPagerApi::class)
        fun <T> factory(
            tabConfig: TabConfig<T>,
            pager: ((PagerConfig<T>) -> @Composable (ColumnScope.() -> Unit))? = null,
            dragEnabled: Boolean = true,
            contents: @Composable (Int) -> @Composable () -> Unit,
            process: (PagerConfig<T>) -> PagerConfig<T> = { it }
        ) = process(PagerConfig(tabConfig, dragEnabled = dragEnabled, contents = contents))
            .run { copy(pager = pager?.invoke(this) ?: defaultPager(this)) }

        @OptIn(ExperimentalPagerApi::class)
        private fun <T> defaultPager(pagerConfig: PagerConfig<T>): @Composable ColumnScope.() -> Unit =
            {
                HorizontalPager(
                    count = pagerConfig.tabConfig.state.tabItemList.count(),
                    state = pagerConfig.tabConfig.state.pagerState,
                    verticalAlignment = pagerConfig.verticalAlignment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)
                ) { page ->
                    if (pagerConfig.dragEnabled)
                        pagerConfig.contents(page).invoke()
                    else
                        Box(modifier = Modifier.pointerInput(Unit) {
                            detectDragGestures { _, _ -> }
                        }) {
                            pagerConfig.contents(page).invoke()
                        }
                }
            }
    }
}

@OptIn(
    ExperimentalPagerApi::class,
    ExperimentalMaterialApi::class,
)
@Composable
fun <T> TabPager(
    argument: Triple<TabPagerState<T>, TabConfig<T>, PagerConfig<T>>
) {
    Column {
        if (argument.second.isTabPositionBottom) {
            argument.third.pager(this)
            argument.second.tabRowLayout(
                this,
                argument.first.pagerState.currentPage,
                argument.second.tabFactory
            )
        } else {
            argument.second.tabRowLayout(
                this,
                argument.first.pagerState.currentPage,
                argument.second.tabFactory
            )
            argument.third.pager(this)
        }
    }
}