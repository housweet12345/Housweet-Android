package com.housweet.domain.usecase.community

import kotlinx.coroutines.flow.Flow

fun interface ClickBookMarkUseCase : suspend (Int) -> Flow<Result<Boolean>>