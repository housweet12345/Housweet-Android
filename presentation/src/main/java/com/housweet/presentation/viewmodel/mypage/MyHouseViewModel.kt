package com.housweet.presentation.viewmodel.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.MyHouse
import com.housweet.domain.repository.MyHouseRepository
import com.housweet.presentation.ui.mypage.state.MyHouseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyHouseViewModel @Inject constructor(
    private val repo: MyHouseRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MyHouseUiState>(MyHouseUiState.Loading)
    val state: StateFlow<MyHouseUiState> = _state

    fun load() {
        viewModelScope.launch {
            _state.value = MyHouseUiState.Loading
            runCatching { repo.getMyHouse() }
                .onSuccess { _state.value = MyHouseUiState.Success(it) }
                .onFailure { _state.value = MyHouseUiState.Error(it.message ?: "불러오기 실패") }
        }
    }

    fun refresh() = load()

    // 수정 화면에서 쓰라고 제공: 최신 데이터 반환
    fun latestOrNull(): MyHouse? = (state.value as? MyHouseUiState.Success)?.data
}