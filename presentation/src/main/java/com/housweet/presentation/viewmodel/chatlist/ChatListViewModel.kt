package com.housweet.presentation.viewmodel.chatlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.ChatMessage
import com.housweet.domain.model.ChatUser
import com.housweet.domain.repository.ChatRepository
import com.housweet.domain.usecase.auth.GetCurrentUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val getCurrentUserId: GetCurrentUserIdUseCase
): ViewModel() {

    private val _chatUsers = MutableStateFlow<List<ChatUser>>(emptyList())
    val chatUsers: StateFlow<List<ChatUser>> = _chatUsers

    private val _myUserId = MutableStateFlow<Int?>(null)
    val myUserId: StateFlow<Int?> = _myUserId.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        // 앱 시작 시 내 ID를 불러오고, 성공하면 채팅유저 리스트를 내 ID로 로드
        refreshMyUserIdAndUsers()
    }

    /** 내 userId를 불러오고 곧바로 채팅방 목록까지 로드 */
    fun refreshMyUserIdAndUsers() = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        try {
            val id = getCurrentUserId()                 // ← ★ operator invoke
            _myUserId.value = id
            if (id != null) {
                fetchChatUsers(id)                      // ← ★ 내 ID로 호출
            } else {
                _chatUsers.value = emptyList()
                _error.value = "로그인이 필요합니다"
            }
        } catch (e: Exception) {
            _error.value = e.message ?: "내 사용자 ID 조회 실패"
        } finally {
            _loading.value = false
        }
    }

    private fun fetchChatUsers(senderId: Int) = viewModelScope.launch {
        val users = runCatching { chatRepository.getChatUsers(senderId) }
            .onFailure { Log.e("ChatListVM", "getChatUsers failed", it) }
            .getOrElse { emptyList() }
        _chatUsers.value = users
    }

    fun sendChatMessage(senderId: Int, receiverId: Int, message: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val ok = runCatching { chatRepository.sendMessage(senderId, receiverId, message) }
                .getOrElse { false }
            onResult(ok)
        }
    }

    fun fetchChatMessages(senderId: Int, receiverId: Int, onResult: (List<ChatMessage>) -> Unit) {
        viewModelScope.launch {
            val messages = runCatching { chatRepository.getChatMessages(senderId, receiverId) }
                .onFailure { Log.e("ChatViewModel", "메시지 불러오기 실패", it) }
                .getOrElse { emptyList() }
            onResult(messages)
        }
    }

    fun deleteChatRoom(receiverId: Int, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = runCatching { chatRepository.deleteRoom(receiverId) }.getOrElse { Result.failure(it) }
            onResult(res.isSuccess, res.exceptionOrNull()?.message)
        }
    }

    fun reportChatRoom(roomId: Int, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = runCatching { chatRepository.reportRoom(roomId) }.getOrElse { Result.failure(it) }
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
        counterpartNickname: String? = null,
        onReady: ((Int) -> Unit)? = null,
    ) = viewModelScope.launch {
        //이미 방이 있을때 재사용
        chatUsers.value.firstOrNull { it.counterpart_id == receiverId }?.let { existing ->
            onReady?.invoke(existing.room_id)       // ✅ 즉시 콜백
            return@launch
        }

        //없으면 생성
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

            // 낙관적 반영(중복 제거 후 맨 위로)
            _chatUsers.update { list ->
                list.filterNot { it.room_id == roomId }
                    .toMutableList().apply { add(0, newItem) }
            }

            // 서버 기준으로 동기화
            refreshChatUsers(senderId)
            onReady?.invoke(roomId)
        }.onFailure {
            // 에러 처리 (스낵바/로그 등)
        }
    }
}