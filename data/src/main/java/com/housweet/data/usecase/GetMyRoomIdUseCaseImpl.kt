package com.housweet.data.usecase

import com.housweet.data.local.AuthLocalDataSource
import com.housweet.data.datasource.RoomRemoteDataSource
import com.housweet.domain.usecase.GetMyRoomIdUseCase
import javax.inject.Inject

class GetMyRoomIdUseCaseImpl @Inject constructor(
    private val roomRemote: RoomRemoteDataSource,
    private val authLocal: AuthLocalDataSource,
) : GetMyRoomIdUseCase {
    override suspend fun invoke(): Int {
        val token = authLocal.getAuthToken() ?: error("AccessToken 없음")
        return roomRemote.getMyRoomInfo(token.accessToken).id
    }
}