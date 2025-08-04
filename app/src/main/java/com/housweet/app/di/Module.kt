package com.housweet.app.di

import android.content.Context
import com.housweet.data.datasource.ImageUploadRemoteDataSourceImpl
import com.housweet.data.local.AuthLocalDataSource
import com.housweet.data.local.AuthLocalDataSourceImpl
import com.housweet.data.local.RoomLocalDataSourceImpl
import com.housweet.data.network.AccessRoomRemoteDataSource
import com.housweet.data.network.AccessRoomRemoteDataSourceImpl
import com.housweet.data.network.AuthRemoteDataSource
import com.housweet.data.network.AuthRemoteDataSourceImpl
import com.housweet.data.network.CommunityRemoteDataSource
import com.housweet.data.network.CommunityRemoteDataSourceImpl
import com.housweet.data.network.HouseRegisterRemoteDataSource
import com.housweet.data.network.HouseRegisterRemoteDataSourceImpl
import com.housweet.data.network.ImageUploadRemoteDataSource
import com.housweet.data.network.KtorService
import com.housweet.data.network.RoomRemoteDataSource
import com.housweet.data.network.RoomRemoteDataSourceImpl
import com.housweet.data.network.RoomPostingRepositoryImpl
import com.housweet.data.repository.AccessRoomRepositoryImpl
import com.housweet.data.repository.AuthRepositoryImpl
import com.housweet.data.repository.CommunityRepositoryImpl
import com.housweet.data.repository.HouseRegisterRepositoryImpl
import com.housweet.data.repository.ImageUploadRepositoryImpl
import com.housweet.data.utils.CryptoManager
import com.housweet.data.utils.NetworkConnectionManager
import com.housweet.domain.event.AuthEventBus
import com.housweet.domain.local.RoomLocalDataSource
import com.housweet.domain.repository.AccessRoomRepository
import com.housweet.domain.repository.AuthRepository
import com.housweet.domain.repository.CommunityRepository
import com.housweet.domain.repository.HouseRegisterRepository
import com.housweet.domain.repository.ImageUploadRepository
import com.housweet.domain.repository.RoomPostingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
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
        accessRoomRemoteDateSourceImpl: AccessRoomRemoteDataSourceImpl
    ): AccessRoomRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAccessRoomRepository(
        accessRoomRepositoryImpl: AccessRoomRepositoryImpl
    ): AccessRoomRepository

    @Binds
    @Singleton
    abstract fun bindCommunityRemoteDataSource(
        communityRemoteDataSourceImpl: CommunityRemoteDataSourceImpl
    ): CommunityRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCommunityRepository(
        communityRepositoryImpl: CommunityRepositoryImpl
    ): CommunityRepository
    @Binds
    @Singleton
    abstract fun bindRoomRemoteDataSource(
        impl: RoomRemoteDataSourceImpl
    ): RoomRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindRoomLocalDataSource(
        impl: RoomLocalDataSourceImpl
    ): RoomLocalDataSource

    @Binds
    @Singleton
    abstract fun bindHouseRegisterRemoteDataSource(
        impl: HouseRegisterRemoteDataSourceImpl
    ): HouseRegisterRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindHouseRegisterRepository(
        impl: HouseRegisterRepositoryImpl
    ): HouseRegisterRepository

    @Binds
    @Singleton
    abstract fun bindImageUploadRemoteDataSource(
        impl: ImageUploadRemoteDataSourceImpl
    ): ImageUploadRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindImageUploadRepository(
        impl: ImageUploadRepositoryImpl
    ): ImageUploadRepository

    @Binds
    abstract fun bindRoomRepository(
        impl: RoomPostingRepositoryImpl
    ): RoomPostingRepository

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
        fun provideHttpClient(
            ktorService: KtorService
        ): HttpClient {
            return ktorService.createHttpClient()
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