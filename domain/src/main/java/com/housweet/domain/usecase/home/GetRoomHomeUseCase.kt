package com.housweet.domain.usecase.home

import com.housweet.domain.model.home.RoomHomeModel

fun interface GetRoomHomeUseCase: suspend () -> Result<RoomHomeModel>