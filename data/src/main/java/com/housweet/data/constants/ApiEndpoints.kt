package com.housweet.data.constants

/**
 * API Endpoint 상수 관리 객체
 * 모든 API endpoint를 중앙에서 관리하여 유지보수성을 향상시킵니다.
 */
object ApiEndpoints {

    /**
     * 인증 관련 API endpoints
     */
    object Auth {
        const val LOGIN = "auth/login"
        const val TOKEN_REFRESH = "auth/token/refresh"
        const val WITHDRAW = "auth/withdraw"
    }

    /**
     * 사용자 관련 API endpoints
     */
    object User {
        const val SETTINGS_ME = "user/settings/me/"
        const val APP_SETTINGS = "user/app-settings/"

        /**
         * @param settingId 설정 ID
         * @return user/app-settings/{settingId}/
         */
        fun appSettingById(settingId: Int) = "user/app-settings/$settingId/"

        /**
         * @param userId 사용자 ID
         * @return profile/{userId}/{userId}
         */
        fun profileById(userId: Int) = "profile/$userId/$userId"
    }

    /**
     * Room(하우스) 관련 API endpoints
     */
    object Room {
        const val ROOMS = "room/rooms/"
        const val ROOMS_ME = "room/rooms/me/"
        const val ROOMS_ME_WITH_SLASH = "room/rooms/me"
        const val ROOMS_INVITE = "room/rooms/invite/"
        const val NEW_INVITE_CODE = "room/rooms/new_invite_code/"
        const val HOME = "room/home/"
        const val ROOM_POSTINGS = "room/room-postings/"
        const val BOOKMARKED_POSTINGS = "room/room-postings/bookmarked-postings/"
        const val REGION_NEAR = "room/region/near/"

        /**
         * @param roomId Room ID
         * @return room/rooms/{roomId}/
         */
        fun roomById(roomId: Int) = "room/rooms/$roomId/"

        /**
         * @param postingId Posting ID
         * @return room/room-postings/{postingId}/
         */
        fun postingById(postingId: Int) = "room/room-postings/$postingId/"

        /**
         * @param postingId Posting ID
         * @return room/room-postings/{postingId}/bookmark/
         */
        fun bookmarkByPostingId(postingId: Int) = "room/room-postings/$postingId/bookmark/"
    }

    /**
     * 채팅 관련 API endpoints
     */
    object Chat {
        /**
         * @param senderId Sender ID
         * @return chat/view-room/{senderId}/
         */
        fun viewRoomBySenderId(senderId: Int) = "chat/view-room/$senderId/"

        /**
         * @param senderId Sender ID
         * @param receiverId Receiver ID
         * @return chat/send/{senderId}/{receiverId}/
         */
        fun send(senderId: Int, receiverId: Int) = "chat/send/$senderId/$receiverId/"

        /**
         * @param senderId Sender ID
         * @param receiverId Receiver ID
         * @return chat/{senderId}/{receiverId}/messages/
         */
        fun messages(senderId: Int, receiverId: Int) = "chat/$senderId/$receiverId/messages/"

        /**
         * @param roomId Room ID
         * @return chat/room/{roomId}/delete/
         */
        fun deleteRoom(roomId: Int) = "chat/room/$roomId/delete/"

        /**
         * @param roomId Room ID
         * @return chat/block/{roomId}/
         */
        fun blockRoom(roomId: Int) = "chat/block/$roomId/"

        /**
         * @param senderId Sender ID
         * @param receiverId Receiver ID
         * @return chat/create_room/{senderId}/{receiverId}/
         */
        fun createRoom(senderId: Int, receiverId: Int) = "chat/create_room/$senderId/$receiverId/"
    }

    /**
     * 알림 및 공지 관련 API endpoints
     */
    object Communications {
        const val NOTIFICATIONS = "communications/notifications/"
        const val NOTICES = "communications/notices/"

        /**
         * @param noticeId 공지 ID
         * @return communications/notices/{noticeId}/
         */
        fun noticeById(noticeId: Int) = "communications/notices/$noticeId/"
    }

    /**
     * 신고 관련 API endpoints
     */
    object Report {
        const val REPORT = "report/"
    }

    /**
     * 이미지 업로드 관련 API endpoints
     */
    object Image {
        const val UPLOAD = "upload-image/"
    }
}
