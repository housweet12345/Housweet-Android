package com.housweet.data.repository

import com.housweet.data.model.request.RegisterHouseRequest
import com.housweet.data.network.HouseRegisterRemoteDataSource
import com.housweet.domain.model.HouseRegisterModel
import com.housweet.domain.repository.HouseRegisterRepository
import javax.inject.Inject

class HouseRegisterRepositoryImpl @Inject constructor(
    private val remoteDataSource: HouseRegisterRemoteDataSource
) : HouseRegisterRepository {

    override suspend fun registerHouse(model: HouseRegisterModel) {
        val request = RegisterHouseRequest(
            room = model.room,
            si = model.si,
            gu = model.gu,
            dong = model.dong,
            title = model.title,
            content = model.content,
            image_uri = model.imageUri,
            traffic_tags = model.trafficTags,
            size_of_house_tags = model.sizeOfHouseTags,
            infra_tags = model.infraTags,
            life_pattern_tags = model.lifePatternTags,
            tidying_up_habit_tags = model.tidyingUpHabitTags,
            personality_tags = model.personalityTags,
            rent = model.rent,
            deposit = model.deposit,
            management_fee = model.managementFee,
            available_from = model.availableFrom
        )

        remoteDataSource.registerHouse(request)
    }
}