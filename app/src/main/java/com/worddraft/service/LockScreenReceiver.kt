package com.worddraft.service

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

/**
 * 锁屏广播接收器
 * 监听屏幕亮起事件，自动启动锁屏界面
 */
class LockScreenReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "LockScreenReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Received intent: ${intent.action}")
        
        when (intent.action) {
            Intent.ACTION_SCREEN_ON -> {
                // 屏幕亮起，启动锁屏界面
                val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                
                // 检查是否启用了锁屏功能
                val prefs = context.getSharedPreferences("worddraft_prefs", Context.MODE_PRIVATE)
                val lockScreenEnabled = prefs.getBoolean("lock_screen_enabled", false)
                
                Log.d(TAG, "Screen ON, lockScreenEnabled=$lockScreenEnabled, keyguardLocked=${keyguardManager.isDeviceLocked}")
                
                if (lockScreenEnabled) {
                    startLockScreenActivity(context)
                }
            }
            Intent.ACTION_SCREEN_OFF -> {
                Log.d(TAG, "Screen OFF")
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                Log.d(TAG, "Boot completed")
                // 开机完成后启动服务
                val serviceIntent = Intent(context, LockScreenService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent)
                }
            }
        }
    }
    
    private fun startLockScreenActivity(context: Context) {
        try {
            val intent = Intent(context, com.worddraft.LockScreenActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }
            context.startActivity(intent)
            Log.d(TAG, "LockScreenActivity started")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start LockScreenActivity: ${e.message}")
        }
    }
}