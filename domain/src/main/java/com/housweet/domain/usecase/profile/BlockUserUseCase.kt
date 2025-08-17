package com.housweet.domain.usecase.profile

import com.housweet.domain.repository.UserRepository
import javax.inject.Inject

class BlockUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(blockedUserId: Int): Result<Boolean> {
        return userRepository.blockUser(blockedUserId)
    }
}