package com.anshtya.taskrecorder.data.network

import kotlinx.serialization.Serializable

@Serializable
data class NetworkProductResponse(
    val products: List<NetworkProduct>
)

@Serializable
data class NetworkProduct(
    val id: Int,
    val description: String,
    val images: List<String>
)
