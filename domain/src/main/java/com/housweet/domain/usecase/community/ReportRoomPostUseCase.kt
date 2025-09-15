package com.housweet.domain.usecase.community


fun interface ReportRoomPostUseCase : suspend (Int) -> Result<Boolean>