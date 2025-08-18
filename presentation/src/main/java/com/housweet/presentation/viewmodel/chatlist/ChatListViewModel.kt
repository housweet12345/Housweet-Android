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
import kotlinx.coroutines.flow.update
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
            try {
                _chatUsers.value = chatRepository.getChatUsers(senderId)
            } catch (e: Exception) {
                Log.e("ChatListVM", "getChatUsers failed", e)
                _chatUsers.value = emptyList() // 안전하게 빈 목록
            }
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

    fun deleteChatRoom(receiverId: Int, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = chatRepository.deleteRoom(receiverId)
            onResult(res.isSuccess, res.exceptionOrNull()?.message)
        }
    }

    fun reportChatRoom(roomId: Int, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = chatRepository.reportRoom(roomId)
            onResult(res.isSuccess, res.exceptionOrNull()?.message)
        }
    }

    fun refreshChatUsers(senderId: Int) = viewModelScope.launch {
        val users = runCatching { chatRepository.getChatUsers(senderId) }
            .getOrElse { emptyList() }
        _chatUsers.value = users
    }

    fun createRoomAndShow(
        senderId: Int,
        receiverId: Int,
        counterpartNickname: String? = null
    ) = viewModelScope.launch {
        val r = chatRepository.createRoom(senderId, receiverId)
        r.onSuccess { roomId ->
            val displayName = counterpartNickname ?: "상대"
            val newItem = ChatUser(
                room_id = roomId,
                sender_id = senderId,
                receiver_id = receiverId,
                created_at = "",                // 아직 서버 시간 모름
                updated_at = "",                // 아직 서버 시간 모름
                is_blocked = false,
                counterpart_id = receiverId,
                sender_nickname = "나",         // 있으면 실제 닉네임으로
                receiver_nickname = displayName,
                counterpart_nickname = displayName,
                last_message_content = "",      // 메시지 없음
                last_message_created_at = ""    // 메시지 없음
            )

            // 1) 낙관적 반영(중복 제거 후 맨 위로)
            _chatUsers.update { list ->
                list.filterNot { it.room_id == roomId }
                    .toMutableList().apply { add(0, newItem) }
            }

            // 2) 서버 기준으로 동기화
            refreshChatUsers(senderId)
        }.onFailure {
            // 에러 처리 (스낵바/로그 등)
        }
    }
}