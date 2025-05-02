package com.housweet.data.network

import com.housweet.data.network.dto.LoginResponseDto

interface AuthRemoteDataSource {
    suspend fun loginWithKakao(kakaoToken: String): LoginResponseDto
    suspend fun test(): LoginResponseDto
}