package com.housweet.domain.usecase

import com.housweet.domain.model.Report

fun interface ReportUseCase: suspend (String, Int) -> Report
