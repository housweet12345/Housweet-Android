package com.housweet.domain.usecase.profile

import com.housweet.domain.model.profile.ProfileModel
import com.housweet.domain.model.profile.ProfileUpdateModel

fun interface GetMyProfileUseCase: suspend () -> Result<ProfileModel>

fun interface GetOtherUserProfileUseCase: suspend (String) -> Result<ProfileModel>

fun interface UpdateProfileUseCase: suspend (String, ProfileUpdateModel) -> Result<ProfileUpdateModel>