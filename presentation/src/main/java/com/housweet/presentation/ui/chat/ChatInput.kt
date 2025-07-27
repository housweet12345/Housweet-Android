package com.housweet.presentation.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatInput(
    inputText: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onAddImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "추가",
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
                .clickable { onAddImageClick() },
            tint = Color.Gray
        )

        TextField(
            value = inputText,
            onValueChange = onTextChange,
            placeholder = { Text("메세지 입력", fontSize = 12.sp) },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        keyboardController?.show()
                    }
                },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(20.dp),
            singleLine = true
        )

        Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "보내기",
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
                .clickable { onSend() },
            tint = Color.Gray
        )
    }
}