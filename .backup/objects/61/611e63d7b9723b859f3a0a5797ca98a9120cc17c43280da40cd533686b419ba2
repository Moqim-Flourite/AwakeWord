package com.worddraft.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.worddraft.data.model.Word
import com.worddraft.ui.theme.*

/**
 * 单词卡片组件
 * 显示单词、音标、释义、备注和勾选框
 */
@Composable
fun WordCard(
    word: Word,
    onSpeak: () -> Unit,
    onCheckChange: (Boolean) -> Unit,
    onNoteChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditingNote by remember { mutableStateOf(false) }
    var noteText by remember(word.note) { mutableStateOf(word.note) }
    
    val cardElevation by animateDpAsState(
        targetValue = if (word.isChecked) 2.dp else 8.dp,
        label = "cardElevation"
    )
    
    val cardAlpha by animateColorAsState(
        targetValue = if (word.isChecked) 
            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
        else 
            MaterialTheme.colorScheme.surface,
        label = "cardAlpha"
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(cardElevation, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardAlpha
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 勾选框
            Checkbox(
                checked = word.isChecked,
                onCheckedChange = onCheckChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Success,
                    uncheckedColor = Primary
                ),
                modifier = Modifier.padding(end = 12.dp)
            )
            
            // 单词内容
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(enabled = !word.isChecked) { isEditingNote = true }
            ) {
                // 第一行：单词 + 音标
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = word.spelling,
                        style = MaterialTheme.typography.titleLarge,
                        color = if (word.isChecked) 
                            TextSecondary 
                        else 
                            MaterialTheme.colorScheme.onSurface,
                        textDecoration = if (word.isChecked) 
                            TextDecoration.LineThrough 
                        else 
                            null,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    
                    // 音标
                    if (word.phonetic.isNotBlank()) {
                        Text(
                            text = word.phonetic,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (word.isChecked) TextSecondary else Primary,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
                
                // 第二行：中文释义
                if (word.meaning.isNotBlank()) {
                    Text(
                        text = word.meaning,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (word.isChecked) TextSecondary else Secondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                // 第三行：备注
                if (word.note.isNotBlank() || isEditingNote) {
                    if (isEditingNote) {
                        OutlinedTextField(
                            value = noteText,
                            onValueChange = { noteText = it },
                            placeholder = { Text("添加备注...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            textStyle = MaterialTheme.typography.bodyMedium,
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = TextSecondary
                            ),
                            trailingIcon = {
                                TextButton(onClick = {
                                    onNoteChange(noteText)
                                    isEditingNote = false
                                }) {
                                    Text("保存")
                                }
                            }
                        )
                    } else {
                        Surface(
                            modifier = Modifier.padding(top = 8.dp),
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "📝 ${word.note}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
            
            // 发音按钮
            IconButton(
                onClick = onSpeak,
                enabled = !word.isChecked,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.VolumeUp,
                    contentDescription = "播放发音",
                    tint = if (word.isChecked) TextSecondary else Primary
                )
            }
        }
    }
}

/**
 * 紧凑版单词卡片（用于锁屏显示）
 */
@Composable
fun WordCardCompact(
    word: Word,
    onSpeak: () -> Unit,
    onCheckChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (word.isChecked) {
        Brush.linearGradient(listOf(CardGradientEnd, CardGradientEnd))
    } else {
        Brush.linearGradient(listOf(CardGradientStart, CardGradientEnd))
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 勾选框
            Surface(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable { onCheckChange(!word.isChecked) },
                shape = CircleShape,
                color = if (word.isChecked) Success else Color.Transparent,
                border = if (word.isChecked) null else {
                    androidx.compose.foundation.BorderStroke(2.dp, Primary)
                }
            ) {
                if (word.isChecked) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(16.dp)
                    )
                }
            }
            
            // 单词内容
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = word.spelling,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (word.isChecked) TextSecondary else Color.White,
                        textDecoration = if (word.isChecked) TextDecoration.LineThrough else null
                    )
                    
                    if (word.phonetic.isNotBlank()) {
                        Text(
                            text = " ${word.phonetic}",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (word.isChecked) TextSecondary else PrimaryLight
                        )
                    }
                }
                
                if (word.meaning.isNotBlank()) {
                    Text(
                        text = word.meaning,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (word.isChecked) TextSecondary else Secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                
                if (word.note.isNotBlank()) {
                    Text(
                        text = "📝 ${word.note}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (word.isChecked) TextSecondary else TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
            
            // 发音按钮
            IconButton(
                onClick = onSpeak,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.VolumeUp,
                    contentDescription = "播放发音",
                    tint = if (word.isChecked) TextSecondary else Primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}