package com.housweet.presentation.ui.chat

import android.graphics.Bitmap

sealed class ChatItem {
    data class TextMessage(val message: String, val isMine: Boolean) : ChatItem()
    data class ImageMessage(val bitmap: Bitmap, val isMine: Boolean) : ChatItem()
}