package com.housweet.presentation.viewmodel.roomposting

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
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

    private val _roomPosts = mutableStateListOf<RoomPost>()
    val roomPosts: List<RoomPost> get() = _roomPosts

    fun loadRoom(id: Int) {
        viewModelScope.launch {
            try {
                val roomPost = repository.getRoomPosting(id)
                Log.d("RoomViewModel", "불러온 room: $roomPost")
                Log.d("RoomViewModel", "roomPost.isHidden: ${roomPost.isHidden}")

//                // 동일한 id의 게시글이 이미 있다면 제거하고 추가
//                _roomPosts.removeAll { it.id == id }
//                _roomPosts.add(roomPost)

                // ✅ 이미 있는 게시글이면 교체, 없으면 추가
                val index = _roomPosts.indexOfFirst { it.id == roomPost.id }
                if (index != -1) {
                    _roomPosts[index] = roomPost
                } else {
                    _roomPosts.add(roomPost)
                }
                Log.d("RoomViewModel", "불러온 room: $roomPost")
            } catch (e: Exception) {
                Log.e("RoomLoading", "Error loading room: $e")
            }
        }
    }

    fun updatePostVisibility(postingId: Int, isVisible: Boolean) {
        viewModelScope.launch {
//            repository.updatePostVisibility(postingId, isVisible)
//
//            // ✅ 리스트에서 해당 게시글 찾아서 isHidden 변경
//            roomPosts.find { it.id == postingId }?.let {
//                it.isHidden = !isVisible
//            }

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

//    suspend fun updatePostVisibility(postingId: Int, isVisible: Boolean) {
//        repository.updatePostVisibility(postingId, isVisible)
//
//        roomPosts.indexOfFirst { it.id == postingId }.takeIf { it != -1 }?.let { index ->
//            val updated = roomPosts[index].copy(isHidden = !isVisible)
//            roomPosts[index] = updated
//        }
//    }
}