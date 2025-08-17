package com.housweet.presentation.viewmodel.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.profile.ProfileUpdateModel
import com.housweet.domain.usecase.auth.GetCurrentUserIdUseCase
import com.housweet.domain.usecase.profile.GetMyProfileUseCase
import com.housweet.domain.usecase.profile.GetOtherUserProfileUseCase
import com.housweet.domain.usecase.profile.UpdateProfileUseCase
import com.housweet.presentation.ui.profile.state.ProfileInfoState
import com.housweet.presentation.ui.profile.state.toProfileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val myProfileUseCase: GetMyProfileUseCase,
    private val otherUserProfileUseCase: GetOtherUserProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _profileState: MutableStateFlow<ProfileInfoState> = MutableStateFlow(ProfileInfoState.Loading)
    val profileState: StateFlow<ProfileInfoState> = _profileState.asStateFlow()

    // 임시 프로필 수정 데이터 저장
    private val _tempProfileData = MutableStateFlow<TempProfileData?>(null)
    val tempProfileData: StateFlow<TempProfileData?> = _tempProfileData.asStateFlow()

    init {
        loadProfile()
    }

    // 첫 번째 화면에서 입력된 데이터 저장 (이미지 정보 보존)
    fun saveBasicProfileData(
        nickname: String,
        yearOfBirth: String,
        gender: String,
        introduce: String
    ) {
        val currentTemp = _tempProfileData.value
        _tempProfileData.value = currentTemp?.copy(
            nickname = nickname,
            yearOfBirth = yearOfBirth,
            gender = gender,
            introduce = introduce
        ) ?: TempProfileData(
            nickname = nickname,
            yearOfBirth = yearOfBirth,
            gender = gender,
            introduce = introduce
        )
    }

    // 이미지 선택 처리
    fun onImageSelected(uri: Uri) {
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val imageData = inputStream?.readBytes()
                val mimeType = context.contentResolver.getType(uri)
                
                val currentTemp = _tempProfileData.value
                _tempProfileData.value = currentTemp?.copy(
                    profileImageUri = uri,
                    profileImageData = imageData,
                    profileImageMimeType = mimeType
                ) ?: TempProfileData(
                    nickname = "",
                    yearOfBirth = "",
                    gender = "",
                    introduce = "",
                    profileImageUri = uri,
                    profileImageData = imageData,
                    profileImageMimeType = mimeType
                )
                
                inputStream?.close()
            } catch (e: Exception) {
                // 이미지 로드 실패 처리
                _profileState.value = ProfileInfoState.Error("이미지 로드에 실패했습니다")
            }
        }
    }

    fun updateProfile(profileUpdateModel: ProfileUpdateModel) {
        val tempData = _tempProfileData.value ?: return
        val currentState = _profileState.value
        
        // 현재 프로필 상태에서 userId 가져오기
        val userId = if (currentState is ProfileInfoState.Success) {
            currentState.profileInfo.userId.toString()
        } else {
            "me" // fallback으로 "me" 사용
        }
        
        viewModelScope.launch {
            val request = profileUpdateModel.copy(
                gender = tempData.gender,
                introduce = tempData.introduce,
                nickname = tempData.nickname,
                yearOfBirth = tempData.yearOfBirth,
                profileImageData = tempData.profileImageData,
                profileImageMimeType = tempData.profileImageMimeType
            )

            _profileState.value = ProfileInfoState.Loading
            val result = updateProfileUseCase(userId, request)

            result.onSuccess { response ->
                if (response.isSuccess) {
                    _profileState.value = ProfileInfoState.EditSuccess
                    _tempProfileData.value = null
                } else {
                    _profileState.value = ProfileInfoState.Error("프로필 수정에 실패했습니다")
                }
            }.onFailure { error ->
                _profileState.value = ProfileInfoState.Error(
                    error.message ?: "프로필 수정에 실패했습니다"
                )
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileInfoState.Loading
            val result = myProfileUseCase()

            result.onSuccess { profileModel ->
                val profileInfo = profileModel.toProfileInfo(isMyProfile = true)
                _profileState.value = ProfileInfoState.Success(profileInfo)
            }.onFailure { error ->
                // /me 실패시 getCurrentUserIdUseCase로 fallback 시도
                tryLoadMyProfileWithCurrentUserId()
            }
        }
    }

    private suspend fun tryLoadMyProfileWithCurrentUserId() {
        try {
            val currentUserId = getCurrentUserIdUseCase()

            if (currentUserId != null) {
                val fallbackResult = otherUserProfileUseCase(currentUserId.toString())
                
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
}

