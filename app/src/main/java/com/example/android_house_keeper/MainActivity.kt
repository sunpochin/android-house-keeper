package com.example.android_house_keeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.android_house_keeper.api.HouseKeeperAPIClient
import com.example.android_house_keeper.models.HouseAlert
import kotlinx.coroutines.launch

/**
 * Android 版房屋守門員主畫面
 * 使用 Jetpack Compose 實作，功能與 iOS 版對齊。
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 這裡預設使用系統的主題
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HouseKeeperScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseKeeperScreen() {
    val scope = rememberCoroutineScope()
    var alerts by remember { mutableStateOf<List<HouseAlert>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 初始化連線設定 (這裡可以改成從 SharedPreferences 讀取)
    LaunchedEffect(Unit) {
        HouseKeeperAPIClient.updateConfiguration(
            "http://10.0.2.2:3000/api/v1/", // 模擬器連到本地電腦的特殊 IP
            "hk_prod_9988_secret_key"
        )
    }

    // 重新整理資料的函式
    val refreshData = {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                alerts = HouseKeeperAPIClient.service.getAlerts(HouseKeeperAPIClient.getApiKey())
            } catch (e: Exception) {
                errorMessage = "連線失敗: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("房屋守門員 (Android)") },
                actions = {
                    IconButton(onClick = { refreshData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "重新整理")
                    }
                    IconButton(onClick = { /* TODO: 開啟設定頁面 */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "設定")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            AlertList(alerts)
        }
    }
}

@Composable
fun AlertList(alerts: List<HouseAlert>) {
    LazyColumn {
        items(alerts) { alert ->
            AlertRow(alert)
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
fun AlertRow(alert: HouseAlert) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = alert.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = alert.source,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            alert.price?.let {
                Text(text = "💰 $it", style = MaterialTheme.typography.bodySmall)
            }
            alert.matchedAt?.let {
                Text(text = "🕒 $it", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}