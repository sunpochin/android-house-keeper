package com.example.android_house_keeper.api

import com.example.android_house_keeper.models.HouseAlert
import com.example.android_house_keeper.models.PropertyStatus
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * 定義 API 的路徑與方法
 */
interface HouseKeeperService {
    @GET("alerts")
    suspend fun getAlerts(
        @Header("X-API-KEY") apiKey: String
    ): List<HouseAlert>

    @GET("status")
    suspend fun getStatus(
        @Header("X-API-KEY") apiKey: String
    ): PropertyStatus
}

/**
 * 負責處理與後端連線的客戶端
 */
object HouseKeeperAPIClient {
    private var baseUrl: String = "http://localhost:3000/api/v1/"
    private var apiKey: String = ""

    // 建立 Retrofit 實例
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: HouseKeeperService by lazy {
        retrofit.create(HouseKeeperService::class.java)
    }

    /**
     * 更新連線設定 (就像 iOS 的 SettingsView 做的事情)
     */
    fun updateConfiguration(newBaseUrl: String, newApiKey: String) {
        baseUrl = if (newBaseUrl.endsWith("/")) newBaseUrl else "$newBaseUrl/"
        apiKey = newApiKey
        // 注意：在實際開發中，重新設定 baseUrl 通常需要重新建立 Retrofit 實例
    }

    fun getApiKey(): String = apiKey
}
