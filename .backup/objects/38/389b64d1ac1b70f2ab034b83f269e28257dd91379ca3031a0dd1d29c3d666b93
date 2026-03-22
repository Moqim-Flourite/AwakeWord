package com.worddraft.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.worddraft.ui.components.WordCard
import com.worddraft.util.TtsManager
import com.worddraft.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * 批次详情页面 - 显示某一天的单词列表
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatchDetailScreen(
    viewModel: MainViewModel = viewModel(),
    dateTimestamp: Long,
    onBack: () -> Unit
) {
    // 计算当天的起始和结束时间
    val calendar = remember { Calendar.getInstance() }
    val startTime = remember(dateTimestamp) {
        calendar.timeInMillis = dateTimestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.timeInMillis
    }
    val endTime = remember(dateTimestamp) {
        calendar.timeInMillis = dateTimestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar.timeInMillis
    }
    
    // 格式化日期显示
    val dateText = remember(dateTimestamp) {
        val today = Calendar.getInstance()
        calendar.timeInMillis = dateTimestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        
        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        val diffDays = ((todayStart.timeInMillis - calendar.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
        
        when (diffDays) {
            0 -> "今天"
            1 -> "昨天"
            2 -> "前天"
            else -> SimpleDateFormat("MM月dd日", Locale.getDefault()).format(Date(dateTimestamp))
        }
    }
    
    // 获取该日期的单词
    val words by viewModel.getWordsByDateRange(startTime, endTime)
        .collectAsState(initial = emptyList())
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(dateText) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        if (words.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    text = "暂无单词",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = words,
                    key = { it.id }
                ) { word ->
                    WordCard(
                        word = word,
                        onSpeak = { TtsManager.speak(word.spelling) },
                        onCheckChange = { isChecked ->
                            if (isChecked) {
                                viewModel.checkWord(word)
                            } else {
                                viewModel.uncheckWord(word)
                            }
                        },
                        onNoteChange = { note ->
                            viewModel.updateNote(word, note)
                        }
                    )
                }
            }
        }
    }
}