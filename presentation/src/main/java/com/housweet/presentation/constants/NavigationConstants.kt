package com.housweet.presentation.constants

/**
 * Navigation 관련 상수 관리 객체
 * 문자열 리터럴로 사용되는 navigation route와 argument key를 중앙에서 관리합니다.
 */
object NavigationConstants {
    /**
     * Route 문자열
     */
    object Routes {
        const val MY_PAGE = "mypage"
        const val MY_PAGE_AFTER_DELETE = "mypage?afterDelete=true"
        const val BOOKMARK = "bookmark"
    }

    /**
     * Navigation Argument Keys
     */
    object Arguments {
        const val AFTER_DELETE = "afterDelete"
        const val MY_HOUSE_REFRESH = "MY_HOUSE_REFRESH"
    }
}
