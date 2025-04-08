package com.housweet.data.repository

import com.housweet.domain.repository.ChatRepository
import io.socket.client.Socket

class ChatRepositoryImpl(
    private val socket: Socket
) : ChatRepository {

}
