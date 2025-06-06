package com.housweet.app.di

import com.housweet.data.network.KtorService
import com.housweet.data.repository.FakeUserRepositoryImpl
import com.housweet.data.repository.UserRepositoryImpl
import com.housweet.domain.repository.AuthRepository
import com.housweet.domain.repository.UserRepository
import com.housweet.domain.usecase.LoginWithKakaoUseCase
import com.housweet.domain.usecase.UseCases
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
    fun provideUseCase(
        authRepository: AuthRepository
    ): UseCases {
        return UseCases(
            loginWithKakaoUseCase = LoginWithKakaoUseCase(authRepository)
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

}