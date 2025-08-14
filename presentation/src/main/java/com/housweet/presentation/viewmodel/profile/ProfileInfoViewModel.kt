package com.housweet.presentation.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.Report
import com.housweet.domain.usecase.ReportUseCase
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
    private val reportUseCase: ReportUseCase
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
                val isMyProfile = userId == "me"
                val profileInfo = profileModel.toProfileInfo(isMyProfile)
                _profileState.value = ProfileInfoState.Success(profileInfo)
            }.onFailure { error ->
                _profileState.value = ProfileInfoState.Error(
                    error.message ?: "프로필 조회에 실패했습니다"
                )
            }
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