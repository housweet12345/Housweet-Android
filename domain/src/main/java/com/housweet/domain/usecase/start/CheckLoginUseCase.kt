package com.housweet.domain.usecase.start

fun interface CheckLoginUseCase: suspend () -> Result<Boolean>