package com.worddraft.data.repository

import com.worddraft.data.local.WordDao
import com.worddraft.data.model.Word
import kotlinx.coroutines.flow.Flow

class WordRepository(
    private val wordDao: WordDao
) {
    fun getUncheckedWords(): Flow<List<Word>> = wordDao.getUncheckedWords()
    
    fun getAllWords(): Flow<List<Word>> = wordDao.getAllWords()
    
    fun getCurrentPageWords(): Flow<List<Word>> = wordDao.getCurrentPageWords()
    
    fun getUncheckedCount(): Flow<Int> = wordDao.getUncheckedCount()
    
    fun getTotalCount(): Flow<Int> = wordDao.getTotalCount()
    
    suspend fun getUncheckedWordsOnce(): List<Word> = wordDao.getUncheckedWordsOnce()
    
    suspend fun insertWord(word: Word): Long = wordDao.insertWord(word)
    
    suspend fun insertWords(words: List<Word>) = wordDao.insertWords(words)
    
    suspend fun updateWord(word: Word) = wordDao.updateWord(word)
    
    suspend fun deleteWord(word: Word) = wordDao.deleteWord(word)
    
    suspend fun getWordById(wordId: Long): Word? = wordDao.getWordById(wordId)
    
    suspend fun isWordExists(spelling: String): Boolean = wordDao.isWordExists(spelling)
    
    suspend fun moveToEnd(wordId: Long) = wordDao.moveToEnd(wordId)
    
    suspend fun deleteAll() = wordDao.deleteAll()
    
    suspend fun resetAllChecked() = wordDao.resetAllChecked()
}
