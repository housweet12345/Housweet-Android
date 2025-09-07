package com.housweet.domain.usecase.community

import kotlinx.coroutines.flow.Flow

fun interface UnClickBookMarkUseCase : suspend (Int) -> Flow<Result<Boolean>>