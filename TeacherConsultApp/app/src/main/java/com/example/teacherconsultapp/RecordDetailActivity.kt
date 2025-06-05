package com.example.teacherconsultapp

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordDetailActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var playButton: Button
    private lateinit var summaryView: TextView
    private lateinit var infoView: TextView
    private var playing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_detail)

        playButton = findViewById(R.id.play_button)
        summaryView = findViewById(R.id.detail_summary)
        infoView = findViewById(R.id.detail_info)

        val recordPath = intent.getStringExtra(EXTRA_RECORD_PATH) ?: return
        val logPath = intent.getStringExtra(EXTRA_LOG_PATH)
        val time = intent.getLongExtra(EXTRA_START_TIME, 0L)
        val phone = intent.getStringExtra(EXTRA_PHONE_NUMBER) ?: ""

        infoView.text = "${formatTime(time)}  $phone"
        logPath?.let { path ->
            val file = File(path)
            if (file.exists()) summaryView.text = file.readText()
        }

        playButton.setOnClickListener {
            togglePlayback(recordPath)
        }
    }

    private fun formatTime(time: Long): String {
        if (time == 0L) return ""
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date(time))
    }

    private fun togglePlayback(path: String) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(path)
                prepare()
                start()
            }
            playing = true
            playButton.text = "일시정지"
        } else {
            if (playing) {
                mediaPlayer?.pause()
                playButton.text = "재생"
            } else {
                mediaPlayer?.start()
                playButton.text = "일시정지"
            }
            playing = !playing
        }
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_RECORD_PATH = "record_path"
        const val EXTRA_LOG_PATH = "log_path"
        const val EXTRA_START_TIME = "start_time"
        const val EXTRA_PHONE_NUMBER = "phone_number"
    }
}
