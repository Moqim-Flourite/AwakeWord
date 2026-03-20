package com.worddraft

import android.app.Application
import androidx.room.Room
import com.worddraft.data.local.WordDatabase
import com.worddraft.data.repository.WordRepository
import com.worddraft.util.TtsManager
import com.worddraft.viewmodel.MainViewModelFactory

/**
 * Application 类
 * 用于初始化数据库和依赖注入
 */
class WordDraftApplication : Application() {
    
    companion object {
        lateinit var database: WordDatabase
            private set
        lateinit var repository: WordRepository
            private set
        lateinit var viewModelFactory: MainViewModelFactory
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        
        // 初始化数据库
        database = Room.databaseBuilder(
            applicationContext,
            WordDatabase::class.java,
            "word_draft_db"
        ).build()
        
        // 初始化仓库
        repository = WordRepository(database.wordDao())
        
        // 初始化 ViewModel 工厂
        viewModelFactory = MainViewModelFactory(repository)
        
        // 提前初始化TTS引擎（异步，不阻塞启动）
        TtsManager.preInit(applicationContext)
    }
}