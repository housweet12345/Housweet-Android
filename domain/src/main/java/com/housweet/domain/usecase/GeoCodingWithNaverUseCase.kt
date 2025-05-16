package com.housweet.domain.usecase

import com.housweet.domain.model.Coordinate
import com.housweet.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GeoCodingWithNaverUseCase
    (private val authRepository: AuthRepository
) {
    suspend operator fun invoke(query: String): Flow<Result<Coordinate>> {
        return authRepository.geoCodingWithNaver(query)
    }
}