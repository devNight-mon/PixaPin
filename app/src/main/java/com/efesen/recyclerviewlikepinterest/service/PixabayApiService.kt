package com.efesen.recyclerviewlikepinterest.service

import com.efesen.recyclerviewlikepinterest.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Efe Şen on 24.10.2023.
 */
interface PixabayApiService {
    @GET("/api/")
    fun searchImages(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("per_page") perPage: Int,
        @Query("image_type") imageType: String,
        @Query("safesearch") safeSearch: Boolean,
    ): Call<ApiResponse>
}