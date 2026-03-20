package com.worddraft.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.worddraft.data.model.Word

@Database(
    entities = [Word::class],
    version = 1,
    exportSchema = false
)
abstract class WordDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}
