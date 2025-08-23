package com.housweet.domain.usecase.start

import kotlinx.coroutines.flow.Flow

fun interface CheckLoginUseCase: suspend () -> Flow<Result<Boolean>>