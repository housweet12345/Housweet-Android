package com.housweet.presentation.ui.chat

import android.graphics.Bitmap

sealed class ChatItem {
    data class TextMessage(val message: String, val isMine: Boolean, val profileImageRes: Int?=null) : ChatItem()
    data class ImageMessage(val bitmap: Bitmap, val isMine: Boolean, val profileImageRes: Int?=null) : ChatItem()
}