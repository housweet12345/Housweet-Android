package com.housweet.data.datasource

import java.io.File

interface ImageUploadRemoteDataSource {
    suspend fun uploadImage(imageFile: File): String
}