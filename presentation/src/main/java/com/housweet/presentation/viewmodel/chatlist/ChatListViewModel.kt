package com.housweet.presentation.viewmodel.chatlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.ChatMessage
import com.housweet.domain.model.ChatUser
import com.housweet.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatRepository: ChatRepository
): ViewModel() {

    private val _chatUsers = MutableStateFlow<List<ChatUser>>(emptyList())
    val chatUsers: StateFlow<List<ChatUser>> = _chatUsers

    init {
        fetchChatUsers(3) //임시 값. 내 아이디가 지금 3임.
    }

    private fun fetchChatUsers(senderId: Int) {
        viewModelScope.launch {
            _chatUsers.value = chatRepository.getChatUsers(senderId)
        }
    }

    fun sendChatMessage(senderId: Int, receiverId: Int, message: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = chatRepository.sendMessage(senderId, receiverId, message)
            onResult(result)
        }
    }

    fun fetchChatMessages(
        senderId: Int,
        receiverId: Int,
        onResult: (List<ChatMessage>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = chatRepository.getChatMessages(senderId, receiverId)
                onResult(response)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "메시지 불러오기 실패: ${e.message}")
                onResult(emptyList())
            }
        }
    }

}