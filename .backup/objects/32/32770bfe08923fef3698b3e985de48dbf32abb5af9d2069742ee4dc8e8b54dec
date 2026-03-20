package com.worddraft.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 单词实体类
 * @param id 主键
 * @param spelling 单词拼写
 * @param phonetic 音标
 * @param meaning 中文意思
 * @param note 用户备注
 * @param isChecked 是否已勾选（记住）
 * @param createdAt 创建时间
 * @param displayOrder 显示顺序
 */
@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val spelling: String,
    val phonetic: String = "",
    val meaning: String = "",
    val note: String = "",
    val isChecked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val displayOrder: Int = 0
)
