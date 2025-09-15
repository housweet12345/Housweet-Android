package com.housweet.data.local

import com.housweet.domain.model.start.AuthToken

interface AuthLocalDataSource {
    suspend fun saveAuthToken(token: AuthToken)
    suspend fun getAuthToken(): AuthToken?
    suspend fun clearAuthToken()
    suspend fun isAccessTokenExpired(): Boolean
    suspend fun isRefreshTokenExpired(): Boolean
}