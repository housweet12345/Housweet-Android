package com.housweet.domain.usecase

import com.housweet.domain.model.BookmarkItem
import com.housweet.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBookmarkedPostingsUseCase @Inject constructor(
    private val repository: CommunityRepository
) {
    operator fun invoke(): Flow<Result<List<BookmarkItem>>> = flow {
        emit(repository.getBookmarkedPostings())
    }
}