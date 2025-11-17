package com.housweet.data.api

import com.housweet.data.constants.ApiEndpoints
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

data class ImageUploadResponse(
    val url: String
)

interface ImageUploadApiService {
    @Multipart
    @POST("upload-image/")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): ImageUploadResponse
}

// Note: Retrofit 어노테이션은 컴파일 타임 상수만 허용하므로,
// ApiEndpoints.Image.UPLOAD를 직접 사용할 수 없습니다.
