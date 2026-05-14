package com.example.housekeeper.models

import java.util.Date

/**
 * 房屋警示模型 (HouseAlert)
 * 對應 iOS 版的 HouseAlert，用來存放從 API 抓回來的房屋資料。
 */
data class HouseAlert(
    val id: String,
    val title: String,
    val source: String,
    val url: String?,
    val price: String?,
    val addressHint: String?,
    val matchedAt: String?, // 這裡先用 String，解析時再處理日期
    val status: AlertStatus
)

enum class AlertStatus {
    new, seen, ignored, confirmed
}

/**
 * 系統狀態模型 (PropertyStatus)
 */
data class PropertyStatus(
    val lastCheckTime: String?,
    val status: String,
    val totalTracked: Int,
    val newAlertsToday: Int,
    val recentLogs: List<String>
)
