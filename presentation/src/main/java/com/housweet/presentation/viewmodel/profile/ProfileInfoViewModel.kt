package com.housweet.presentation.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.Report
import com.housweet.domain.usecase.ReportUseCase
import com.housweet.domain.usecase.auth.GetCurrentUserIdUseCase
import com.housweet.domain.usecase.profile.GetMyProfileUseCase
import com.housweet.domain.usecase.profile.GetOtherUserProfileUseCase
import com.housweet.presentation.ui.profile.state.ProfileInfoState
import com.housweet.presentation.ui.profile.state.toProfileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileInfoViewModel @Inject constructor(
    private val myProfileUseCase: GetMyProfileUseCase,
    private val otherProfileUseCase: GetOtherUserProfileUseCase,
    private val reportUseCase: ReportUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
): ViewModel() {
    private val _profileState: MutableStateFlow<ProfileInfoState> = MutableStateFlow(ProfileInfoState.Loading)
    val profileState: StateFlow<ProfileInfoState> = _profileState.asStateFlow()

    private val _reportResult = MutableStateFlow<ReportResult?>(null)
    val reportResult: StateFlow<ReportResult?> = _reportResult.asStateFlow()

    fun loadProfile(userId: String? = null) {
        viewModelScope.launch {
            _profileState.value = ProfileInfoState.Loading

            val result = if (userId == "me") {
                myProfileUseCase()
            } else {
                if (userId != null) {
                    otherProfileUseCase(userId)
                } else {
                    throw Exception("프로필 조회에 실패했습니다")
                }
            }

            result.onSuccess { profileModel ->
                // 내 프로필인지 확인 (userId == "me" 또는 현재 userId와 같은 경우)
                val currentUserId = getCurrentUserIdUseCase()
                val isMyProfile = userId == "me" || (currentUserId != null && userId == currentUserId.toString())
                val profileInfo = profileModel.toProfileInfo(isMyProfile)
                _profileState.value = ProfileInfoState.Success(profileInfo)
            }.onFailure { error ->
                // 내 프로필 조회("me") 실패시 fallback 시도
                if (userId == "me") {
                    tryLoadMyProfileWithCurrentUserId()
                } else {
                    _profileState.value = ProfileInfoState.Error(
                        error.message ?: "프로필 조회에 실패했습니다"
                    )
                }
            }
        }
    }

    private suspend fun tryLoadMyProfileWithCurrentUserId() {
        try {
            val currentUserId = getCurrentUserIdUseCase()

            if (currentUserId != null) {
                val fallbackResult = otherProfileUseCase(currentUserId.toString())
                
                fallbackResult.onSuccess { profileModel ->
                    val profileInfo = profileModel.toProfileInfo(isMyProfile = true)
                    _profileState.value = ProfileInfoState.Success(profileInfo)
                }.onFailure { error ->
                    _profileState.value = ProfileInfoState.Error(
                        error.message ?: "프로필 조회에 실패했습니다"
                    )
                }
            } else {
                _profileState.value = ProfileInfoState.Error("사용자 정보를 찾을 수 없습니다")
            }
        } catch (e: Exception) {
            _profileState.value = ProfileInfoState.Error(
                e.message ?: "프로필 조회에 실패했습니다"
            )
        }
    }

    fun report(type: String, id: Int) {
        viewModelScope.launch {
            _reportResult.value = ReportResult.Loading
            try {
                val response = reportUseCase(type, id)
                _reportResult.value = ReportResult.Success(response)
            } catch (e: Exception) {
                _reportResult.value = ReportResult.Error(e)
            }
        }
    }

    sealed class ReportResult {
        data object Loading : ReportResult()
        data class Success(val data: Report) : ReportResult()
        data class Error(val exception: Throwable) : ReportResult()
    }
}