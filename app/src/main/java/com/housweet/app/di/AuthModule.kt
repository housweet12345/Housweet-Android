package com.housweet.app.di

import com.housweet.data.auth.JwtDecoderImpl
import com.housweet.data.repository.AuthRepositoryImpl
import com.housweet.domain.auth.JwtDecoder
import com.housweet.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindModule {
    @Binds abstract fun bindJwtDecoder(impl: JwtDecoderImpl): JwtDecoder
}