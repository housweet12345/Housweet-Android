package com.housweet.presentation.ui.communityPage.postScreen.postsScreen

sealed interface PostsState {
    data object Idle : PostsState
    data object IsLoading : PostsState
}

sealed interface PostsEvent {
    data object Error: PostsEvent
    data object BookMarkError: PostsEvent
}