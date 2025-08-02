package com.housweet.data.datasource

import com.housweet.data.api.ImageUploadApiService
import com.housweet.data.network.ImageUploadRemoteDataSource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import java.io.File

class ImageUploadRemoteDataSourceImpl @Inject constructor(
    private val apiService: ImageUploadApiService
) : ImageUploadRemoteDataSource {

    override suspend fun uploadImage(imageFile: File): String {
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageFile)
        val multipartBody = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

        val response = apiService.uploadImage(multipartBody)
        return response.url // "url" 필드를 응답으로 받는다고 가정
    }
}