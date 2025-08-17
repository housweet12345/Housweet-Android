package com.housweet.data.di

import com.housweet.data.BuildConfig
import com.housweet.data.api.ImageUploadApiService
import com.housweet.data.local.AuthLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageUploadNetworkModule {

    private val BASE_URL = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(authLocal: AuthLocalDataSource): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val req = chain.request()
                val token = runBlocking { authLocal.getAuthToken()?.accessToken }
                val newReq = if(!token.isNullOrBlank()) {
                    req.newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else req
                chain.proceed(newReq)
            }
            .build()

    @Provides
    @Singleton
    fun provideImageUploadApiService(okHttpClient: OkHttpClient): ImageUploadApiService {
        return Retrofit.Builder()
            .baseUrl("${BASE_URL}/") // 반드시 '/' 로 끝나야 함!
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImageUploadApiService::class.java)
    }
}