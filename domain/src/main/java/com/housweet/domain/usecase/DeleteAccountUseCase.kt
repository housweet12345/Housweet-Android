package com.housweet.domain.usecase

fun interface DeleteAccountUseCase: suspend () -> Result<Boolean>