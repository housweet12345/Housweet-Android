package com.housweet.domain.usecase.start

import kotlinx.coroutines.flow.Flow

fun interface AccessRoomWithInviteCodeUseCase {
    suspend operator fun invoke(inviteCode: String): Flow<Result<Boolean>>
}