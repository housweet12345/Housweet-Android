package com.housweet.domain.usecase.community

import com.housweet.domain.model.community.RoomPostsByLocationDataModel

fun interface GetRoomPostsByLocationUseCase :
    suspend (String) -> Result<List<RoomPostsByLocationDataModel>>