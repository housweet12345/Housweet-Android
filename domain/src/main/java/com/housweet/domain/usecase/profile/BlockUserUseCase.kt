package com.housweet.domain.usecase.profile

fun interface BlockUserUseCase: suspend (Int) -> Result<Boolean>