package com.housweet.app.di

import com.housweet.data.local.AuthLocalDataSource
import com.housweet.data.network.KtorService
import com.housweet.data.repository.FakeUserRepositoryImpl
import com.housweet.data.repository.RoomRepositoryImpl
import com.housweet.data.repository.UserRepositoryImpl
import com.housweet.domain.repository.AuthRepository
import com.housweet.domain.repository.RoomRepository
import com.housweet.domain.repository.UserRepository
import com.housweet.domain.usecase.auth.GetCurrentUserIdUseCase
import com.housweet.domain.usecase.home.GetRoomHomeUseCase
import com.housweet.domain.usecase.home.GetRoomMembersUseCase
import com.housweet.domain.usecase.home.UpdateMoodUseCase
import com.housweet.domain.usecase.profile.BlockUserUseCase
import com.housweet.domain.usecase.profile.GetMyProfileUseCase
import com.housweet.domain.usecase.profile.GetOtherUserProfileUseCase
import com.housweet.domain.usecase.profile.UpdateProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {


    @Provides
    @Singleton
    fun provideUserRepository(
        ktorService: KtorService,
        authLocalDataSource: AuthLocalDataSource,
    ): UserRepository {
        val isFake = false
        return if (isFake) {
            FakeUserRepositoryImpl()
        } else {
            UserRepositoryImpl(ktorService, authLocalDataSource)
        }
    }

    @Provides
    fun provideGetMyProfileUseCase(
        repository: UserRepository
    ): GetMyProfileUseCase = GetMyProfileUseCase(repository::getMyProfile)

    @Provides
    fun provideGetOtherUserProfileUseCase(
        repository: UserRepository
    ): GetOtherUserProfileUseCase = GetOtherUserProfileUseCase(repository::getOtherUserProfile)

    @Provides
    fun provideUpdateProfileUseCase(
        repository: UserRepository
    ): UpdateProfileUseCase = UpdateProfileUseCase(repository::updateProfile)

    @Provides
    @Singleton
    fun provideRoomRepository(
        ktorService: KtorService
    ): RoomRepository {
        return RoomRepositoryImpl(ktorService)
    }

    @Provides
    fun provideGetRoomHomeUseCase(
        repository: RoomRepository
    ): GetRoomHomeUseCase = GetRoomHomeUseCase(repository::getRoomHome)

    @Provides
    fun provideGetRoomMembersUseCase(
        repository: RoomRepository
    ): GetRoomMembersUseCase = GetRoomMembersUseCase(repository::getRoomMembers)

    @Provides
    fun provideGetCurrentUserIdUseCase(
        repository: AuthRepository
    ): GetCurrentUserIdUseCase = GetCurrentUserIdUseCase(repository)

    @Provides
    fun provideUpdateMoodUseCase(
        repository: RoomRepository
    ): UpdateMoodUseCase = UpdateMoodUseCase(repository)

    @Provides
    fun provideBlockUserUseCase(
        repository: UserRepository
    ): BlockUserUseCase = BlockUserUseCase(repository)

}