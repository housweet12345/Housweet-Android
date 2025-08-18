package com.housweet.data.repository

import com.housweet.data.network.MyHouseRemoteDataSource
import com.housweet.domain.model.MyHouse
import com.housweet.domain.repository.MyHouseRepository
import javax.inject.Inject

class MyHouseRepositoryImpl @Inject constructor(
    private val remote: MyHouseRemoteDataSource
) : MyHouseRepository {

    override suspend fun getMyHouseOrNull(): MyHouse? =
        remote.getMyHouse()?.toDomain()

    override suspend fun updateMyHouseName(roomId: Int, name: String): MyHouse {
        return remote.updateMyHouseName(roomId, name).toDomain()
    }

    override suspend fun refreshInviteCode(): MyHouse =
        remote.refreshInviteCode().toDomain()

    override suspend fun deleteMyHouse(roomId: Int) {
        remote.deleteMyHouse(roomId)
    }
}