package com.housweet.data.network

import com.housweet.data.network.dto.GeoCodingResponseDto
import com.housweet.data.network.dto.LoginResponseDto

interface AuthRemoteDataSource {
    suspend fun loginWithKakao(kakaoToken: String): LoginResponseDto
    suspend fun geoCodingWithNaver(query: String): GeoCodingResponseDto
}