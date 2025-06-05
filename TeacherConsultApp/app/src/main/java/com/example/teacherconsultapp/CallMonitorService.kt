package com.example.teacherconsultapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.teacherconsultapp.SummaryActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CallMonitorService : Service() {

    private lateinit var telephonyManager: TelephonyManager
    private var recorder: MediaRecorder? = null
    private var isRecording = false
    private var currentFile: File? = null
    private var currentNumber: String? = null
    private var callStart: Long = 0L

    private val listener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String?) {
            when (state) {
                TelephonyManager.CALL_STATE_OFFHOOK -> startRecording(incomingNumber)
                TelephonyManager.CALL_STATE_IDLE -> stopRecording()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Call Monitor",
                NotificationManager.IMPORTANCE_LOW
            )
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Call monitoring")
                .setSmallIcon(android.R.drawable.ic_menu_call)
                .build()
            startForeground(1, notification)
        }
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    override fun onDestroy() {
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE)
        stopRecording()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startRecording(number: String?) {
        if (isRecording) return
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != android.content.pm.PackageManager.PERMISSION_GRANTED) return
        callStart = System.currentTimeMillis()
        currentNumber = number ?: ""
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date(callStart))
        val safeNumber = currentNumber?.replace("[^0-9+]".toRegex(), "") ?: "unknown"
        val fileName = "${timeStamp}_${safeNumber}.m4a"
        val file = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)
        currentFile = file
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }
        isRecording = true
        sendStatus(true)
    }

    private fun stopRecording() {
        if (!isRecording) return
        try {
            recorder?.apply {
                stop()
                reset()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        recorder = null
        isRecording = false
        currentFile?.let { file ->
            val prefs = getSharedPreferences("summary", Context.MODE_PRIVATE)
            prefs.edit().putString("lastRecordingPath", file.absolutePath).apply()
            val intent = Intent(this, SummaryActivity::class.java).apply {
                putExtra(SummaryActivity.EXTRA_RECORDING_PATH, file.absolutePath)
                putExtra(SummaryActivity.EXTRA_PHONE_NUMBER, currentNumber)
                putExtra(SummaryActivity.EXTRA_START_TIME, callStart)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
        currentFile = null
        sendStatus(false)
    }

    private fun sendStatus(recording: Boolean) {
        val intent = Intent(ACTION_RECORDING_STATUS)
        intent.putExtra(EXTRA_IS_RECORDING, recording)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        const val CHANNEL_ID = "call_monitor"
        const val ACTION_RECORDING_STATUS = "com.example.teacherconsultapp.RECORDING_STATUS"
        const val EXTRA_IS_RECORDING = "isRecording"
    }
}
