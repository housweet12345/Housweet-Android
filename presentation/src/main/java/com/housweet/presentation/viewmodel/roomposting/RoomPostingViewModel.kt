package com.housweet.presentation.viewmodel.roomposting

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.RoomPost
import com.housweet.domain.repository.RoomPostingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomPostingViewModel @Inject constructor(
    private val repository: RoomPostingRepository
) : ViewModel() {

    val currentUserId: Int = 1 // TODO: 추후 로그인 유저의 ID로 교체
    private val _roomPosts = mutableStateListOf<RoomPost>()
    val roomPosts: SnapshotStateList<RoomPost> get() = _roomPosts

    fun loadMyRooms() {
        viewModelScope.launch {
            val result = repository.getMyRoomPostings()
            _roomPosts.clear()
            _roomPosts.addAll(result)
        }
    }

    fun updatePostVisibility(postingId: Int, isVisible: Boolean) {
        viewModelScope.launch {
            try {
                repository.updatePostVisibility(postingId, isVisible)

                val index = _roomPosts.indexOfFirst { it.id == postingId }
                if (index != -1) {
                    val updated = _roomPosts[index].copy(isHidden = !isVisible)
                    _roomPosts[index] = updated
                }
            } catch (e: Exception) {
                Log.e("RoomViewModel", "숨김 처리 실패", e)
            }
        }
    }

    suspend fun deletePost(id: Int): Boolean {
        return try {
            repository.deletePost(id)
            _roomPosts.removeAll { it.id == id } // ✅ 리스트에서 직접 제거
            true
        } catch (e: Exception) {
            Log.e("RoomViewModel", "삭제 실패: ${e.message}")
            false
        }
    }
}