package com.housweet.domain.usecase.community

import com.housweet.domain.model.community.RoomPostsByLocationDataModel
import kotlinx.coroutines.flow.Flow

fun interface GetRoomPostsByLocationUseCase :
    suspend (String) -> Flow<Result<List<RoomPostsByLocationDataModel>>>