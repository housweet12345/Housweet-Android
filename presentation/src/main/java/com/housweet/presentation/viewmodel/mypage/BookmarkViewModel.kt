package com.housweet.presentation.viewmodel.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.UseCases
import com.housweet.presentation.ui.mypage.BookmarkUiItem
import com.housweet.presentation.ui.mypage.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _bookmarks = MutableStateFlow<List<BookmarkUiItem>>(emptyList())
    val bookmarks: StateFlow<List<BookmarkUiItem>> = _bookmarks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _isLoading.value = true
            useCases.getBookmarkedPostingsUseCase().collect { result ->
                result.onSuccess { domainList ->
                    _bookmarks.value = domainList.map { it.toUi() }
                }.onFailure {
                    // TODO: 에러 처리
                }
                _isLoading.value = false
            }
        }
    }

    fun toggleBookmark(item: BookmarkUiItem) {
        val current = _bookmarks.value.toMutableList()
        val idx = current.indexOfFirst { it.id == item.id }
        if (idx == -1) return

        // 낙관적 제거
        val removed = current.removeAt(idx)
        _bookmarks.value = current

        viewModelScope.launch {
            useCases.unClickBookMarkUseCase(item.id).collect { r ->
                r.onFailure {
                    // 롤백
                    val rollback = _bookmarks.value.toMutableList()
                    rollback.add(idx, removed)
                    _bookmarks.value = rollback
                }
            }
        }
    }
}