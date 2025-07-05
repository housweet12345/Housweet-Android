package com.housweet.data.di

import com.housweet.data.local.AuthLocalDataSource
import com.housweet.data.local.AuthLocalDataSourceImpl
import com.housweet.data.network.AuthRemoteDataSource
import com.housweet.data.network.AuthRemoteDataSourceImpl
import com.housweet.data.network.KtorService
import com.housweet.data.repository.AuthRepositoryImpl
import com.housweet.data.utils.CryptoManager
import com.housweet.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAuthLocalDataSource(
        authLocalDataSourceImpl: AuthLocalDataSourceImpl
    ): AuthLocalDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideKtorClient(
            authLocalDataSource: AuthLocalDataSource
        ): KtorService {
            return KtorService(authLocalDataSource)
        }

        @Provides
        @Singleton
        fun provideCryptoManager(): CryptoManager {
            return CryptoManager()
        }
    }
}