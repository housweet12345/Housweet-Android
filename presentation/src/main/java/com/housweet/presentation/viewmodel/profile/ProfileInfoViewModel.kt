package com.housweet.presentation.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val otherProfileUseCase: GetOtherUserProfileUseCase
): ViewModel() {
    private val _profileState: MutableStateFlow<ProfileInfoState> = MutableStateFlow(ProfileInfoState.Loading)
    val profileState: StateFlow<ProfileInfoState> = _profileState.asStateFlow()

    fun loadProfile(userId: String? = null) {
        viewModelScope.launch {
            _profileState.value = ProfileInfoState.Loading

            val result = if (userId == null) {
                myProfileUseCase()
            } else {
                otherProfileUseCase(userId)
            }

            result.onSuccess { profileModel ->
                val isMyProfile = userId == null
                val profileInfo = profileModel.toProfileInfo(isMyProfile)
                _profileState.value = ProfileInfoState.Success(profileInfo)
            }.onFailure { error ->
                _profileState.value = ProfileInfoState.Error(
                    error.message ?: "프로필 조회에 실패했습니다"
                )
            }
        }
    }
}