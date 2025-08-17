package com.housweet.domain.usecase

import com.housweet.domain.usecase.community.ClickBookMarkUseCase
import com.housweet.domain.usecase.community.GetNearbyPostCountUseCase
import com.housweet.domain.usecase.community.GetRoomPostDetailUseCase
import com.housweet.domain.usecase.community.GetRoomPostsByLocationUsaCase
import com.housweet.domain.usecase.community.ReportRoomPostUseCase
import com.housweet.domain.usecase.community.UnClickBookMarkUseCase
import com.housweet.domain.usecase.start.AccessRoomWithInviteCodeUseCase
import com.housweet.domain.usecase.start.AgreeTermsOfServiceUseCase
import com.housweet.domain.usecase.start.CheckLoginUseCase
import com.housweet.domain.usecase.start.CreateRoomUseCase
import com.housweet.domain.usecase.start.IsBelongToRoomUseCase
import com.housweet.domain.usecase.start.IsSetProfileUseCase
import com.housweet.domain.usecase.start.IsTermsOfServiceAgreedUseCase
import com.housweet.domain.usecase.start.LoginWithKakaoUseCase

data class UseCases (
    val loginWithKakaoUseCase: LoginWithKakaoUseCase,
    val logoutUseCase: LogoutUseCase,
    val checkLoginUseCase: CheckLoginUseCase,
    val createRoomUseCase: CreateRoomUseCase,
    val getNearbyPostCountUseCase: GetNearbyPostCountUseCase,
    val getRoomPostsByLocationUsaCase: GetRoomPostsByLocationUsaCase,
    val clickBookMarkUseCase: ClickBookMarkUseCase,
    val unClickBookMarkUseCase: UnClickBookMarkUseCase,
    val getRoomPostDetailUseCase: GetRoomPostDetailUseCase,
    val accessRoomWithInviteCodeUseCase: AccessRoomWithInviteCodeUseCase,
    val agreeTermsOfServiceUseCase: AgreeTermsOfServiceUseCase,
    val isTermsOfServiceAgreedUseCase: IsTermsOfServiceAgreedUseCase,
    val isSetProfileUseCase: IsSetProfileUseCase,
    val isBelongToRoomUseCase: IsBelongToRoomUseCase,
    val reportRoomPostUseCase: ReportRoomPostUseCase,
    val getBookmarkedPostingsUseCase: GetBookmarkedPostingsUseCase,
)