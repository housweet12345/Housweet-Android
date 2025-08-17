package com.housweet.data.datasource

import android.util.Log
import com.housweet.data.api.ImageUploadApiService
import com.housweet.data.network.ImageUploadRemoteDataSource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.internal.http.hasBody
import javax.inject.Inject
import java.io.File
import java.net.URLConnection

class ImageUploadRemoteDataSourceImpl @Inject constructor(
    private val apiService: ImageUploadApiService
) : ImageUploadRemoteDataSource {

    override suspend fun uploadImage(imageFile: File): String {
        val mime = imageFile.toMimeType()
        Log.d("ImageUpload", "▶️ start upload: name=${imageFile.name}, size=${imageFile.length()}, mime=$mime")
        val body = imageFile.asRequestBody(mime.toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData(
            name = "image",
            filename = imageFile.name,
            body = body
        )
        val res = apiService.uploadImage(part)
        Log.d("ImageUpload", "✅ uploaded url=${res.url}")
        return res.url
    }
}

private fun File.toMimeType(): String {
    // 이름 기반 추정(예: ".png"면 image/png), 실패 시 확장자 스위치
    val guessed = URLConnection.guessContentTypeFromName(name)
    if (!guessed.isNullOrBlank()) return guessed

    return when (extension.lowercase()) {
        "jpg", "jpeg" -> "image/jpeg"
        "png"         -> "image/png"
        "webp"        -> "image/webp"
        else          -> "application/octet-stream"
    }
}