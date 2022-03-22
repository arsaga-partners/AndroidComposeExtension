package jp.co.arsaga.extensions.compose.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import timber.log.Timber

@Composable
fun WebImage(
    imageUrl: String,
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    indicatorWidget: @Composable BoxScope.() -> Unit = {},
    contentScale: ContentScale = ContentScale.Fit,
    errorWidget: @Composable () -> Unit = {},
    requestBuilder: ImageRequest.Builder.() -> Unit = {},
) {
    WebImage(
        imageUrl = imageUrl,
        painter = painter,
        modifier = modifier,
        indicatorWidget = indicatorWidget,
        errorWidget = errorWidget,
        requestBuilder = requestBuilder
    ) {
        Image(
            painter = it,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.matchParentSize(),
        )
    }
}

@Composable
fun WebImage(
    imageUrl: String?,
    painter: Painter,
    modifier: Modifier = Modifier,
    indicatorWidget: @Composable BoxScope.() -> Unit = {},
    errorWidget: @Composable () -> Unit = {},
    requestBuilder: ImageRequest.Builder.() -> Unit = {},
    imageWidget: @Composable BoxScope.(Painter) -> Unit
) {
    WebImageImpl(
        imageUrl,
        painter,
        modifier,
        indicatorWidget,
        errorWidget,
        requestBuilder,
        imageWidget
    )
}

@Composable
private fun WebImageImpl(
    imageUrl: String?,
    dummyPainter: Painter?,
    modifier: Modifier = Modifier,
    indicatorWidget: @Composable BoxScope.() -> Unit,
    errorWidget: @Composable () -> Unit = {},
    requestBuilder: ImageRequest.Builder.() -> Unit,
    imageWidget: @Composable BoxScope.(Painter) -> Unit
) {
    val painter = imageUrl?.let {
        rememberImagePainter(
            data = imageUrl,
            builder = requestBuilder
        )
    }
    Box(
        modifier = modifier
    ) {
        when (val state = painter?.state) {
            null -> dummyPainter
            is ImagePainter.State.Loading -> {
                indicatorWidget(this)
                null
            }
            is ImagePainter.State.Error -> {
                Timber.e("${state.result.throwable}::$imageUrl")
                errorWidget()
                dummyPainter
            }
            is ImagePainter.State.Success,
            is ImagePainter.State.Empty -> painter
        }?.let {
            imageWidget(this, it)
        }
    }
}