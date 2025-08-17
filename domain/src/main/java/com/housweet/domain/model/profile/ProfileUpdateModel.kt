package com.housweet.domain.model.profile

data class ProfileUpdateModel(
    val gender: String,
    val introduce: String,
    val mbti: String,
    val nickname: String,
    val tags: List<String>,
    val yearOfBirth: String,
    val profileImageData: ByteArray? = null,
    val profileImageMimeType: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProfileUpdateModel

        if (gender != other.gender) return false
        if (introduce != other.introduce) return false
        if (mbti != other.mbti) return false
        if (nickname != other.nickname) return false
        if (tags != other.tags) return false
        if (yearOfBirth != other.yearOfBirth) return false
        if (profileImageData != null) {
            if (other.profileImageData == null) return false
            if (!profileImageData.contentEquals(other.profileImageData)) return false
        } else if (other.profileImageData != null) return false
        if (profileImageMimeType != other.profileImageMimeType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = gender.hashCode()
        result = 31 * result + introduce.hashCode()
        result = 31 * result + mbti.hashCode()
        result = 31 * result + nickname.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + yearOfBirth.hashCode()
        result = 31 * result + (profileImageData?.contentHashCode() ?: 0)
        result = 31 * result + (profileImageMimeType?.hashCode() ?: 0)
        return result
    }
}