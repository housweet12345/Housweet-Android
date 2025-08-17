package com.housweet.domain.repository

import java.io.File

interface ImageUploadRepository {
    suspend fun uploadImage(imageFile: File): String
}