package com.housweet.app.di

import android.content.Context
import com.housweet.data.local.AuthLocalDataSource
import com.housweet.data.local.AuthLocalDataSourceImpl
import com.housweet.data.network.AccessRoomRemoteDateSource
import com.housweet.data.network.AccessRoomRemoteDateSourceImpl
import com.housweet.data.network.AuthRemoteDataSource
import com.housweet.data.network.AuthRemoteDataSourceImpl
import com.housweet.data.network.KtorService
import com.housweet.data.repository.AccessRoomRepositoryImpl
import com.housweet.data.repository.AuthRepositoryImpl
import com.housweet.data.utils.CryptoManager
import com.housweet.data.utils.NetworkConnectionManager
import com.housweet.domain.event.AuthEventBus
import com.housweet.domain.repository.AccessRoomRepository
import com.housweet.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Module {
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

    @Binds
    @Singleton
    abstract fun bindAccessRoomRemoteDateSource(
        accessRoomRemoteDateSourceImpl: AccessRoomRemoteDateSourceImpl
    ): AccessRoomRemoteDateSource

    @Binds
    @Singleton
    abstract fun bindAccessRoomRepository(
        accessRoomRepositoryImpl: AccessRoomRepositoryImpl
    ): AccessRoomRepository


    companion object {
        @Provides
        @Singleton
        fun provideAuthEventBus(): AuthEventBus {
            return AuthEventBus()
        }

        @Provides
        @Singleton
        fun provideKtorClient(
            authLocalDataSource: AuthLocalDataSource,
            authEventBus: AuthEventBus
        ): KtorService {
            return KtorService(authLocalDataSource, authEventBus)
        }

        @Provides
        @Singleton
        fun provideCryptoManager(): CryptoManager {
            return CryptoManager()
        }

        @Provides
        @Singleton
        fun provideNetworkConnectionManager(@ApplicationContext context: Context): NetworkConnectionManager {
            return NetworkConnectionManager(context)
        }
    }
}