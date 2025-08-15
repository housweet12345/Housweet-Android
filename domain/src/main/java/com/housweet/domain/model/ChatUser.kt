package com.housweet.domain.model

data class ChatUser(
    val room_id : Int,
    val sender_id : Int,
    val receiver_id : Int,
    val created_at : String,
    val updated_at : String,
    val is_blocked : Boolean,
    val counterpart_id : Int
)
