package com.housweet.domain.usecase.community

import com.housweet.domain.model.community.NearByPostCountDataModel

fun interface GetNearbyPostCountUseCase :
    suspend (Double, Double, Int) -> Result<List<NearByPostCountDataModel>>