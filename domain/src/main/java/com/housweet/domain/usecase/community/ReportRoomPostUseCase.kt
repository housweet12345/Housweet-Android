package com.housweet.domain.usecase.community

import kotlinx.coroutines.flow.Flow

fun interface ReportRoomPostUseCase : suspend (Int) -> Flow<Result<Boolean>>