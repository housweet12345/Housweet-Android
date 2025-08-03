package com.housweet.data.di

import com.housweet.data.api.ImageUploadApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageUploadNetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .build()

    @Provides
    @Singleton
    fun provideImageUploadApiService(okHttpClient: OkHttpClient): ImageUploadApiService {
        return Retrofit.Builder()
            .baseUrl("http://YOUR_BASE_URL/") // 🔺 반드시 '/' 로 끝나야 함!
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImageUploadApiService::class.java)
    }
}