package jp.co.arsaga.extensions.compose.screen

import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding

fun Fragment.composable(
    fragmentId: Int,
    content: @Composable () -> Unit
): View = ComposeView(requireContext()).apply {
    id = fragmentId
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    setContent(content)
}

@Composable
fun SystemPaddingLayout(
    content: @Composable () -> Unit
) {
    ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsWithImePadding()
        ) {
            content()
        }
    }
}