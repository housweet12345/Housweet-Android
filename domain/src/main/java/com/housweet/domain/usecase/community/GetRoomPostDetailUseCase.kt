package com.housweet.domain.usecase.community

import com.housweet.domain.model.community.RoomPostDetailDataModel

fun interface GetRoomPostDetailUseCase : suspend (Int) -> Result<RoomPostDetailDataModel>