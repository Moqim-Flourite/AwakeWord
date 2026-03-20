package com.worddraft.service

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import com.worddraft.R

/**
 * 守护服务
 * 定期检查LockScreenService是否运行，如果被杀则重启
 */
class GuardService : Service() {
    
    companion object {
        private const val TAG = "GuardService"
        private const val CHANNEL_ID = "guard_service"
        private const val NOTIFICATION_ID = 1002
        private const val CHECK_INTERVAL = 60 * 1000L // 1分钟检查一次
        
        const val ACTION_START_GUARD = "com.worddraft.action.START_GUARD"
        const val ACTION_STOP_GUARD = "com.worddraft.action.STOP_GUARD"
        const val ACTION_CHECK_SERVICE = "com.worddraft.action.CHECK_SERVICE"
        
        fun startGuard(context: Context) {
            val intent = Intent(context, GuardService::class.java).apply {
                action = ACTION_START_GUARD
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
        
        fun stopGuard(context: Context) {
            val intent = Intent(context, GuardService::class.java).apply {
                action = ACTION_STOP_GUARD
            }
            context.startService(intent)
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "GuardService onCreate")
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "GuardService onStartCommand: ${intent?.action}")
        
        when (intent?.action) {
            ACTION_STOP_GUARD -> {
                cancelAlarm()
                stopSelf()
                return START_NOT_STICKY
            }
            ACTION_CHECK_SERVICE -> {
                checkAndRestartService()
            }
            ACTION_START_GUARD -> {
                startForegroundService()
                scheduleAlarm()
            }
        }
        
        return START_STICKY
    }
    
    private fun startForegroundService() {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        Log.d(TAG, "Guard foreground service started")
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "守护服务",
                NotificationManager.IMPORTANCE_MIN
            ).apply {
                description = "确保锁屏服务持续运行"
                setShowBadge(false)
            }
            
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("单词草稿本")
            .setContentText("守护服务运行中")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setOngoing(true)
            .build()
    }
    
    private fun scheduleAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, GuardService::class.java).apply {
            action = ACTION_CHECK_SERVICE
        }
        
        val pendingIntent = PendingIntent.getService(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // 使用ELAPSED_REALTIME_WAKEUP确保在睡眠时也能唤醒
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + CHECK_INTERVAL,
            CHECK_INTERVAL,
            pendingIntent
        )
        
        Log.d(TAG, "Alarm scheduled")
    }
    
    private fun cancelAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, GuardService::class.java).apply {
            action = ACTION_CHECK_SERVICE
        }
        
        val pendingIntent = PendingIntent.getService(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "Alarm cancelled")
    }
    
    private fun checkAndRestartService() {
        val serviceEnabled = LockScreenService.isServiceEnabled(this)
        Log.d(TAG, "Checking service, enabled=$serviceEnabled")
        
        if (serviceEnabled) {
            // 发送心跳给LockScreenService
            val intent = Intent(this, LockScreenService::class.java).apply {
                action = LockScreenService.ACTION_KEEP_ALIVE
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
                Log.d(TAG, "Keep alive ping sent to LockScreenService")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to send keep alive: ${e.message}")
                // LockScreenService可能已死，尝试重启
                restartLockScreenService()
            }
        }
    }
    
    private fun restartLockScreenService() {
        Log.d(TAG, "Restarting LockScreenService...")
        val intent = Intent(this, LockScreenService::class.java).apply {
            action = LockScreenService.ACTION_START
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            Log.d(TAG, "LockScreenService restarted")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to restart LockScreenService: ${e.message}")
        }
    }
    
    override fun onDestroy() {
        Log.d(TAG, "GuardService onDestroy")
        cancelAlarm()
        super.onDestroy()
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}
