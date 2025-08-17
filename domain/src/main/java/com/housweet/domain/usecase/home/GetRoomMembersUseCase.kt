package com.housweet.domain.usecase.home

import com.housweet.domain.model.home.RoomMemberModel

fun interface GetRoomMembersUseCase: suspend (Int) -> Result<List<RoomMemberModel>>