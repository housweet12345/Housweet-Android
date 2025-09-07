package com.housweet.domain.usecase.home

import com.housweet.domain.model.home.RoomMemberModel

fun interface UpdateMoodUseCase: suspend (Int, String) -> Result<RoomMemberModel>