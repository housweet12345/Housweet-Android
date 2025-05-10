package com.housweet.data.local

import com.housweet.domain.model.AuthToken

interface AuthLocalDataSource {
    suspend fun saveAuthToken(token: AuthToken)
    suspend fun getAuthToken(): AuthToken?
    suspend fun clearAuthToken()
    suspend fun isTokenExpired(): Boolean
}