package com.housweet.app.di

import com.housweet.data.network.KtorService
import com.housweet.data.repository.FakeUserRepositoryImpl
import com.housweet.data.repository.RoomRepositoryImpl
import com.housweet.data.repository.UserRepositoryImpl
import com.housweet.domain.repository.AccessRoomRepository
import com.housweet.domain.repository.AuthRepository
import com.housweet.domain.repository.CommunityRepository
import com.housweet.domain.repository.RoomRepository
import com.housweet.domain.repository.UserRepository
import com.housweet.domain.usecase.GetBookmarkedPostingsUseCase
import com.housweet.domain.usecase.LogoutUseCase
import com.housweet.domain.usecase.UseCases
import com.housweet.domain.usecase.community.ClickBookMarkUseCase
import com.housweet.domain.usecase.community.GetNearbyPostCountUseCase
import com.housweet.domain.usecase.community.GetRoomPostDetailUseCase
import com.housweet.domain.usecase.community.GetRoomPostsByLocationUsaCase
import com.housweet.domain.usecase.community.ReportRoomPostUseCase
import com.housweet.domain.usecase.community.UnClickBookMarkUseCase
import com.housweet.domain.usecase.home.GetRoomHomeUseCase
import com.housweet.domain.usecase.home.GetRoomMembersUseCase
import com.housweet.domain.usecase.home.UpdateMoodUseCase
import com.housweet.domain.usecase.profile.GetMyProfileUseCase
import com.housweet.domain.usecase.profile.GetOtherUserProfileUseCase
import com.housweet.domain.usecase.profile.UpdateProfileUseCase
import com.housweet.domain.usecase.auth.GetCurrentUserIdUseCase
import com.housweet.domain.usecase.start.AccessRoomWithInviteCodeUseCase
import com.housweet.domain.usecase.start.AgreeTermsOfServiceUseCase
import com.housweet.domain.usecase.start.CheckLoginUseCase
import com.housweet.domain.usecase.start.CreateRoomUseCase
import com.housweet.domain.usecase.start.IsBelongToRoomUseCase
import com.housweet.domain.usecase.start.IsTermsOfServiceAgreedUseCase
import com.housweet.domain.usecase.start.LoginWithKakaoUseCase
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
    fun provideUseCase(
        authRepository: AuthRepository,
        accessRoomRepository: AccessRoomRepository,
        communityRepository: CommunityRepository
    ): UseCases {
        return UseCases(
            loginWithKakaoUseCase = LoginWithKakaoUseCase(authRepository),
            logoutUseCase = LogoutUseCase(authRepository),
            checkLoginUseCase = CheckLoginUseCase(authRepository),
            createRoomUseCase = CreateRoomUseCase(accessRoomRepository),
            getNearbyPostCountUseCase = GetNearbyPostCountUseCase(communityRepository),
            getRoomPostsByLocationUsaCase = GetRoomPostsByLocationUsaCase(communityRepository),
            clickBookMarkUseCase = ClickBookMarkUseCase(communityRepository),
            unClickBookMarkUseCase = UnClickBookMarkUseCase(communityRepository),
            getRoomPostDetailUseCase = GetRoomPostDetailUseCase(communityRepository),
            accessRoomWithInviteCodeUseCase = AccessRoomWithInviteCodeUseCase(accessRoomRepository),
            agreeTermsOfServiceUseCase = AgreeTermsOfServiceUseCase(authRepository),
            isTermsOfServiceAgreedUseCase = IsTermsOfServiceAgreedUseCase(authRepository),
            isBelongToRoomUseCase = IsBelongToRoomUseCase(authRepository),
            reportRoomPostUseCase = ReportRoomPostUseCase(communityRepository),
            getBookmarkedPostingsUseCase = GetBookmarkedPostingsUseCase(communityRepository),
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        ktorService: KtorService
    ): UserRepository {
        val isFake = true
        return if (isFake) {
            FakeUserRepositoryImpl()
        } else {
            UserRepositoryImpl(ktorService)
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

}