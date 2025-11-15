package com.housweet.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.housweet.data.api.ReportApi
import com.housweet.data.datasource.ImageUploadRemoteDataSourceImpl
import com.housweet.data.local.AuthLocalDataSource
import com.housweet.data.local.AuthLocalDataSourceImpl
import com.housweet.data.local.RoomLocalDataSourceImpl
import com.housweet.data.manager.BaseUrlManager
import com.housweet.data.datasource.AccessRoomRemoteDataSource
import com.housweet.data.datasource.AccessRoomRemoteDataSourceImpl
import com.housweet.data.datasource.AppSettingRemoteDataSource
import com.housweet.data.datasource.AuthRemoteDataSource
import com.housweet.data.datasource.AuthRemoteDataSourceImpl
import com.housweet.data.datasource.ChatRemoteDataSource
import com.housweet.data.datasource.ChatRemoteDataSourceImpl
import com.housweet.data.datasource.CommunityRemoteDataSource
import com.housweet.data.datasource.CommunityRemoteDataSourceImpl
import com.housweet.data.datasource.HouseRegisterRemoteDataSource
import com.housweet.data.datasource.HouseRegisterRemoteDataSourceImpl
import com.housweet.data.datasource.ImageUploadRemoteDataSource
import com.housweet.data.network.KtorService
import com.housweet.data.datasource.MyHouseRemoteDataSource
import com.housweet.data.datasource.MyHouseRemoteDataSourceImpl
import com.housweet.data.datasource.NoticeRemoteDataSource
import com.housweet.data.datasource.NoticeRemoteDataSourceImpl
import com.housweet.data.datasource.NotificationRemoteDataSource
import com.housweet.data.datasource.NotificationRemoteDataSourceImpl
import com.housweet.data.datasource.ReportRemoteDataSource
import com.housweet.data.datasource.ReportRemoteDataSourceImpl
import com.housweet.data.datasource.RoomPostingRepositoryImpl
import com.housweet.data.datasource.RoomRemoteDataSource
import com.housweet.data.datasource.RoomRemoteDataSourceImpl
import com.housweet.data.datasource.AppSettingRemoteDataSourceImpl
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
import com.housweet.data.usecase.GetDebugConfigUseCaseImpl
import com.housweet.data.usecase.GetMyRoomIdUseCaseImpl
import com.housweet.data.usecase.SetDebugConfigUseCaseImpl
import com.housweet.domain.usecase.debug.GetDebugConfigUseCase
import com.housweet.domain.usecase.debug.SetDebugConfigUseCase
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

    @Binds
    @Singleton
    abstract fun bindGetDebugConfigUseCase(
        impl: GetDebugConfigUseCaseImpl
    ): GetDebugConfigUseCase

    @Binds
    @Singleton
    abstract fun bindSetDebugConfigUseCase(
        impl: SetDebugConfigUseCaseImpl
    ): SetDebugConfigUseCase

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
            authEventBus: AuthEventBus,
            baseUrlManager: BaseUrlManager
        ): KtorService {
            return KtorService(authLocalDataSource, authEventBus, baseUrlManager)
        }

        private val Context.debugConfigDataStore: DataStore<Preferences> by preferencesDataStore(name = "debug_config")

        @Provides
        @Singleton
        fun provideDebugConfigDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.debugConfigDataStore
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