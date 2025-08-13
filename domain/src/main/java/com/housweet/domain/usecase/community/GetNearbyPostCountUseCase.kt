package com.housweet.domain.usecase.community

import com.housweet.domain.model.NearByPostCountDataModel
import com.housweet.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow

class GetNearbyPostCountUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        filteringDistance: Int
    ): Flow<Result<List<NearByPostCountDataModel>>> {
        return communityRepository.getNearbyPostCount(latitude, longitude, filteringDistance)
    }
}