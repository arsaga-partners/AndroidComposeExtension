package jp.co.arsaga.extensions.compose.screen

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewWidget(
    url: String,
    onPageFinished: (String) -> Unit
) {
    AndroidView(
        factory = { cxt ->
            WebView(cxt)
        },
        update = { webView ->
            webView.apply {
                isVerticalScrollBarEnabled = false
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        url?.run { onPageFinished(this) }
                    }
                }
            }
            webView.loadUrl(url)
        }
    )
}

