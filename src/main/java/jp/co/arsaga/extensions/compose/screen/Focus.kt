package jp.co.arsaga.extensions.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import com.google.accompanist.insets.LocalWindowInsets

//　TextFieldを触った際にフォーカスを当てて画面をスクロールする処理関連
@ExperimentalFoundationApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FocusTextField(
    textField: @Composable (relocationRequester: BringIntoViewRequester, interactionSource: MutableInteractionSource) -> Unit
) {
    val relocationRequester = remember { BringIntoViewRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val ime = LocalWindowInsets.current.ime
    if (ime.isVisible && !ime.animationInProgress && isFocused) {
        LaunchedEffect(Unit) {
            relocationRequester.bringIntoView()
        }
    }
    textField(relocationRequester, interactionSource)
}