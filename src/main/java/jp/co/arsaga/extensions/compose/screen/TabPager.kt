package jp.co.arsaga.extensions.compose.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class TabPagerState @ExperimentalPagerApi constructor(
    val coroutineScope: CoroutineScope,
    val pagerState: PagerState
) {
    companion object {
        @OptIn(ExperimentalPagerApi::class)
        @Composable
        fun create(tabItemList: Array<*>) = TabPagerState(
            rememberCoroutineScope(),
            rememberPagerState(pageCount = tabItemList.size)
        )
    }
}

@ExperimentalMaterialApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun <T : Enum<*>> TabPager(
    tabItemList: Array<T>,
    isTabPositionBottom: Boolean = true,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    state: TabPagerState = TabPagerState.create(tabItemList),
    onClick: (Int) -> Unit = {
        state.coroutineScope.launch {
            state.pagerState.scrollToPage(it)
        }
    },
    contents: (Int) -> @Composable () -> Unit,
    tabRowLayout: @Composable (currentPage: Int, tabItemLayout: @Composable () -> Unit) -> Unit,
    tabItemLayout: @Composable ColumnScope.(isSelected: Boolean, tabEntity: T) -> Unit
) {
    val layoutPart = listOf<@Composable ColumnScope.() -> Unit>(
        {
            HorizontalPager(
                state = state.pagerState,
                verticalAlignment = verticalAlignment,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) { page ->
                contents(page).invoke()
            }
        }, {
            tabRowLayout(state.pagerState.currentPage) {
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    tabItemList.forEachIndexed { index, tabPagerType ->
                        val isSelected = state.pagerState.currentPage == index
                        Tab(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1F),
                            selected = isSelected,
                            onClick = { onClick(index) },
                        ) {
                            tabItemLayout(this, isSelected, tabPagerType)
                        }
                    }
                }
            }
        }
    )
    Column {
        if (isTabPositionBottom) {
            layoutPart[0]()
            layoutPart[1]()
        } else {
            layoutPart[1]()
            layoutPart[0]()
        }
    }
}