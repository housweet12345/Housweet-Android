package com.housweet.data.utils

import com.housweet.domain.model.profile.ProfileUpdateModel
import io.ktor.client.request.forms.FormBuilder
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

fun FormBuilder.appendProfileData(profile: ProfileUpdateModel) {
    append("gender", profile.gender)
    append("introduce", profile.introduce)
    append("mbti", profile.mbti)
    append("nickname", profile.nickname)
    append("year_of_birth", profile.yearOfBirth)
    
    // 태그들 추가
    profile.tags.forEachIndexed { index, tag ->
        append("tag[$index]", tag)
    }
    
    // 이미지 추가 (있는 경우)
    profile.profileImageData?.let { imageData ->
        val mimeType = profile.profileImageMimeType ?: "image/jpeg"
        append("profile_image", imageData, Headers.build {
            append(HttpHeaders.ContentType, mimeType)
            append(HttpHeaders.ContentDisposition, "filename=\"profile_image.jpg\"")
        })
    }
}