package com.housweet.domain.usecase.community


fun interface ClickBookMarkUseCase : suspend (Int) -> Result<Boolean>