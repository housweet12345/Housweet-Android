package com.housweet.domain.usecase.community

import com.housweet.domain.model.NearByPostCountDataModel
import com.housweet.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNearbyPostCountUseCase @Inject constructor(
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