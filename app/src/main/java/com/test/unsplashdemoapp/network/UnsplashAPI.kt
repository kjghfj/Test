package com.test.unsplashdemoapp.network

import com.test.unsplashdemoapp.models.ImageRoot
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashAPI {

    @GET("/photos")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<ImageRoot>

    @GET("/photos/{imageId}")
    suspend fun getImage(
        @Path("imageId", encoded = false) imageId: String,
    ): ImageRoot
}