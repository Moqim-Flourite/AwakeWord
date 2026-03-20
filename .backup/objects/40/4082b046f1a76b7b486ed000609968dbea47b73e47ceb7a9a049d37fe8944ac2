package com.worddraft.service

import android.accessibilityservice.AccessibilityService
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent

/**
 * 无障碍服务 - 监听屏幕亮起事件
 * 用于在锁屏界面显示单词卡片
 * 
 * 优势：系统级服务，MIUI不会杀掉，比前台服务更可靠
 */
class LockScreenAccessibilityService : AccessibilityService() {
    
    companion object {
        private const val TAG = "LockScreenA11yService"
        
        // 保存服务实例，用于检查服务是否启用
        private var instance: LockScreenAccessibilityService? = null
        
        /**
         * 检查无障碍服务是否已启用
         */
        fun isServiceEnabled(): Boolean = instance != null
        
        // 标记LockScreenActivity是否正在显示
        var isLockScreenShowing: Boolean = false
            private set
        
        fun setLockScreenShowing(showing: Boolean) {
            isLockScreenShowing = showing
            Log.d(TAG, "LockScreenShowing set to: $showing")
        }
    }
    
    private var lastTriggerTime = 0L
    private var wasScreenOff = true  // 初始假设屏幕是关闭的
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        Log.d(TAG, "Accessibility service connected")
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        
        // 只处理窗口状态变化事件
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            checkScreenState()
        }
    }
    
    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        instance = null
        Log.d(TAG, "Accessibility service destroyed")
    }
    
    /**
     * 检查屏幕状态，如果是亮屏+锁屏状态则显示单词
     */
    private fun checkScreenState() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        
        val isScreenOn = powerManager.isInteractive
        val isLocked = keyguardManager.isDeviceLocked
        
        Log.d(TAG, "Screen state: isScreenOn=$isScreenOn, isLocked=$isLocked, wasScreenOff=$wasScreenOff, isLockScreenShowing=$isLockScreenShowing")
        
        if (isScreenOn) {
            // 屏幕亮起
            if (isLocked && wasScreenOff && !isLockScreenShowing) {
                // 检查是否启用锁屏功能
                val prefs = getSharedPreferences("worddraft_prefs", Context.MODE_PRIVATE)
                val lockScreenEnabled = prefs.getBoolean("lock_screen_enabled", false)
                
                if (lockScreenEnabled) {
                    // 防止短时间内重复触发（3秒内只触发一次）
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastTriggerTime > 3000) {
                        lastTriggerTime = currentTime
                        Log.d(TAG, "Triggering lock screen activity")
                        startLockScreenActivity()
                    } else {
                        Log.d(TAG, "Too frequent trigger, skipping")
                    }
                } else {
                    Log.d(TAG, "Lock screen feature is disabled")
                }
            }
            wasScreenOff = false
        } else {
            // 屏幕关闭，重置状态
            wasScreenOff = true
            isLockScreenShowing = false
            Log.d(TAG, "Screen off, reset state")
        }
    }
    
    /**
     * 启动锁屏Activity
     */
    private fun startLockScreenActivity() {
        try {
            val intent = Intent(this, com.worddraft.LockScreenActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }
            startActivity(intent)
            isLockScreenShowing = true
            Log.d(TAG, "LockScreenActivity started successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start LockScreenActivity: ${e.message}", e)
        }
    }
}