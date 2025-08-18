package com.housweet.presentation.ui.communityPage.postScreen.postsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.RoomPostsByLocationDataModel
import com.housweet.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow<PostsState>(PostsState.Idle)
    val uiState: StateFlow<PostsState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<PostsEvent>()
    val event: SharedFlow<PostsEvent> = _event.asSharedFlow()

    private val _posts = MutableStateFlow<Map<String, List<RoomPostsByLocationDataModel>>>(mapOf())
    val posts: StateFlow<Map<String, List<RoomPostsByLocationDataModel>>> = _posts.asStateFlow()

    val postRegions = savedStateHandle.get<String>("postRegions")?.split(",") ?: emptyList()

    init {
        getPostsByLocation(postRegions)
    }

    private fun getPostsByLocation(postRegions: List<String>) {
        viewModelScope.launch {
            _uiState.value = PostsState.IsLoading
            val posts = mutableMapOf<String, List<RoomPostsByLocationDataModel>>()

            postRegions.forEach { postRegion ->
                useCases.getRoomPostsByLocationUsaCase(postRegion).collect { result ->
                    result.onSuccess {
                        posts[postRegion] = it
                    }

                    result.onFailure {
                        _event.emit(PostsEvent.Error)
                    }
                }
            }

            _posts.value = posts
            _uiState.value = PostsState.Idle
        }
    }

    fun updatePostBookmark(updatedPostId: Int) {
        val updatedPosts = _posts.value.mapValues { (_, postList) ->
            postList.map { post ->
                if (post.id == updatedPostId) {
                    post.copy(isBookmarked = !post.isBookmarked)
                } else {
                    post
                }
            }
        }
        _posts.value = updatedPosts
    }

    fun toggleLike(postRegion: String, postIndex: Int) {
        val originalPost = _posts.value[postRegion]?.get(postIndex) ?: return
        val isBookmarked = !originalPost.isBookmarked

        val currentPosts = _posts.value.toMutableMap()
        val updatedPostList = currentPosts[postRegion]?.toMutableList() ?: return
        updatedPostList[postIndex] = originalPost.copy(
            isBookmarked = isBookmarked
        )

        currentPosts[postRegion] = updatedPostList
        _posts.value = currentPosts

        viewModelScope.launch {
            if (isBookmarked) {
                useCases.clickBookMarkUseCase(originalPost.id).collect { result ->
                    result.onFailure {
                        rollbackBookMark(postRegion, postIndex, originalPost)
                        _event.emit(PostsEvent.Error)
                    }
                }
            } else {
                useCases.unClickBookMarkUseCase(originalPost.id).collect { result ->
                    result.onFailure {
                        rollbackBookMark(postRegion, postIndex, originalPost)
                        _event.emit(PostsEvent.Error)
                    }
                }
            }
        }
    }

    private fun rollbackBookMark(postRegion: String, postIndex: Int, originalPost: RoomPostsByLocationDataModel) {
        val rollbackPosts = _posts.value.toMutableMap()
        val rollbackPostList = rollbackPosts[postRegion]?.toMutableList() ?: return
        rollbackPostList[postIndex] = originalPost
        rollbackPosts[postRegion] = rollbackPostList
        _posts.value = rollbackPosts
    }
}