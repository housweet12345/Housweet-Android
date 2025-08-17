package com.housweet.presentation.viewmodel.profile

import android.net.Uri

// 임시 프로필 데이터 클래스
data class TempProfileData(
    val nickname: String,
    val yearOfBirth: String,
    val gender: String,
    val introduce: String,
    val profileImageUri: Uri? = null,
    val profileImageData: ByteArray? = null,
    val profileImageMimeType: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TempProfileData

        if (nickname != other.nickname) return false
        if (yearOfBirth != other.yearOfBirth) return false
        if (gender != other.gender) return false
        if (introduce != other.introduce) return false
        if (profileImageUri != other.profileImageUri) return false
        if (profileImageData != null) {
            if (other.profileImageData == null) return false
            if (!profileImageData.contentEquals(other.profileImageData)) return false
        } else if (other.profileImageData != null) return false
        if (profileImageMimeType != other.profileImageMimeType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nickname.hashCode()
        result = 31 * result + yearOfBirth.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + introduce.hashCode()
        result = 31 * result + (profileImageUri?.hashCode() ?: 0)
        result = 31 * result + (profileImageData?.contentHashCode() ?: 0)
        result = 31 * result + (profileImageMimeType?.hashCode() ?: 0)
        return result
    }
}