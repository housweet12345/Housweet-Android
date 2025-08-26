package com.housweet.app.di

import android.content.Context
import com.housweet.data.api.ReportApi
import com.housweet.data.datasource.ImageUploadRemoteDataSourceImpl
import com.housweet.data.local.AuthLocalDataSource
import com.housweet.data.local.AuthLocalDataSourceImpl
import com.housweet.data.local.RoomLocalDataSourceImpl
import com.housweet.data.network.AccessRoomRemoteDataSource
import com.housweet.data.network.AccessRoomRemoteDataSourceImpl
import com.housweet.data.network.AppSettingRemoteDataSource
import com.housweet.data.network.AuthRemoteDataSource
import com.housweet.data.network.AuthRemoteDataSourceImpl
import com.housweet.data.network.ChatRemoteDataSource
import com.housweet.data.network.ChatRemoteDataSourceImpl
import com.housweet.data.network.CommunityRemoteDataSource
import com.housweet.data.network.CommunityRemoteDataSourceImpl
import com.housweet.data.network.HouseRegisterRemoteDataSource
import com.housweet.data.network.HouseRegisterRemoteDataSourceImpl
import com.housweet.data.network.ImageUploadRemoteDataSource
import com.housweet.data.network.KtorService
import com.housweet.data.network.MyHouseRemoteDataSource
import com.housweet.data.network.MyHouseRemoteDataSourceImpl
import com.housweet.data.network.NoticeRemoteDataSource
import com.housweet.data.network.NoticeRemoteDataSourceImpl
import com.housweet.data.network.NotificationRemoteDataSource
import com.housweet.data.network.NotificationRemoteDataSourceImpl
import com.housweet.data.network.ReportRemoteDataSource
import com.housweet.data.network.ReportRemoteDataSourceImpl
import com.housweet.data.network.RoomPostingRepositoryImpl
import com.housweet.data.network.RoomRemoteDataSource
import com.housweet.data.network.RoomRemoteDataSourceImpl
import com.housweet.data.network.AppSettingRemoteDataSourceImpl
import com.housweet.data.repository.AccessRoomRepositoryImpl
import com.housweet.data.repository.AppSettingRepositoryImpl
import com.housweet.data.repository.AuthRepositoryImpl
import com.housweet.data.repository.ChatRepositoryImpl
import com.housweet.data.repository.CommunityRepositoryImpl
import com.housweet.data.repository.HouseRegisterRepositoryImpl
import com.housweet.data.repository.ImageUploadRepositoryImpl
import com.housweet.data.repository.MyHouseRepositoryImpl
import com.housweet.data.repository.ReportRepositoryImpl
import com.housweet.data.repository.NoticeRepositoryImpl
import com.housweet.data.utils.CryptoManager
import com.housweet.data.utils.NetworkConnectionManager
import com.housweet.domain.event.AuthEventBus
import com.housweet.domain.local.RoomLocalDataSource
import com.housweet.domain.repository.AccessRoomRepository
import com.housweet.domain.repository.AppSettingRepository
import com.housweet.domain.repository.AuthRepository
import com.housweet.domain.repository.ChatRepository
import com.housweet.domain.repository.CommunityRepository
import com.housweet.domain.repository.HouseRegisterRepository
import com.housweet.domain.repository.ImageUploadRepository
import com.housweet.domain.repository.NotificationRepository
import com.housweet.data.repository.NotificationRepositoryImpl
import com.housweet.data.usecase.GetMyRoomIdUseCaseImpl
import com.housweet.domain.repository.MyHouseRepository
import com.housweet.domain.repository.NoticeRepository
import com.housweet.domain.repository.ReportRepository
import com.housweet.domain.repository.RoomPostingRepository
import com.housweet.domain.usecase.GetMyRoomIdUseCase
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

    @Binds
    @Singleton
    abstract fun bindChatRemoteDataSource(
        impl: ChatRemoteDataSourceImpl
    ): ChatRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindAppSettingRemoteDataSource(
        impl: AppSettingRemoteDataSourceImpl
    ): AppSettingRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAppSettingRepository(
        impl: AppSettingRepositoryImpl
    ): AppSettingRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRemoteDataSource(
        impl: NotificationRemoteDataSourceImpl
    ): NotificationRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindNoticeRemoteDataSource(
        impl: NoticeRemoteDataSourceImpl
    ): NoticeRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindNoticeRepository(
        impl: NoticeRepositoryImpl
    ): NoticeRepository
    @Binds
    @Singleton
    abstract fun bindReportRemoteDataSource(
        impl: ReportRemoteDataSourceImpl
    ): ReportRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindReportRepository(
        impl: ReportRepositoryImpl
    ): ReportRepository

    @Binds
    @Singleton
    abstract fun bindMyHouseRemoteDataSource(
        impl: MyHouseRemoteDataSourceImpl
    ): MyHouseRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMyHouseRepository(
        impl: MyHouseRepositoryImpl
    ): MyHouseRepository

    @Binds
    @Singleton
    abstract fun bindGetMyRoomIdUseCase(
        impl: GetMyRoomIdUseCaseImpl
    ): GetMyRoomIdUseCase

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
        fun provideReportApi(ktorService: KtorService): ReportApi {
            return ReportApi(ktorService)
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