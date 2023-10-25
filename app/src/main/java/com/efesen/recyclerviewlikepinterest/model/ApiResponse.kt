package com.efesen.recyclerviewlikepinterest.model

/**
 * Created by Efe Şen on 24.10.2023.
 */
data class ApiResponse(
    val totalHits: Int,
    val hits: List<ImageItem> // It is included in the images from the api
)