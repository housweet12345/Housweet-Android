package com.housweet.domain.usecase.community

import com.housweet.domain.model.community.RoomPostDetailDataModel
import kotlinx.coroutines.flow.Flow

fun interface GetRoomPostDetailUseCase : suspend (Int) -> Flow<Result<RoomPostDetailDataModel>>