package com.housweet.domain.usecase

import kotlinx.coroutines.flow.Flow

fun interface DeleteAccountUseCase: suspend () -> Flow<Result<Boolean>>