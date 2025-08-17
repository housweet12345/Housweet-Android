package com.housweet.presentation.viewmodel.roomposting

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.RoomPost
import com.housweet.domain.repository.RoomPostingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
open class RoomPostingViewModel @Inject constructor(
    private val repository: RoomPostingRepository
) : ViewModel() {
    private val _roomPosts = mutableStateListOf<RoomPost>()
    val roomPosts: SnapshotStateList<RoomPost> get() = _roomPosts

    // 로딩 상태 (Compose 상태)
    var isLoading by mutableStateOf(false)
        private set

    // 단발성 이벤트 (스낵바 등)
    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()


//    fun loadMyRooms() {
//        viewModelScope.launch {
//            val result = repository.getMyRoomPostings()
//            _roomPosts.clear()
//            _roomPosts.addAll(result)
//        }
//    }

    fun loadMyRooms() {
        viewModelScope.launch {
            isLoading = true
            runCatching { repository.getMyRoomPostings() }
                .onSuccess { posts ->
                    _roomPosts.clear()
                    _roomPosts.addAll(posts)
                }
                .onFailure { t ->
                    Log.e("RoomPostingVM", "loadMyRooms failed", t)
                    _events.tryEmit(UiEvent.Error(t.toReadableMessage()))
                }
            isLoading = false
        }
    }

    /** UI를 즉시 반영(낙관적 업데이트) */
//    fun setHiddenLocally(id: Int, hidden: Boolean) {
//        val index = _roomPosts.indexOfFirst { it.id == id }
//        if (index != -1) {
//            val curr = _roomPosts[index]
//            _roomPosts[index] = curr.copy(
//                isHidden = hidden,
//                // 도메인 모델에 isVisible이 있으면 같이 맞춰줘도 OK
//                // isVisible = !hidden
//            )
//        }
//    }

    fun setHiddenLocally(id: Int, hidden: Boolean) {
        val index = _roomPosts.indexOfFirst { it.id == id }
        if (index != -1) {
            val curr = _roomPosts[index]
            _roomPosts[index] = curr.copy(isHidden = hidden)
        }
    }

    /**
     * 서버에 is_visible 패치 (호출부에서 await 가능하도록 suspend)
     * 성공 시 true, 실패 시 false
     */
//    suspend fun updatePostVisibilityRemote(postingId: Int, isVisible: Boolean): Boolean {
//        return try {
//            repository.updatePostVisibility(postingId, isVisible)
//            true
//        } catch (e: Exception) {
//            Log.e("RoomViewModel", "가시성 변경 실패: ${e.message}", e)
//            false
//        }
//    }
    suspend fun updatePostVisibilityRemote(postingId: Int, isVisible: Boolean): Boolean {
        return try {
            repository.updatePostVisibility(postingId, isVisible).also { ok ->
                if (!ok) _events.emit(UiEvent.Error("상태 변경에 실패했습니다."))
            }
        } catch (e: Exception) {
            Log.e("RoomPostingVM", "updatePostVisibility failed", e)
            _events.emit(UiEvent.Error(e.toReadableMessage("가시성 변경 실패")))
            false
        }
    }


//    suspend fun deletePost(id: Int): Boolean {
//        return try {
//            repository.deletePost(id)
//            _roomPosts.removeAll { it.id == id } // ✅ 리스트에서 직접 제거
//            true
//        } catch (e: Exception) {
//            Log.e("RoomViewModel", "삭제 실패: ${e.message}")
//            false
//        }
//    }

    suspend fun deletePost(id: Int): Boolean {
        return try {
            val ok = repository.deletePost(id)
            if (ok) {
                _roomPosts.removeAll { it.id == id }
            } else {
                _events.emit(UiEvent.Error("삭제에 실패했습니다."))
            }
            ok
        } catch (e: Exception) {
            Log.e("RoomPostingVM", "deletePost failed", e)
            _events.emit(UiEvent.Error(e.toReadableMessage("삭제 실패")))
            false
        }
    }

    sealed interface UiEvent {
        data class Error(val message: String) : UiEvent
    }
}

private fun Throwable.toReadableMessage(prefix: String? = null): String {
    val base = when (this) {
        is ConnectException -> "서버에 연결할 수 없습니다. (ECONNREFUSED)"
        is SocketTimeoutException -> "서버 응답이 지연되고 있어요."
        is UnknownHostException -> "서버 주소를 확인할 수 없습니다."
        else -> (message ?: "알 수 없는 오류가 발생했습니다.")
    }
    return if (prefix.isNullOrBlank()) base else "$prefix: $base"
}