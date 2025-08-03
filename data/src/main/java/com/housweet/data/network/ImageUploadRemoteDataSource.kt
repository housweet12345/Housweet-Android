package com.housweet.data.network

import java.io.File

interface ImageUploadRemoteDataSource {
    suspend fun uploadImage(imageFile: File): String
}