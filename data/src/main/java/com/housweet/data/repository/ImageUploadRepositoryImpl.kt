package com.housweet.data.repository

import com.housweet.data.datasource.ImageUploadRemoteDataSource
import com.housweet.domain.repository.ImageUploadRepository
import java.io.File
import javax.inject.Inject

class ImageUploadRepositoryImpl @Inject constructor(
    private val remoteDataSource: ImageUploadRemoteDataSource
) : ImageUploadRepository {
    override suspend fun uploadImage(imageFile: File): String {
        return remoteDataSource.uploadImage(imageFile)
    }
}