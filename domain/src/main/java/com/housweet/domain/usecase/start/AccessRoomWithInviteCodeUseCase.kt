package com.housweet.domain.usecase.start


fun interface AccessRoomWithInviteCodeUseCase {
    suspend operator fun invoke(inviteCode: String): Result<Boolean>
}