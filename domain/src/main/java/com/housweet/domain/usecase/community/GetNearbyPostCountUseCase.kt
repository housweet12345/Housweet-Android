package com.housweet.domain.usecase.community

import com.housweet.domain.model.NearByPostCountDataModel
import kotlinx.coroutines.flow.Flow

fun interface GetNearbyPostCountUseCase : suspend (Double, Double, Int) -> Flow<Result<List<NearByPostCountDataModel>>>