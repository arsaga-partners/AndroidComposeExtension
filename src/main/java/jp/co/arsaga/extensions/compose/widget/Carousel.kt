package jp.co.arsaga.extensions.compose.widget

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun <T> Carousel(
    entityList: List<T>,
    modifier: Modifier = Modifier,
    indicatorSpace: Dp = 0.dp,
    indicatorWidget: @Composable RowScope.(currentPage: Int) -> Unit,
    pagerState: PagerState = rememberPagerState(),
    imageWidget: @Composable (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            count = entityList.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
        ) { page ->
            imageWidget(page)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(indicatorSpace)
        ) {
            indicatorWidget(this, pagerState.currentPage)
        }
    }
}