package jp.co.arsaga.extensions.compose.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun <T> SimpleAlertDialogBuilder(
    title: @Composable (T) -> String,
    text: @Composable (T) -> String? = { null },
    confirmButtonText: @Composable (T) -> String,
    cancelButtonText: @Composable (T) -> String? = { null },
    onShow: (Pair<Any, suspend () -> Unit>)? = null,
    onNegative: (MutableState<T?>) -> Unit = { it.value = null },
    onDismiss: (MutableState<T?>) -> Unit = { it.value = null },
    onPositive: (MutableState<T?>) -> Unit
): MutableState<T?> = remember { mutableStateOf<T?>(null) }.also { dialogState ->
    dialogState.value?.let {
        SimpleAlertDialog(
            value = it,
            title = title,
            text = text,
            confirmButtonText = confirmButtonText,
            cancelButtonText = cancelButtonText,
            onShow = onShow,
            onDismiss = { onDismiss(dialogState) },
            onNegative = { onNegative(dialogState) },
            onPositive = { onPositive(dialogState) }
        )
    }
}

@Composable
fun SimpleAlertDialogBuilder(
    title: String,
    text: String? = null,
    confirmButtonText: String,
    cancelButtonText: String? = null,
    onShow: (Pair<Any, suspend () -> Unit>)? = null,
    onNegative: (MutableState<Boolean>) -> Unit = { it.value = false },
    onDismiss: (MutableState<Boolean>) -> Unit = { it.value = false },
    onPositive: (MutableState<Boolean>) -> Unit
): MutableState<Boolean> = remember { mutableStateOf(false) }.also { dialogState ->
    if (dialogState.value) SimpleAlertDialog(
        value = dialogState.value,
        title = { title },
        text = { text },
        confirmButtonText = { confirmButtonText },
        cancelButtonText = { cancelButtonText },
        onShow = onShow,
        onDismiss = { onDismiss(dialogState) },
        onNegative = { onNegative(dialogState) },
        onPositive = { onPositive(dialogState) }
    )
}

@Composable
fun <T> SimpleAlertDialog(
    value: T,
    title: @Composable (T) -> String,
    text: @Composable (T) -> String? = { null },
    confirmButtonText: @Composable (T) -> String,
    cancelButtonText: @Composable (T) -> String? = { null },
    onShow: (Pair<Any, suspend () -> Unit>)? = null,
    onDismiss: () -> Unit,
    onNegative: () -> Unit,
    titleWidget: @Composable (T) -> Unit = { Text(text = title(value), fontSize = 16.sp) },
    textWidget: (@Composable (T) -> (@Composable () -> Unit)?)? = {
        text(value)?.let {
            {
                Text(
                    text = it,
                    fontSize = 14.sp
                )
            }
        }
    },
    onPositive: () -> Unit
) {
    onShow?.let {
        LaunchedEffect(it.first) {
            it.second()
        }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { titleWidget(value) },
        text = textWidget?.invoke(value),

        confirmButton = {
            Text(
                text = confirmButtonText(value),
                color = Color(0xFF35B8AA),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(end = 12.dp, bottom = 12.dp)
                    .clickable(onClick = onPositive)
                    .padding(12.dp)
            )
        },

        dismissButton = cancelButtonText(value)?.let {
            {
                Text(
                    text = it,
                    color = Color(0xFF35B8AA),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .clickable(onClick = onNegative)
                        .padding(12.dp)
                )
            }
        }
    )
}
