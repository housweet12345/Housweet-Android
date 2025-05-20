package com.housweet.presentation.ui.profile.screen

import com.housweet.domain.usecase.profile.GetMyProfileUseCase
import com.housweet.domain.usecase.profile.GetOtherUserProfileUseCase
import com.housweet.domain.usecase.profile.UpdateProfileUseCase
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getOtherUserProfileUseCase: GetOtherUserProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
){

}