package com.housweet.domain.usecase.profile

import com.housweet.domain.model.profile.ProfileModel
import com.housweet.domain.model.profile.ProfileUpdateModel
import com.housweet.domain.model.profile.ProfileUpdateResponseModel

fun interface GetMyProfileUseCase: suspend () -> Result<ProfileModel>

fun interface GetOtherUserProfileUseCase: suspend (String) -> Result<ProfileModel>

fun interface UpdateProfileUseCase: suspend (String, ProfileUpdateModel) -> Result<ProfileUpdateResponseModel>