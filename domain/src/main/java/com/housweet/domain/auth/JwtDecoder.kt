package com.housweet.domain.auth

interface JwtDecoder { fun getUserIdFrom(token: String): Int? }