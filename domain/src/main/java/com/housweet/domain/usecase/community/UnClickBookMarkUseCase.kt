package com.housweet.domain.usecase.community

fun interface UnClickBookMarkUseCase : suspend (Int) -> Result<Boolean>