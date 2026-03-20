package com.worddraft.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.worddraft.MainActivity
import com.worddraft.R

/**
 * 锁屏服务
 * 前台服务，监听屏幕亮起事件并自动显示锁屏界面
 * 添加守护机制防止被系统杀死
 */
class LockScreenService : Service() {
    
    companion object {
        private const val TAG = "LockScreenService"
        private const val CHANNEL_ID = "lock_screen_service"
        private const val NOTIFICATION_ID = 1001
        
        const val ACTION_START = "com.worddraft.action.START_LOCK_SCREEN"
        const val ACTION_STOP = "com.worddraft.action.STOP_LOCK_SCREEN"
        const val ACTION_TOGGLE = "com.worddraft.action.TOGGLE_LOCK_SCREEN"
        const val ACTION_KEEP_ALIVE = "com.worddraft.action.KEEP_ALIVE"
        
        const val PREFS_NAME = "worddraft_prefs"
        const val KEY_LOCK_SCREEN_ENABLED = "lock_screen_enabled"
        
        fun isServiceEnabled(context: Context): Boolean {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return prefs.getBoolean(KEY_LOCK_SCREEN_ENABLED, false)
        }
        
        fun setServiceEnabled(context: Context, enabled: Boolean) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putBoolean(KEY_LOCK_SCREEN_ENABLED, enabled).apply()
        }
    }
    
    private var screenReceiver: LockScreenReceiver? = null
    private var wakeLock: PowerManager.WakeLock? = null
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service onCreate")
        createNotificationChannel()
        acquireWakeLock()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service onStartCommand: ${intent?.action}")
        
        when (intent?.action) {
            ACTION_STOP -> {
                stopSelf()
                return START_NOT_STICKY
            }
            ACTION_TOGGLE -> {
                if (isServiceEnabled(this)) {
                    stopSelf()
                } else {
                    startForegroundService()
                }
                return START_NOT_STICKY
            }
            ACTION_KEEP_ALIVE -> {
                // 守护心跳，确保服务运行
                Log.d(TAG, "Keep alive ping received")
                if (!isServiceEnabled(this)) {
                    stopSelf()
                    return START_NOT_STICKY
                }
            }
        }
        
        // 启动前台服务
        startForegroundService()
        
        // 注册屏幕亮起广播接收器
        registerScreenReceiver()
        
        // 返回START_STICKY确保服务被杀后自动重启
        return START_STICKY
    }
    
    private fun startForegroundService() {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        setServiceEnabled(this, true)
        // 启动守护服务
        GuardService.startGuard(this)
        Log.d(TAG, "Foreground service started")
    }
    
    private fun acquireWakeLock() {
        try {
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "WordDraft::LockScreenService"
            ).apply {
                acquire(10 * 60 * 1000L) // 10分钟超时
            }
            Log.d(TAG, "WakeLock acquired")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to acquire WakeLock: ${e.message}")
        }
    }
    
    private fun releaseWakeLock() {
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                    Log.d(TAG, "WakeLock released")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing WakeLock: ${e.message}")
        }
    }
    
    override fun onDestroy() {
        Log.d(TAG, "Service onDestroy")
        
        // 如果用户没有主动关闭服务，尝试重启
        if (isServiceEnabled(this)) {
            Log.d(TAG, "Service killed unexpectedly, trying to restart...")
            val restartIntent = Intent(this, LockScreenService::class.java).apply {
                action = ACTION_START
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(restartIntent)
            } else {
                startService(restartIntent)
            }
        }
        
        // 停止守护服务
        GuardService.stopGuard(this)
        unregisterScreenReceiver()
        releaseWakeLock()
        setServiceEnabled(this, false)
        super.onDestroy()
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "锁屏服务",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "锁屏单词显示服务运行中"
                setShowBadge(false)
            }
            
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("单词草稿本")
            .setContentText("锁屏服务运行中，亮屏将显示单词")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
    
    private fun registerScreenReceiver() {
        if (screenReceiver == null) {
            screenReceiver = LockScreenReceiver()
        }
        
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_BOOT_COMPLETED)
            addAction(Intent.ACTION_USER_PRESENT)
        }
        
        // Android 13+ 需要使用 RECEIVER_NOT_EXPORTED
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(screenReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            registerReceiver(screenReceiver, filter)
        }
        
        Log.d(TAG, "Screen receiver registered")
    }
    
    private fun unregisterScreenReceiver() {
        screenReceiver?.let {
            try {
                unregisterReceiver(it)
            } catch (e: Exception) {
                Log.e(TAG, "Error unregistering receiver: ${e.message}")
            }
        }
        screenReceiver = null
    }
}
