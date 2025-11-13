package com.anshtya.taskrecorder.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Single

@Single
class ApiClient(
    private val httpClient: HttpClient
) {
    suspend fun getProducts(): NetworkProductResponse {
        return httpClient.get("products").body<NetworkProductResponse>()
    }
}