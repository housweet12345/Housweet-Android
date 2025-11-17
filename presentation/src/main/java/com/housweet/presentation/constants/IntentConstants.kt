package com.housweet.presentation.constants

/**
 * Intent Extra Key 상수 관리 객체
 * Intent로 데이터를 전달할 때 사용하는 key를 중앙에서 관리합니다.
 */
object IntentConstants {
    /**
     * 토큰 갱신 실패 여부
     */
    const val IS_FAILED_REFRESH_TOKEN = "isFailedRefreshToken"

    /**
     * 로그아웃 여부
     */
    const val IS_LOGOUT = "isLogout"

    /**
     * 계정 삭제 여부
     */
    const val IS_DELETE_ACCOUNT = "isDeleteAccount"
}
