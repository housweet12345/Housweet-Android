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
open class RoomPostingViewModel @Inject constructor(
    private val repository: RoomPostingRepository
) : ViewModel() {
    private val _roomPosts = mutableStateListOf<RoomPost>()
    val roomPosts: SnapshotStateList<RoomPost> get() = _roomPosts

    fun loadMyRooms() {
        viewModelScope.launch {
            val result = repository.getMyRoomPostings()
            _roomPosts.clear()
            _roomPosts.addAll(result)
        }
    }

    /** UI를 즉시 반영(낙관적 업데이트) */
    fun setHiddenLocally(id: Int, hidden: Boolean) {
        val index = _roomPosts.indexOfFirst { it.id == id }
        if (index != -1) {
            val curr = _roomPosts[index]
            _roomPosts[index] = curr.copy(
                isHidden = hidden,
                // 도메인 모델에 isVisible이 있으면 같이 맞춰줘도 OK
                // isVisible = !hidden
            )
        }
    }

    /**
     * 서버에 is_visible 패치 (호출부에서 await 가능하도록 suspend)
     * 성공 시 true, 실패 시 false
     */
    suspend fun updatePostVisibilityRemote(postingId: Int, isVisible: Boolean): Boolean {
        return try {
            repository.updatePostVisibility(postingId, isVisible)
            true
        } catch (e: Exception) {
            Log.e("RoomViewModel", "가시성 변경 실패: ${e.message}", e)
            false
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