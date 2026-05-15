package com.example.android_house_keeper.models

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
    val matchedAt: String?,
    val status: String // 改用 String 以匹配 API 的 "new", "old" 等字串
)

/**
 * 系統狀態模型 (PropertyStatus)
 */
data class PropertyStatus(
    val last_check_time: String?,
    val status: String,
    val total_tracked: Int,
    val new_alerts_today: Int,
    val recent_logs: List<String>
)
